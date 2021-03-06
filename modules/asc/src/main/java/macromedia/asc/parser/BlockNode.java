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
public class BlockNode extends Node
{
	public AttributeListNode attributes;
	public StatementListNode statements;

	public BlockNode(AttributeListNode attributes, StatementListNode statements)
	{
		this.attributes = attributes;
		this.statements = statements;
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
		return "Block";
	}

    public BlockNode clone() throws CloneNotSupportedException
    {
        BlockNode result = (BlockNode) super.clone();

        if (attributes != null) result.attributes = attributes.clone();
        if (statements != null) result.statements = statements.clone();

        return result;
    }

    @Override
    public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;

		BlockNode blockNode = (BlockNode) o;

		return !(attributes != null ? !attributes.equals(blockNode.attributes) : blockNode.attributes != null) && !(statements != null ? !statements.equals(blockNode.statements) : blockNode.statements != null);

	}

//    @Override
//    public int hashCode() {
//        int result = super.hashCode();
//        result = 31 * result + (attributes != null ? attributes.hashCode() : 0);
//        result = 31 * result + (statements != null ? statements.hashCode() : 0);
//        return result;
//    }
}
