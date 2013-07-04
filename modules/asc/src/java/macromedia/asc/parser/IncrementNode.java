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
 *
 * @author Jeff Dyer
 */
public class IncrementNode extends SelectorNode
{
	public int op;
	public boolean isPostfix;
	public Slot slot;
	public NumberUsage numberUsage;

	public IncrementNode(int op, Node expr, boolean isPostfix)
	{
		this.op    = op;
        this.expr  = expr;
        this.isPostfix = isPostfix;
		slot = null;
		ref = null;
        numberUsage = null;
		void_result = false;
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
		expr.voidResult();
	}

    public boolean isQualified()
    {
        QualifiedIdentifierNode qin = expr instanceof QualifiedIdentifierNode ? (QualifiedIdentifierNode) expr : null;
        return qin!=null?qin.qualifier!=null:false;
    }
    
    public boolean isAttributeIdentifier()
    {
    	return expr instanceof IdentifierNode ? ((IdentifierNode)expr).isAttr() : false;
    }
    
    public boolean isAny()
    {
    	return expr instanceof IdentifierNode ? ((IdentifierNode)expr).isAny() : false;
    }

    public String toString()
	{
		return "IncrementNode";
	}

    public boolean hasSideEffect()
    {
        return true;
    }

    public IncrementNode clone() throws CloneNotSupportedException
    {
        IncrementNode result = (IncrementNode) super.clone();

        if (numberUsage != null) result.numberUsage = numberUsage.clone();
        if (slot != null) result.slot = slot.clone();

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        IncrementNode that = (IncrementNode) o;

        if (isPostfix != that.isPostfix) return false;
        if (op != that.op) return false;
        if (void_result != that.void_result) return false;
        if (numberUsage != null ? !numberUsage.equals(that.numberUsage) : that.numberUsage != null) return false;
        if (slot != null ? !slot.equals(that.slot) : that.slot != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + op;
        result = 31 * result + (isPostfix ? 1 : 0);
        result = 31 * result + (slot != null ? slot.hashCode() : 0);
        result = 31 * result + (numberUsage != null ? numberUsage.hashCode() : 0);
        result = 31 * result + (void_result ? 1 : 0);
        return result;
    }
}
