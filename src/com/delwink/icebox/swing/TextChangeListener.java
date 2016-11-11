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

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public abstract class TextChangeListener implements DocumentListener {
    @Override
    public void insertUpdate(DocumentEvent e) {
        textChanged(e);
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        textChanged(e);
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        textChanged(e);
    }
    
    public abstract void textChanged(DocumentEvent e);
}
