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
public class HasNextNode extends Node
{
	public RegisterNode objectRegister;
	public RegisterNode indexRegister;

	public HasNextNode(RegisterNode objectRegister,
					   RegisterNode indexRegister)
	{
		this.objectRegister = objectRegister;
		this.indexRegister = indexRegister;		
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
		return "HasNext";
	}

    public HasNextNode clone() throws CloneNotSupportedException
    {
        HasNextNode result = (HasNextNode) super.clone();

        if (indexRegister != null) result.indexRegister = indexRegister.clone();
        if (objectRegister != null) result.objectRegister = objectRegister.clone();

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        HasNextNode that = (HasNextNode) o;

        if (indexRegister != null ? !indexRegister.equals(that.indexRegister) : that.indexRegister != null)
            return false;
		return !(objectRegister != null ? !objectRegister.equals(that.objectRegister) : that.objectRegister != null);

	}

//    @Override
//    public int hashCode() {
//        int result = super.hashCode();
//        result = 31 * result + (objectRegister != null ? objectRegister.hashCode() : 0);
//        result = 31 * result + (indexRegister != null ? indexRegister.hashCode() : 0);
//        return result;
//    }
}
