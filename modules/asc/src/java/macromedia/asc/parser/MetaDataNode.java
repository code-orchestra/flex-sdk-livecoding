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

package macromedia.asc.parser;

import macromedia.asc.semantics.MetaData;
import macromedia.asc.semantics.Value;
import macromedia.asc.util.Context;

/**
 * @author Clement Wong
 */
public class MetaDataNode extends Node
{
	public LiteralArrayNode data;

	public MetaDataNode(LiteralArrayNode data)
	{
		this.data = data;
		def = null;
	}

    private MetaData md;

	public DefinitionNode def;

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

    public void setMetadata(MetaData md)
    {
        this.md = md;
    }

	public String getValue(String key)
	{
        return md != null ? md.getValue(key) : null;
	}

	public String getValue(int index)
	{
        return md != null ? md.getValue(index) : null;
	}

	public int count()
	{
		return getValues() != null ? getValues().length : 0;
	}

	public String toString()
	{
		return "MetaData";
	}

    public String getId()
    {
        return md != null ? md.id : null;
    }

    public void setId(String id)
    {
        if( this.md == null )
            this.md = new MetaData();
        this.md.id = id;
    }

    public Value[] getValues()
    {
        return md != null ? md.values : null;
    }

    public void setValues(Value[] values)
    {
        if( this.md == null )
            this.md = new MetaData();
        this.md.values = values;
    }

    public MetaData getMetadata()
    {
        return md;
    }

    public MetaDataNode clone() throws CloneNotSupportedException
    {
        MetaDataNode result = (MetaDataNode) super.clone();

        if (data != null) result.data = data.clone();
        if (def != null) result.def = def.clone();
        if (md != null) result.md = md.clone();

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        MetaDataNode that = (MetaDataNode) o;

        if (data != null ? !data.equals(that.data) : that.data != null) return false;
        if (def != null ? !def.equals(that.def) : that.def != null) return false;
        if (md != null ? !md.equals(that.md) : that.md != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (data != null ? data.hashCode() : 0);
        result = 31 * result + (md != null ? md.hashCode() : 0);
        result = 31 * result + (def != null ? def.hashCode() : 0);
        return result;
    }
}
