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

package com.delwink.icebox.table;

import com.delwink.icebox.Inventory;
import com.delwink.icebox.Order;
import com.delwink.icebox.lang.Lang;
import java.util.Date;
import javax.swing.table.AbstractTableModel;

/**
 * Table model for the order list.
 * @author David McMackins II
 */
public class OrderListTableModel extends AbstractTableModel {
    private final Inventory INVENTORY;
    
    public OrderListTableModel(Inventory inventory) {
        INVENTORY = inventory;
    }
    
    @Override
    public int getRowCount() {
        return INVENTORY.getOrders().size();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }
    
    @Override
    public String getColumnName(int column) {
        return Lang.get("OrderList.column" + column);
    }
    
    @Override
    public Class<?> getColumnClass(int column) {
        switch (column) {
        case 0:
            return String.class;
            
        case 1:
            return Date.class;
        
        default:
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public Object getValueAt(int row, int column) {
        Order order = INVENTORY.getOrders().get(row);
        
        switch (column) {
        case 0:
            return order.getOrderNumber();
            
        case 1:
            return order.getOrderDate();
            
        default:
            throw new IndexOutOfBoundsException();
        }
    }
}
