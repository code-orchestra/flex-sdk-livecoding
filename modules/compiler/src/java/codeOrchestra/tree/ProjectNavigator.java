package codeOrchestra.tree;

import codeOrchestra.LiveCodingUtil;
import macromedia.asc.parser.ClassDefinitionNode;
import macromedia.asc.parser.FunctionDefinitionNode;
import macromedia.asc.parser.ProgramNode;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author Anton.I.Neverov
 */
public class ProjectNavigator {

    public final List<ProgramNode> loadedSyntaxTrees = new LinkedList<ProgramNode>();

    public Set<String> getLiveCodingClassNames(String packageName) {
        // TODO: At this step livecoding classes are not created yet, so we collect names of methods assuming that every one of them will be extracted to some class
        HashSet<String> result = new HashSet<String>();
        for (ProgramNode syntaxTree : loadedSyntaxTrees) {
            ClassDefinitionNode classDefinition = TreeNavigator.getClassDefinition(syntaxTree);
            if (!classDefinition.pkgdef.name.id.pkg_part.equals(packageName)) {
                continue;
            }
            if (!LiveCodingUtil.hasLiveAnnotation(classDefinition)) {
                continue;
            }
            for (FunctionDefinitionNode functionDefinitionNode : TreeNavigator.getMethodDefinitions(classDefinition)) {
                if (!LiveCodingUtil.hasLiveAnnotation(functionDefinitionNode)) {
                    continue;
                }
                result.add(LiveCodingUtil.constructLiveCodingClassName(functionDefinitionNode, classDefinition.name.name));
            }
        }
        return result;
    }

    public Set<String> getClassNames(String packageName) {
        HashSet<String> result = new HashSet<String>();
        for (ProgramNode syntaxTree : loadedSyntaxTrees) {
            ClassDefinitionNode classDefinition = TreeNavigator.getClassDefinition(syntaxTree);
            if (!classDefinition.pkgdef.name.id.pkg_part.equals(packageName)) {
                continue;
            }
            result.add(classDefinition.name.name);
        }
        return result;
    }
}
