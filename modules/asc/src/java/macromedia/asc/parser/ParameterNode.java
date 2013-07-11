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
 *
 * @author Jeff Dyer
 */
public class ParameterNode extends Node
{
	public int kind;
	public IdentifierNode identifier;
	public Node type;
	public Node init;
	public ReferenceValue ref;
	public ReferenceValue typeref;
	public AttributeListNode attrs;
    public boolean no_anno;

	public ParameterNode(int kind, IdentifierNode identifier, Node type, Node init)
	{
		this.kind = kind;
		this.identifier = identifier;
		this.type = type;
		this.init = init;
		ref = null;
		typeref = null;
		attrs = null;
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

	public int size()
	{
		return 1;
	}
	
	public String toString()
	{
		return "Parameter";
	}

    public ParameterNode clone() throws CloneNotSupportedException
    {
        ParameterNode result = (ParameterNode) super.clone();

        if (attrs != null) result.attrs = attrs.clone();
        if (identifier != null) result.identifier = identifier.clone();
        if (init != null) result.init = init.clone();
        if (ref != null) result.ref = ref.clone();
        if (type != null) result.type = type.clone();
        if (typeref != null) result.typeref = typeref.clone();

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ParameterNode that = (ParameterNode) o;

        if (kind != that.kind) return false;
        if (no_anno != that.no_anno) return false;
        if (attrs != null ? !attrs.equals(that.attrs) : that.attrs != null) return false;
        if (identifier != null ? !identifier.equals(that.identifier) : that.identifier != null) return false;
        if (init != null ? !init.equals(that.init) : that.init != null) return false;
        if (ref != null ? !ref.equals(that.ref) : that.ref != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        if (typeref != null ? !typeref.equals(that.typeref) : that.typeref != null) return false;

        return true;
    }

//    @Override
//    public int hashCode() {
//        int result = super.hashCode();
//        result = 31 * result + kind;
//        result = 31 * result + (identifier != null ? identifier.hashCode() : 0);
//        result = 31 * result + (type != null ? type.hashCode() : 0);
//        result = 31 * result + (init != null ? init.hashCode() : 0);
//        result = 31 * result + (ref != null ? ref.hashCode() : 0);
//        result = 31 * result + (typeref != null ? typeref.hashCode() : 0);
//        result = 31 * result + (attrs != null ? attrs.hashCode() : 0);
//        result = 31 * result + (no_anno ? 1 : 0);
//        return result;
//    }
}
