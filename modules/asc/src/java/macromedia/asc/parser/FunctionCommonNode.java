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
import static macromedia.asc.parser.Tokens.*;

/**
 * Node
 *
 * @author Jeff Dyer
 */
public class FunctionCommonNode extends Node
{
	public FunctionDefinitionNode def;
	
	public IdentifierNode identifier;
	public FunctionSignatureNode signature;
	public StatementListNode body;
	public int fixedCount;
	public ObjectValue fun;
	public ReferenceValue ref;
	public ObjectList<FunctionCommonNode> fexprs;
	public int var_count;
	public int temp_count;
	public String internal_name = "";
	public ObjectList<Block> blocks;

    public int with_depth;

    public ObjectList<ObjectValue> scope_chain;

	public String debug_name = "";
	public int needsArguments;
	public transient Context cx; // CodeOrchestra: made transient
	public int kind;
    public ObjectList<String> namespace_ids;
    public StatementListNode use_stmts;
    public DefaultXMLNamespaceNode default_dxns;
    public Multinames imported_names;

	public Namespaces used_namespaces;
	public ObjectValue private_namespace;
	public ObjectValue default_namespace;
	public ObjectValue public_namespace;

	private int flags;

	private boolean void_result;
	
	private static final int USER_DEFINED_BODY_Flag = 1;
	private static final int IS_FUNDEF_Flag         = 2;
	private static final int WITH_USED_Flag         = 4;
	private static final int IS_NATIVE_Flag         = 8;
	private static final int EXCEPTIONS_USED_Flag   = 16;
    private static final int NAMED_INNER_Flag       = 32;
	
	public FunctionCommonNode(Context cx, StatementListNode use_stmts, String internal_name, IdentifierNode identifier, FunctionSignatureNode signature, StatementListNode body, boolean hasUserDefinedBody)
	{
		this.cx = cx;
		fun = null;
		ref = null;
		fexprs = null;
		var_count = 0;
		temp_count = 0;
		setNative(false);
		needsArguments = 0;
		kind = EMPTY_TOKEN;
		setFunctionDefinition(false);

        this.use_stmts = use_stmts;
        this.internal_name = internal_name;
		this.identifier = identifier;
		this.signature = signature;
		this.body = body;

		private_namespace = null;
		default_namespace = null;
		public_namespace = null;
		default_dxns = null;

        with_depth = -1;

        setUserDefinedBody(hasUserDefinedBody);
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

	public boolean isFunctionExpression()
	{
		return true;
	}

	public String toString()
	{
		return "FunctionCommon";
	}

	public void setFunctionDefinition(boolean is_fundef)
	{
		flags = is_fundef ? (flags|IS_FUNDEF_Flag) : (flags&~IS_FUNDEF_Flag);
	}

	public boolean isFunctionDefinition()
	{
		return (flags & IS_FUNDEF_Flag) != 0;
	}

	public void setUserDefinedBody(boolean userDefinedBody)
	{
		flags = userDefinedBody ? (flags|USER_DEFINED_BODY_Flag) : (flags&~USER_DEFINED_BODY_Flag);
	}

	public boolean isUserDefinedBody()
	{
		return (flags & USER_DEFINED_BODY_Flag) != 0;
	}

	public void setNative(boolean isNative)
	{
		flags = isNative ? (flags|IS_NATIVE_Flag) : (flags&~IS_NATIVE_Flag);
	}

	public boolean isNative()
	{
		return (flags & IS_NATIVE_Flag) != 0;
	}

	public void setWithUsed(boolean withUsed)
	{
		flags = withUsed ? (flags|WITH_USED_Flag) : (flags&~WITH_USED_Flag);
	}

	public boolean isWithUsed()
	{
		return (flags & WITH_USED_Flag) != 0;
	}

    public void setNamedInnerFunc(boolean isNamedInnerFunc)
    {
        flags = isNamedInnerFunc ? (flags|NAMED_INNER_Flag) : (flags&~NAMED_INNER_Flag);
    }
    public boolean isNamedInnerFunc()
    {
        return (flags & NAMED_INNER_Flag) != 0;
    }
	public void setExceptionsUsed(boolean exceptionsUsed)

	{
		flags = exceptionsUsed ? (flags|EXCEPTIONS_USED_Flag) : (flags&~EXCEPTIONS_USED_Flag);
	}

	public boolean isExceptionsUsed()
	{
		return (flags & EXCEPTIONS_USED_Flag) != 0;
	}
	
	public void voidResult()
	{
		this.void_result = true;
	}
	
	public boolean isVoidResult()
	{
		return this.void_result;
	}

    public FunctionCommonNode cloneWithDef(FunctionDefinitionNode def_value, FunctionDefinitionNode def_clone) throws CloneNotSupportedException
    {
        FunctionCommonNode result = (FunctionCommonNode) super.clone();

        if (def == def_value)
        {
            result.def = def_clone;
        }
        else
        {
            result.def = def.clone();
        }

        if (blocks != null) result.blocks = CloneUtil.cloneListBlock(blocks);
        if (body != null) result.body = body.clone();
        //debug_name is String
        //if (debug_name != null);
        if (default_dxns != null) result.default_dxns = default_dxns.clone();
        if (default_namespace != null) result.default_namespace = default_namespace.clone();
        if (fexprs != null) {
            result.fexprs = CloneUtil.cloneListFCNode(fexprs);
        }
        if (fun != null) result.fun = fun.clone();
        if (identifier != null) result.identifier = identifier.clone();
        //internal_name is String
        //if (internal_name != null);
        if (namespace_ids != null) result.namespace_ids = CloneUtil.cloneListString(namespace_ids);
        if (private_namespace != null) result.private_namespace = private_namespace.clone();
        if (public_namespace != null) result.public_namespace = public_namespace.clone();
        if (ref != null) result.ref = ref.clone();
        if (scope_chain != null) result.scope_chain = CloneUtil.cloneObjectListList(scope_chain);
        if (signature != null) result.signature = signature.clone();
        if (use_stmts != null) result.use_stmts = use_stmts.clone();
        if (used_namespaces != null) result.used_namespaces = CloneUtil.cloneList(used_namespaces);

        if (imported_names != null) result.imported_names = CloneUtil.cloneMultinames(imported_names);

        return result;
    }

    public FunctionCommonNode clone() throws CloneNotSupportedException
    {
        FunctionCommonNode result = (FunctionCommonNode) super.clone();

        if (def != null) result.def = def.clone();
        if (blocks != null) result.blocks = CloneUtil.cloneListBlock(blocks);
        if (body != null) result.body = body.clone();
        //debug_name is String
        //if (debug_name != null);
        if (default_dxns != null) result.default_dxns = default_dxns.clone();
        if (default_namespace != null) result.default_namespace = default_namespace.clone();
        if (fexprs != null) {
            result.fexprs = CloneUtil.cloneListFCNode(fexprs);
        }
        if (fun != null) result.fun = fun.clone();
        if (identifier != null) result.identifier = identifier.clone();
        //internal_name is String
        //if (internal_name != null);
        if (namespace_ids != null) result.namespace_ids = CloneUtil.cloneListString(namespace_ids);
        if (private_namespace != null) result.private_namespace = private_namespace.clone();
        if (public_namespace != null) result.public_namespace = public_namespace.clone();
        if (ref != null) result.ref = ref.clone();
        if (scope_chain != null) result.scope_chain = CloneUtil.cloneObjectListList(scope_chain);
        if (signature != null) result.signature = signature.clone();
        if (use_stmts != null) result.use_stmts = use_stmts.clone();
        if (used_namespaces != null) result.used_namespaces = CloneUtil.cloneList(used_namespaces);

        if (imported_names != null) result.imported_names = CloneUtil.cloneMultinames(imported_names);

        // cx is transient
        //if (cx != null);

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        FunctionCommonNode that = (FunctionCommonNode) o;

        if (fixedCount != that.fixedCount) return false;
        if (flags != that.flags) return false;
        if (kind != that.kind) return false;
        if (needsArguments != that.needsArguments) return false;
        if (temp_count != that.temp_count) return false;
        if (var_count != that.var_count) return false;
        if (void_result != that.void_result) return false;
        if (with_depth != that.with_depth) return false;
        if (blocks != null ? !blocks.equals(that.blocks) : that.blocks != null) return false;
        if (body != null ? !body.equals(that.body) : that.body != null) return false;
        // cx is transient
        if (cx != null ? !cx.equals(that.cx) : that.cx != null) return false;
        if (debug_name != null ? !debug_name.equals(that.debug_name) : that.debug_name != null) return false;
//        if (def != null ? !(def == that.def) : that.def != null) return false;
        if (default_dxns != null ? !default_dxns.equals(that.default_dxns) : that.default_dxns != null) return false;
        if (default_namespace != null ? !default_namespace.equals(that.default_namespace) : that.default_namespace != null)
            return false;
        if (fexprs != null ? !fexprs.equals(that.fexprs) : that.fexprs != null) return false;
        if (fun != null ? !fun.equals(that.fun) : that.fun != null) return false;
        if (identifier != null ? !identifier.equals(that.identifier) : that.identifier != null) return false;
        if (imported_names != null ? !imported_names.equals(that.imported_names) : that.imported_names != null)
            return false;
        if (internal_name != null ? !internal_name.equals(that.internal_name) : that.internal_name != null)
            return false;
        if (namespace_ids != null ? !namespace_ids.equals(that.namespace_ids) : that.namespace_ids != null)
            return false;
        if (private_namespace != null ? !private_namespace.equals(that.private_namespace) : that.private_namespace != null)
            return false;
        if (public_namespace != null ? !public_namespace.equals(that.public_namespace) : that.public_namespace != null)
            return false;
        if (ref != null ? !ref.equals(that.ref) : that.ref != null) return false;
        if (scope_chain != null ? !scope_chain.equals(that.scope_chain) : that.scope_chain != null) return false;
        if (signature != null ? !signature.equals(that.signature) : that.signature != null) return false;
        if (use_stmts != null ? !use_stmts.equals(that.use_stmts) : that.use_stmts != null) return false;
        if (used_namespaces != null ? !used_namespaces.equals(that.used_namespaces) : that.used_namespaces != null)
            return false;

        return true;
    }

//    @Override
//    public int hashCode() {
//        int result = super.hashCode();
//        result = 31 * result + (def != null ? def.hashCode() : 0);
//        result = 31 * result + (identifier != null ? identifier.hashCode() : 0);
//        result = 31 * result + (signature != null ? signature.hashCode() : 0);
//        result = 31 * result + (body != null ? body.hashCode() : 0);
//        result = 31 * result + fixedCount;
//        result = 31 * result + (fun != null ? fun.hashCode() : 0);
//        result = 31 * result + (ref != null ? ref.hashCode() : 0);
//        result = 31 * result + (fexprs != null ? fexprs.hashCode() : 0);
//        result = 31 * result + var_count;
//        result = 31 * result + temp_count;
//        result = 31 * result + (internal_name != null ? internal_name.hashCode() : 0);
//        result = 31 * result + (blocks != null ? blocks.hashCode() : 0);
//        result = 31 * result + with_depth;
//        result = 31 * result + (scope_chain != null ? scope_chain.hashCode() : 0);
//        result = 31 * result + (debug_name != null ? debug_name.hashCode() : 0);
//        result = 31 * result + needsArguments;
//        result = 31 * result + (cx != null ? cx.hashCode() : 0);
//        result = 31 * result + kind;
//        result = 31 * result + (namespace_ids != null ? namespace_ids.hashCode() : 0);
//        result = 31 * result + (use_stmts != null ? use_stmts.hashCode() : 0);
//        result = 31 * result + (default_dxns != null ? default_dxns.hashCode() : 0);
//        result = 31 * result + (imported_names != null ? imported_names.hashCode() : 0);
//        result = 31 * result + (used_namespaces != null ? used_namespaces.hashCode() : 0);
//        result = 31 * result + (private_namespace != null ? private_namespace.hashCode() : 0);
//        result = 31 * result + (default_namespace != null ? default_namespace.hashCode() : 0);
//        result = 31 * result + (public_namespace != null ? public_namespace.hashCode() : 0);
//        result = 31 * result + flags;
//        result = 31 * result + (void_result ? 1 : 0);
//        return result;
//    }
}
