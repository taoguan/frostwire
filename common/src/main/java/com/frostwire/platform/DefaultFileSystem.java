/*
 * Created by Angel Leon (@gubatron), Alden Torres (aldenml)
 * Copyright (c) 2011-2016, FrostWire(R). All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.frostwire.platform;

import com.frostwire.logging.Logger;
import org.apache.commons.io.FileUtils;

import java.io.File;

/**
 * @author gubatron
 * @author aldenml
 */
public class DefaultFileSystem implements FileSystem {

    private static final Logger LOG = Logger.getLogger(DefaultFileSystem.class);

    @Override
    public boolean isDirectory(File file) {
        return file.isDirectory();
    }

    @Override
    public boolean canWrite(File file) {
        return file.canWrite();
    }

    @Override
    public boolean mkdirs(File file) {
        return file.mkdirs();
    }

    @Override
    public boolean copy(File src, File dest) {
        try {
            FileUtils.copyFile(src, dest);
            return true;
        } catch (Throwable e) {
            LOG.error("Error in copy file: " + src + " -> " + dest, e);
        }

        return false;
    }

    @Override
    public boolean write(File file, byte[] data) {
        try {
            FileUtils.writeByteArrayToFile(file, data);
            return true;
        } catch (Throwable e) {
            LOG.error("Error in writing to file: " + file, e);
        }

        return false;
    }
}