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

import com.delwink.icebox.Inventory;
import com.delwink.icebox.InventoryItem;
import com.delwink.icebox.Order;
import com.delwink.icebox.lang.Lang;
import com.delwink.icebox.table.OrderTableModel;
import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.optionalusertools.DateChangeListener;
import com.github.lgooddatepicker.zinternaltools.DateChangeEvent;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import org.threeten.bp.LocalDate;

/**
 * Dialog for editing orders.
 * @author David McMackins II
 */
public class OrderEditor extends JDialog {
    private final DatePicker ORDER_DATE_FIELD;
    private final JButton ADD_BUTTON, CANCEL_BUTTON, SAVE_BUTTON;
    private final JComboBox<String> NEW_ITEM_MENU;
    private final JTable BOM;
    private final JTextField ORDER_NUMBER_FIELD;
    private final Order ORDER;
    
    private boolean orderNumberChanged = false;
    
    /**
     * Creates a new editor.
     * @param parent The parent frame of this dialog.
     * @param inventory The inventory to which this order applies.
     * @param order The order to be edited.
     */
    public OrderEditor(Frame parent, Inventory inventory, Order order) {
        super(parent, Lang.get("OrderEditor.title"));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setModal(true);
        
        ORDER = order;
        
        ORDER_DATE_FIELD = new DatePicker();
        ORDER_DATE_FIELD.setDate(LocalDate.ofEpochDay(ORDER.getOrderDate().getTime() / (3600 * 24 * 1000)));
        ORDER_DATE_FIELD.addDateChangeListener(new DateChangeListener() {
            @Override
            public void dateChanged(DateChangeEvent dce) {
                ORDER.setOrderDate(new Date(dce.getNewDate().toEpochDay()));
            }
        });
        
        ORDER_NUMBER_FIELD = new JTextField(15);
        ORDER_NUMBER_FIELD.setText(ORDER.getOrderNumber());
        ORDER_NUMBER_FIELD.getDocument().addDocumentListener(new TextChangeListener() {
            @Override
            public void textChanged(DocumentEvent e) {
                ORDER.setOrderNumber(ORDER_NUMBER_FIELD.getText());
                orderNumberChanged = true;
            }
        });
        
        BOM = new JTable(new OrderTableModel(ORDER));
        JScrollPane bomPane = new JScrollPane(BOM);
        
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
                ORDER.setItem(itemIDs.get(NEW_ITEM_MENU.getSelectedIndex()), 0);
                BOM.setModel(new OrderTableModel(ORDER));
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
        
        JPanel orderHeader = new JPanel();
        orderHeader.setLayout(new BoxLayout(orderHeader, BoxLayout.Y_AXIS));
        
        JPanel orderNumberBox = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        orderNumberBox.add(new JLabel(Lang.get("Order.number")));
        orderNumberBox.add(ORDER_NUMBER_FIELD);
        
        JPanel orderDateBox = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        orderDateBox.add(new JLabel(Lang.get("Order.date")));
        orderDateBox.add(ORDER_DATE_FIELD);
        
        orderHeader.add(orderNumberBox);
        orderHeader.add(orderDateBox);
        
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
        add(orderHeader, BorderLayout.NORTH);
        add(bomPane, BorderLayout.CENTER);
        add(buttonBox, BorderLayout.SOUTH);
        
        pack();
        centorOnParent();
    }
    
    public boolean isOrderNumberChanged() {
        return orderNumberChanged;
    }
    
    /**
     * Overrides the order number changed flag.
     * @param b Whether to say the order number has changed.
     */
    public void setOrderNumberChanged(boolean b) {
        orderNumberChanged = b;
    }
    
    public Order getOrder() {
        return ORDER;
    }
    
    public void addSaveListener(ActionListener al) {
        SAVE_BUTTON.addActionListener(al);
    }
}
