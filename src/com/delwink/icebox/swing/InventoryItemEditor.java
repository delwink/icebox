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

package com.delwink.icebox.swing;

import com.delwink.icebox.DataDir;
import com.delwink.icebox.Inventory;
import com.delwink.icebox.InventoryItem;
import com.delwink.icebox.lang.Lang;
import com.delwink.icebox.table.InventoryItemTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class InventoryItemEditor extends JDialog {
    private final List<Change> CHANGES;
    private final Inventory INVENTORY;
    private final List<InventoryItem> ADDED;
    private final JButton ADD_BUTTON, CANCEL_BUTTON, SAVE_BUTTON;
    private final JTable TABLE;
    
    public InventoryItemEditor(Frame parent, Inventory inventory) {
        super(parent, Lang.get("InventoryItemEditor.title"));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setModal(true);
        
        INVENTORY = inventory;
        CHANGES = new ArrayList<>();
        ADDED = new ArrayList<>();
        
        TABLE = new JTable(new InventoryItemTableModel(INVENTORY));
        JScrollPane tablePane = new JScrollPane(TABLE);
        
        ADD_BUTTON = new JButton("+");
        ADD_BUTTON.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                InventoryItem item = new InventoryItem(INVENTORY.getNextID());
                ADDED.add(item);
                INVENTORY.addNewItem(item);
            }
        });
        
        CANCEL_BUTTON = new JButton(Lang.get("cancel"));
        CANCEL_BUTTON.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        
        SAVE_BUTTON = new JButton(Lang.get("save"));
        SAVE_BUTTON.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (InventoryItem item : ADDED) {
                    INVENTORY.addNewItem(item);
                }
                
                for (Change change : CHANGES) {
                    InventoryItem item = change.getItem();
                    
                    switch (change.getType()) {
                    case NAME:
                        item.setName((String) change.getData());
                        break;
                        
                    case UNIT:
                        item.setUnit((String) change.getData());
                        break;
                        
                    case REORDER:
                        item.setReorderAt((Integer) change.getData());
                        break;
                    }
                }
                
                try (OutputStream stream = new FileOutputStream(DataDir.INVENTORY_FILE)) {
                    INVENTORY.saveXml(stream);
                } catch (IOException ex) {
                    Logger.getLogger(InventoryItemEditor.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                dispose();
            }
        });
        
        JPanel buttonBox = new JPanel(new BorderLayout());
        JPanel leftButtonBox = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel rightButtonBox = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        leftButtonBox.add(ADD_BUTTON);
        buttonBox.add(leftButtonBox, BorderLayout.WEST);
        
        rightButtonBox.add(CANCEL_BUTTON);
        rightButtonBox.add(SAVE_BUTTON);
        buttonBox.add(rightButtonBox);
        
        setLayout(new BorderLayout());
        add(tablePane, BorderLayout.CENTER);
        add(buttonBox, BorderLayout.SOUTH);
        pack();
        centorOnParent();
    }
    
    private static class Change {
        private final InventoryItem ITEM;
        private final Object DATA;
        private final Type TYPE;
        
        protected Change(InventoryItem item, Type type, Object data) {
            ITEM = item;
            TYPE = type;
            DATA = data;
        }
        
        public InventoryItem getItem() {
            return ITEM;
        }
        
        public Type getType() {
            return TYPE;
        }
        
        public Object getData() {
            return DATA;
        }
        
        protected enum Type {
            NAME,
            UNIT,
            REORDER
        }
    }
}
