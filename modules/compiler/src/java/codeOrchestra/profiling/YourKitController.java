package codeOrchestra.profiling;

import codeOrchestra.LiveCodingCLIParameters;
import codeOrchestra.util.FileUtils;
import com.yourkit.api.Controller;
import com.yourkit.api.ProfilingModes;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author Alexander Eliseyev
 */
public class YourKitController {

    public static final long CPU_PROFILING_MODE = ProfilingModes.CPU_SAMPLING;

    public static YourKitController getInstance() throws Exception {
        if (INSTANCE == null) {
            INSTANCE = new YourKitController();
        }
        return INSTANCE;
    }

    private static YourKitController INSTANCE;

    private Controller controller;
    private boolean inProgress;

    private YourKitController() {
        try {
            this.controller = new Controller();
        } catch (Exception e) {
            throw new RuntimeException("Can't instantiate the YourKit controller", e);
        }
    }

    public void startCPUProfiling() throws Exception {
        if (inProgress) {
            return;
        }

        controller.startCPUProfiling(CPU_PROFILING_MODE, Controller.DEFAULT_FILTERS, Controller.DEFAULT_WALLTIME_SPEC);
        inProgress = true;
    }


    public String stopCPUProfilingAndSaveSnapshot() throws Exception {
        if (!(inProgress)) {
            return null;
        }
        controller.stopCPUProfiling();
        inProgress = false;

        String path = controller.captureSnapshot(ProfilingModes.SNAPSHOT_WITHOUT_HEAP);

        return copyIt(zipIt(renameIt(path)));
    }

    private String renameIt(String path) {
        try {

            String date = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date(System.currentTimeMillis()));
            File file = new File(path);
            String dir = file.getParentFile().getCanonicalPath();
            String name = file.getName();
            name = name.substring(name.lastIndexOf("."));
            String dumpOrSnapshot;
            if (name.equals(".hprof")) {
                dumpOrSnapshot = "dump";
            } else if (name.equals(".snapshot")) {
                dumpOrSnapshot = "snapshot";
            } else {
                return path;
            }
            name = "COLT_compiler_" + dumpOrSnapshot + "_" + date + name;
            File newFile = new File(dir, name);
            boolean b = file.renameTo(newFile);
            return (b ?
                    newFile.getAbsolutePath() :
                    path
            );

        } catch (Throwable t) {
            return path;
        }
    }

    private String zipIt(final String path) {
        try {

            String zipFilePath = path.substring(0, path.lastIndexOf(".")) + ".zip";

            ZipOutputStream zipOutputStream = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFilePath)));
            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(path));

            File file = new File(path);
            ZipEntry zipEntry = new ZipEntry(file.getName());
            zipOutputStream.putNextEntry(zipEntry);

            int bufferSize = 4096;
            byte[] buffer = new byte[bufferSize];
            int count;
            while ((count = bufferedInputStream.read(buffer, 0, bufferSize)) != -1) {
                zipOutputStream.write(buffer, 0, count);
            }

            bufferedInputStream.close();
            zipOutputStream.close();

            file.delete();
            return zipFilePath;

        } catch (Throwable t) {
            return path;
        }
    }

    private String copyIt(String path) {
        File oldFile = new File(path);
        FileUtils.copyFile(oldFile, new File(getProfilingDirectory()));

        String newPath = getProfilingDirectory() + File.separator + oldFile.getName();

        oldFile.delete();

        return newPath;
    }

    private String getProfilingDirectory() {
        File profilingFolder = new File(LiveCodingCLIParameters.getProfilingFolder());
        if (!profilingFolder.exists()) {
            profilingFolder.mkdirs();
        }
        return profilingFolder.getPath();
    }

    public static boolean isJVMLaunchedWithAgent() {
        try {
            List<String> args = ManagementFactory.getRuntimeMXBean().getInputArguments();
            for (String arg : args) {
                if (arg.startsWith("-agentpath:") && arg.contains("yjpagent.")) {
                    return true;
                }
            }
            return false;
        } catch (Throwable t) {
            return false;
        }
    }

}
