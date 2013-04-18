package codeOrchestra.util;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexander Eliseyev
 */
public class FileUtils {

    public static List<File> listFileRecursively(File dir, FileFilter fileFilter) {
        assert dir.isDirectory();
        List<File> files = new ArrayList<File>();

        File[] subdirs = dir.listFiles(new FileFilter() {
            public boolean accept(File f) {
                return f.isDirectory() && !(".svn".equals(f.getName()));
            }
        });

        if (subdirs != null) {
            for (File subdir : subdirs) {
                files.addAll(listFileRecursively(subdir, fileFilter));
            }
            addArrayToList(files, dir.listFiles(fileFilter));
        }

        return files;
    }

    public static boolean clear(File dir) {
        File[] files = dir.listFiles();
        if (files == null) return true;

        boolean result = true;

        for (File f : files) {
            boolean r = delete(f);
            result = result && r;
        }

        return result;
    }

    public static boolean delete(File root) {
        boolean result = true;

        if (root.isDirectory()) {
            for (File child : root.listFiles()) {
                result = delete(child) && result;
            }
        }
        // !result means one of children was not deleted, hence you may not delete this directory
        return result && root.delete();
    }

    private static void addArrayToList(List<File> list, File[] array) {
        for (File file : array) {
            list.add(file);
        }
    }

}
