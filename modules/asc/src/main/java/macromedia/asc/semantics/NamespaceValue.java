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

import macromedia.asc.util.Context;

public class NamespaceValue extends ObjectValue
{
	public NamespaceValue()
	{
		super();
        ns_kind = Context.NS_PUBLIC;
        config_ns = false;
	}

    public byte ns_kind;
    boolean config_ns;

    public NamespaceValue(byte ns_kind)
    {
        this.ns_kind = ns_kind;
    }

    public boolean isInternal()
    {
        return ns_kind == Context.NS_INTERNAL;
    }

    public boolean isProtected()
    {
        return ns_kind == Context.NS_PROTECTED;
    }

    public boolean isPrivate()
    {
        return ns_kind == Context.NS_PRIVATE;
    }

    public boolean isConfigNS()
    {
        return config_ns;
    }
    
    public byte getNamespaceKind()
    {
        return ns_kind;
    }

    public NamespaceValue clone() throws CloneNotSupportedException
    {
        NamespaceValue result = (NamespaceValue) super.clone();

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        NamespaceValue that = (NamespaceValue) o;

        if (config_ns != that.config_ns) return false;
        if (ns_kind != that.ns_kind) return false;

        return true;
    }

//    @Override
//    public int hashCode() {
//        int result = super.hashCode();
//        result = 31 * result + (int) ns_kind;
//        result = 31 * result + (config_ns ? 1 : 0);
//        return result;
//    }
}
