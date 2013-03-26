/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * http://www.mirthcorp.com
 *
 * The software in this package is published under the terms of the MPL
 * license a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 */

package com.mirth.connect.connectors.file.filesystems;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import jcifs.smb.SmbFileOutputStream;
import jcifs.smb.SmbFilenameFilter;

import org.mule.MuleException;
import org.mule.config.i18n.Message;
import org.mule.config.i18n.Messages;

import com.mirth.connect.connectors.file.filters.SmbFilenameWildcardFilter;

/**
 * The SmbFileSystemConnection class for networked files
 * 
 */
public class SmbFileConnection implements FileSystemConnection {
    public class SmbFileFileInfo implements FileInfo {
        private SmbFile theFile;

        public SmbFileFileInfo(SmbFile theFile) {
            this.theFile = theFile;
        }

        public String getName() {
            return this.theFile.getName();
        }

        public String getAbsolutePath() {
            return this.theFile.getPath();
        }

        public String getParent() {
            return this.theFile.getParent();
        }

        public long getSize() {
            return this.theFile.getContentLength();
        }

        public long getLastModified() {
            return this.theFile.getLastModified();
        }

        public boolean isDirectory() {
            try {
                return this.theFile.isDirectory();
            } catch (SmbException e) {
                return false;
            }
        }

        public boolean isFile() {
            try {
                return this.theFile.isFile();
            } catch (SmbException e) {
                return false;
            }
        }

        public boolean isReadable() {
            try {
                return this.theFile.canRead();
            } catch (SmbException e) {
                return false;
            }
        }
    }

    private NtlmPasswordAuthentication auth = null;
    private SmbFile share = null;

    public SmbFileConnection(String share, String domainAndUser, String password, int timeout) throws Exception {
        String[] params = Pattern.compile("[\\\\|/|@|:|;]").split(domainAndUser);
        String domain = null;
        String username = null;

        if (params.length > 1) {
            domain = params[0];
            username = params[1];
        } else {
            username = params[0];
        }

        if ((username != null) && (password != null)) {
            auth = new NtlmPasswordAuthentication(domain, username, password);
        }

        this.share = new SmbFile("smb://" + share, auth);
        this.share.setConnectTimeout(timeout);
    }

    private String getPath(String dir, String name) {
        if (name != null) {
            return dir + "/" + name;
        } else {
            return dir + "/";
        }
    }

    private SmbFile getSmbFile(SmbFile context, String name) throws Exception {
        return new SmbFile(context, name);
    }

    @Override
    public List<FileInfo> listFiles(String dir, String filenamePattern, boolean isRegex, boolean ignoreDot) throws Exception {
        SmbFile readDirectory = null;
        SmbFilenameFilter filenameFilter = new SmbFilenameWildcardFilter(filenamePattern);

        try {
            readDirectory = getSmbFile(share, getPath(dir, null));
        } catch (Exception e) {
            throw new MuleException(new Message(Messages.FILE_X_DOES_NOT_EXIST, dir), e);
        }

        try {
            SmbFile[] todoFiles = readDirectory.listFiles(filenameFilter);

            if (todoFiles == null) {
                return new ArrayList<FileInfo>();
            } else {
                List<FileInfo> result = new ArrayList<FileInfo>(todoFiles.length);

                for (SmbFile f : todoFiles) {
                    if (!(ignoreDot && f.getName().startsWith("."))) {
                        result.add(new SmbFileFileInfo(f));    
                    }
                }

                return result;
            }
        } catch (Exception e) {
            throw new MuleException(new Message("file", 1), e);
        }
    }
    
    @Override
    public boolean exists(String file, String path) {
        try {
            return getSmbFile(share, getPath(path, file)).exists();    
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean canRead(String readDir) {
        try {
            return getSmbFile(share, getPath(readDir, null)).canRead();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean canWrite(String writeDir) {
        try {
            return getSmbFile(share, getPath(writeDir, null)).canWrite();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public InputStream readFile(String name, String dir) throws MuleException {
        SmbFile src = null;

        try {
            src = getSmbFile(share, getPath(dir, name));
            return new SmbFileInputStream(src);
        } catch (Exception e) {
            throw new MuleException(new Message("file", 1, src.getPath()), e);
        }
    }

    /** Must be called after readFile when reading is complete */
    @Override
    public void closeReadFile() throws Exception {
    // nothing
    }

    @Override
    public boolean canAppend() {
        return true;
    }

    @Override
    public void writeFile(String name, String dir, boolean append, byte[] message) throws Exception {
        OutputStream os = null;
        SmbFile dst = null;
        SmbFile dstDir = null;

        try {
            dstDir = getSmbFile(share, getPath(dir, null));

            if (!dstDir.exists()) {
                dstDir.mkdirs();
            }

            dst = getSmbFile(share, getPath(dir, name));
            os = new SmbFileOutputStream(dst, append);
            os.write(message);
        } finally {
            if (os != null) {
                os.close();
            }
        }
    }

    @Override
    public void delete(String name, String dir, boolean mayNotExist) throws MuleException {
        SmbFile src = null;

        try {
            src = getSmbFile(share, getPath(dir, name));
            src.delete();

            if (src.exists()) {
                if (!mayNotExist) {
                    throw new MuleException(new Message("file", 3, src.getPath()));
                }
            }
        } catch (Exception e) {
            throw new MuleException(new Message("file", 3, src.getPath()), e);
        }
    }

    @Override
    public void move(String fromName, String fromDir, String toName, String toDir) throws MuleException {
        SmbFile src = null;
        SmbFile dst = null;
        SmbFile dstDir = null;

        try {
            src = getSmbFile(share, getPath(fromDir, fromName));
            dstDir = getSmbFile(share, getPath(toDir, null));

            if (!dstDir.exists()) {
                dstDir.mkdirs();
            }

            dst = getSmbFile(share, getPath(toDir, toName));

            try {
                dst.delete();
            } catch (Exception e) {
                // ignore if file alread doesn't exist
            }

            src.renameTo(dst);
        } catch (Exception e) {
            throw new MuleException(new Message("file", 4, src.getPath(), dst.getPath()), e);
        }
    }

    @Override
    public boolean isConnected() {
        return true;
    }

    @Override
    public void activate() {}

    @Override
    public void passivate() {}

    @Override
    public void destroy() {}

    @Override
    public boolean isValid() {
        return true;
    }
}