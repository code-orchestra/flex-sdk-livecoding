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

import macromedia.asc.util.*;
import macromedia.asc.semantics.*;

/**
 * Node
 */

public class UsePragmaNode extends Node {
	
	

	public Node identifier;
	public Node argument;

	public UsePragmaNode(Node idNode, Node argNode)
	{
		identifier = idNode;
		argument = argNode;
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
		return "UsePragma";
	}

    public UsePragmaNode clone() throws CloneNotSupportedException
    {
        UsePragmaNode result = (UsePragmaNode) super.clone();

        result.identifier = identifier.clone();
        result.argument = argument.clone();

        return result;
    }

    @Override
    public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;

		UsePragmaNode that = (UsePragmaNode) o;

		return !(argument != null ? !argument.equals(that.argument) : that.argument != null) && !(identifier != null ? !identifier.equals(that.identifier) : that.identifier != null);

	}

//    @Override
//    public int hashCode() {
//        int result = super.hashCode();
//        result = 31 * result + (identifier != null ? identifier.hashCode() : 0);
//        result = 31 * result + (argument != null ? argument.hashCode() : 0);
//        return result;
//    }
}
