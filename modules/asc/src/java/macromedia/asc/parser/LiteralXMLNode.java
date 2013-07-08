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
 *
 * @author Jeff Dyer
 */
public class LiteralXMLNode extends Node
{
	public ListNode list;
	public boolean is_xmllist;

	public LiteralXMLNode(ListNode list, boolean is_xmllist)
	{
		void_result = false;
		this.list = list;
		this.is_xmllist = is_xmllist;
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

	public void voidResult()
	{
		void_result = true;
	}

	public String toString()
	{
		return "LiteralXML";
	}

    public LiteralXMLNode clone() throws CloneNotSupportedException
    {
        LiteralXMLNode result = (LiteralXMLNode) super.clone();

        if (list != null) result.list = list.clone();

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        LiteralXMLNode that = (LiteralXMLNode) o;

        if (is_xmllist != that.is_xmllist) return false;
        if (void_result != that.void_result) return false;
        if (list != null ? !list.equals(that.list) : that.list != null) return false;

        return true;
    }

//    @Override
//    public int hashCode() {
//        int result = super.hashCode();
//        result = 31 * result + (list != null ? list.hashCode() : 0);
//        result = 31 * result + (is_xmllist ? 1 : 0);
//        result = 31 * result + (void_result ? 1 : 0);
//        return result;
//    }
}
