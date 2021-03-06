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
package flash.tools.debugger;

/**
 * The Isolate object uniquely identifies a "Worker" in ActionScript.
 * Workers are conceptually similar to Threads, but their implementation
 * closely follows more that of a web worker than an actual OS Thread.
 * 
 * By default there is a default isolate object with id DEFAULT_ID.
 */
public interface Isolate {
	
	int DEFAULT_ID = 1;
	
	/**
	 * Get the unique integer ID associated with the
	 * worker. This is Isolate.DEFAULT_ID for the
	 * primordial. 
	 * @return unique integer ID
	 */
	int getId();
	
}
