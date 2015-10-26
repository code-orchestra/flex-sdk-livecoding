/*
 *
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package macromedia.asc.semantics;

import macromedia.asc.parser.Node;
import macromedia.asc.util.Context;

public class UnresolvedNamespace extends NamespaceValue
{
	public UnresolvedNamespace(Context cx, Node node, ReferenceValue ref)
	{
		super();
		this.node = node;
		this.ref = ref;
		resolved = false;
        this.cx = cx.makeCopyOf();  // must make a copy of the current context, the actual context will have
                                    //  its guts swapped out when we go into or out of an included file.
	}

	public Node node;
	public ReferenceValue ref;
	public boolean resolved;
    public Context cx;              // We must report errors relative to this context.  node could come from an included file.

    public UnresolvedNamespace clone() throws CloneNotSupportedException
    {
        UnresolvedNamespace result = (UnresolvedNamespace) super.clone();

        if (cx != null);// FIXME: need clone
        if (node != null) result.node = node.clone();
        if (ref != null) result.ref = ref.clone();

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        UnresolvedNamespace that = (UnresolvedNamespace) o;

        if (resolved != that.resolved) return false;
        if (cx != null ? !cx.equals(that.cx) : that.cx != null) return false;
        return !(node != null ? !node.equals(that.node) : that.node != null) && !(ref != null ? !ref.equals(that.ref) : that.ref != null);

    }

//    @Override
//    public int hashCode() {
//        int result = super.hashCode();
//        result = 31 * result + (node != null ? node.hashCode() : 0);
//        result = 31 * result + (ref != null ? ref.hashCode() : 0);
//        result = 31 * result + (resolved ? 1 : 0);
//        result = 31 * result + (cx != null ? cx.hashCode() : 0);
//        return result;
//    }
}
