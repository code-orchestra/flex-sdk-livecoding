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
public class DoStatementNode extends Node implements HasBody, LoopStatement
{
	public Node statements;
	public Node expr;
	
	public DoStatementNode(Node statements, Node expr)
	{
		loop_index = 0;
        this.statements = statements;
		this.expr = expr;
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

	public int countVars()
	{
		return statements.countVars();
	}

	public int loop_index;

	boolean isBranch()
	{
		return true;
	}

	public String toString()
	{
		return "DoStatement";
	}

    @Override
    public Node getBody() {
        return statements;
    }

    @Override
    public void setBody(Node body) {
        this.statements = body;
    }

    public DoStatementNode clone() throws CloneNotSupportedException
    {
        DoStatementNode result = (DoStatementNode) super.clone();

        if (expr != null) result.expr = expr.clone();
        if (statements != null) result.statements = statements.clone();

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        DoStatementNode that = (DoStatementNode) o;

        if (loop_index != that.loop_index) return false;
        if (expr != null ? !expr.equals(that.expr) : that.expr != null) return false;
        if (statements != null ? !statements.equals(that.statements) : that.statements != null) return false;

        return true;
    }

//    @Override
//    public int hashCode() {
//        int result = super.hashCode();
//        result = 31 * result + (statements != null ? statements.hashCode() : 0);
//        result = 31 * result + (expr != null ? expr.hashCode() : 0);
//        result = 31 * result + loop_index;
//        return result;
//    }
}
