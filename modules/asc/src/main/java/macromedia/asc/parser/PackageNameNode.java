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
public class PackageNameNode extends Node
{
	public LiteralStringNode url;
	public PackageIdentifiersNode id;

	public PackageNameNode(LiteralStringNode url, int pos)
	{
		super(pos);
		this.url = url;
		this.id = null;
	}

	public PackageNameNode(PackageIdentifiersNode id, int pos)
	{
		super(pos);
		this.url = null;
		this.id = id;
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
		return "PackageName";
	}

    public PackageNameNode clone() throws CloneNotSupportedException
    {
        PackageNameNode result = (PackageNameNode) super.clone();

        if (id != null) result.id = id.clone();
        if (url != null) result.url = url.clone();

        return result;
    }

    @Override
    public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;

		PackageNameNode that = (PackageNameNode) o;

		return !(id != null ? !id.equals(that.id) : that.id != null) && !(url != null ? !url.equals(that.url) : that.url != null);

	}

//    @Override
//    public int hashCode() {
//        int result = super.hashCode();
//        result = 31 * result + (url != null ? url.hashCode() : 0);
//        result = 31 * result + (id != null ? id.hashCode() : 0);
//        return result;
//    }
}
