package codeOrchestra;

import flex2.compiler.io.VirtualFile;
import flex2.compiler.util.MimeMappings;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author  Anton.I.Neverov
 */
public class FakeASVirtualFile implements VirtualFile {

    private String name;

    public FakeASVirtualFile(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getNameForReporting() {
        return name;
    }

    @Override
    public String getURL() {
        return null;
    }

    @Override
    public String getParent() {
        return null;
    }

    @Override
    public boolean isDirectory() {
        return false;
    }

    @Override
    public long size() {
        return 0;
    }

    @Override
    public String getMimeType() {
        return MimeMappings.AS;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return null;
    }

    @Override
    public byte[] toByteArray() throws IOException {
        return new byte[0];
    }

    @Override
    public boolean isTextBased() {
        return true;
    }

    @Override
    public long getLastModified() {
        return 0;
    }

    @Override
    public void close() {
    }

    @Override
    public VirtualFile resolve(String path) {
        return null;
    }
}
