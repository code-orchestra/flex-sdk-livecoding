package codeOrchestra.tree.visitor;

import macromedia.asc.parser.ClassNameNode;

/**
 * @author Anton.I.Neverov
 */
public class ClassNameNodeVisitor extends NodeVisitor<ClassNameNode> {
    @Override
    protected StuffToCompare createStuffToCompare(ClassNameNode left, ClassNameNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();



        return stuffToCompare;
    }
}
