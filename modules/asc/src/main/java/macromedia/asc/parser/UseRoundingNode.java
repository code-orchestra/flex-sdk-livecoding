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

import macromedia.asc.util.*;
import macromedia.asc.semantics.*;

/**
 * Node
 */


public class UseRoundingNode extends UsePragmaNode {

	public int mode;

	public UseRoundingNode(Node id, Node argument)
	{
		super(id, argument);
		this.mode = NumberUsage.round_HALF_EVEN; // until proven otherwise
		if (argument instanceof IdentifierNode) {
			String arg = ((IdentifierNode)argument).toIdentifierString();
			switch (arg) {
				case "HALF_EVEN":
					mode = NumberUsage.round_HALF_EVEN;
					break;
				case "DOWN":
					mode = NumberUsage.round_DOWN;
					break;
				case "FLOOR":
					mode = NumberUsage.round_FLOOR;
					break;
				case "UP":
					mode = NumberUsage.round_UP;
					break;
				case "CEILING":
					mode = NumberUsage.round_CEILING;
					break;
				case "HALF_UP":
					mode = NumberUsage.round_HALF_UP;
					break;
				case "HALF_DOWN":
					mode = NumberUsage.round_HALF_DOWN;
					break;
			}
			// should report error if something else
		}
		// should report error if not identifier
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
		return "UseRounding " + NumberUsage.roundingModeName[mode];
	}

    public UseRoundingNode clone() throws CloneNotSupportedException
    {
        UseRoundingNode result = (UseRoundingNode) super.clone();

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        UseRoundingNode that = (UseRoundingNode) o;

		return mode == that.mode;

	}

//    @Override
//    public int hashCode() {
//        int result = super.hashCode();
//        result = 31 * result + mode;
//        return result;
//    }
}
