package codeOrchestra;

import java.io.File;

/**
 * @author Alexander Eliseyev
 */
public class LiveCodingCLIParameters {

    private static final int DEFAULT_MAX_ITERATIONS_COUNT = 10000;
    private static final int DEFAULT_TRACE_HOST = 6126;
    private static final String DEFAULT_LOCALHOST = "localhost";

    public static String getTraceHost() {
        String traceHost = System.getProperty("codeOrchestra.trace.host");
        if (traceHost == null) {
            return DEFAULT_LOCALHOST;
        }
        return traceHost;
    }

    public static int getTracePort() {
        String tracePort = System.getProperty("codeOrchestra.trace.port");
        if (tracePort == null) {
            return DEFAULT_TRACE_HOST;
        }

        try {
            return Integer.valueOf(tracePort);
        } catch (NumberFormatException e) {
            return DEFAULT_TRACE_HOST;
        }
    }

    public static String getProfilingFolder() {
        return new File(getDigestsFolder()).getParent();
    }

    public static String getDigestsFolder() {
        return System.getProperty("codeOrchestra.digestsDir");
    }

    public static LiveMethods getLiveMethods() {
        String liveMethodsStringValue = System.getProperty("codeOrchestra.live.liveMethods");
        if (liveMethodsStringValue == null) {
            return LiveMethods.ANNOTATED;
        }

        return LiveMethods.parseValue(liveMethodsStringValue);
    }

    public static boolean makeGettersSettersLive() {
        return Boolean.parseBoolean(System.getProperty("codeOrchestra.live.gettersSetters"));
    }

    public static int getMaxLoopIterations() {
        String maxIterationsStr = System.getProperty("codeOrchestra.live.maxLoops");
        if (maxIterationsStr == null) {
            return DEFAULT_MAX_ITERATIONS_COUNT;
        }

        try {
            return Integer.valueOf(maxIterationsStr);
        } catch (NumberFormatException e) {
            return DEFAULT_MAX_ITERATIONS_COUNT;
        }
    }

}
