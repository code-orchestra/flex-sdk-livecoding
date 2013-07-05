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
 *
 * @author Jeff Dyer
 */
public class IncludeDirectiveNode extends DefinitionNode
{
	public LiteralStringNode filespec;
	public ProgramNode program;
    public transient Context cx; // CodeOrchestra: made transient
    public boolean in_this_include;
    public transient Context prev_cx; // CodeOrchestra: made transient

	public IncludeDirectiveNode(Context cx, LiteralStringNode filespec, ProgramNode program)
	{
        super(null, null, -1);
		this.filespec = filespec;
		this.program = program;
        this.cx = cx;
        in_this_include = false;
        prev_cx = null;        
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
		return "IncludeDirective";
	}

    public IncludeDirectiveNode clone() throws CloneNotSupportedException
    {
        IncludeDirectiveNode result = (IncludeDirectiveNode) super.clone();

        // cx is transient
        //if (cx != null);
        // prev_cx is transient
        //if (prev_cx != null);
        if (filespec != null) result.filespec = filespec.clone();
        if (program != null) result.program = program.clone();

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        IncludeDirectiveNode that = (IncludeDirectiveNode) o;

        if (in_this_include != that.in_this_include) return false;
        if (cx != null ? !cx.equals(that.cx) : that.cx != null) return false;
        if (filespec != null ? !filespec.equals(that.filespec) : that.filespec != null) return false;
        if (prev_cx != null ? !prev_cx.equals(that.prev_cx) : that.prev_cx != null) return false;
        if (program != null ? !program.equals(that.program) : that.program != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (filespec != null ? filespec.hashCode() : 0);
        result = 31 * result + (program != null ? program.hashCode() : 0);
        result = 31 * result + (cx != null ? cx.hashCode() : 0);
        result = 31 * result + (in_this_include ? 1 : 0);
        result = 31 * result + (prev_cx != null ? prev_cx.hashCode() : 0);
        return result;
    }
}
