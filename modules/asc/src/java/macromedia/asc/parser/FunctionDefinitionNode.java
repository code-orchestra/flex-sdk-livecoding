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
import macromedia.asc.embedding.avmplus.InstanceBuilder;
import macromedia.asc.embedding.avmplus.ClassBuilder;
import macromedia.asc.embedding.avmplus.ActivationBuilder;

import static macromedia.asc.parser.Tokens.*;

/**
 * Node
 *
 * @author Jeff Dyer
 */
public class FunctionDefinitionNode extends DefinitionNode
{
	public FunctionNameNode name;
	public FunctionCommonNode fexpr;
	public int fixedCount;
	public ObjectValue fun;
	public ReferenceValue ref;
	public transient Context cx; // CodeOrchestra: made transient
	public ExpressionStatementNode init;
	public boolean needs_init;
    public boolean is_prototype;
    public int version = -1;

    public boolean skipLiveCoding;

    public FunctionDefinitionNode(Context cx, PackageDefinitionNode pkgdef, AttributeListNode attrs, FunctionNameNode name, FunctionCommonNode fexpr)
	{
		super(pkgdef, attrs, -1);
		this.cx = cx;
		ref = null;
		this.name = name;
		this.fexpr = fexpr;
		fexpr.def = this;
		init = null;
		needs_init = false;
        is_prototype = false;
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

	public Node first()
	{
		return fexpr.body.first();
	}

	public Node last()
	{
		return fexpr.body.last();
	}

	public Node initializerStatement(Context cx)
	{
        NodeFactory nodeFactory = cx.getNodeFactory();
        ObjectValue obj = cx.scope();
        Builder bui = obj.builder;
        Node node;

        // If this is a getter, setter or package, class or instance method, then don't create a closure

        if( !(bui instanceof ActivationBuilder) &&
            ( bui instanceof ClassBuilder ||
              bui instanceof InstanceBuilder ||
              this.pkgdef != null ||
              this.name.kind == GET_TOKEN ||
              this.name.kind == SET_TOKEN ) )
        {
            node = nodeFactory.emptyStatement();
        }
        else
        {
            node = this;
            this.needs_init = true;
        }
        return node;
	}

	public int countVars()
	{
		return name.kind==EMPTY_TOKEN?1:0;
	}

	public boolean isDefinition()
	{
		return true;
	}

	public boolean isConst()
	{
		return true;
	}

	public String toString()
	{
		return "FunctionDefinition";
	}

    public FunctionDefinitionNode clone() throws CloneNotSupportedException
    {
        FunctionDefinitionNode result = (FunctionDefinitionNode) super.clone();

        // cx is transient
        //if (cx != null);
        result.cx = null;
        if (fexpr != null) result.fexpr = fexpr.cloneWithDef(this, result);
        if (fun != null) result.fun = fun.clone();
        if (init != null) result.init = init.clone();
        if (name != null) result.name = name.clone();
        if (ref != null) result.ref = ref.clone();

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        FunctionDefinitionNode that = (FunctionDefinitionNode) o;

        if (fixedCount != that.fixedCount) return false;
        if (is_prototype != that.is_prototype) return false;
        if (needs_init != that.needs_init) return false;
        if (skipLiveCoding != that.skipLiveCoding) return false;
        if (version != that.version) return false;
        //if (cx != null ? !cx.equals(that.cx) : that.cx != null) return false;
        if (fexpr != null ? !fexpr.equals(that.fexpr) : that.fexpr != null) return false;
        if (fun != null ? !fun.equals(that.fun) : that.fun != null) return false;
        if (init != null ? !init.equals(that.init) : that.init != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (ref != null ? !ref.equals(that.ref) : that.ref != null) return false;

        return true;
    }

//    @Override
//    public int hashCode() {
//        int result = super.hashCode();
//        result = 31 * result + (name != null ? name.hashCode() : 0);
//        result = 31 * result + (fexpr != null ? fexpr.hashCode() : 0);
//        result = 31 * result + fixedCount;
//        result = 31 * result + (fun != null ? fun.hashCode() : 0);
//        result = 31 * result + (ref != null ? ref.hashCode() : 0);
//        result = 31 * result + (cx != null ? cx.hashCode() : 0);
//        result = 31 * result + (init != null ? init.hashCode() : 0);
//        result = 31 * result + (needs_init ? 1 : 0);
//        result = 31 * result + (is_prototype ? 1 : 0);
//        result = 31 * result + version;
//        result = 31 * result + (skipLiveCoding ? 1 : 0);
//        return result;
//    }
}
