/*

   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */
package org.apache.flex.forks.batik.svggen.font.table;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author <a href="mailto:david@steadystate.co.uk">David Schweinsberg</a>
 * @version $Id: KernSubtableFormat2.java 475477 2006-11-15 22:44:28Z cam $
 */
public class KernSubtableFormat2 extends KernSubtable {

    private int rowWidth;
    private int leftClassTable;
    private int rightClassTable;
    private int array;

    /** Creates new KernSubtableFormat2 */
    protected KernSubtableFormat2(RandomAccessFile raf) throws IOException {
        rowWidth = raf.readUnsignedShort();
        leftClassTable = raf.readUnsignedShort();
        rightClassTable = raf.readUnsignedShort();
        array = raf.readUnsignedShort();
    }

    public int getKerningPairCount() {
        return 0;
    }

    public KerningPair getKerningPair(int i) {
        return null;
    }

}
