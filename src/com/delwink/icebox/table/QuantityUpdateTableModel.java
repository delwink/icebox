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
import com.delwink.icebox.QuantityUpdate;
import com.delwink.icebox.lang.Lang;
import javax.swing.table.AbstractTableModel;

/**
 * Table model for the quantity update dialog.
 * @author David McMackins II
 */
public class QuantityUpdateTableModel extends AbstractTableModel {
    private final Inventory INVENTORY;
    private final QuantityUpdate UPDATE;
    
    public QuantityUpdateTableModel(QuantityUpdate update, Inventory inventory) {
        INVENTORY = inventory;
        UPDATE = update;
    }

    @Override
    public int getRowCount() {
        return UPDATE.getRecords().size();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }
    
    @Override
    public String getColumnName(int columnIndex) {
        return Lang.get("QuantityUpdate.column" + columnIndex);
    }
    
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columnIndex == 0 ? String.class : Integer.class;
    }
    
    @Override
    public boolean isCellEditable(int row, int col) {
        return col >= 1;
    }

    @Override
    public Object getValueAt(int row, int col) {
        QuantityUpdate.Record record = UPDATE.getRecords().get(row);
        
        switch (col) {
        case 0:
            return INVENTORY.getItemByID(record.getItemID()).getName();
            
        case 1:
            return record.getSold();
            
        case 2:
            return record.getWaste();
            
        default:
            throw new IndexOutOfBoundsException();
        }
    }
    
    @Override
    public void setValueAt(Object o, int row, int col) {
        QuantityUpdate.Record record = UPDATE.getRecords().get(row);
        
        switch (col) {
        case 1:
            record.setSold((Integer) o);
            break;
            
        case 2:
            record.setWaste((Integer) o);
            break;
            
        default:
            throw new IndexOutOfBoundsException();
        }
    }
}
