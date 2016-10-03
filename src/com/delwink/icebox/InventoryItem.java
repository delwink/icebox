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

/**
 * An tracked inventory item.
 * @author David McMackins II
 */
public class InventoryItem implements Comparable<InventoryItem> {
    protected final int ID;
    protected int inStock, reorderAt;
    protected String name, unit;
    
    /**
     * Creates a new inventory item.
     * @param id A unique numeric ID for this item.
     */
    public InventoryItem(int id) {
        this(id, "Item", "units", 0);
    }
    
    /**
     * Creates a new inventory item with known properties.
     * @param id A unique numeric ID for this item.
     * @param name A name for this item.
     * @param unit Unit per quantity.
     * @param reorderAt Quantity at which this item should be reordered.
     */
    public InventoryItem(int id, String name, String unit, int reorderAt) {
        ID = id;
        inStock = 0;
        this.name = name;
        this.unit = unit;
        this.reorderAt = reorderAt;
    }

    public int getID() {
        return ID;
    }

    public int getStock() {
        return inStock;
    }
    
    public void addStock(int n) {
        inStock += n;
    }

    public int getReorderAt() {
        return reorderAt;
    }

    public void setReorderAt(int reorderAt) {
        this.reorderAt = reorderAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public int compareTo(InventoryItem item) {
        return getName().compareTo(item.getName());
    }
    
    @Override
    public String toString() {
        return "<item id=\"" + getID() + "\" name=\"" + getName() + "\" "
                + "unit=\"" + getUnit() + "\" reorder=\"" + getReorderAt() + "\"/>";
    }
}
