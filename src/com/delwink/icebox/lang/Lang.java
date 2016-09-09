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

package com.delwink.icebox.lang;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Static class for getting localized GUI text.
 * @author David McMackins II
 */
public final class Lang {
    private static Properties lang;
    
    /**
     * Sets the active language.
     * @param path Local path to the lang file inside the lang directory.
     * @throws java.io.FileNotFoundException if lang file not found.
     */
    public static void setLang(String path) throws FileNotFoundException {
        Properties oldLang = lang;
        
        try {
            lang = new Properties();
            lang.load(Lang.class.getResourceAsStream(path));
        } catch (IOException | NullPointerException ex) {
            lang = oldLang;
            throw new FileNotFoundException("Could not find lang file " + path);
        }
    }
    
    /**
     * Gets appropriate GUI text for an element.
     * @param key The key for the text.
     * @return The appropriate GUI text for the key, or the key itself if no
     * text is found.
     */
    public static String get(String key) {
        try {
            return lang.getProperty(key, key);
        } catch (NullPointerException ex) {
            return key;
        }
    }
}
