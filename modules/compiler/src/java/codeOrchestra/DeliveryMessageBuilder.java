package codeOrchestra;

import codeOrchestra.tree.TreeNavigator;
import macromedia.asc.parser.ClassDefinitionNode;
import macromedia.asc.parser.FunctionDefinitionNode;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Alexander Eliseyev
 */
public class DeliveryMessageBuilder {

    private String fqName;
    private List<FunctionDefinitionNode> definitionNodes;
    private Map<FunctionDefinitionNode, String> functionToClassNames;
    private ClassDefinitionNode classDefinitionNode;

    public DeliveryMessageBuilder(String fqName, List<FunctionDefinitionNode> definitionNodes, Map<FunctionDefinitionNode, String> functionToClassNames, ClassDefinitionNode classDefinitionNode) {
        this.fqName = fqName.replace(":", ".");
        this.definitionNodes = definitionNodes;
        this.functionToClassNames = functionToClassNames;
        this.classDefinitionNode = classDefinitionNode;
    }

    public String build() {
        StringBuilder sb = new StringBuilder();

        Iterator<FunctionDefinitionNode> iterator = definitionNodes.iterator();
        while (iterator.hasNext()) {
            FunctionDefinitionNode functionDefinitionNode = iterator.next();
            String deliveryClassName = functionToClassNames.get(functionDefinitionNode);

            sb.append("method").append(":");
            sb.append("codeOrchestra.liveCoding.load.").append(deliveryClassName).append(":");
            sb.append(fqName).append(":");
            sb.append(functionDefinitionNode.name.identifier.name).append(":");
            sb.append(LiveCodingUtil.constructLiveCodingMethodId(functionDefinitionNode, shortNameFromLongName(fqName))).append(":");
            sb.append(TreeNavigator.isStaticMethod(functionDefinitionNode) ? "1" : "0").append(":");

            // TODO: add static initializer support
            if (TreeNavigator.isSetter(functionDefinitionNode)) {
                sb.append("2");
            } else if (TreeNavigator.isGetter(functionDefinitionNode)) {
                sb.append("3");
            } else if (TreeNavigator.isConstructor(functionDefinitionNode, classDefinitionNode)) {
                sb.append("4");
            } else {
                sb.append("1");
            }
            sb.append(":");

            sb.append(System.currentTimeMillis()); // TODO: wrong timestamp?

            if (iterator.hasNext()) {
                sb.append("|");
            }
        }

        return sb.toString();
    }

    private static String shortNameFromLongName(String fqName) {
        if (fqName == null) return fqName;
        int offset = fqName.lastIndexOf('.');
        if (offset < 0) return fqName;

        return fqName.substring(offset + 1);
    }


//    private String getMethodId

}
