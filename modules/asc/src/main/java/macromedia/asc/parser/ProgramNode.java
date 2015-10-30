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

import java.util.Set;
import java.util.HashSet;

/**
 * Node
 */
public class ProgramNode extends Node
{
	public StatementListNode statements;
	public ObjectList<FunctionCommonNode> fexprs;
	public ObjectList<ClassDefinitionNode> clsdefs;
	public ObjectList<PackageDefinitionNode> pkgdefs = new ObjectList<>();
	public ObjectList<ImportNode> imports = new ObjectList<>();

	public int temp_count;
	public int var_count;
	public ObjectList<Block> blocks;
	public transient Context cx; // CodeOrchestra: made transient
	public boolean has_unnamed_package;

	public ObjectValue frame;
	public ObjectValue importFrame;

	public ObjectValue default_namespace;
	public ObjectValue public_namespace;

	public static final int Inheritance = 1;
	public static final int Else = 2;
	public static final int Done = 3;
	public int state = Inheritance;

    public Namespaces used_def_namespaces = new Namespaces(); // don't delete
	
    public Set<ReferenceValue> import_def_unresolved = new HashSet<>();
    public Set<ReferenceValue> package_unresolved = new HashSet<>();
	public Set<ReferenceValue> ns_unresolved = new HashSet<>();
	public Set<ReferenceValue> fa_unresolved = new HashSet<>();
	public Set<ReferenceValue> ce_unresolved = new HashSet<>();
	public Set<ReferenceValue> body_unresolved = new HashSet<>();
	public Set<ReferenceValue> rt_unresolved = new HashSet<>();

	public ProgramNode(Context cx, StatementListNode statements)
	{
	    this.cx = cx;
		this.statements = statements;
		has_unnamed_package = false;
		frame = null;
		default_namespace = null;
		public_namespace  = null;
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
		return "Program";
	}

    public ProgramNode clone() throws CloneNotSupportedException
    {
        ProgramNode result = (ProgramNode) super.clone();

        if (blocks != null) result.blocks = CloneUtil.cloneListBlock(blocks);
        if (body_unresolved != null) result.body_unresolved = CloneUtil.cloneSet(body_unresolved);
        if (ce_unresolved != null) result.ce_unresolved = CloneUtil.cloneSet(ce_unresolved);
        if (clsdefs != null) result.clsdefs = CloneUtil.cloneListCDNode(clsdefs);
        // cx is transient
        //if (cx != null);
        result.cx = null;
        if (default_namespace != null) result.default_namespace = default_namespace.clone();
        if (fa_unresolved != null) result.fa_unresolved = CloneUtil.cloneSet(fa_unresolved);
        if (fexprs != null) result.fexprs = CloneUtil.cloneListFCNode(fexprs);
        if (frame != null) result.frame = frame.clone();
        if (importFrame != null) result.importFrame = importFrame.clone();
        if (import_def_unresolved != null) result.import_def_unresolved = CloneUtil.cloneSet(import_def_unresolved);
        if (imports != null) result.imports = CloneUtil.cloneListImNode(imports);
        if (ns_unresolved != null) result.ns_unresolved = CloneUtil.cloneSet(ns_unresolved);
        if (package_unresolved != null) result.package_unresolved = CloneUtil.cloneSet(package_unresolved);
        if (pkgdefs != null) result.pkgdefs = CloneUtil.cloneListPDNode(pkgdefs);
        if (public_namespace != null) result.public_namespace = public_namespace.clone();
        if (rt_unresolved != null) result.rt_unresolved = CloneUtil.cloneSet(rt_unresolved);
        if (statements != null) result.statements = statements.clone();
        if (used_def_namespaces != null) result.used_def_namespaces = CloneUtil.cloneList(used_def_namespaces);

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ProgramNode that = (ProgramNode) o;

        if (has_unnamed_package != that.has_unnamed_package) return false;
        if (state != that.state) return false;
        if (temp_count != that.temp_count) return false;
        if (var_count != that.var_count) return false;
        if (blocks != null ? !blocks.equals(that.blocks) : that.blocks != null) return false;
        if (body_unresolved != null ? !body_unresolved.equals(that.body_unresolved) : that.body_unresolved != null)
            return false;
        if (ce_unresolved != null ? !ce_unresolved.equals(that.ce_unresolved) : that.ce_unresolved != null)
            return false;
        if (clsdefs != null ? !clsdefs.equals(that.clsdefs) : that.clsdefs != null) return false;
        //if (cx != null ? !cx.equals(that.cx) : that.cx != null) return false;
        if (default_namespace != null ? !default_namespace.equals(that.default_namespace) : that.default_namespace != null)
            return false;
        if (fa_unresolved != null ? !fa_unresolved.equals(that.fa_unresolved) : that.fa_unresolved != null)
            return false;
        if (fexprs != null ? !fexprs.equals(that.fexprs) : that.fexprs != null) return false;
        if (frame != null ? !frame.equals(that.frame) : that.frame != null) return false;
        if (importFrame != null ? !importFrame.equals(that.importFrame) : that.importFrame != null) return false;
        if (import_def_unresolved != null ? !import_def_unresolved.equals(that.import_def_unresolved) : that.import_def_unresolved != null)
            return false;
        if (imports != null ? !imports.equals(that.imports) : that.imports != null) return false;
        if (ns_unresolved != null ? !ns_unresolved.equals(that.ns_unresolved) : that.ns_unresolved != null)
            return false;
        if (package_unresolved != null ? !package_unresolved.equals(that.package_unresolved) : that.package_unresolved != null)
            return false;
        if (pkgdefs != null ? !pkgdefs.equals(that.pkgdefs) : that.pkgdefs != null) return false;
        if (public_namespace != null ? !public_namespace.equals(that.public_namespace) : that.public_namespace != null)
            return false;
        return !(rt_unresolved != null ? !rt_unresolved.equals(that.rt_unresolved) : that.rt_unresolved != null) && !(statements != null ? !statements.equals(that.statements) : that.statements != null) && !(used_def_namespaces != null ? !used_def_namespaces.equals(that.used_def_namespaces) : that.used_def_namespaces != null);

    }

//    @Override
//    public int hashCode() {
//        int result = super.hashCode();
//        result = 31 * result + (statements != null ? statements.hashCode() : 0);
//        result = 31 * result + (fexprs != null ? fexprs.hashCode() : 0);
//        result = 31 * result + (clsdefs != null ? clsdefs.hashCode() : 0);
//        result = 31 * result + (pkgdefs != null ? pkgdefs.hashCode() : 0);
//        result = 31 * result + (imports != null ? imports.hashCode() : 0);
//        result = 31 * result + temp_count;
//        result = 31 * result + var_count;
//        result = 31 * result + (blocks != null ? blocks.hashCode() : 0);
//        result = 31 * result + (cx != null ? cx.hashCode() : 0);
//        result = 31 * result + (has_unnamed_package ? 1 : 0);
//        result = 31 * result + (frame != null ? frame.hashCode() : 0);
//        result = 31 * result + (importFrame != null ? importFrame.hashCode() : 0);
//        result = 31 * result + (default_namespace != null ? default_namespace.hashCode() : 0);
//        result = 31 * result + (public_namespace != null ? public_namespace.hashCode() : 0);
//        result = 31 * result + state;
//        result = 31 * result + (used_def_namespaces != null ? used_def_namespaces.hashCode() : 0);
//        result = 31 * result + (import_def_unresolved != null ? import_def_unresolved.hashCode() : 0);
//        result = 31 * result + (package_unresolved != null ? package_unresolved.hashCode() : 0);
//        result = 31 * result + (ns_unresolved != null ? ns_unresolved.hashCode() : 0);
//        result = 31 * result + (fa_unresolved != null ? fa_unresolved.hashCode() : 0);
//        result = 31 * result + (ce_unresolved != null ? ce_unresolved.hashCode() : 0);
//        result = 31 * result + (body_unresolved != null ? body_unresolved.hashCode() : 0);
//        result = 31 * result + (rt_unresolved != null ? rt_unresolved.hashCode() : 0);
//        return result;
//    }
}
