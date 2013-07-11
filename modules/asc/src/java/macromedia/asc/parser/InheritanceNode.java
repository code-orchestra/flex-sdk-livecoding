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
public class InheritanceNode extends Node
{
	public Node baseclass;
	public ListNode interfaces;

	public InheritanceNode(Node baseclass, ListNode interfaces)
	{
		this.baseclass = baseclass;
		this.interfaces = interfaces;
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

	public String toString()
	{
		return "inheritance";
	}

    public InheritanceNode clone() throws CloneNotSupportedException
    {
        InheritanceNode result = (InheritanceNode) super.clone();

        if (baseclass != null) result.baseclass = baseclass.clone();
        if (interfaces != null) result.interfaces = interfaces.clone();

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        InheritanceNode that = (InheritanceNode) o;

        if (baseclass != null ? !baseclass.equals(that.baseclass) : that.baseclass != null) return false;
        if (interfaces != null ? !interfaces.equals(that.interfaces) : that.interfaces != null) return false;

        return true;
    }

//    @Override
//    public int hashCode() {
//        int result = super.hashCode();
//        result = 31 * result + (baseclass != null ? baseclass.hashCode() : 0);
//        result = 31 * result + (interfaces != null ? interfaces.hashCode() : 0);
//        return result;
//    }
}
