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
public class InvokeNode extends SelectorNode
{
	public String name;
	public ArgumentListNode args;
	public int index;

	public InvokeNode(String name, ArgumentListNode args)
	{
		this.name = name.intern();
		this.args = args;
		ref = null;
		index = 0;
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
		return "Invoke";
	}

    public InvokeNode clone() throws CloneNotSupportedException
    {
        InvokeNode result = (InvokeNode) super.clone();

        if (args != null) result.args = args.clone();

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        InvokeNode that = (InvokeNode) o;

        if (index != that.index) return false;
        if (args != null ? !args.equals(that.args) : that.args != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

//    @Override
//    public int hashCode() {
//        int result = super.hashCode();
//        result = 31 * result + (name != null ? name.hashCode() : 0);
//        result = 31 * result + (args != null ? args.hashCode() : 0);
//        result = 31 * result + index;
//        return result;
//    }
}
