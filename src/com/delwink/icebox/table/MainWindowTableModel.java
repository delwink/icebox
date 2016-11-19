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
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 * Table model designed for the IceBox main window.
 * @author David McMackins II
 */
public class MainWindowTableModel extends AbstractTableModel {
    private final List<InventoryItem> ITEMS;
    
    public MainWindowTableModel(Inventory inventory, boolean reorderOnly) {
        if (reorderOnly) {
            ITEMS = new ArrayList<>();
            for (InventoryItem item : inventory.getItems())
                if (item.getStock() <= item.getReorderAt())
                    ITEMS.add(item);
        } else {
            ITEMS = new ArrayList<>(inventory.getItems());
        }
    }

    @Override
    public int getRowCount() {
        return ITEMS.size();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return Lang.get("MainWindow.column" + columnIndex);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    @Override
    public Object getValueAt(int i, int j) {
        InventoryItem item = null;
        for (InventoryItem temp : ITEMS) {
            if (i-- == 0) {
                item = temp;
                break;
            }
        }
        
        if (item == null)
            throw new IndexOutOfBoundsException();
        
        switch (j) {
        case 0:
            return item.getName();
            
        case 1:
            return String.valueOf(item.getStock());
            
        default:
            return new IndexOutOfBoundsException();
        }
    }
}
