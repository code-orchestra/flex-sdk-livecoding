/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package macromedia.asc.parser;

import macromedia.asc.semantics.*;
import macromedia.asc.util.*;

import static macromedia.asc.parser.Tokens.*;

/**
 * Node
 */
public class VariableDefinitionNode extends DefinitionNode
{
	public int kind;
	public ListNode list;
	public transient Context cx; // CodeOrchestra: made transient

	public VariableDefinitionNode(PackageDefinitionNode pkgdef, AttributeListNode attrs, int kind, ListNode list, int pos)
	{
		super(pkgdef,attrs,pos);
		this.kind = kind;
		this.list = list;
		this.cx = null;
	}

	public Value evaluate(Context cx, Evaluator evaluator)
	{
		if (evaluator.checkFeature(cx, this))
		{
			return evaluator.evaluate(cx, this);
		}
		else
		{
			return null;
		}
	}

	public boolean hasAttribute(String name)
	{
		return attrs != null && attrs.hasAttribute(name);
	}

	public Node initializerStatement(Context cx)
	{
		NodeFactory nodeFactory = cx.getNodeFactory();
		ListNode list = this.list;
		ListNode init_list = null;

		// if there are attributes, then create a special qualified identifier
		// by using the attributes as the potential qualifier. We don't yet
		// know if any of the attributes are actually namespaces, so the qulaified
		// identifier will have to check when it is evaluated, and return an unqualified
		// reference if not.

        if( !this.isConst() )
		for (Node n : list.items)
		{
//			if( n instanceof VariableBindingNode )
			{
				VariableBindingNode binding = (VariableBindingNode) n;
	
	            // If its a simple var declaration, then hoist to the regional namespace ("public")
	
	            if( binding.attrs == null && binding.variable.type == null )
	            {
	                NodeFactory nf = cx.getNodeFactory();
	                AttributeListNode aln = null; // This will get filled in correctly when FA evaluates the VariableBindingNode
	                binding.variable.identifier = nf.qualifiedIdentifier(aln,binding.variable.identifier.name, binding.variable.identifier.pos());
	            }
	
	
	            if (binding.initializer != null)
				{
	            	Node assign_node = nodeFactory.assignmentExpression(binding.variable.identifier,binding.kind==CONST_TOKEN?CONST_TOKEN:ASSIGN_TOKEN,binding.initializer);
	            	if( assign_node instanceof MemberExpressionNode && ((MemberExpressionNode)assign_node).selector instanceof SetExpressionNode)
	            	{
	            		((SetExpressionNode)((MemberExpressionNode)assign_node).selector).is_initializer = true;
	            	}
					init_list = nodeFactory.list(init_list,assign_node);
				}
	            else
	            {
	            }
			}
		}

		if (init_list != null)
		{
			ExpressionStatementNode init = nodeFactory.expressionStatement(init_list);
			init.isVarStatement(true); // var statements always have a empty result
			return init;
		}
		else
		{
			return nodeFactory.emptyStatement();
		}
	}

	public int countVars()
	{
		return list.size();
	}

	public void setContext(Context cx) {
	   	this.cx = cx;
	}

	public Context getContext() {
	    	return this.cx;
	}

	public String toString()
	{
		if(Node.useDebugToStrings)
         return "VarDefinition@" + pos();
      else
         return "VarDefinition";
	}

    public VariableDefinitionNode clone() throws CloneNotSupportedException
    {
        VariableDefinitionNode result = (VariableDefinitionNode) super.clone();

        // cx is transient
        //if (cx != null);
        result.cx = null;
        if (list != null) result.list = list.clone();

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        VariableDefinitionNode that = (VariableDefinitionNode) o;

        if (kind != that.kind) return false;
        //if (cx != null ? !cx.equals(that.cx) : that.cx != null) return false;
		return !(list != null ? !list.equals(that.list) : that.list != null);

	}

//    @Override
//    public int hashCode() {
//        int result = super.hashCode();
//        result = 31 * result + kind;
//        result = 31 * result + (list != null ? list.hashCode() : 0);
//        result = 31 * result + (cx != null ? cx.hashCode() : 0);
//        return result;
//    }
}
