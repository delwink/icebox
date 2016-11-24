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
import com.delwink.icebox.QuantityUpdate;
import com.delwink.icebox.lang.Lang;
import com.delwink.icebox.table.QuantityUpdateTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 * A dialog for adding a quantity update.
 * @author David McMackins II
 */
public class QuantityUpdateDialog extends JDialog {
    private final Inventory INVENTORY;
    private final JButton ADD_BUTTON, CANCEL_BUTTON, SAVE_BUTTON;
    private final JComboBox<String> NEW_ITEM_MENU;
    private final JTable TABLE;
    private final QuantityUpdate UPDATE;
    
    public QuantityUpdateDialog(Frame parent, Inventory inventory) {
        super(parent, Lang.get("QuantityUpdate.title"));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setModal(true);
        
        INVENTORY = inventory;
        UPDATE = new QuantityUpdate(new Date());
        
        TABLE = new JTable(new QuantityUpdateTableModel(UPDATE, INVENTORY));
        JScrollPane tablePane = new JScrollPane(TABLE);
        
        final List<Integer> itemIDs = new ArrayList<>();
        NEW_ITEM_MENU = new JComboBox<>();
        for (InventoryItem item : new TreeSet<>(inventory.getItems())) {
            String name = item.getName();
            
            for (int i = 0; i < NEW_ITEM_MENU.getItemCount(); ++i) {
                if (NEW_ITEM_MENU.getItemAt(i).equals(name)) {
                    name += " (id=" + item.getID() + ")";
                    break;
                }
            }
            
            NEW_ITEM_MENU.addItem(name);
            itemIDs.add(item.getID());
        }
        
        ADD_BUTTON = new JButton("+");
        ADD_BUTTON.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UPDATE.addItem(itemIDs.get(NEW_ITEM_MENU.getSelectedIndex()), 0, 0);
                TABLE.setModel(new QuantityUpdateTableModel(UPDATE, INVENTORY));
            }
        });
        
        final ActionListener doneEditingListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        };
        
        CANCEL_BUTTON = new JButton(Lang.get("cancel"));
        CANCEL_BUTTON.addActionListener(doneEditingListener);
        
        SAVE_BUTTON = new JButton(Lang.get("save"));
        SAVE_BUTTON.addActionListener(doneEditingListener);
        SAVE_BUTTON.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                INVENTORY.addUpdate(UPDATE);
                
                try (OutputStream os = new FileOutputStream(DataDir.INVENTORY_FILE)) {
                    INVENTORY.saveXml(os);
                } catch (IOException ex) {
                    Logger.getLogger(QuantityUpdateDialog.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        JPanel buttonBox = new JPanel(new BorderLayout());
        JPanel leftButtonBox = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel rightButtonBox = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        leftButtonBox.add(ADD_BUTTON);
        leftButtonBox.add(NEW_ITEM_MENU);
        buttonBox.add(leftButtonBox, BorderLayout.WEST);
        
        rightButtonBox.add(CANCEL_BUTTON);
        rightButtonBox.add(SAVE_BUTTON);
        buttonBox.add(rightButtonBox, BorderLayout.EAST);
        
        setLayout(new BorderLayout());
        add(tablePane, BorderLayout.CENTER);
        add(buttonBox, BorderLayout.SOUTH);
        
        pack();
        centorOnParent();
    }
    
}
