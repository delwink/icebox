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

import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * A mouse listener which detects double clicks.
 * @author David McMackins II
 */
public abstract class DoubleClickListener extends MouseAdapter {
    public static final int INTERVAL = (Integer) Toolkit.getDefaultToolkit().getDesktopProperty("awt.multiClickInterval");
    
    private Long lastClick = null;

    @Override
    public final void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            if (lastClick != null && e.getWhen() - lastClick < INTERVAL) {
                mouseDoubleClicked(e);
                lastClick = null;
            } else {
                lastClick = e.getWhen();
            }
        }
    }
    
    /**
     * Called when a double click is detected.
     * @param e The last MouseEvent which triggered the double click.
     */
    public abstract void mouseDoubleClicked(MouseEvent e);
}
