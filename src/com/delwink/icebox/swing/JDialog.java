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

import java.awt.Container;
import java.awt.Frame;

/**
 * A custom JDialog.
 * @author David McMackins II
 */
public class JDialog extends javax.swing.JDialog {
    /**
     * Creates a new dialog.
     * @param parent The parent frame of this dialog.
     */
    public JDialog(Frame parent) {
        super(parent);
    }
    
    /**
     * Creates a new dialog with a title.
     * @param parent The parent frame of this dialog.
     * @param title The title of the dialog window.
     */
    public JDialog(Frame parent, String title) {
        super(parent, title);
    }
    
    /**
     * Centers the dialog on its parent frame.
     */
    public final void centorOnParent() {
        Container parent = getParent();
        
        setLocation(parent.getX() + (parent.getWidth() / 2) - (getWidth() / 2),
                parent.getY() + (parent.getHeight() / 2) - (getHeight() / 2));
    }
}
