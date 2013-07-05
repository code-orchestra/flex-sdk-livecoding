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
public class VariableBindingNode extends Node
{
	public TypedIdentifierNode variable;
	public Node initializer;
	public ReferenceValue ref;
	public ReferenceValue typeref;
	public AttributeListNode attrs;
	public String debug_name;
	public int kind;
	
	protected static final int PACKAGE_FLAG = 1;	

	public VariableBindingNode(PackageDefinitionNode pkgdef, AttributeListNode attrs, int kind, TypedIdentifierNode variable, Node initializer)
	{
		ref = null;
		typeref = null;
		this.attrs = attrs;
		this.variable = variable;
		this.initializer = initializer;
		this.kind = kind;
		
		if (pkgdef != null)
		{
			flags |= PACKAGE_FLAG;
		}
	}

	public boolean inPackage()
	{
		return (flags & PACKAGE_FLAG) != 0;
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

	public int pos()
	{
		return variable.identifier.pos();
	}

	public String toString()
	{
		return "VariableBinding";
	}

    public VariableBindingNode clone() throws CloneNotSupportedException
    {
        VariableBindingNode result = (VariableBindingNode) super.clone();

        if (attrs != null) result.attrs = attrs.clone();
        // debug_name is String
        //if (debug_name != null);
        if (initializer != null) result.initializer = initializer.clone();
        if (ref != null) result.ref = ref.clone();
        if (typeref != null) result.typeref = typeref.clone();
        if (variable != null) result.variable = variable.clone();

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        VariableBindingNode that = (VariableBindingNode) o;

        if (kind != that.kind) return false;
        if (attrs != null ? !attrs.equals(that.attrs) : that.attrs != null) return false;
        if (debug_name != null ? !debug_name.equals(that.debug_name) : that.debug_name != null) return false;
        if (initializer != null ? !initializer.equals(that.initializer) : that.initializer != null) return false;
        if (ref != null ? !ref.equals(that.ref) : that.ref != null) return false;
        if (typeref != null ? !typeref.equals(that.typeref) : that.typeref != null) return false;
        if (variable != null ? !variable.equals(that.variable) : that.variable != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (variable != null ? variable.hashCode() : 0);
        result = 31 * result + (initializer != null ? initializer.hashCode() : 0);
        result = 31 * result + (ref != null ? ref.hashCode() : 0);
        result = 31 * result + (typeref != null ? typeref.hashCode() : 0);
        result = 31 * result + (attrs != null ? attrs.hashCode() : 0);
        result = 31 * result + (debug_name != null ? debug_name.hashCode() : 0);
        result = 31 * result + kind;
        return result;
    }
}
