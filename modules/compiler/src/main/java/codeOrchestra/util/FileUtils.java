package codeOrchestra.util;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Alexander Eliseyev
 */
public class FileUtils {

    public static void read(byte[] bytes, InputStream stream) throws IOException {
        int offset = 0;
        int len = bytes.length;

        while (true) {
            int number = stream.read(bytes, offset, len - offset);
            if (number == -1 || len == offset) break;
            offset += number;
        }

        if (offset != len) {
            throw new RuntimeException("This can't happen");
        }
    }

    public static byte[] read(InputStream is) throws IOException {
        return read(is, 4096);
    }

    public static byte[] read(InputStream is, int size) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream(size);
        byte[] buff = new byte[4096];
        while (true) {
            int read = is.read(buff, 0, buff.length);
            if (read == -1) break;
            os.write(buff, 0, read);
        }
        return os.toByteArray();
    }

    public static void copyFileChecked(File f, File to) throws IOException {
        copyFileChecked(f, to, false);
    }

    public static void copyFile(File f, File to) {
        try {
            copyFileChecked(f, to);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void copyFileChecked(File f, File to, boolean checkEquals) throws IOException {
        File target;
        if (to.isDirectory()) {
            target = new File(to, f.getName());
        } else {
            target = to;
        }

        if (!to.getParentFile().exists()) {
            to.getParentFile().mkdirs();
        }

        if (checkEquals && target.exists()) {
            String existingContents = FileUtils.read(target);
            if (existingContents.equals(FileUtils.read(f))) {
                return;
            }
        }

        byte[] bytes = new byte[(int) f.length()];

        OutputStream os = new FileOutputStream(target);
        FileInputStream is = new FileInputStream(f);

        read(bytes, is);
        os.write(bytes);

        is.close();
        os.close();
    }

    public static String read(File file) {
        try {
            return read(new FileReader(file));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String read(Reader reader) {
        BufferedReader r = null;
        try {
            r = new BufferedReader(reader);

            StringBuilder result = new StringBuilder();

            String line = null;
            while ((line = r.readLine()) != null) {
                result.append(line).append("\n");
            }

            return result.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (r != null) {
                    r.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String readLine(Reader reader, int lineNo) {
        BufferedReader r = null;
        try {
            r = new BufferedReader(reader);

            String line = null;
            int currentLine = 0;
            while ((line = r.readLine()) != null) {
                if (currentLine == lineNo) {
                    return line;
                }
                currentLine++;
            }

            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (r != null) {
                    r.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static List<File> listFileRecursively(File dir, FileFilter fileFilter) {
        assert dir.isDirectory();
        List<File> files = new ArrayList<>();

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
        Collections.addAll(list, array);
    }

}
