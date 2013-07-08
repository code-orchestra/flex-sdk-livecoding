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

import macromedia.asc.parser.util.CloneUtil;
import macromedia.asc.util.*;
import macromedia.asc.semantics.*;

/**
 * Node
 *
 * @author Jeff Dyer
 */
public class QualifiedExpressionNode extends QualifiedIdentifierNode
{
	public Node expr;
    public Namespaces nss;

    public QualifiedExpressionNode(Node qualifier, Node expr, int pos)
	{
	    super(qualifier,null,pos);
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

	public String toString()
	{
		return "QualifiedExpression";
	}

	public boolean isAttribute()
	{
		return true;
	}

    public QualifiedExpressionNode clone() throws CloneNotSupportedException
    {
        QualifiedExpressionNode result = (QualifiedExpressionNode) super.clone();

        if (expr != null) result.expr = expr.clone();
        if (nss != null) result.nss = CloneUtil.cloneList(nss);

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        QualifiedExpressionNode that = (QualifiedExpressionNode) o;

        if (expr != null ? !expr.equals(that.expr) : that.expr != null) return false;
        if (nss != null ? !nss.equals(that.nss) : that.nss != null) return false;

        return true;
    }

//    @Override
//    public int hashCode() {
//        int result = super.hashCode();
//        result = 31 * result + (expr != null ? expr.hashCode() : 0);
//        result = 31 * result + (nss != null ? nss.hashCode() : 0);
//        return result;
//    }
}
