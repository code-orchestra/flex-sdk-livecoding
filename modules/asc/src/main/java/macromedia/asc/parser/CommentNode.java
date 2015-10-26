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

/**
 * CommentNode
 */
public class CommentNode extends Node
{
	int type;		// either slashslashcomment_token or blockcomment_token 
	String comment;	// contents of comment string including delimiters 

	public CommentNode(String val, int ctype)
	{
		type = ctype;
		comment = val;
	}

	public String toString()
	{
		return comment;
	}
	
	public int getType(){
		return type;
	}

    public CommentNode clone() throws CloneNotSupportedException
    {
        CommentNode result = (CommentNode) super.clone();

        return result;
    }

    @Override
    public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;

		CommentNode that = (CommentNode) o;

		return type == that.type && !(comment != null ? !comment.equals(that.comment) : that.comment != null);

	}

//    @Override
//    public int hashCode() {
//        int result = super.hashCode();
//        result = 31 * result + type;
//        result = 31 * result + (comment != null ? comment.hashCode() : 0);
//        return result;
//    }
}
