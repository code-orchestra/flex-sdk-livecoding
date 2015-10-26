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
 */
public class ImportDirectiveNode extends DefinitionNode
{
	public AttributeListNode attrs;
	public PackageNameNode name;
	public PackageDefinitionNode pkg_node;
	public ReferenceValue ref;
	public boolean package_retrieved;
	public transient Context cx; // CodeOrchestra: made transient

	public ImportDirectiveNode(PackageDefinitionNode pkgdef, AttributeListNode attrs, PackageNameNode name, PackageDefinitionNode pkg_node, Context cx)
	{
		super(pkgdef, attrs, -1);
		ref = null;
		this.attrs = attrs;
		this.name = name;
		this.pkg_node = pkg_node;
		package_retrieved = false;
		this.cx = cx;
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

	public boolean isConst()
	{
		return true;
	}

	public Node initializerStatement(Context cx)
	{
		return cx.getNodeFactory().emptyStatement();
	}

	public String toString()
	{
		return "importdirective";
	}

    public ImportDirectiveNode clone() throws CloneNotSupportedException
    {
        ImportDirectiveNode result = (ImportDirectiveNode) super.clone();

        if (attrs != null) result.attrs = attrs.clone();
        // cx is transient
        //if (cx != null);
        result.cx = null;
        if (name != null) result.name = name.clone();
        if (pkg_node != null) result.pkg_node = pkg_node.clone();
        if (ref != null) result.ref = ref.clone();

        return result;
    }

    @Override
    public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;

		ImportDirectiveNode that = (ImportDirectiveNode) o;

		if (package_retrieved != that.package_retrieved) return false;
		if (attrs != null ? !attrs.equals(that.attrs) : that.attrs != null) return false;
		//if (cx != null ? !cx.equals(that.cx) : that.cx != null) return false;
		if (name != null ? !name.equals(that.name) : that.name != null) return false;
		return !(pkg_node != null ? !pkg_node.equals(that.pkg_node) : that.pkg_node != null) && !(ref != null ? !ref.equals(that.ref) : that.ref != null);

	}

//    @Override
//    public int hashCode() {
//        int result = super.hashCode();
//        result = 31 * result + (attrs != null ? attrs.hashCode() : 0);
//        result = 31 * result + (name != null ? name.hashCode() : 0);
//        result = 31 * result + (pkg_node != null ? pkg_node.hashCode() : 0);
//        result = 31 * result + (ref != null ? ref.hashCode() : 0);
//        result = 31 * result + (package_retrieved ? 1 : 0);
//        result = 31 * result + (cx != null ? cx.hashCode() : 0);
//        return result;
//    }
}
