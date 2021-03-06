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

import java.io.Serializable;

public abstract class NumberConstant implements Serializable, Cloneable { // CodeOrchestra: made serializable
	
	protected final static double MAXUINT = ((double)Integer.MAX_VALUE) * 2 + 1;

	public abstract byte number_type();
	
	public abstract int intValue(); // value as an integer
	
	public abstract long uintValue(); // value as unsigned integer
	
	public abstract double doubleValue();
	
	public abstract Decimal128 decimalValue();
	
	public abstract String toString();

    public NumberConstant clone() throws CloneNotSupportedException
    {

		return (NumberConstant) super.clone();
    }
}
