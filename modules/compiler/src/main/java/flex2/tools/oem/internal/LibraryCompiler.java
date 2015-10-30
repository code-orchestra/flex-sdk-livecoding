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

package flex2.tools.oem.internal;

import java.io.*;
import java.net.*;
import java.util.*;

import flash.localization.LocalizationManager;
import flash.localization.ResourceBundleLocalizer;
import flex2.compiler.CompilerAPI;
import flex2.compiler.common.DefaultsConfigurator;
import flex2.compiler.config.ConfigurationBuffer;
import flex2.compiler.config.ConfigurationException;
import flex2.compiler.io.FileUtil;
import flex2.compiler.io.VirtualFile;
import flex2.compiler.swc.SwcException;
import flex2.compiler.util.ThreadLocalToolkit;
import flex2.linker.FlexMovie;
import flex2.tools.CompcConfiguration;
import flex2.tools.Mxmlc;
import flex2.tools.oem.Library;
import flex2.tools.oem.Report;

/**
 * Command line utility for compiling libraries using the OEM API.
 *
 * @version 2.0.1
 */
public class LibraryCompiler
{
	public static void main(String[] args)
	{
	    final OEMConsole console = new OEMConsole();

        try
        {
            init();
            run(console, args);
        }
        catch (ConfigurationException ex)
        {
            Mxmlc.processConfigurationException(ex, "compc");
        }
        catch (SwcException ex)
        {
            assert ThreadLocalToolkit.errorCount() > 0;
        }
        catch (Throwable t) // IOException, Throwable
        {
            ThreadLocalToolkit.logError(t.getMessage());
            t.printStackTrace();
        }
        finally
        {
            clean();
        }

        System.exit(console.errorCount());
	}

	public static void run(OEMConsole console, String[] args)
        throws ConfigurationException, IOException, URISyntaxException
	{
	    ThreadLocalToolkit.setLogger(console);
        
		final Library lib = new Library();
		lib.setLogger(console);
		OEMConfiguration c1 = (OEMConfiguration) lib.getDefaultConfiguration();
		CompcConfiguration c2 = getCompcConfiguration(args);
		OEMConfiguration c3 = new OEMConfiguration(null, c2);
		c1.importDefaults(c3);
		lib.setConfiguration(c1);
	
		// transfer the value of compute-digest from CompcConfiguration to OEMConfiguration.
		c1.enableDigestComputation(c2.getComputeDigest());

		c2.getClasses().forEach(lib::addComponent);
		
		List fileList = flex2.compiler.CompilerAPI.getVirtualFileList(c2.getIncludeSources(),
				new HashSet<>(Arrays.asList(flex2.tools.WebTierAPI.getSourcePathMimeTypes())));

		for (Object aFileList : fileList) {
			lib.addComponent(new File(((VirtualFile) aFileList).getName()));
		}

		Map ss = c2.getStylesheets();
		for (Object o1 : ss.keySet()) {
			String key = (String) o1;
			lib.addStyleSheet(key, new File(((VirtualFile) ss.get(key)).getName()));
		}

		c2.getIncludeResourceBundles().forEach(lib::addResourceBundle);

		for (String s : c2.getNamespaces()) {
			lib.addComponent(new URI(s));
		}
		
		Map m = c2.getFiles();
		for (Object o : m.keySet()) {
			String key = (String) o;
			lib.addArchiveFile(key, new File(((VirtualFile) m.get(key)).getName()));
		}
        
        try
		{
            lib.load(new BufferedInputStream(new FileInputStream(new File(c2.getOutput() + ".incr"))));
            // load() wipes out our ThreadLocal and we lose our logger
            ThreadLocalToolkit.setLogger(console);
		}
		catch (IOException ignored)
		{
		}
        
		long size;
		if ((size = lib.build(new BufferedOutputStream(new FileOutputStream(new File(c2.getOutput()))), true)) == 0)
		{
            ThreadLocalToolkit.logError("Build unsuccessful.");
		}
		else
		{
			System.out.println(c2.getOutput() + " (" + size + " bytes)");
		    if (c2.generateRBList() && c2.getRBListFileName() != null)
		    {
		    	Report r = lib.getReport();
		    	String[] rbNames = r.getResourceBundleNames();
		    	HashSet<String> set = new HashSet<>();
		    	for (int i = 0, l = rbNames == null ? 0 : rbNames.length; i < l; i++)
		    	{
		    		set.add(rbNames[i]);
		    	}
		    	String list = FlexMovie.dumpRBList(set);
		    	FileUtil.writeFile(c2.getRBListFileName(), list);
		    }

		}

		lib.save(new BufferedOutputStream(new FileOutputStream(new File(c2.getOutput() + ".incr"))));		
		lib.clean();
	}
	
	private static CompcConfiguration getCompcConfiguration(String[] args)
        throws ConfigurationException, IOException
	{
        ConfigurationBuffer cfgbuf = new ConfigurationBuffer(CompcConfiguration.class,
        													 CompcConfiguration.getAliases());
        cfgbuf.setDefaultVar("include-classes");
        DefaultsConfigurator.loadCompcDefaults( cfgbuf );
        Object obj = Mxmlc.processConfiguration(ThreadLocalToolkit.getLocalizationManager(),
        										   "compc",
        										   args,
        										   cfgbuf,
        										   CompcConfiguration.class,
        										   "include-classes");
        return (CompcConfiguration) obj;
	}
	
	static void init()
	{
        CompilerAPI.useAS3();
        CompilerAPI.usePathResolver();
        setupLocalizationManager();
	}
    
    static void clean()
    {
        CompilerAPI.removePathResolver();
        ThreadLocalToolkit.setLogger(null);
        ThreadLocalToolkit.setLocalizationManager(null);
    }

	static LocalizationManager setupLocalizationManager()
	{
        // set up for localizing messages
		LocalizationManager l10n = new LocalizationManager();
		l10n.addLocalizer(new ResourceBundleLocalizer());
		ThreadLocalToolkit.setLocalizationManager(l10n);
		
		return l10n;
	}
}
