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

package com.frostwire.android;

import android.app.Application;
import android.os.Build;
import com.frostwire.android.core.ConfigurationManager;
import com.frostwire.android.core.Constants;
import com.frostwire.jlibtorrent.LibTorrent;
import com.frostwire.jlibtorrent.swig.posix_stat;
import com.frostwire.jlibtorrent.swig.posix_wrapper;
import com.frostwire.logging.Logger;
import com.frostwire.platform.AbstractPlatform;
import com.frostwire.platform.DefaultFileSystem;
import com.frostwire.platform.FileSystem;

/**
 * @author gubatron
 * @author aldenml
 */
public final class AndroidPlatform extends AbstractPlatform {

    private static final Logger LOG = Logger.getLogger(AndroidPlatform.class);

    private static final int VERSION_CODE_LOLLIPOP = 21;

    private final int sdk;

    public AndroidPlatform(Application app) {
        super(buildFileSystem(app), new AndroidPaths(app));

        this.sdk = Build.VERSION.SDK_INT;
    }

    @Override
    public boolean android() {
        return true;
    }

    @Override
    public int androidVersion() {
        return sdk;
    }

    @Override
    public boolean experimental() {
        return ConfigurationManager.instance().getBoolean(Constants.PREF_KEY_CORE_EXPERIMENTAL);
    }

    private static FileSystem buildFileSystem(Application app) {
        FileSystem fs;

        if (Build.VERSION.SDK_INT >= VERSION_CODE_LOLLIPOP) {
            LollipopFileSystem lfs = new LollipopFileSystem(app);
            LibTorrent.setPosixWrapper(new PosixCalls(lfs));
            fs = lfs;
        } else {
            fs = new DefaultFileSystem();
        }

        return fs;
    }

    private static final class PosixCalls extends posix_wrapper {

        private final LollipopFileSystem fs;

        public PosixCalls(LollipopFileSystem fs) {
            this.fs = fs;
        }

        @Override
        public int open(String path, int flags, int mode) {
            LOG.info("posix - open:" + path);
            return super.open(path, flags, mode);
        }

        @Override
        public int stat(String path, posix_stat buf) {
            LOG.info("posix - stat:" + path);
            return super.stat(path, buf);
        }

        @Override
        public int mkdir(String path, int mode) {
            LOG.info("posix - mkdir:" + path);
            return super.mkdir(path, mode);
        }

        @Override
        public int rename(String oldpath, String newpath) {
            LOG.info("posix - rename:" + oldpath + " -> " + newpath);
            return super.rename(oldpath, newpath);
        }

        @Override
        public int remove(String path) {
            LOG.info("posix - remove:" + path);
            return super.remove(path);
        }
    }
}