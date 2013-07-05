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
public class BoxNode extends Node
{
	public Node expr;
	public TypeValue actual;

	public BoxNode(Node expr, TypeValue actual)
	{
		this.expr = expr;
		this.actual = actual;
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

	public boolean isLiteralInteger()
	{
		return expr.isLiteralInteger();
	}


	public boolean void_result;

	public void voidResult()
	{
		void_result = true;
		expr.voidResult();
	}

    public BoxNode clone() throws CloneNotSupportedException
    {
        BoxNode result = (BoxNode) super.clone();

        if (actual != null) result.actual = actual.clone();
        if (expr != null) result.expr = expr.clone();

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        BoxNode boxNode = (BoxNode) o;

        if (void_result != boxNode.void_result) return false;
        if (actual != null ? !actual.equals(boxNode.actual) : boxNode.actual != null) return false;
        if (expr != null ? !expr.equals(boxNode.expr) : boxNode.expr != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (expr != null ? expr.hashCode() : 0);
        result = 31 * result + (actual != null ? actual.hashCode() : 0);
        result = 31 * result + (void_result ? 1 : 0);
        return result;
    }
}
