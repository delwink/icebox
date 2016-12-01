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
import com.delwink.icebox.Order;
import com.delwink.icebox.lang.Lang;
import com.delwink.icebox.table.OrderListTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 * Dialog for selecting an order to edit.
 * @author David McMackins II
 */
public class OrderListDialog extends JDialog {
    private final JButton ADD_BUTTON;
    private final JTable LIST_TABLE;
    private final Inventory INVENTORY;
    
    /**
     * Creates a new list dialog.
     * @param parent The parent frame of this dialog.
     * @param inventory The inventory whose orders will be listed.
     */
    public OrderListDialog(final Frame parent, Inventory inventory) {
        super(parent, Lang.get("OrderList.title"));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setModal(true);
        
        INVENTORY = inventory;
        
        LIST_TABLE = new JTable(new OrderListTableModel(INVENTORY));
        JScrollPane tablePane = new JScrollPane(LIST_TABLE);
        
        final WindowListener doneEditingListener = new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                OrderListDialog.this.setVisible(true);
            }
        };
        
        final ActionListener orderSavedListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton button = (JButton) e.getSource();
                // I'm sorry
                OrderEditor editor = (OrderEditor) button.getParent().getParent().getParent().getParent().getParent().getParent();
                Order order = editor.getOrder();
                
                if (editor.isOrderNumberChanged()) {
                    List<Order> orders = INVENTORY.getOrders();
                    String orig = order.getOrderNumber();
                    int suffix = 1;
                    
                    for (int i = 0; i < orders.size(); ++i) {
                        Order o = orders.get(i);
                        if (o.getOrderNumber().equals(order.getOrderNumber()) && o != order) {
                            order.setOrderNumber(String.format("%s (%d)", orig, suffix++));
                            i = -1; // will be 0 on next loop to restart it
                        }
                    }
                }
                
                LIST_TABLE.setModel(new OrderListTableModel(INVENTORY));
                
                try (OutputStream os = new FileOutputStream(DataDir.INVENTORY_FILE)) {
                    INVENTORY.saveXml(os);
                } catch (IOException ex) {
                    Logger.getLogger(OrderListDialog.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        
        LIST_TABLE.addMouseListener(new DoubleClickListener() {
            @Override
            public void mouseDoubleClicked(MouseEvent e) {
                int row = LIST_TABLE.getSelectedRow();
                String orderNumber = (String) LIST_TABLE.getModel().getValueAt(row, 0);
                Order order = null;
                
                for (Order o : INVENTORY.getOrders()) {
                    if (o.getOrderNumber().equals(orderNumber)) {
                        order = o;
                        break;
                    }
                }
                
                if (order == null)
                    throw new IllegalStateException("Order with number \"" + orderNumber + "\" does not exist!");
                
                OrderEditor editor = new OrderEditor(parent, INVENTORY, order);
                editor.addSaveListener(orderSavedListener);
                editor.addWindowListener(doneEditingListener);
                
                OrderListDialog.this.setVisible(false);
                editor.setVisible(true);
            }
        });
        
        ADD_BUTTON = new JButton("+");
        ADD_BUTTON.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final Order order = new Order(INVENTORY, Lang.get("Order.new"), new Date());
                OrderEditor editor = new OrderEditor(parent, INVENTORY, order);
                editor.setOrderNumberChanged(true); // since order name might conflict
                editor.addSaveListener(orderSavedListener);
                editor.addWindowListener(doneEditingListener);
                
                editor.addSaveListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        INVENTORY.addOrder(order);
                    }
                });
                
                OrderListDialog.this.setVisible(false);
                editor.setVisible(true);
            }
        });
        
        JPanel buttonBox = new JPanel(new BorderLayout());
        JPanel leftButtonBox = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        leftButtonBox.add(ADD_BUTTON);
        buttonBox.add(leftButtonBox, BorderLayout.WEST);
        
        setLayout(new BorderLayout());
        add(tablePane, BorderLayout.CENTER);
        add(buttonBox, BorderLayout.SOUTH);
        
        pack();
        centorOnParent();
    }
}
