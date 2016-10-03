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
import com.delwink.icebox.InventoryItem;
import com.delwink.icebox.lang.Lang;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 * Table model for the inventory item editor.
 * @author David McMackins II
 */
public class InventoryItemTableModel extends AbstractTableModel {
    private final Inventory INVENTORY;
    
    public InventoryItemTableModel(Inventory inventory) {
        INVENTORY = inventory;
    }

    @Override
    public int getRowCount() {
        return INVENTORY.getItems().size();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return Lang.get("InventoryItemEditor.column" + columnIndex);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
        case 0:
        case 1:
            return String.class;
            
        case 2:
            return Integer.class;
            
        default:
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public boolean isCellEditable(int arg0, int arg1) {
        return true;
    }

    @Override
    public Object getValueAt(int row, int column) {
        InventoryItem item = new ArrayList<>(INVENTORY.getItems()).get(row);
        
        switch (column) {
        case 0:
            return item.getName();
            
        case 1:
            return item.getUnit();
            
        case 2:
            return item.getReorderAt();
            
        default:
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public void setValueAt(Object o, int row, int column) {
        InventoryItem item = new ArrayList<>(INVENTORY.getItems()).get(row);
        
        switch (column) {
        case 0:
            item.setName((String) o);
            break;
            
        case 1:
            item.setUnit((String) o);
            break;
            
        case 2:
            item.setReorderAt((Integer) o);
            break;
        
        default:
            throw new IndexOutOfBoundsException();
        }
    }
}
