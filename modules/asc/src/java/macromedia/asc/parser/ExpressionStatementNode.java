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

import macromedia.asc.util.*;
import macromedia.asc.semantics.*;
import static macromedia.asc.util.BitSet.*;

/**
 * Node
 */
public class ExpressionStatementNode extends Node
{
	public Node expr;
	public BitSet gen_bits;
	public ReferenceValue ref;
	public TypeValue expected_type;
	public boolean is_var_stmt;

	public ExpressionStatementNode(Node expr)
	{
		this.expr = expr;
		gen_bits = null;
		ref = null;
		expected_type = null;
		is_var_stmt = false;
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

	public BitSet getGenBits()
	{
		return or(expr.getGenBits(), gen_bits); // union of expr and cv bits.
	}

	public BitSet getKillBits()
	{
		BitSet kb = expr.getKillBits();

		// Union kill bits of the embedded expression and
		// the kill bits of the completion value definition.

		if (ref != null && ref.slot != null)
		{
			if (ref.slot.getDefBits() != null)
			{
				return or(kb, xor(ref.slot.getDefBits(),gen_bits));
			}
			else
			{
				return or(kb, gen_bits);
			}
		}
		else
		{
			return kb;
		}
	}

	public String toString()
	{
      if(Node.useDebugToStrings)
         return "ExpressionStatement@" + pos();
      else
         return "ExpressionStatement";
	}

	public ReferenceValue getRef(Context cx)
	{
		if (ref == null)
		{
			ref = new ReferenceValue(cx, null, "_cv", ObjectValue.internalNamespace);
		}
		return ref;
	}

	public void expectedType(TypeValue type)
	{
		expected_type = type;
   		expr.expectedType(type);
	}

	public void isVarStatement(boolean b)
	{
		is_var_stmt = b;
	}

	public boolean isVarStatement()
	{
		return is_var_stmt;
	}

	public boolean isExpressionStatement()
	{
		return true;
	}

	public boolean isAttribute()
	{
		return expr.isAttribute();
	}
	
	public void voidResult()
	{
	    expr.voidResult();
	}
	
	public boolean isConfigurationName()
	{
		return expr.isConfigurationName();
	}

	private boolean skip = false;
	public void skipNode(boolean b)
	{
		skip = b;
	}

	public boolean skip()
	{
		return skip;
	}

    public ExpressionStatementNode clone() throws CloneNotSupportedException
    {
        ExpressionStatementNode result = (ExpressionStatementNode) super.clone();

        //if (expected_type != null) result.expected_type = expected_type.clone();
        if (expr != null) result.expr = expr.clone();
        if (gen_bits != null) result.gen_bits = BitSet.copy(gen_bits);
        if (ref != null) result.ref = ref.clone();

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ExpressionStatementNode that = (ExpressionStatementNode) o;

        if (is_var_stmt != that.is_var_stmt) return false;
        if (skip != that.skip) return false;
//        if (expected_type != null ? !expected_type.equals(that.expected_type) : that.expected_type != null)
//            return false;
        if (expr != null ? !expr.equals(that.expr) : that.expr != null) return false;
        if (gen_bits != null ? !gen_bits.equals(that.gen_bits) : that.gen_bits != null) return false;
        if (ref != null ? !ref.equals(that.ref) : that.ref != null) return false;

        return true;
    }

//    @Override
//    public int hashCode() {
//        int result = super.hashCode();
//        result = 31 * result + (expr != null ? expr.hashCode() : 0);
//        result = 31 * result + (gen_bits != null ? gen_bits.hashCode() : 0);
//        result = 31 * result + (ref != null ? ref.hashCode() : 0);
//        result = 31 * result + (expected_type != null ? expected_type.hashCode() : 0);
//        result = 31 * result + (is_var_stmt ? 1 : 0);
//        result = 31 * result + (skip ? 1 : 0);
//        return result;
//    }
}
