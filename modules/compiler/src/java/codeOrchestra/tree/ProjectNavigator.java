package codeOrchestra.tree;

import codeOrchestra.LiveCodingUtil;
import macromedia.asc.parser.*;

import java.util.*;

/**
 * @author Anton.I.Neverov
 */
public class ProjectNavigator {

    public final List<ProgramNode> loadedSyntaxTrees = new LinkedList<ProgramNode>();
    public Map<String, Set<String>> modelDependencies;

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

    public Set<String> getModelDependencies(String packageName) {
        if (modelDependencies == null) {
            modelDependencies = new HashMap<String, Set<String>>();

            // Fill user packages
            for (ProgramNode programNode : loadedSyntaxTrees) {
                ClassDefinitionNode classDefinition = TreeNavigator.getClassDefinition(programNode);
                String name = classDefinition.pkgdef.name.id.pkg_part;
                modelDependencies.put(name, new HashSet<String>() {
                    @Override
                    public boolean add(String s) {
                        return modelDependencies.containsKey(s) && super.add(s);
                    }
                });
            }

            // Fill filtered dependencies
            for (ProgramNode programNode : loadedSyntaxTrees) {
                ClassDefinitionNode classDefinition = TreeNavigator.getClassDefinition(programNode);
                PackageDefinitionNode pkgdef = classDefinition.pkgdef;
                String name = pkgdef.name.id.pkg_part;

                Set<String> deps = modelDependencies.get(name);

                for (ImportDirectiveNode importDirectiveNode : TreeNavigator.getImports(pkgdef)) {
                    deps.add(importDirectiveNode.name.id.pkg_part);
                }
            }
        }

        return modelDependencies.get(packageName);
    }
}
