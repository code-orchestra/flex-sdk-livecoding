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

package flex2.compiler.mxml.rep;

import flex2.compiler.mxml.reflect.Type;

/**
 * This class represents an XMLList node in a Mxml document.
 */
public class XMLList extends Model {
    private String literalXML;
    private boolean hasBindings;

    public XMLList(MxmlDocument document, Type type, Model parent, int line) {
        super(document, type, parent, line);
    }

    public boolean hasBindings() {
        return hasBindings;
    }

    public void setHasBindings(boolean hasBindings) {
        this.hasBindings = hasBindings;
    }

    public String getLiteralXML() {
        return literalXML;
    }

    public void setLiteralXML(String literalXML) {
        this.literalXML = literalXML;
    }
}
