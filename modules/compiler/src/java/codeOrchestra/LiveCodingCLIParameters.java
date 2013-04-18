package codeOrchestra;

/**
 * @author Alexander Eliseyev
 */
public class LiveCodingCLIParameters {

    private static final int DEFAULT_MAX_ITERATIONS_COUNT = 10000;

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
