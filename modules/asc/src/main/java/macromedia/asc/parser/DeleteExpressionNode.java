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

/**
 * Node
 */
public class DeleteExpressionNode extends SelectorNode
{
	public int op;
	public Slot slot;

	public DeleteExpressionNode(int op, Node expr)
	{
		super();
		this.op    = op;
		this.expr  = expr;
		ref = null;
		void_result = false;
		slot = null;
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

	public boolean void_result;

	public void voidResult()
	{
		void_result = true;
		//expr.voidResult();
	}
	
    public boolean isQualified()
    {
        QualifiedIdentifierNode qin = expr instanceof QualifiedIdentifierNode ? (QualifiedIdentifierNode) expr : null;
        return qin != null && qin.qualifier != null;
    }
    
    public boolean isAttributeIdentifier()
    {
        return expr instanceof IdentifierNode && ((IdentifierNode) expr).isAttr();
    }
    
    public boolean isAny()
    {
        return expr instanceof IdentifierNode && ((IdentifierNode) expr).isAny();
    }	

	public String toString()
	{
		return "DeleteExpression";
	}
	public boolean isDeleteExpression()
	{
		return true;
	}

    public DeleteExpressionNode clone() throws CloneNotSupportedException
    {
        DeleteExpressionNode result = (DeleteExpressionNode) super.clone();

        if (slot != null) result.slot = slot.clone();

        return result;
    }

    @Override
    public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;

		DeleteExpressionNode that = (DeleteExpressionNode) o;

		return op == that.op && void_result == that.void_result && !(slot != null ? !slot.equals(that.slot) : that.slot != null);

	}

//    @Override
//    public int hashCode() {
//        int result = super.hashCode();
//        result = 31 * result + op;
//        result = 31 * result + (slot != null ? slot.hashCode() : 0);
//        result = 31 * result + (void_result ? 1 : 0);
//        return result;
//    }
}
