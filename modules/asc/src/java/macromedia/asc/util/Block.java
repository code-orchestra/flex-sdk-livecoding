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

package macromedia.asc.util;

/**
 * @author Jeff Dyer
 */
public class Block implements Cloneable
{
	public Block() { is_terminal = false; }
	
	public BitSet def_bits;
	public BitSet gen_bits;
	public BitSet kill_bits;
	public BitSet in_bits;
	public BitSet out_bits;
	public String stmts;
	public boolean is_terminal; // This block can not have a successor, it represents the block jumped to by "return"

	//public ObjectList<Node> nodes = new ObjectList<Node>();
	//public ObjectList<Node> epilog = new ObjectList<Node>();
	public IntList preds = new IntList(1);
	public IntList succs = new IntList(1);
	//public Blocks preds_blk = new Blocks();
	//public Blocks succs_blk = new Blocks();

    public Block clone() throws CloneNotSupportedException {
        Block result = (Block) super.clone();

        result.def_bits = BitSet.copy(def_bits);
        result.gen_bits = BitSet.copy(gen_bits);
        result.kill_bits = BitSet.copy(kill_bits);
        result.in_bits = BitSet.copy(in_bits);
        result.out_bits = BitSet.copy(out_bits);

        result.preds = new IntList(preds);
        result.succs = new IntList(succs);

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Block block = (Block) o;

        if (is_terminal != block.is_terminal) return false;
        if (def_bits != null ? !def_bits.equals(block.def_bits) : block.def_bits != null) return false;
        if (gen_bits != null ? !gen_bits.equals(block.gen_bits) : block.gen_bits != null) return false;
        if (in_bits != null ? !in_bits.equals(block.in_bits) : block.in_bits != null) return false;
        if (kill_bits != null ? !kill_bits.equals(block.kill_bits) : block.kill_bits != null) return false;
        if (out_bits != null ? !out_bits.equals(block.out_bits) : block.out_bits != null) return false;
        if (preds != null ? !preds.equals(block.preds) : block.preds != null) return false;
        if (stmts != null ? !stmts.equals(block.stmts) : block.stmts != null) return false;
        if (succs != null ? !succs.equals(block.succs) : block.succs != null) return false;

        return true;
    }

//    @Override
//    public int hashCode() {
//        int result = def_bits != null ? def_bits.hashCode() : 0;
//        result = 31 * result + (gen_bits != null ? gen_bits.hashCode() : 0);
//        result = 31 * result + (kill_bits != null ? kill_bits.hashCode() : 0);
//        result = 31 * result + (in_bits != null ? in_bits.hashCode() : 0);
//        result = 31 * result + (out_bits != null ? out_bits.hashCode() : 0);
//        result = 31 * result + (stmts != null ? stmts.hashCode() : 0);
//        result = 31 * result + (is_terminal ? 1 : 0);
//        result = 31 * result + (preds != null ? preds.hashCode() : 0);
//        result = 31 * result + (succs != null ? succs.hashCode() : 0);
//        return result;
//    }
}
