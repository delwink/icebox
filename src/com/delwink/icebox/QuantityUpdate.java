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

import com.delwink.icebox.QuantityUpdate.Record;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * An update to inventory quantity.
 * @author David McMackins II
 */
public class QuantityUpdate implements Iterable<Record>  {
    protected final List<Record> RECORDS;
    protected Date updateDate;
    
    /**
     * Creates a new quantity update with a specific date.
     * @param date The date of this update.
     */
    public QuantityUpdate(Date date) {
        RECORDS = new ArrayList<>();
        updateDate = date;
    }
    
    public Record getItemByID(int itemID) {
        for (Record r : RECORDS)
            if (r.getItemID() == itemID)
                return r;
        
        return null;
    }
    
    public void addItem(int itemID, int sold, int waste) {
        Record r = getItemByID(itemID);
        if (r == null) {
            RECORDS.add(new Record(itemID, sold, waste));
        } else {
            r.setSold(r.getSold() + sold);
            r.setWaste(r.getWaste() + waste);
        }
    }

    public Date getDate() {
        return updateDate;
    }

    public void setDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    
    public List<Record> getRecords() {
        return RECORDS;
    }

    @Override
    public Iterator<Record> iterator() {
        return RECORDS.iterator();
    }
    
    /**
     * A single record of this quantity update.
     */
    public class Record {
        protected final int itemID;
        protected int sold, waste;

        /**
         * Creates a new record with unknown quantities.
         * @param itemID The ID of this record's item.
         */
        public Record(int itemID) {
            this(itemID, 0, 0);
        }

        /**
         * Creates a new record with known quantities.
         * @param itemID The ID of this record's item.
         * @param sold The number of units sold.
         * @param waste The number of units wasted.
         */
        public Record(int itemID, int sold, int waste) {
            this.itemID = itemID;
            this.sold = sold;
            this.waste = waste;
        }

        public int getItemID() {
            return itemID;
        }

        public int getSold() {
            return sold;
        }

        public void setSold(int sold) {
            this.sold = sold;
        }

        public int getWaste() {
            return waste;
        }

        public void setWaste(int waste) {
            this.waste = waste;
        }
    }
}
