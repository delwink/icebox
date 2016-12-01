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

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Static class which handles the IceBox data directory.
 * @author David McMackins II
 */
public final class DataDir {
    /**
     * A File object referring to the file which stores the inventory data.
     */
    public static final File INVENTORY_FILE = getDataFile("inventory.xml");
    
    private static File rootDir = null;
    
    /**
     * Gets a file in the data directory.
     * @param path The relative path of the data file.
     * @return The requested file.
     */
    public static File getDataFile(String path) {
        if (rootDir == null) {
            try {
                init();
            } catch (FileNotFoundException ex) {
                return new File(System.getProperty("user.home"), path);
            }
        }
        
        return new File(rootDir, path);
    }
    
    private static void init() throws FileNotFoundException {
        String path = System.getProperty("user.home");
        
        if (isWinblows())
            path = new File(path, "AppData\\Roaming").getAbsolutePath();
        
        File f = new File(path, ".icebox");
        if (f.exists()) {
            if (f.isDirectory()) {
                rootDir = f;
            } else {
                throw new IllegalStateException("Data directory path is not a directory");
            }
        } else {
            try {
                if (!f.mkdirs())
                    throw new Exception();
                
                rootDir = f;
            } catch (Exception ex) {
                throw new FileNotFoundException("Failed to create data directory");
            }
        }
    }
    
    private static boolean isWinblows() { // tee hee
        return System.getProperty("os.name").toLowerCase().startsWith("win");
    }
}
