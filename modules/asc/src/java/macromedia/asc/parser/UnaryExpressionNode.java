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
 *
 * @author Jeff Dyer
 */
public class UnaryExpressionNode extends Node
{
	public Node expr;
	public int op;
	public ReferenceValue ref;
	public Slot slot;
    public NumberUsage numberUsage;

	public UnaryExpressionNode(int op, Node expr)
	{
		this.op = op;
		this.expr = expr;
		void_result = false;
		slot = null;
		numberUsage = null;
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

	boolean isbooleaneanExpression()
	{
		return op == NOT_TOKEN ? true : false;
	}

	public String toString()
	{
		return "UnaryExpression";
	}

    public UnaryExpressionNode clone() throws CloneNotSupportedException
    {
        UnaryExpressionNode result = (UnaryExpressionNode) super.clone();

        if (expr != null) result.expr = expr.clone();
        if (numberUsage != null) result.numberUsage = numberUsage.clone();
        if (ref != null) result.ref = ref.clone();
        if (slot != null) result.slot = slot.clone();

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        UnaryExpressionNode that = (UnaryExpressionNode) o;

        if (op != that.op) return false;
        if (void_result != that.void_result) return false;
        if (expr != null ? !expr.equals(that.expr) : that.expr != null) return false;
        if (numberUsage != null ? !numberUsage.equals(that.numberUsage) : that.numberUsage != null) return false;
        if (ref != null ? !ref.equals(that.ref) : that.ref != null) return false;
        if (slot != null ? !slot.equals(that.slot) : that.slot != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (expr != null ? expr.hashCode() : 0);
        result = 31 * result + op;
        result = 31 * result + (ref != null ? ref.hashCode() : 0);
        result = 31 * result + (slot != null ? slot.hashCode() : 0);
        result = 31 * result + (numberUsage != null ? numberUsage.hashCode() : 0);
        result = 31 * result + (void_result ? 1 : 0);
        return result;
    }
}
