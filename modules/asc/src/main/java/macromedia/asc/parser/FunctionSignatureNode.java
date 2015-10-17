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
public class FunctionSignatureNode extends Node
{
	public ParameterListNode parameter;
	public Node result;
	public TypeInfo type;
	public ReferenceValue typeref;
	public ListNode inits; //initializers for ctor signatures (instead of a result type)
	//public Slot slot;
    public boolean no_anno;
    public boolean void_anno;

	public FunctionSignatureNode(ParameterListNode parameter, Node result)
	{
		type = null;
		this.parameter = parameter;
		this.result = result;
		typeref = null;
        no_anno = false;
        void_anno = false;
	}

	
	public FunctionSignatureNode(ParameterListNode parameter, ListNode initializers)
	{
		type = null;
		this.parameter = parameter;
		this.inits = initializers;
		typeref = null;
        no_anno = false;
        void_anno = false;
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
		return parameter != null ? parameter.size() : 0;
	}

	public String toString()
	{
		return "FunctionSignature";
	}

	public StringBuilder toCanonicalString(Context cx, StringBuilder buff)
	{
		if (parameter != null)
			parameter.toCanonicalString(cx, buff);
		buff.append(" result_type='");
		if (result != null)
			result.toCanonicalString(cx, buff);
        else if( this.void_anno )
            buff.append("void");
        else
            buff.append(cx.noType().name.toString());
		buff.append("'");
		return buff;
	}

    public FunctionSignatureNode clone() throws CloneNotSupportedException
    {
        FunctionSignatureNode clone = (FunctionSignatureNode) super.clone();

        if (inits != null) clone.inits = inits.clone();
        if (parameter != null) clone.parameter = parameter.clone();
        if (result != null) clone.result = result.clone();
        if (type != null) clone.type = type.clone();
        if (typeref != null) clone.typeref = typeref.clone();

        return clone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        FunctionSignatureNode that = (FunctionSignatureNode) o;

        if (no_anno != that.no_anno) return false;
        if (void_anno != that.void_anno) return false;
        if (inits != null ? !inits.equals(that.inits) : that.inits != null) return false;
        if (parameter != null ? !parameter.equals(that.parameter) : that.parameter != null) return false;
        if (result != null ? !result.equals(that.result) : that.result != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        if (typeref != null ? !typeref.equals(that.typeref) : that.typeref != null) return false;

        return true;
    }

//    @Override
//    public int hashCode() {
//        int result1 = super.hashCode();
//        result1 = 31 * result1 + (parameter != null ? parameter.hashCode() : 0);
//        result1 = 31 * result1 + (result != null ? result.hashCode() : 0);
//        result1 = 31 * result1 + (type != null ? type.hashCode() : 0);
//        result1 = 31 * result1 + (typeref != null ? typeref.hashCode() : 0);
//        result1 = 31 * result1 + (inits != null ? inits.hashCode() : 0);
//        result1 = 31 * result1 + (no_anno ? 1 : 0);
//        result1 = 31 * result1 + (void_anno ? 1 : 0);
//        return result1;
//    }
}
