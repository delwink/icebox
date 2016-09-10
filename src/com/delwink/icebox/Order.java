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

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * An order of inventory items.
 * @author David McMackins II
 */
public class Order implements Iterable<Integer> {
    protected final Map<Integer, Integer> ITEMS;
    protected Date orderDate;
    protected String orderNumber;
    
    /**
     * Creates a new empty order.
     * @param orderNumber Identifier for this order.
     * @param orderDate The date of this order.
     */
    public Order(String orderNumber, Date orderDate) {
        ITEMS = new HashMap<>();
        this.orderDate = orderDate;
        this.orderNumber = orderNumber;
    }
    
    /**
     * Creates a new empty order.
     */
    public Order() {
        this("", new Date());
    }
    
    /**
     * Adds an item to this order.
     * @param itemID ID of the item to be added.
     * @param qty Quantity of the item to be added.
     */
    public void addItem(int itemID, int qty) {
        ITEMS.put(itemID, qty);
    }
    
    public int getQuantityByID(int itemID) {
        return ITEMS.get(itemID);
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
        return ITEMS.keySet().iterator();
    }
}
