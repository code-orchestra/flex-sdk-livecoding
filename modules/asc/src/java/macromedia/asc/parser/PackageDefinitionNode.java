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
public class PackageDefinitionNode extends DefinitionNode
{
	public ReferenceValue ref;
	public PackageNameNode name;
	public StatementListNode statements;

	public ObjectList<FunctionCommonNode> fexprs = new ObjectList<FunctionCommonNode>();
	public ObjectList<ClassDefinitionNode> clsdefs = new ObjectList<ClassDefinitionNode>();

	public ObjectValue defaultNamespace;
	public ObjectValue publicNamespace;
    public ObjectValue internalNamespace;
    
    public Namespaces used_namespaces = new Namespaces();
    public Namespaces used_def_namespaces = new Namespaces();
    public Multinames imported_names = new Multinames();
    
	public int var_count;
	public int temp_count;
	public transient Context cx; // CodeOrchestra: made transient

	public boolean package_retrieved;
    public boolean in_this_pkg;

	public PackageDefinitionNode(Context cx, AttributeListNode attrs, PackageNameNode name, StatementListNode statements)
	{
        super(null,attrs,0);
		this.cx = cx;
		this.ref = null;
		this.name = name;
		this.statements = statements;
		this.var_count = 0;
		this.temp_count = 0;
		
		package_retrieved = false;
		in_this_pkg = false;

		defaultNamespace = null;
		publicNamespace = null;
        internalNamespace = null;
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

	public Node initializerStatement(Context cx)
	{
		return cx.getNodeFactory().emptyStatement();
	}

	public boolean isConst()
	{
		return true;
	}

    public boolean isDefinition()
    {
        return false;  // this is not an error. this keeps packages from getting hoisted
    }

	public String toString()
	{
		if(Node.useDebugToStrings)
         return "PackageDefinition@" + pos();
      else
         return "PackageDefinition";
	}

    public PackageDefinitionNode clone() throws CloneNotSupportedException
    {
        PackageDefinitionNode result = (PackageDefinitionNode) super.clone();

        if (clsdefs != null) result.clsdefs = CloneUtil.cloneListCDNode(clsdefs);
        if (defaultNamespace != null) result.defaultNamespace = defaultNamespace.clone();
        if (fexprs != null) result.fexprs = CloneUtil.cloneListFCNode(fexprs);
        if (imported_names != null) result.imported_names = CloneUtil.cloneMultinames(imported_names);
        if (internalNamespace != null) result.internalNamespace = internalNamespace;
        if (name != null) result.name = name.clone();
        if (publicNamespace != null) result.publicNamespace = publicNamespace.clone();
        if (ref != null) result.ref = ref.clone();
        if (statements != null) {
            result.statements = statements;
            ObjectList<Node> list = result.statements.items;
            list.add(list.indexOf(this), result);
        }
        if (used_def_namespaces != null) result.used_def_namespaces = CloneUtil.cloneList(used_def_namespaces);
        if (used_namespaces != null) result.used_namespaces = CloneUtil.cloneList(used_namespaces);

        // cx is transient
        //if (cx != null);
        result.cx = null;

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        PackageDefinitionNode that = (PackageDefinitionNode) o;

        if (in_this_pkg != that.in_this_pkg) return false;
        if (package_retrieved != that.package_retrieved) return false;
        if (temp_count != that.temp_count) return false;
        if (var_count != that.var_count) return false;
        if (clsdefs != null ? !clsdefs.equals(that.clsdefs) : that.clsdefs != null) return false;
        // cx is transient
        //if (cx != null ? !cx.equals(that.cx) : that.cx != null) return false;
        if (defaultNamespace != null ? !defaultNamespace.equals(that.defaultNamespace) : that.defaultNamespace != null)
            return false;
        if (fexprs != null ? !fexprs.equals(that.fexprs) : that.fexprs != null) return false;
        if (imported_names != null ? !imported_names.equals(that.imported_names) : that.imported_names != null)
            return false;
        if (internalNamespace != null ? !internalNamespace.equals(that.internalNamespace) : that.internalNamespace != null)
            return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (publicNamespace != null ? !publicNamespace.equals(that.publicNamespace) : that.publicNamespace != null)
            return false;
        if (ref != null ? !ref.equals(that.ref) : that.ref != null) return false;
        if (statements != null ? !statements.equals(that.statements) : that.statements != null) return false;
        if (used_def_namespaces != null ? !used_def_namespaces.equals(that.used_def_namespaces) : that.used_def_namespaces != null)
            return false;
        if (used_namespaces != null ? !used_namespaces.equals(that.used_namespaces) : that.used_namespaces != null)
            return false;

        return true;
    }

//    @Override
//    public int hashCode() {
//        int result = super.hashCode();
//        result = 31 * result + (ref != null ? ref.hashCode() : 0);
//        result = 31 * result + (name != null ? name.hashCode() : 0);
//        result = 31 * result + (statements != null ? statements.hashCode() : 0);
//        result = 31 * result + (fexprs != null ? fexprs.hashCode() : 0);
//        result = 31 * result + (clsdefs != null ? clsdefs.hashCode() : 0);
//        result = 31 * result + (defaultNamespace != null ? defaultNamespace.hashCode() : 0);
//        result = 31 * result + (publicNamespace != null ? publicNamespace.hashCode() : 0);
//        result = 31 * result + (internalNamespace != null ? internalNamespace.hashCode() : 0);
//        result = 31 * result + (used_namespaces != null ? used_namespaces.hashCode() : 0);
//        result = 31 * result + (used_def_namespaces != null ? used_def_namespaces.hashCode() : 0);
//        result = 31 * result + (imported_names != null ? imported_names.hashCode() : 0);
//        result = 31 * result + var_count;
//        result = 31 * result + temp_count;
//        result = 31 * result + (cx != null ? cx.hashCode() : 0);
//        result = 31 * result + (package_retrieved ? 1 : 0);
//        result = 31 * result + (in_this_pkg ? 1 : 0);
//        return result;
//    }
}
