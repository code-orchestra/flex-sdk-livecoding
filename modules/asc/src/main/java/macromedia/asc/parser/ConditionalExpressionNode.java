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

/**
 * Node
 */
public class ConditionalExpressionNode extends Node
{
	public Node condition;
	public Node thenexpr;
	public Node elseexpr;

    public Value thenvalue;
    public Value elsevalue;

	public ConditionalExpressionNode(Node condition, Node thenexpr, Node elseexpr)
	{
		this.condition = condition;
		this.thenexpr = thenexpr;
		this.elseexpr = elseexpr;
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
	
	public void voidResult()
	{
		this.thenexpr.voidResult();
		this.elseexpr.voidResult();
	}

	public String toString()
	{
		return "ConditionalExpression";
	}

    public ConditionalExpressionNode clone() throws CloneNotSupportedException
    {
        ConditionalExpressionNode result = (ConditionalExpressionNode) super.clone();

        if (condition != null) result.condition = condition.clone();
        if (elseexpr != null) result.elseexpr = elseexpr.clone();
        if (elsevalue != null) result.elsevalue = elsevalue.clone();
        if (thenexpr != null) result.thenexpr = thenexpr.clone();
        if (thenvalue != null) result.thenvalue = thenvalue.clone();

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ConditionalExpressionNode that = (ConditionalExpressionNode) o;

        if (condition != null ? !condition.equals(that.condition) : that.condition != null) return false;
        if (elseexpr != null ? !elseexpr.equals(that.elseexpr) : that.elseexpr != null) return false;
        if (elsevalue != null ? !elsevalue.equals(that.elsevalue) : that.elsevalue != null) return false;
        if (thenexpr != null ? !thenexpr.equals(that.thenexpr) : that.thenexpr != null) return false;
        if (thenvalue != null ? !thenvalue.equals(that.thenvalue) : that.thenvalue != null) return false;

        return true;
    }

//    @Override
//    public int hashCode() {
//        int result = super.hashCode();
//        result = 31 * result + (condition != null ? condition.hashCode() : 0);
//        result = 31 * result + (thenexpr != null ? thenexpr.hashCode() : 0);
//        result = 31 * result + (elseexpr != null ? elseexpr.hashCode() : 0);
//        result = 31 * result + (thenvalue != null ? thenvalue.hashCode() : 0);
//        result = 31 * result + (elsevalue != null ? elsevalue.hashCode() : 0);
//        return result;
//    }
}
