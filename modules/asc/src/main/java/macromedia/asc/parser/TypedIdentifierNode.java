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
public class TypedIdentifierNode extends Node
{
	public IdentifierNode identifier;
	// public IdentifierNode type;
	public Node type;
	//public Slot slot;
    public boolean no_anno;

	public TypedIdentifierNode(Node identifier, Node type, int pos)
	{
		super(pos);
		this.identifier = (IdentifierNode) identifier;
		// C: In ascap.exe, type could be MemberExpressionNode!
		// this.type = (IdentifierNode) type;
		this.type = type;
        no_anno = false;
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
		return "TypedIdentifier";
	}

    public TypedIdentifierNode clone() throws CloneNotSupportedException
    {
        TypedIdentifierNode result = (TypedIdentifierNode) super.clone();

        if (identifier != null) result.identifier = identifier.clone();
        if (type != null) result.type = type.clone();

        return result;
    }

    @Override
    public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;

		TypedIdentifierNode that = (TypedIdentifierNode) o;

		if (no_anno != that.no_anno) return false;
		return !(identifier != null ? !identifier.equals(that.identifier) : that.identifier != null) && !(type != null ? !type.equals(that.type) : that.type != null);

	}

//    @Override
//    public int hashCode() {
//        int result = super.hashCode();
//        result = 31 * result + (identifier != null ? identifier.hashCode() : 0);
//        result = 31 * result + (type != null ? type.hashCode() : 0);
//        result = 31 * result + (no_anno ? 1 : 0);
//        return result;
//    }
}
