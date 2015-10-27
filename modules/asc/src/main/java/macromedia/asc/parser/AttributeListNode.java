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
 */
public class AttributeListNode extends Node
{
	public ObjectList<Node> items = new ObjectList<Node>(1);
	public boolean hasIntrinsic;
	public boolean hasStatic;
	public boolean hasFinal;
	public boolean hasVirtual;
	public boolean hasOverride;
	public boolean hasDynamic;
    public boolean hasNative;
    public boolean hasPrivate;
    public boolean hasProtected;
    public boolean hasPublic;
    public boolean hasInternal;
    public boolean hasConst;
    public boolean hasFalse;
    public boolean hasPrototype;
    public boolean compileDefinition;
 
    public Namespaces namespaces = new Namespaces(3);
    public ObjectList<String> namespace_ids = new ObjectList<String>(3);

    private ObjectValue userNamespace;
    
	public AttributeListNode(Node item, int pos)
	{
		super(pos);
		items.add(item);
		hasIntrinsic = false;
		hasStatic = false;
		hasFinal = false;
		hasVirtual = false;
		hasOverride = false;
		hasDynamic = false;
		hasNative = false;
        hasPrivate = false;
        hasProtected = false;
        hasPublic = false;
        hasInternal = false;
        hasConst = false;
        hasFalse = false;
        hasPrototype = false;
        compileDefinition = true; // Compile everything by default
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

	int size()
	{
		return items.size();
	}

	public int pos()
	{
		return items.size() != 0 ? items.last().pos() : 0;
	}

	public String toString()
	{
      if(Node.useDebugToStrings)
         return "AttributeList@" + pos();
      else
         return "AttributeList";
	}

	public boolean isAttribute()
	{
		for (Node n : items)
		{
			if (n.isAttribute())
			{
				return false;
			}
		}
		return true;
	}

	public boolean hasAttribute(String name)
	{
		for (Node n : items)
		{
			if (n.hasAttribute(name))
			{
				return true;
			}
		}
		return false;
	}

	public boolean isLabel()
	{
		return items.size() == 1 && items.last().isLabel();
	}
	
	public ObjectValue getUserNamespace()
	{
		return userNamespace;
	}
	
	public void setUserNamespace(ObjectValue userNamespace)
	{
		this.userNamespace = userNamespace;
	}
	
	public boolean hasUserNamespace()
	{
		return userNamespace != null;
	}

    public AttributeListNode clone() throws CloneNotSupportedException
    {
        AttributeListNode result = (AttributeListNode) super.clone();

        if (items != null) result.items = CloneUtil.cloneListNode(items);
        if (namespace_ids != null) result.namespace_ids = CloneUtil.cloneListString(namespace_ids);
        if (namespaces != null) result.namespaces = CloneUtil.cloneList(namespaces);
        if (userNamespace != null) result.userNamespace = userNamespace.clone();

        return result;
    }

    @Override
    public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;

		AttributeListNode that = (AttributeListNode) o;

		if (compileDefinition != that.compileDefinition) return false;
		if (hasConst != that.hasConst) return false;
		if (hasDynamic != that.hasDynamic) return false;
		if (hasFalse != that.hasFalse) return false;
		if (hasFinal != that.hasFinal) return false;
		if (hasInternal != that.hasInternal) return false;
		if (hasIntrinsic != that.hasIntrinsic) return false;
		if (hasNative != that.hasNative) return false;
		if (hasOverride != that.hasOverride) return false;
		if (hasPrivate != that.hasPrivate) return false;
		if (hasProtected != that.hasProtected) return false;
		if (hasPrototype != that.hasPrototype) return false;
		if (hasPublic != that.hasPublic) return false;
		if (hasStatic != that.hasStatic) return false;
		if (hasVirtual != that.hasVirtual) return false;
		if (items != null ? !items.equals(that.items) : that.items != null) return false;
		return !(namespace_ids != null ? !namespace_ids.equals(that.namespace_ids) : that.namespace_ids != null) && !(namespaces != null ? !namespaces.equals(that.namespaces) : that.namespaces != null) && !(userNamespace != null ? !userNamespace.equals(that.userNamespace) : that.userNamespace != null);

	}

//    @Override
//    public int hashCode() {
//        int result = super.hashCode();
//        result = 31 * result + (items != null ? items.hashCode() : 0);
//        result = 31 * result + (hasIntrinsic ? 1 : 0);
//        result = 31 * result + (hasStatic ? 1 : 0);
//        result = 31 * result + (hasFinal ? 1 : 0);
//        result = 31 * result + (hasVirtual ? 1 : 0);
//        result = 31 * result + (hasOverride ? 1 : 0);
//        result = 31 * result + (hasDynamic ? 1 : 0);
//        result = 31 * result + (hasNative ? 1 : 0);
//        result = 31 * result + (hasPrivate ? 1 : 0);
//        result = 31 * result + (hasProtected ? 1 : 0);
//        result = 31 * result + (hasPublic ? 1 : 0);
//        result = 31 * result + (hasInternal ? 1 : 0);
//        result = 31 * result + (hasConst ? 1 : 0);
//        result = 31 * result + (hasFalse ? 1 : 0);
//        result = 31 * result + (hasPrototype ? 1 : 0);
//        result = 31 * result + (compileDefinition ? 1 : 0);
//        result = 31 * result + (namespaces != null ? namespaces.hashCode() : 0);
//        result = 31 * result + (namespace_ids != null ? namespace_ids.hashCode() : 0);
//        result = 31 * result + (userNamespace != null ? userNamespace.hashCode() : 0);
//        return result;
//    }
}

