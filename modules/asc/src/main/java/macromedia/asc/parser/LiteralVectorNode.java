/*
 *
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package macromedia.asc.parser;

import macromedia.asc.semantics.Value;
import macromedia.asc.util.Context;

/**
 * LiteralVectorNode represents a vector literal, e.g., Vector.<int>.[1,2,3]
 */
public class LiteralVectorNode extends Node 
{
	public ArgumentListNode elementlist;
	public Node type;

	public LiteralVectorNode(ArgumentListNode elementlist, Node type)
	{
		void_result = false;
		this.elementlist = elementlist;
		this.type = type;
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

	public boolean void_result;
	public Value value;

	public void voidResult()
	{
		void_result = true;
	}

	public String toString()
	{
		return "LiteralVector";
	}

    public LiteralVectorNode clone() throws CloneNotSupportedException
    {
        LiteralVectorNode result = (LiteralVectorNode) super.clone();

        if (elementlist != null) result.elementlist = elementlist.clone();
        if (type != null) result.type = type.clone();
        if (value != null) result.value = value.clone();

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        LiteralVectorNode that = (LiteralVectorNode) o;

        if (void_result != that.void_result) return false;
        if (elementlist != null ? !elementlist.equals(that.elementlist) : that.elementlist != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
		return !(value != null ? !value.equals(that.value) : that.value != null);

	}

//    @Override
//    public int hashCode() {
//        int result = super.hashCode();
//        result = 31 * result + (elementlist != null ? elementlist.hashCode() : 0);
//        result = 31 * result + (type != null ? type.hashCode() : 0);
//        result = 31 * result + (void_result ? 1 : 0);
//        result = 31 * result + (value != null ? value.hashCode() : 0);
//        return result;
//    }
}
