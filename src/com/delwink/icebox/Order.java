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

import com.delwink.icebox.lang.Lang;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * An order of inventory items.
 * @author David McMackins II
 */
public class Order implements Iterable<Integer> {
    protected final List<InventoryItem> ITEMS;
    protected final Map<Integer, Integer> BOM;
    protected Date orderDate;
    protected String orderNumber;
    
    /**
     * Creates a new empty order.
     * @param inventory The inventory for this order.
     * @param orderNumber Identifier for this order.
     * @param orderDate The date of this order.
     */
    public Order(Inventory inventory, String orderNumber, Date orderDate) {
        ITEMS = new ArrayList<>(inventory.getItems());
        BOM = new HashMap<>();
        this.orderDate = orderDate;
        this.orderNumber = orderNumber;
    }
    
    /**
     * Creates a new empty order.
     * @param inventory The inventory for this order.
     */
    public Order(Inventory inventory) {
        this(inventory, Lang.get("Order.new"), new Date());
    }
    
    /**
     * Sets the quantity of an item on this order (adds if item not on order).
     * @param itemID ID of the item to be added.
     * @param qty Quantity of the item to be added.
     */
    public void setItem(int itemID, int qty) {
        BOM.put(itemID, qty);
    }
    
    /**
     * Gets an item from the order by index.
     * @param i The index of the item.
     * @return The InventoryItem at index i.
     */
    public InventoryItem getItem(int i) {
        for (Integer id : BOM.keySet()) {
            if (i == 0) {
                for (InventoryItem item : ITEMS)
                    if (item.getID() == id)
                        return item;
                
                throw new IndexOutOfBoundsException();
            }
            
            --i;
        }
        
        throw new IndexOutOfBoundsException();
    }
    
    public int getItemCount() {
        return BOM.size();
    }
    
    public int getQuantityByID(int itemID) {
        return BOM.get(itemID);
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    @Override
    public Iterator<Integer> iterator() {
        return BOM.keySet().iterator();
    }
}
