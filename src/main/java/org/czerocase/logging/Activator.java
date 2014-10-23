/*
 *  Czero Case is the Open Source Platform, realized by ImagoItalia Srl,
 *  to quickly develop and deploy innovative Case Management solutions.
 *  Czero Case framework, based on Java environment, enables designer
 *  and developers to build advanced solutions for document and process
 *  management ensuring compliance with government regulations
 *  and industry standards.
 * 
 *  Copyright (C) 2012 ImagoItalia srl <http://www.imagoitalia.com>
 *  
 *  This file is part of Czero Case.
 *  
 *  Czero Case is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *   
 *  Czero Case is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *   
 *  You should have received a copy of the GNU General Public License
 *  along with Czero Case.  If not, see <http://www.gnu.org/licenses/>.
*/
package org.czerocase.logging;

import org.czerocase.core.logging.LogReaderService;
import org.czerocase.core.logging.Logger;
import org.czerocase.logging.services.LogReaderServiceFactory;
import org.czerocase.logging.services.LogServiceFactory;
import org.czerocase.logging.services.LogServiceListener;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public final class Activator implements BundleActivator
{
    private static final String MAX_SIZE_PROPERTY = "org.apache.felix.log.maxSize";
    private static final String STORE_DEBUG_PROPERTY = "org.apache.felix.log.storeDebug";
    private static final int DEFAULT_MAX_SIZE = 100;
    private static final boolean DEFAULT_STORE_DEBUG = false;
    
    private LogServiceListener logContainer;

    private static int getMaxSize(final BundleContext context)
    {
        int maxSize = DEFAULT_MAX_SIZE;

        String maxSizePropValue = context.getProperty(MAX_SIZE_PROPERTY);
        if (maxSizePropValue != null)
        {
            try
            {
                maxSize = Integer.parseInt(maxSizePropValue);
            }
            catch (NumberFormatException e)
            {
            	maxSize = DEFAULT_MAX_SIZE;
            }
        }

        return maxSize;
    }

    public void start(final BundleContext bundleContext) throws Exception
    {
        logContainer = new LogServiceListener(getMaxSize(bundleContext), getStoreDebug(bundleContext));

        bundleContext.addBundleListener(logContainer);
        bundleContext.addFrameworkListener(logContainer);
        bundleContext.addServiceListener(logContainer);

        bundleContext.registerService(Logger.class.getName(),
            new LogServiceFactory(logContainer), null);

        bundleContext.registerService(LogReaderService.class.getName(),
            new LogReaderServiceFactory(logContainer), null);
    }

    public void stop(final BundleContext context) throws Exception
    {
        logContainer.close();
    }

    private static boolean getStoreDebug(final BundleContext context)
    {
        boolean storeDebug = DEFAULT_STORE_DEBUG;

        String storeDebugPropValue = context.getProperty(STORE_DEBUG_PROPERTY);
        if (storeDebugPropValue != null)
        {
            storeDebug = Boolean.valueOf(storeDebugPropValue).booleanValue();
        }

        return storeDebug;
    }

}