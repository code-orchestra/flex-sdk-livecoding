package codeOrchestra;

import codeOrchestra.tree.TreeNavigator;
import macromedia.asc.parser.*;

/**
 * @author Anton.I.Neverov
 */
public class LiveCodingUtil {

    public static String constructLiveCodingClassName(FunctionDefinitionNode functionDefinitionNode, String className) {
        return"LiveMethod_" + constructLiveCodingMethodId(functionDefinitionNode, className).replaceAll("\\.", "_");
    }

    public static String constructLiveCodingMethodId(FunctionDefinitionNode functionDefinitionNode, String className) {
        StringBuilder builder = new StringBuilder(functionDefinitionNode.pkgdef.name.id.pkg_part); // TODO: Check empty package
        builder.append(".");
        builder.append(className);
        builder.append(".");
        if (TreeNavigator.isStaticMethod(functionDefinitionNode)) {
            builder.append("static.");
        }
        if (TreeNavigator.isGetter(functionDefinitionNode)) {
            builder.append("get.");
        } else if (TreeNavigator.isSetter(functionDefinitionNode)) {
            builder.append("set.");
        }
        builder.append(functionDefinitionNode.name.identifier.name);
        return builder.toString();
    }

    public static boolean hasLiveAnnotation(DefinitionNode definitionNode) {
        if (definitionNode.metaData == null) { return false; }
        for (Node item : definitionNode.metaData.items) {
            if (item instanceof MetaDataNode) {
                if ("Live".equals(((MetaDataNode) item).getId()) && definitionNode instanceof ClassDefinitionNode) {
                    return true;
                }
                if ("LiveCodeUpdateListener".equals(((MetaDataNode) item).getId()) && definitionNode instanceof FunctionDefinitionNode) {
                    return true;
                }
            }
        }
        return false;
    }
}
