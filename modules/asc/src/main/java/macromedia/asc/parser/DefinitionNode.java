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
 * Node
 */
public abstract class DefinitionNode extends Node
{
	public AttributeListNode attrs;

    public StatementListNode metaData;
    public PackageDefinitionNode pkgdef;

	// C: allow Evaluators to skip this definition node and the rest of the branch. e.g. LintEvaluator skips
	//    coaching of VariableDefinitionNode, FunctionDefinition and ClassDefinitionNode. The purpose is
	//    to allow LintEvaluator to coach part of ProgramNode. Other Evaluators can find this boolean value
	//    for different purposes.
	private boolean skip;
	//skip is also now used by uiactionsevaluator to keep from duplicate processing of nodes

	public DefinitionNode(PackageDefinitionNode pkgdef, AttributeListNode attrs, int pos)
	{
		super(pos);
		this.attrs = attrs;
		this.pkgdef = pkgdef;
	}
	
	public boolean isDefinition()
	{
		return true;
	}

	public void skipNode(boolean b)
	{
		skip = b;
	}

	public boolean skip()
	{
		return skip;
	}

    public void addMetaDataNode(Node node)
    {
        if( metaData == null )
        {
            metaData = new StatementListNode(node);
        }
        else
        {
            metaData.items.push_back(node);
        }
    }

    public DefinitionNode clone() throws CloneNotSupportedException
    {
        DefinitionNode result = (DefinitionNode) super.clone();

        if (attrs != null) result.attrs = attrs.clone();
        if (metaData != null)
        {
            result.metaData = metaData.cloneWithDef(this, result);
        }
        if (pkgdef != null) result.pkgdef = pkgdef.clone();

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        DefinitionNode that = (DefinitionNode) o;

        if (skip != that.skip) return false;
        if (attrs != null ? !attrs.equals(that.attrs) : that.attrs != null) return false;
        if (metaData != null ? !metaData.equals(that.metaData) : that.metaData != null) return false;
        return !(pkgdef != null ? !pkgdef.equals(that.pkgdef) : that.pkgdef != null);

    }

//    @Override
//    public int hashCode() {
//        int result = super.hashCode();
//        result = 31 * result + (attrs != null ? attrs.hashCode() : 0);
//        result = 31 * result + (metaData != null ? metaData.hashCode() : 0);
//        result = 31 * result + (pkgdef != null ? pkgdef.hashCode() : 0);
//        result = 31 * result + (skip ? 1 : 0);
//        return result;
//    }
}
