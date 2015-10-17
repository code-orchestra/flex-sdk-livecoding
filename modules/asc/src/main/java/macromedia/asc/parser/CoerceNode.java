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
public class CoerceNode extends Node
{
	public Node expr;
	public TypeInfo actual;
	public TypeInfo expected;
	public boolean void_result;
	public boolean is_explicit;

	public CoerceNode(Node expr, TypeInfo actual, TypeInfo expected, boolean is_explicit)
	{
		this.expr = expr;
		this.actual = actual;
		this.expected = expected;
		void_result = false;
		this.is_explicit = is_explicit;
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
		void_result = true;
		expr.voidResult();
	}

    public CoerceNode clone() throws CloneNotSupportedException
    {
        CoerceNode result = (CoerceNode) super.clone();

        if (actual != null) result.actual = actual.clone();
        if (expected != null) result.expected = expected.clone();
        if (expr != null) result.expr = expr.clone();

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        CoerceNode that = (CoerceNode) o;

        if (is_explicit != that.is_explicit) return false;
        if (void_result != that.void_result) return false;
        if (actual != null ? !actual.equals(that.actual) : that.actual != null) return false;
        if (expected != null ? !expected.equals(that.expected) : that.expected != null) return false;
        if (expr != null ? !expr.equals(that.expr) : that.expr != null) return false;

        return true;
    }

//    @Override
//    public int hashCode() {
//        int result = super.hashCode();
//        result = 31 * result + (expr != null ? expr.hashCode() : 0);
//        result = 31 * result + (actual != null ? actual.hashCode() : 0);
//        result = 31 * result + (expected != null ? expected.hashCode() : 0);
//        result = 31 * result + (void_result ? 1 : 0);
//        result = 31 * result + (is_explicit ? 1 : 0);
//        return result;
//    }
}
