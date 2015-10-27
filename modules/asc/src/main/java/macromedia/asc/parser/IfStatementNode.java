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
public class IfStatementNode extends Node
{
	public Node condition;
	public Node thenactions;
	public Node elseactions;
	public boolean is_true;
	public boolean is_false;
	
	public IfStatementNode(Node condition, Node thenactions, Node elseactions)
	{
	    is_true = false;
	    is_false = false;
	    
		this.condition = condition;
		this.thenactions = thenactions;
		this.elseactions = elseactions;
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
		return (thenactions != null ? thenactions.countVars() : 0) + (elseactions != null ? elseactions.countVars() : 0);
	}

	public String toString()
	{
		return "IfStatement";
	}

	public boolean isBranch()
	{
		return true;
	}

    public IfStatementNode clone() throws CloneNotSupportedException
    {
        IfStatementNode result = (IfStatementNode) super.clone();

        if (condition != null) result.condition = condition.clone();
        if (elseactions != null) result.elseactions = elseactions.clone();
        if (thenactions != null) result.thenactions = thenactions.clone();

        return result;
    }

    @Override
    public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;

		IfStatementNode that = (IfStatementNode) o;

		if (is_false != that.is_false) return false;
		if (is_true != that.is_true) return false;
		return !(condition != null ? !condition.equals(that.condition) : that.condition != null) && !(elseactions != null ? !elseactions.equals(that.elseactions) : that.elseactions != null) && !(thenactions != null ? !thenactions.equals(that.thenactions) : that.thenactions != null);

	}

//    @Override
//    public int hashCode() {
//        int result = super.hashCode();
//        result = 31 * result + (condition != null ? condition.hashCode() : 0);
//        result = 31 * result + (thenactions != null ? thenactions.hashCode() : 0);
//        result = 31 * result + (elseactions != null ? elseactions.hashCode() : 0);
//        result = 31 * result + (is_true ? 1 : 0);
//        result = 31 * result + (is_false ? 1 : 0);
//        return result;
//    }
}
