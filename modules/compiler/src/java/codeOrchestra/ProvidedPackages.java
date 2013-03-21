package codeOrchestra;

/**
 * @author Anton.I.Neverov
 */
public class ProvidedPackages {
    public static String[] packages = new String[] {
            "codeOrchestra.actionScript.collections.util.ModelDependencies_codeOrchestra_actionScript_collections_util",
            "codeOrchestra.actionScript.enums.util.ModelDependencies_codeOrchestra_actionScript_enums_util",
            "codeOrchestra.actionScript.extensionMethods.methods.ModelDependencies_codeOrchestra_actionScript_extensionMethods_methods",
            "codeOrchestra.actionScript.liveCoding.util.ModelDependencies_codeOrchestra_actionScript_liveCoding_util",
            "codeOrchestra.actionScript.logging.logUtil.ModelDependencies_codeOrchestra_actionScript_logging_logUtil",
            "codeOrchestra.actionScript.util.ModelDependencies_codeOrchestra_actionScript_util",
            "org.casalib.control.ModelDependencies_org_casalib_control",
            "org.casalib.core.ModelDependencies_org_casalib_core",
            "org.casalib.errors.ModelDependencies_org_casalib_errors",
            "org.casalib.events.ModelDependencies_org_casalib_events",
            "org.casalib.load.ModelDependencies_org_casalib_load",
            "org.casalib.math.ModelDependencies_org_casalib_math",
            "org.casalib.math.geom.ModelDependencies_org_casalib_math_geom",
            "org.casalib.process.ModelDependencies_org_casalib_process",
            "org.casalib.time.ModelDependencies_org_casalib_time",
            "org.casalib.util.ModelDependencies_org_casalib_util",
            "org.casalib.ui.ModelDependencies_org_casalib_ui"
    };

    public static String[] getClassNames() {
        String[] result = new String[packages.length];
        for (int i = 0; i < packages.length; i++) {
            result[i] = packages[i].substring(packages[i].lastIndexOf(".") + 1);
        }
        return result;
    }

    public static boolean isProvidedPackage(String packageName) {
        for (String s : packages) {
            if (s.startsWith(packageName)) {
                return true;
            }
        }
        return false;
    }
}
