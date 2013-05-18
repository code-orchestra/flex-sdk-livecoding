package codeOrchestra.digest;

import macromedia.asc.parser.MetaDataNode;
import macromedia.asc.util.Context;

import java.io.File;

/**
 * @author Alexander Eliseyev
 */
public class EmbedDigest {

    private String source;
    private String mimeType;
    private String fullPath;
    private String packageName;

    public EmbedDigest(MetaDataNode embed, Context cx) {
        this.source = embed.getValue("source");
        this.mimeType = embed.getValue("mimeType");
        this.packageName = embed.def.pkgdef.name.id.pkg_part;
        this.fullPath = calculateAssetPath(cx);
    }

    public String getSource() {
        return source;
    }

    public String getMimeType() {
        return mimeType;
    }

    public String getFullPath() {
        return fullPath;
    }

    private String calculateAssetPath(Context cx) {
        File file = new File(cx.path());

        if (!source.startsWith("/")) {
            String[] relativePathSplit = source.trim().split("\\/");

            for (int i = 0; i < relativePathSplit.length; i++) {
                String pathSegment = relativePathSplit[i];
                if (pathSegment.equals("..")) {
                    file = file.getParentFile();
                } else {
                    file = new File(file, pathSegment);
                }
            }
        } else {
            if (packageName.contains(".")) {
                String[] packageNameSplit = packageName.split("\\.");
                for (int i = packageNameSplit.length - 1; i >= 0; i--) {
                    if (file.getName().equals(packageNameSplit[i])) {
                        file = file.getParentFile();
                    }
                }
            }

            file = new File(file, source.substring(1));
        }

        return file.getPath();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EmbedDigest that = (EmbedDigest) o;

        if (fullPath != null ? !fullPath.equals(that.fullPath) : that.fullPath != null) return false;
        if (mimeType != null ? !mimeType.equals(that.mimeType) : that.mimeType != null) return false;
        if (source != null ? !source.equals(that.source) : that.source != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = source != null ? source.hashCode() : 0;
        result = 31 * result + (mimeType != null ? mimeType.hashCode() : 0);
        result = 31 * result + (fullPath != null ? fullPath.hashCode() : 0);
        return result;
    }
}
