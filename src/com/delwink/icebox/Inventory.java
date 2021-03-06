/*
 * IceBox - inventory management software for restaurants
 * Copyright (C) 2016 Delwink, LLC
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, version 3 only.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.delwink.icebox;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * An inventory of items.
 * @author David McMackins II
 */
public class Inventory {
    protected final List<Order> ORDERS;
    protected final List<QuantityUpdate> UPDATES;
    protected final Map<Integer, InventoryItem> ITEMS;
    
    /**
     * Creates a new empty inventory.
     */
    public Inventory() {
        ORDERS = new ArrayList<>();
        UPDATES = new ArrayList<>();
        ITEMS = new TreeMap<>();
    }
    
    /**
     * Loads an inventory from an XML input stream.
     * @param input An XML input stream.
     */
    public Inventory(InputStream input)
            throws ParserConfigurationException, SAXException, IOException {
        this();
        
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = dbf.newDocumentBuilder();
        Document doc = builder.parse(input);
        
        Element root = doc.getDocumentElement();
        root.normalize();
        
        { // load item definitions
            NodeList items = root.getElementsByTagName("item");
            for (int i = 0; i < items.getLength(); ++i) {
                Element item = (Element) items.item(i);
                
                if (item.getParentNode().isSameNode(root)) { // make sure it's top-level
                    int id = Integer.parseInt(item.getAttribute("id"));
                    int reorderAt = Integer.parseInt(item.getAttribute("reorder"));
                    String name = item.getAttribute("name");
                    String unit = item.getAttribute("unit");

                    addNewItem(new InventoryItem(id, name, unit, reorderAt));
                }
            }
        }
        
        { // load orders
            NodeList orders = root.getElementsByTagName("order");
            for (int i = 0; i < orders.getLength(); ++i) {
                Element order = (Element) orders.item(i);
                
                String orderNumber = order.getAttribute("num");
                Date orderDate = new Date(Long.parseLong(order.getAttribute("date")));
                Order newOrder = new Order(this, orderNumber, orderDate);
                
                NodeList items = order.getElementsByTagName("item");
                for (int j = 0; j < items.getLength(); ++j) {
                    Element item = (Element) items.item(j);
                    
                    int id = Integer.parseInt(item.getAttribute("id"));
                    int qty = Integer.parseInt(item.getAttribute("qty"));
                    
                    newOrder.setItem(id, qty);
                }
                
                addOrder(newOrder);
            }
        }
        
        { // load updates
            NodeList updates = root.getElementsByTagName("update");
            for (int i = 0; i < updates.getLength(); ++i) {
                Element update = (Element) updates.item(i);
                
                Date date = new Date(Long.parseLong(update.getAttribute("date")));
                QuantityUpdate newUpdate = new QuantityUpdate(date);
                
                NodeList records = update.getElementsByTagName("record");
                for (int j = 0; j < records.getLength(); ++j) {
                    Element record = (Element) records.item(j);
                    
                    int id = Integer.parseInt(record.getAttribute("item"));
                    int sold = Integer.parseInt(record.getAttribute("sold"));
                    int waste = Integer.parseInt(record.getAttribute("waste"));
                    
                    newUpdate.addItem(id, sold, waste);
                }
                
                addUpdate(newUpdate);
            }
        }
    }
    
    /**
     * Outputs the inventory data as XML.
     * @param output The output target.
     */
    public void saveXml(OutputStream output) {
        PrintWriter writer = new PrintWriter(output);
        
        writer.println("<!-- Generated by IceBox. DO NOT EDIT! -->");
        writer.println("<inventory>");
        
        for (Integer id : ITEMS.keySet())
            writer.println("  " + ITEMS.get(id));
        
        writer.println();
        
        for (Order order : ORDERS) {
            writer.println("  <order num=\"" + order.getOrderNumber() + "\" "
                    + "date=\"" + order.getOrderDate().getTime() + "\">");
            
            for (Integer itemID : order) {
                writer.println("    <item id=\"" + itemID + "\" "
                        + "qty=\"" + order.getQuantityByID(itemID) + "\"/>");
            }
            
            writer.println("  </order>");
        }
        
        writer.println();
        
        for (QuantityUpdate update : UPDATES) {
            writer.println("  <update date=\"" + update.getDate().getTime() + "\">");
            
            for (QuantityUpdate.Record record : update) {
                writer.println("    <record item=\"" + record.getItemID() + "\" "
                        + "sold=\"" + record.getSold() + "\" "
                        + "waste=\"" + record.getWaste() + "\"/>");
            }
            
            writer.println("  </update>");
        }
        
        writer.println("</inventory>");
        writer.flush();
    }
    
    /**
     * Refreshes the quantities of items in stock based on orders and updates.
     */
    public void refreshQuantities() {
        for (InventoryItem item : ITEMS.values())
            item.addStock(-item.getStock()); // clear stock before refreshing
        
        List<Order> orders = new ArrayList<>(ORDERS);
        ORDERS.clear();
        for (Order order : orders)
            addOrder(order);
        
        List<QuantityUpdate> updates = new ArrayList<>(UPDATES);
        UPDATES.clear();
        for (QuantityUpdate update : updates)
            addUpdate(update);
    }
    
    /**
     * Adds an order to this inventory stock.
     * @param order The order to be added.
     */
    public final void addOrder(Order order) {
        ORDERS.add(order);
        
        for (int item : order) {
            InventoryItem affectedItem = getItemByID(item);
            if (affectedItem == null)
                throw new IllegalStateException("Order has an undefined item " + item);
            
            affectedItem.addStock(order.getQuantityByID(item));
        }
    }
    
    public List<Order> getOrders() {
        return ORDERS;
    }
    
    /**
     * Adds a quantity update to this inventory's stock.
     * @param update The update to be added.
     */
    public final void addUpdate(QuantityUpdate update) {
        UPDATES.add(update);
        
        for (QuantityUpdate.Record record : update) {
            InventoryItem affectedItem = getItemByID(record.getItemID());
            if (affectedItem == null)
                throw new IllegalStateException("Update has an undefined item " + record.getItemID());
            
            affectedItem.addStock(-(record.getSold() + record.getWaste()));
        }
    }
    
    public List<QuantityUpdate> getUpdates() {
        return UPDATES;
    }
    
    public final void addNewItem(InventoryItem item) {
        ITEMS.put(item.getID(), item);
    }
    
    public void deleteItem(int id) {
        ITEMS.remove(id);
    }
    
    public Collection<InventoryItem> getItems() {
        return ITEMS.values();
    }
    
    public InventoryItem getItemByID(int id) {
        return ITEMS.get(id);
    }
    
    /**
     * Finds the next unused item ID.
     * @return The next available item ID for use.
     */
    public int getNextID() {
        Set<Integer> keys = ITEMS.keySet();
        for (int i = 0; i <= keys.size(); ++i)
            if (!ITEMS.containsKey(i))
                return i;
        
        if (ITEMS.isEmpty())
            return 0;
        
        throw new IllegalStateException("Could not find next item ID");
    }
}
