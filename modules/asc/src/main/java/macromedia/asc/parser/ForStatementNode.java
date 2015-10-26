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
public class ForStatementNode extends Node implements HasBody, LoopStatement
{
	public Node initialize;
	public Node test;
	public Node increment;
	public Node statement;
	public boolean is_forin;

    public ForStatementNode(Node initialize, Node test, Node increment, Node statement, boolean is_forin)
	{
		loop_index = 0;
		this.initialize = initialize;
		this.test = test;
		this.increment = increment;
		this.statement = statement;
        this.is_forin = is_forin;
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

	public void expectedType(TypeValue type)
	{
		statement.expectedType(type);
	}

	public int loop_index;

	public boolean isBranch()
	{
		return true;
	}

	public boolean isDefinition()
	{
		return initialize != null && initialize.isDefinition();
	}

	public int countVars()
	{
		return (initialize != null ? initialize.countVars() : 0) + (statement != null ? statement.countVars() : 0);
	}

	public String toString()
	{
		return "ForStatement";
	}

    @Override
    public Node getBody() {
        return statement;
    }

    @Override
    public void setBody(Node body) {
        this.statement = body;
    }

    public ForStatementNode clone() throws CloneNotSupportedException
    {
        ForStatementNode result = (ForStatementNode) super.clone();

        if (increment != null) result.increment = increment.clone();
        if (initialize != null) result.initialize = initialize.clone();
        if (statement != null) result.statement = statement.clone();
        if (test != null) result.test = test.clone();

        return result;
    }

    @Override
    public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;

		ForStatementNode that = (ForStatementNode) o;

		if (is_forin != that.is_forin) return false;
		if (loop_index != that.loop_index) return false;
		if (increment != null ? !increment.equals(that.increment) : that.increment != null) return false;
		if (initialize != null ? !initialize.equals(that.initialize) : that.initialize != null) return false;
		return !(statement != null ? !statement.equals(that.statement) : that.statement != null) && !(test != null ? !test.equals(that.test) : that.test != null);

	}

//    @Override
//    public int hashCode() {
//        int result = super.hashCode();
//        result = 31 * result + (initialize != null ? initialize.hashCode() : 0);
//        result = 31 * result + (test != null ? test.hashCode() : 0);
//        result = 31 * result + (increment != null ? increment.hashCode() : 0);
//        result = 31 * result + (statement != null ? statement.hashCode() : 0);
//        result = 31 * result + (is_forin ? 1 : 0);
//        result = 31 * result + loop_index;
//        return result;
//    }
}
