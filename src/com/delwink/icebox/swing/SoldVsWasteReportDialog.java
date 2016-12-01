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
import com.delwink.icebox.QuantityUpdate;
import com.delwink.icebox.lang.Lang;
import com.github.lgooddatepicker.components.DatePicker;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * A dialog for building a sold versus waste report.
 * @author David McMackins II
 */
public class SoldVsWasteReportDialog extends JDialog {
    private final DatePicker START_DATE_FIELD, END_DATE_FIELD;
    private final JButton CANCEL_BUTTON, OK_BUTTON;
    
    /**
     * Opens a dialog for running the sold versus waste report.
     * @param parent The parent frame of this dialog.
     * @param inventory The inventory on which to report.
     */
    public SoldVsWasteReportDialog(final Frame parent, final Inventory inventory) {
        super(parent, Lang.get("Report.soldVsWaste"));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setModal(true);
        
        START_DATE_FIELD = new DatePicker();
        START_DATE_FIELD.setDateToToday();
        
        END_DATE_FIELD = new DatePicker();
        END_DATE_FIELD.setDateToToday();
        
        CANCEL_BUTTON = new JButton(Lang.get("cancel"));
        CANCEL_BUTTON.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        
        OK_BUTTON = new JButton(Lang.get("ok"));
        OK_BUTTON.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Map<Integer, Integer[]> items = new HashMap<>();
                for (QuantityUpdate update : inventory.getUpdates()) {
                    long start = START_DATE_FIELD.getDate().toEpochDay();
                    long end = END_DATE_FIELD.getDate().toEpochDay();
                    long actual = update.getDate().getTime() / (3600 * 24 * 1000);
                    
                    if (actual >= start && actual <= end) {
                        for (QuantityUpdate.Record record : update) {
                            if (!items.containsKey(record.getItemID()))
                                items.put(record.getItemID(), new Integer[] { 0, 0 });
                            
                            Integer[] value = items.get(record.getItemID());
                            value[0] += record.getSold();
                            value[1] += record.getWaste();
                            
                            items.put(record.getItemID(), value);
                        }
                    }
                }
                
                Map<String, Integer[]> sortedItems = new TreeMap<>();
                for (Integer id : items.keySet()) {
                    String name = inventory.getItemByID(id).getName();
                    if (sortedItems.containsKey(name))
                        name += " (id=" + id + ")";
                    
                    sortedItems.put(name, items.get(id));
                }
                
                List<String> columns = new ArrayList<>();
                for (int i = 0; i < 3; ++i)
                    columns.add(Lang.get("SoldVsWaste.column" + i));
                
                List<List<Object>> data = new ArrayList<>();
                for (String name : sortedItems.keySet()) {
                    List<Object> line = new ArrayList<>();
                    line.add(name);
                    line.addAll(Arrays.asList(sortedItems.get(name)));
                    
                    data.add(line);
                }
                
                ReportResultsDialog dialog = new ReportResultsDialog(parent, new ReportResultsDialog.Model(columns, data));
                setVisible(false);
                dialog.setVisible(true);
            }
        });
        
        JPanel buttonBox = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonBox.add(CANCEL_BUTTON);
        buttonBox.add(OK_BUTTON);
        
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        add(START_DATE_FIELD);
        add(END_DATE_FIELD);
        add(buttonBox);
        
        pack();
        centorOnParent();
    }
}
