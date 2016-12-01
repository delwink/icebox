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

import com.delwink.icebox.lang.Lang;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

/**
 * A dialog box for showing table-based report results.
 * @author David McMackins II
 */
public class ReportResultsDialog extends JDialog {
    private final JButton OK_BUTTON;
    private final JTable TABLE;
    
    /**
     * Creates a new results dialog.
     * @param parent The parent frame of this dialog.
     * @param model The table model to be used for the results.
     */
    public ReportResultsDialog(Frame parent, Model model) {
        this(parent, Lang.get("Report.results"), model);
    }
    
    /**
     * Creates a new results dialog with a custom window title.
     * @param parent The parent frame of this dialog.
     * @param title The title of the dialog window.
     * @param model The table model to be used for the results.
     */
    public ReportResultsDialog(Frame parent, String title, Model model) {
        super(parent, title);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setModal(true);
        
        TABLE = new JTable(model);
        JScrollPane tablePane = new JScrollPane(TABLE);
        
        OK_BUTTON = new JButton(Lang.get("ok"));
        OK_BUTTON.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        
        JPanel buttonBox = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonBox.add(OK_BUTTON);
        
        setLayout(new BorderLayout());
        add(tablePane, BorderLayout.CENTER);
        add(buttonBox, BorderLayout.SOUTH);
        
        pack();
        centorOnParent();
    }
    
    /**
     * A table model for table-based report results.
     */
    public static final class Model extends AbstractTableModel {
        private final List<String> COLUMN_NAMES;
        private final List<List<Object>> DATA;
        
        /**
         * Creates a new model.
         * @param columnNames A list of the column names.
         * @param data The data of the report arranged by row and column.
         */
        public Model(List<String> columnNames, List<List<Object>> data) {
            COLUMN_NAMES = columnNames;
            DATA = data;
        }
        
        @Override
        public int getRowCount() {
            return DATA.size();
        }

        @Override
        public int getColumnCount() {
            return COLUMN_NAMES.size();
        }
        
        @Override
        public String getColumnName(int columnIndex) {
            return COLUMN_NAMES.get(columnIndex);
        }
        
        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return (DATA.isEmpty() || DATA.get(0).isEmpty()) ? String.class : DATA.get(0).get(columnIndex).getClass();
        }

        @Override
        public Object getValueAt(int row, int col) {
            return DATA.get(row).get(col);
        }
    }
}
