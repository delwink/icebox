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

package com.delwink.icebox;

import com.delwink.icebox.lang.Lang;
import com.delwink.icebox.swing.JDialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

/**
 * Dialog for customizing IceBox.
 * @author David McMackins II
 */
public class SettingsDialog extends JDialog {
    protected final JButton CANCEL_BUTTON, OK_BUTTON;
    protected final JCheckBox SETLAF;
    
    public SettingsDialog(Frame parent) {
        super(parent, Lang.get("Setting.dialogTitle"));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        SETLAF = new JCheckBox(Lang.get("Setting.setlaf"));
        SETLAF.setSelected(Config.get("setlaf").equals("y"));
        JPanel setlafPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        setlafPanel.add(SETLAF);
        
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
                Config.put("setlaf", SETLAF.isSelected() ? "y" : "n");
                
                dispose();
            }
        });
        
        JPanel buttonBox = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonBox.add(CANCEL_BUTTON);
        buttonBox.add(OK_BUTTON);
        
        setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        add(setlafPanel);
        add(buttonBox);
        
        pack();
        centorOnParent();
    }
}
