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

import com.delwink.icebox.InventoryItem;
import com.delwink.icebox.Order;
import com.delwink.icebox.lang.Lang;
import javax.swing.table.AbstractTableModel;

/**
 * Table model for the order editor.
 * @author David McMackins II
 */
public class OrderTableModel extends AbstractTableModel {
    private final Order ORDER;
    
    public OrderTableModel(Order order) {
        ORDER = order;
    }

    @Override
    public int getRowCount() {
        return ORDER.getItemCount();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }
    
    @Override
    public String getColumnName(int columnIndex) {
        return Lang.get("OrderEditor.column" + columnIndex);
    }
    
    @Override
    public boolean isCellEditable(int row, int col) {
        return col == 1;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
        case 0:
            return String.class;
            
        case 1:
            return Integer.class;
            
        default:
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public Object getValueAt(int row, int col) {
        InventoryItem item = ORDER.getItem(row);
        
        switch (col) {
        case 0:
            return item.getName();
            
        case 1:
            return ORDER.getQuantityByID(item.getID());
            
        default:
            throw new IndexOutOfBoundsException();
        }
    }
    
    @Override
    public void setValueAt(Object o, int row, int col) {
        InventoryItem item = ORDER.getItem(row);
        
        switch (col) {
        case 1:
            ORDER.setItem(item.getID(), (Integer) o);
            break;
            
        default:
            throw new IndexOutOfBoundsException();
        }
    }
}
