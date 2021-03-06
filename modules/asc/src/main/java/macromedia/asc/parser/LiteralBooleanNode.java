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
public class LiteralBooleanNode extends Node
{
	public boolean value;
	public boolean void_result;

    public LiteralBooleanNode(boolean value)
	{
		this.value = value;
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

    public boolean isLiteral()
    {
        return true;
    }

	public void voidResult()
	{
		void_result = true;
	}

	public boolean isBooleanExpression()
	{
		return true;
	}

	public String toString()
	{
		return "LiteralBoolean";
	}

    public LiteralBooleanNode clone() throws CloneNotSupportedException
    {

		return (LiteralBooleanNode) super.clone();
    }

    @Override
    public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;

		LiteralBooleanNode that = (LiteralBooleanNode) o;

		return value == that.value && void_result == that.void_result;

	}

//    @Override
//    public int hashCode() {
//        int result = super.hashCode();
//        result = 31 * result + (value ? 1 : 0);
//        result = 31 * result + (void_result ? 1 : 0);
//        return result;
//    }
}
