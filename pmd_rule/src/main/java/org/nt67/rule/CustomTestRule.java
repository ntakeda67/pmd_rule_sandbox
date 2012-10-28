package org.nt67.rule;

import net.sourceforge.pmd.AbstractJavaRule;
import net.sourceforge.pmd.ast.ASTCompilationUnit;

public class CustomTestRule extends AbstractJavaRule {
    @Override
    public Object visit(ASTCompilationUnit node, Object data) {
	System.out.println(getName());
	node.dump("> ");
	System.out.println();

	return super.visit(node, data);
    }
}