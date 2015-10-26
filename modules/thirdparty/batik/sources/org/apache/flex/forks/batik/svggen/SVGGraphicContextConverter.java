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
package org.apache.flex.forks.batik.svggen;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.flex.forks.batik.ext.awt.g2d.GraphicContext;
import org.apache.flex.forks.batik.ext.awt.g2d.TransformStackElement;

/**
 * This class performs the task of converting the state of the
 * Java 2D API graphic context into a set of graphic attributes.
 * It also manages a set of SVG definitions referenced by the
 * SVG attributes.
 *
 * @author <a href="mailto:vincent.hardy@eng.sun.com">Vincent Hardy</a>
 * @version $Id: SVGGraphicContextConverter.java 478176 2006-11-22 14:50:50Z dvholten $
 */
public class SVGGraphicContextConverter {
    private static final int GRAPHIC_CONTEXT_CONVERTER_COUNT = 6;

    private SVGTransform transformConverter;
    private SVGPaint paintConverter;
    private SVGBasicStroke strokeConverter;
    private SVGComposite compositeConverter;
    private SVGClip clipConverter;
    private SVGRenderingHints hintsConverter;
    private SVGFont fontConverter;
    private SVGConverter[] converters =
        new SVGConverter[GRAPHIC_CONTEXT_CONVERTER_COUNT];

    public SVGTransform getTransformConverter() { return transformConverter; }
    public SVGPaint getPaintConverter(){ return paintConverter; }
    public SVGBasicStroke getStrokeConverter(){ return strokeConverter; }
    public SVGComposite getCompositeConverter(){ return compositeConverter; }
    public SVGClip getClipConverter(){ return clipConverter; }
    public SVGRenderingHints getHintsConverter(){ return hintsConverter; }
    public SVGFont getFontConverter(){ return fontConverter; }

    /**
     * @param generatorContext the context that will be used to create
     * elements, handle extension and images.
     */
    public SVGGraphicContextConverter(SVGGeneratorContext generatorContext) {
        if (generatorContext == null)
            throw new SVGGraphics2DRuntimeException(ErrorConstants.ERR_CONTEXT_NULL);

        transformConverter = new SVGTransform(generatorContext);
        paintConverter = new SVGPaint(generatorContext);
        strokeConverter = new SVGBasicStroke(generatorContext);
        compositeConverter = new SVGComposite(generatorContext);
        clipConverter = new SVGClip(generatorContext);
        hintsConverter = new SVGRenderingHints(generatorContext);
        fontConverter = new SVGFont(generatorContext);

        int i=0;
        converters[i++] = paintConverter;
        converters[i++] = strokeConverter;
        converters[i++] = compositeConverter;
        converters[i++] = clipConverter;
        converters[i++] = hintsConverter;
        converters[i++] = fontConverter;
    }

    /**
     * @return a String containing the transform attribute value
     *         equivalent of the input transform stack.
     */
    public String toSVG(TransformStackElement[] transformStack) {
        return transformConverter.toSVGTransform(transformStack);
    }

    /**
     * @return an object that describes the set of SVG attributes that
     *         represent the equivalent of the input GraphicContext state.
     */
    public SVGGraphicContext toSVG(GraphicContext gc) {
        // no need for synchronized map => use HashMap
        Map groupAttrMap = new HashMap();

        for (SVGConverter converter : converters) {
            SVGDescriptor desc = converter.toSVG(gc);
            if (desc != null)
                desc.getAttributeMap(groupAttrMap);
        }

        // the ctor will to the splitting (group/element) job
        return new SVGGraphicContext(groupAttrMap,
                                     gc.getTransformStack());
    }

    /**
     * @return a set of element containing definitions for the attribute
     *         values generated by this converter since its creation.
     */
    public List getDefinitionSet() {
        List defSet = new LinkedList();
        for (SVGConverter converter : converters) defSet.addAll(converter.getDefinitionSet());

        return defSet;
    }
}
