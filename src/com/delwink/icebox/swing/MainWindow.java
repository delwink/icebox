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

import com.delwink.icebox.Config;
import com.delwink.icebox.DataDir;
import com.delwink.icebox.Inventory;
import com.delwink.icebox.lang.Lang;
import com.delwink.icebox.table.MainWindowTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 * The main IceBox hub window.
 * @author David McMackins II
 */
public class MainWindow extends JFrame {
    protected final Inventory INVENTORY;
    protected final JButton ITEMS_BUTTON, ORDERS_BUTTON, UPDATE_BUTTON;
    protected final JCheckBox REORDER_ONLY;
    protected final JMenu REPORT_MENU, SESSION_MENU;
    protected final JMenuBar MENU_BAR;
    protected final JTable INVENTORY_TABLE;
    
    /**
     * Creates a new main IceBox window.
     * @param inventory The inventory tracked in this window.
     */
    public MainWindow(Inventory inventory) {
        super(Lang.get("MainWindow.title"));
        INVENTORY = inventory;
        
        // menus
        MENU_BAR = new JMenuBar();
        setJMenuBar(MENU_BAR);
        
        SESSION_MENU = new JMenu(Lang.get("MainWindow.SessionMenu.name"));
        MENU_BAR.add(SESSION_MENU);
        
        JMenuItem settings = new JMenuItem(Lang.get("Setting.dialogTitle"));
        SESSION_MENU.add(settings);
        settings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SettingsDialog sd = new SettingsDialog(MainWindow.this);
                sd.setVisible(true);
            }
        });
        
        SESSION_MENU.addSeparator();
        
        JMenuItem quit = new JMenuItem(Lang.get("MainWindow.quit"));
        SESSION_MENU.add(quit);
        quit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainWindow.this.dispatchEvent(new WindowEvent(MainWindow.this, WindowEvent.WINDOW_CLOSING));
            }
        });
        
        REPORT_MENU = new JMenu(Lang.get("MainWindow.ReportMenu.name"));
        MENU_BAR.add(REPORT_MENU);
        
        JMenuItem soldVsWaste = new JMenuItem(Lang.get("Report.soldVsWaste"));
        REPORT_MENU.add(soldVsWaste);
        soldVsWaste.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        
        // top section
        REORDER_ONLY = new JCheckBox(Lang.get("MainWindow.reorderOnly"));
        REORDER_ONLY.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        
        JPanel optionBox = new JPanel(new FlowLayout(FlowLayout.CENTER));
        optionBox.add(REORDER_ONLY);
        
        // center section
        INVENTORY_TABLE = new JTable(new MainWindowTableModel(INVENTORY));
        JScrollPane inventoryBox = new JScrollPane(INVENTORY_TABLE);
        
        // bottom section
        ITEMS_BUTTON = new JButton(Lang.get("MainWindow.items"));
        ITEMS_BUTTON.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        
        ORDERS_BUTTON = new JButton(Lang.get("MainWindow.orders"));
        ORDERS_BUTTON.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        
        UPDATE_BUTTON = new JButton(Lang.get("MainWindow.update"));
        UPDATE_BUTTON.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        
        JPanel buttonBox = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonBox.add(ITEMS_BUTTON);
        buttonBox.add(ORDERS_BUTTON);
        buttonBox.add(UPDATE_BUTTON);
        
        // big picture
        setLayout(new BorderLayout());
        add(optionBox, BorderLayout.NORTH);
        add(inventoryBox, BorderLayout.CENTER);
        add(buttonBox, BorderLayout.SOUTH);
        
        // window properties
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                if (isMaximized()) {
                    Config.put("mainwindow.maximized", "y");
                } else {
                    Config.put("mainwindow.maximized", "n");
                    Config.put("mainwindow.width", String.valueOf(getWidth()));
                    Config.put("mainwindow.height", String.valueOf(getHeight()));
                }
            }
        });
        
        setSize(Integer.parseInt(Config.get("mainwindow.width")),
                Integer.parseInt(Config.get("mainwindow.height")));
        
        if (Config.get("mainwindow.maximized").equals("y"))
            setMaximized();
    }
    
    public final void setMaximized() {
        setExtendedState(getExtendedState() | MAXIMIZED_BOTH);
    }
    
    public final boolean isMaximized() {
        return (getExtendedState() & MAXIMIZED_BOTH) == MAXIMIZED_BOTH;
    }
    
    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        try {
            Lang.setLang(Config.get("lang") + ".lang");
        } catch (FileNotFoundException ignored) {
        }
        
        if (Config.get("setlaf").equals("y")) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                System.err.println("Failed to set look and feel");
            }
        }
        
        Inventory inventory;
        File inventoryFile = DataDir.INVENTORY_FILE;
        if (inventoryFile.exists()) {
            try (FileInputStream stream = new FileInputStream(inventoryFile)) {
                inventory = new Inventory(stream);
            }
        } else {
            inventory = new Inventory();
        }
        
        MainWindow mainWindow = new MainWindow(inventory);
        mainWindow.setVisible(true);
    }
}
