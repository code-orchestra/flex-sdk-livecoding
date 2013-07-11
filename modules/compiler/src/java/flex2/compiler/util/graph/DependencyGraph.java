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

package flex2.compiler.util.graph;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

//TODO Try to remove this class and use ASC's equivalent

/**
 * Used by the compiler driver to represent a dependency graph.
 *
 * @author Clement Wong
 */
public class DependencyGraph<EdgeWeight> extends Graph<String, EdgeWeight> {
    public DependencyGraph() {
    }

    private final Map<String, EdgeWeight> map = new HashMap<String, EdgeWeight>(300);
    private final Map<String, Vertex<String, EdgeWeight>> vertices = new HashMap<String, Vertex<String, EdgeWeight>>(300);

    // put(), get(), remove() are methods for 'map'

    public void put(String key, EdgeWeight value) {
        synchronized (map) {
            map.put(key, value);
        }
    }

    public synchronized EdgeWeight get(String key) {
        synchronized (map) {
            return map.get(key);
        }
    }

    public synchronized void remove(String key) {
        synchronized (map) {
            map.remove(key);
        }
    }

    public Set<String> keySet() {
        synchronized (map) {
            return map.keySet();
        }
    }

    public int size() {
        synchronized (map) {
            return map.size();
        }
    }

    public boolean containsKey(String key) {
        synchronized (map) {
            return map.containsKey(key);
        }
    }

    public boolean containsVertex(String key) {
        synchronized (vertices) {
            return vertices.containsKey(key);
        }
    }

    public synchronized void clear() {
        super.clear();
        map.clear();
        vertices.clear();
    }

    // methods for graph manipulations

    public void addVertex(Vertex<String, EdgeWeight> v) {
        synchronized (vertices) {
            super.addVertex(v);
            vertices.put(v.getWeight(), v);
        }
    }

    public Vertex<String, EdgeWeight> getVertex(String weight) {
        synchronized (vertices) {
            return vertices.get(weight);
        }
    }

    public void removeVertex(String weight) {
        synchronized (vertices) {
            Vertex<String, EdgeWeight> v = vertices.remove(weight);
            if (v != null) {
                super.removeVertex(v);
            }
        }
    }

    public void addDependency(String name, String dep) {
        synchronized (vertices) {
            Vertex<String, EdgeWeight> tail = null, head = null;

            if ((head = vertices.get(name)) == null) {
                head = new Vertex<String, EdgeWeight>(name);
                addVertex(head);
            }

            if ((tail = vertices.get(dep)) == null) {
                tail = new Vertex<String, EdgeWeight>(dep);
                addVertex(tail);
            }

            addEdge(new Edge<String, EdgeWeight>(tail, head, null));
        }
    }

    public boolean dependencyExists(String name, String dep) {
        synchronized (vertices) {
            Vertex<String, EdgeWeight> tail = null, head = null;

            if ((head = vertices.get(name)) == null) {
                return false;
            }

            if ((tail = vertices.get(dep)) == null) {
                return false;
            }

            Set<Vertex<String, EdgeWeight>> predecessors = head.getPredecessors();

            if (predecessors != null) {
                return predecessors.contains(tail);
            } else {
                return false;
            }
        }
    }

    /**
     * Get the dependencies of a given node.
     *
     * @param name The node to get the dependencies for.
     * @return The set of dependencies.
     */
    public Set<String> getDependencies(String name) {
        synchronized (vertices) {
            Vertex<String, EdgeWeight> head = null;

            if ((head = vertices.get(name)) == null) {
                return Collections.emptySet();
            }

            Set<String> dependencies = new LinkedHashSet<String>();
            Set<Vertex<String, EdgeWeight>> predecessors = head.getPredecessors();

            if (predecessors != null) {
                for (Vertex<String, EdgeWeight> pred : predecessors) {
                    dependencies.add(pred.getWeight());
                }
            }

            return dependencies;
        }
    }
}

