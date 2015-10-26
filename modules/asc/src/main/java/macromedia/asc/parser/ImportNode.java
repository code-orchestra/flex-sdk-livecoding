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
public class ImportNode extends Node
{
	public LiteralStringNode filespec;
	public ProgramNode program;

	public ImportNode(LiteralStringNode spec, ProgramNode prog)
	{
		this.filespec = spec;
		this.program = prog;
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
		return "ImportNode";
	}

    public ImportNode clone() throws CloneNotSupportedException
    {
        ImportNode result = (ImportNode) super.clone();

        if (filespec != null) result.filespec = filespec.clone();
        if (program != null) result.program = program.clone();

        return result;
    }

    @Override
    public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;

		ImportNode that = (ImportNode) o;

		return !(filespec != null ? !filespec.equals(that.filespec) : that.filespec != null) && !(program != null ? !program.equals(that.program) : that.program != null);

	}

//    @Override
//    public int hashCode() {
//        int result = super.hashCode();
//        result = 31 * result + (filespec != null ? filespec.hashCode() : 0);
//        result = 31 * result + (program != null ? program.hashCode() : 0);
//        return result;
//    }
}
