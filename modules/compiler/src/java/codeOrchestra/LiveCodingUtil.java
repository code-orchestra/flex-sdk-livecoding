package codeOrchestra;

import codeOrchestra.tree.RegularNode;
import codeOrchestra.tree.TreeNavigator;
import codeOrchestra.tree.TreeUtil;
import macromedia.asc.parser.*;

/**
 * @author Anton.I.Neverov
 * @author Alexander Eliseyev
 */
public class LiveCodingUtil {

    public static final String LIVE_ANNOTATION = "Live";
    public static final String LIVE_CODE_DISABLE_ANNOTATION = "LiveCodeDisable";
    public static final String CODE_UPDATE_METHOD_ANNOTATION = "CodeUpdateMethod";
    public static final String LIVE_CODE_UPDATE_LISTENER_ANNOTATION = "LiveCodeUpdateListener";
    public static final String LIVE_CONSOLE_ANNOTATION = "LiveConsole";

    public static boolean canBeUsedForLiveCoding(ClassDefinitionNode cl) {
        if (cl == null) {
            return false;
        }
        if (cl.isInterface()) {
            return false;
        }
        if (hasAnnotation(cl, LIVE_CODE_DISABLE_ANNOTATION)) {
            return false;
        }
        if (LiveCodingCLIParameters.getLiveMethods() == LiveMethods.ANNOTATED && !hasAnnotation(cl, LIVE_ANNOTATION)) {
            return false;
        }
        if (ProvidedPackages.isProvidedPackage(cl.pkgdef.name.id.pkg_part)) {
            return false;
        }

        return true;
    }

    public static boolean canBeUsedForLiveCoding(FunctionDefinitionNode function) {
        if (function == null) {
            return false;
        }
        if (function.skipLiveCoding) {
            return false;
        }
        if (TreeNavigator.isGetter(function) || TreeNavigator.isSetter(function)) {
            if (!LiveCodingCLIParameters.makeGettersSettersLive()) {
                return false;
            }
        }
        if (hasAnnotation(function, LIVE_CODE_DISABLE_ANNOTATION)) {
            return false;
        }
        if (hasAnnotation(function, CODE_UPDATE_METHOD_ANNOTATION)) {
            return false;
        }
        if (!new RegularNode(function.fexpr.body).getDescendants(MetaDataNode.class).isEmpty()) {
            try {
                function.cx.localizedWarning(function.pos(), "[COLT] Meta tags not supported within Live methods");
            } catch (Throwable t) {
                // ignore
            }
            return false;
        }
        for (RegularNode regularNode : new RegularNode(function.fexpr.body).getDescendants(MemberExpressionNode.class)) {
            MemberExpressionNode memberExpressionNode = (MemberExpressionNode) regularNode.getASTNode();
            if (memberExpressionNode.base != null && TreeUtil.isEqualToIdentifier("arguments", memberExpressionNode.base)) {
                if (memberExpressionNode.selector != null && memberExpressionNode.selector.getIdentifier() != null && "callee".equals(memberExpressionNode.selector.getIdentifier().name)) {
                    try {
                        function.cx.localizedWarning(function.pos(), "[COLT] arguments.callee not supported within Live methods");
                    } catch (Throwable t) {
                        // ignore
                    }
                    return false;
                }
            }
        }


        return true;
    }

    public static boolean isLiveCodeUpdateListener(FunctionDefinitionNode function) {
        return hasAnnotation(function, LIVE_CODE_UPDATE_LISTENER_ANNOTATION) || hasAnnotation(function, LIVE_CONSOLE_ANNOTATION);
    }

    public static String constructLiveCodingClassName(FunctionDefinitionNode functionDefinitionNode, String className) {
        return "LiveMethod_" + constructLiveCodingMethodId(functionDefinitionNode, className).replaceAll("\\.", "_");
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

    public static MetaDataNode getAnnotation(DefinitionNode definitionNode, String annotation) {
        if (definitionNode.metaData == null) { return null; }

        for (Node item : definitionNode.metaData.items) {
            if (item instanceof MetaDataNode) {
                if (annotation.equals(((MetaDataNode) item).getId())) {
                    return (MetaDataNode) item;
                }
            }
        }

        return null;
    }

    public static boolean hasAnnotation(DefinitionNode definitionNode, String annotation) {
        if (definitionNode.metaData == null) { return false; }

        for (Node item : definitionNode.metaData.items) {
            if (item instanceof MetaDataNode) {
                if (annotation.equals(((MetaDataNode) item).getId())) {
                    return true;
                }
            }
        }

        return false;
    }

}
