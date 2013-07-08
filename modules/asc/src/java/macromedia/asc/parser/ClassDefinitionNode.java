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
public class ClassDefinitionNode extends DefinitionNode
{
	public IdentifierNode name;
	public StatementListNode statements;
	public Node baseclass;
	public ReferenceValue baseref;
	public ListNode interfaces;
	public ReferenceValue ref;
	public ObjectList<FunctionCommonNode> fexprs;
	public ObjectList<ClassDefinitionNode> clsdefs;
	public ObjectList<Node> instanceinits;
	public ObjectList<FunctionCommonNode> staticfexprs;
	public int var_count;
	public int temp_count;
	public TypeValue cframe;
	public ObjectValue iframe;
	public int is_dynamic;
	public transient Context cx; // CodeOrchestra: made transient
	public String debug_name;
    public String package_name;
    public boolean owns_cframe;
    public boolean needs_init;
	public ObjectList<ClassDefinitionNode> deferred_subclasses;
    public ExpressionStatementNode init;
    public boolean needs_prototype_ns;
    public int version = -1;

    public Multinames imported_names = new Multinames();
	public Namespaces used_namespaces = new Namespaces(); // don't delete
    public Namespaces used_def_namespaces = new Namespaces(); // don't delete
	public ObjectValue private_namespace;
	public ObjectValue default_namespace;
    public ObjectValue public_namespace;
    public ObjectValue protected_namespace;
    public ObjectValue static_protected_namespace;

    public boolean is_default_nullable = true;

    public int state;
    public static final int INIT = 0;
    public static final int INHERIT = 1;
    public static final int MAIN = 2;

	public Namespaces namespaces = new Namespaces();

	public Node initializerStatement(Context cx)
	{
		return this;
	}

	public ClassDefinitionNode(Context cx, PackageDefinitionNode pkgdef, AttributeListNode attrs, IdentifierNode name, Node baseclass, ListNode interfaces, StatementListNode statements)
	{
		super(pkgdef, attrs, -1);
		this.name = name;
		this.baseclass = baseclass;
		this.interfaces = interfaces;
		this.statements = statements;
		ref = null;
		cframe = null;
		iframe = null;
		var_count = 0;
		temp_count = 0;
		staticfexprs = null;
		this.cx = cx;
		state = INIT;
        needs_prototype_ns = true;

        private_namespace = null;
		default_namespace = null;
        public_namespace = null;
        protected_namespace = null;
        package_name = "";
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
		return statements.first();
	}

	public Node last()
	{
		return statements.last();
	}

	public boolean isConst()
	{
		return true;
	}
	
	public boolean isInterface() 
	{
		return false;
	}


	public String toString()
	{
      if(Node.useDebugToStrings)
         return "ClassDefinition@" + pos() + (name != null ? ": " + name.toString() : "");
      else
         return "ClassDefinition";
	}

    public ClassDefinitionNode clone() throws CloneNotSupportedException
    {
        ClassDefinitionNode result = (ClassDefinitionNode) super.clone();

        if (baseclass != null) result.baseclass = baseclass.clone();
        if (baseref != null) result.baseref = baseref.clone();
        //if (cframe != null) result.cframe = cframe.clone();
        if (clsdefs != null) result.clsdefs = CloneUtil.cloneListCDNode(clsdefs);
        // cx is transient
        //if (cx != null);
        // debug_name is String
        //if (debug_name != null);
        if (default_namespace != null) result.default_namespace = default_namespace.clone();
        if (deferred_subclasses != null) result.deferred_subclasses = CloneUtil.cloneListCDNode(deferred_subclasses);
        if (fexprs != null) result.fexprs = CloneUtil.cloneListFCNode(fexprs);
        if (iframe != null) result.iframe = iframe.clone();
        if (imported_names != null) result.imported_names = CloneUtil.cloneMultinames(imported_names);
        if (init != null) result.init = init.clone();
        if (instanceinits != null) result.instanceinits = CloneUtil.cloneListNode(instanceinits);
        if (interfaces != null) result.interfaces = interfaces.clone();
        if (name != null) result.name = name.clone();
        if (namespaces != null) result.namespaces = CloneUtil.cloneList(namespaces);
        // package_name is String
        //if (package_name != null);
        if (private_namespace != null) result.private_namespace = private_namespace.clone();
        if (protected_namespace != null) result.protected_namespace = protected_namespace.clone();
        if (public_namespace != null) result.public_namespace = public_namespace.clone();
        if (ref != null) result.ref = ref.clone();
        if (statements != null) result.statements = statements.clone();
        if (static_protected_namespace != null) result.static_protected_namespace = static_protected_namespace.clone();
        if (staticfexprs != null) result.staticfexprs = CloneUtil.cloneListFCNode(staticfexprs);
        if (used_def_namespaces != null) result.used_def_namespaces = CloneUtil.cloneList(used_def_namespaces);
        if (used_namespaces != null) result.used_namespaces = CloneUtil.cloneList(used_namespaces);

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ClassDefinitionNode that = (ClassDefinitionNode) o;

        if (is_default_nullable != that.is_default_nullable) return false;
        if (is_dynamic != that.is_dynamic) return false;
        if (needs_init != that.needs_init) return false;
        if (needs_prototype_ns != that.needs_prototype_ns) return false;
        if (owns_cframe != that.owns_cframe) return false;
        if (state != that.state) return false;
        if (temp_count != that.temp_count) return false;
        if (var_count != that.var_count) return false;
        if (version != that.version) return false;
        if (baseclass != null ? !baseclass.equals(that.baseclass) : that.baseclass != null) return false;
        if (baseref != null ? !baseref.equals(that.baseref) : that.baseref != null) return false;
        if (cframe != null ? !cframe.equals(that.cframe) : that.cframe != null) return false;
        if (clsdefs != null ? !clsdefs.equals(that.clsdefs) : that.clsdefs != null) return false;
        if (cx != null ? !cx.equals(that.cx) : that.cx != null) return false;
        if (debug_name != null ? !debug_name.equals(that.debug_name) : that.debug_name != null) return false;
        if (default_namespace != null ? !default_namespace.equals(that.default_namespace) : that.default_namespace != null)
            return false;
        if (deferred_subclasses != null ? !deferred_subclasses.equals(that.deferred_subclasses) : that.deferred_subclasses != null)
            return false;
        if (fexprs != null ? !fexprs.equals(that.fexprs) : that.fexprs != null) return false;
        if (iframe != null ? !iframe.equals(that.iframe) : that.iframe != null) return false;
        if (imported_names != null ? !imported_names.equals(that.imported_names) : that.imported_names != null)
            return false;
        if (init != null ? !init.equals(that.init) : that.init != null) return false;
        if (instanceinits != null ? !instanceinits.equals(that.instanceinits) : that.instanceinits != null)
            return false;
        if (interfaces != null ? !interfaces.equals(that.interfaces) : that.interfaces != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (namespaces != null ? !namespaces.equals(that.namespaces) : that.namespaces != null) return false;
        if (package_name != null ? !package_name.equals(that.package_name) : that.package_name != null) return false;
        if (private_namespace != null ? !private_namespace.equals(that.private_namespace) : that.private_namespace != null)
            return false;
        if (protected_namespace != null ? !protected_namespace.equals(that.protected_namespace) : that.protected_namespace != null)
            return false;
        if (public_namespace != null ? !public_namespace.equals(that.public_namespace) : that.public_namespace != null)
            return false;
        if (ref != null ? !ref.equals(that.ref) : that.ref != null) return false;
        if (statements != null ? !statements.equals(that.statements) : that.statements != null) return false;
        if (static_protected_namespace != null ? !static_protected_namespace.equals(that.static_protected_namespace) : that.static_protected_namespace != null)
            return false;
        if (staticfexprs != null ? !staticfexprs.equals(that.staticfexprs) : that.staticfexprs != null) return false;
        if (used_def_namespaces != null ? !used_def_namespaces.equals(that.used_def_namespaces) : that.used_def_namespaces != null)
            return false;
        if (used_namespaces != null ? !used_namespaces.equals(that.used_namespaces) : that.used_namespaces != null)
            return false;

        return true;
    }

//    @Override
//    public int hashCode() {
//        int result = super.hashCode();
//        result = 31 * result + (name != null ? name.hashCode() : 0);
//        result = 31 * result + (statements != null ? statements.hashCode() : 0);
//        result = 31 * result + (baseclass != null ? baseclass.hashCode() : 0);
//        result = 31 * result + (baseref != null ? baseref.hashCode() : 0);
//        result = 31 * result + (interfaces != null ? interfaces.hashCode() : 0);
//        result = 31 * result + (ref != null ? ref.hashCode() : 0);
//        result = 31 * result + (fexprs != null ? fexprs.hashCode() : 0);
//        result = 31 * result + (clsdefs != null ? clsdefs.hashCode() : 0);
//        result = 31 * result + (instanceinits != null ? instanceinits.hashCode() : 0);
//        result = 31 * result + (staticfexprs != null ? staticfexprs.hashCode() : 0);
//        result = 31 * result + var_count;
//        result = 31 * result + temp_count;
//        result = 31 * result + (cframe != null ? cframe.hashCode() : 0);
//        result = 31 * result + (iframe != null ? iframe.hashCode() : 0);
//        result = 31 * result + is_dynamic;
//        result = 31 * result + (cx != null ? cx.hashCode() : 0);
//        result = 31 * result + (debug_name != null ? debug_name.hashCode() : 0);
//        result = 31 * result + (package_name != null ? package_name.hashCode() : 0);
//        result = 31 * result + (owns_cframe ? 1 : 0);
//        result = 31 * result + (needs_init ? 1 : 0);
//        result = 31 * result + (deferred_subclasses != null ? deferred_subclasses.hashCode() : 0);
//        result = 31 * result + (init != null ? init.hashCode() : 0);
//        result = 31 * result + (needs_prototype_ns ? 1 : 0);
//        result = 31 * result + version;
//        result = 31 * result + (imported_names != null ? imported_names.hashCode() : 0);
//        result = 31 * result + (used_namespaces != null ? used_namespaces.hashCode() : 0);
//        result = 31 * result + (used_def_namespaces != null ? used_def_namespaces.hashCode() : 0);
//        result = 31 * result + (private_namespace != null ? private_namespace.hashCode() : 0);
//        result = 31 * result + (default_namespace != null ? default_namespace.hashCode() : 0);
//        result = 31 * result + (public_namespace != null ? public_namespace.hashCode() : 0);
//        result = 31 * result + (protected_namespace != null ? protected_namespace.hashCode() : 0);
//        result = 31 * result + (static_protected_namespace != null ? static_protected_namespace.hashCode() : 0);
//        result = 31 * result + (is_default_nullable ? 1 : 0);
//        result = 31 * result + state;
//        result = 31 * result + (namespaces != null ? namespaces.hashCode() : 0);
//        return result;
//    }
}
