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
package org.czerocase.logging.services;

import java.util.Enumeration;

import org.czerocase.core.logging.LogEntry;
import org.czerocase.core.logging.LogListener;
import org.czerocase.core.logging.Logger;
import org.czerocase.logging.impl.LogEntryImpl;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.osgi.framework.FrameworkEvent;
import org.osgi.framework.FrameworkListener;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;

public class LogServiceListener implements BundleListener, FrameworkListener, ServiceListener
{
    private LogNode headNode;
    private LogNode tailNode;
    private int size;
    private LogListenerThread logListenerThread;
    private final int maxSize;
    private final boolean storeDebug;

    public LogServiceListener(final int maxSize, final boolean storeDebug)
    {
        this.maxSize = maxSize;
        this.storeDebug = storeDebug;
    }

    public void close()
    {
        if (logListenerThread != null)
        {
            logListenerThread.shutdown();
            logListenerThread = null;
        }

        headNode = null;
        tailNode = null;
        size = 0;
    }

    public synchronized void addEntry(final LogEntry entry)
    {
        if (maxSize != 0)
        {
            if (storeDebug || entry.getLevel() != Logger.LOG_DEBUG)
            {
                LogNode node = new LogNode(entry);

                if (headNode != null)
                {
                    headNode.setPreviousNode(node);
                }

                headNode = node;

                ++size;

                if (tailNode == null)
                {
                    tailNode = node;
                }
            }

            if (maxSize != -1)
            {
                if (size > maxSize)
                {
                    LogNode last = tailNode.getPreviousNode();
                    last.setNextNode(null);
                    tailNode = last;
                    --size;
                }
            }
        }

        if (logListenerThread != null)
        {
            logListenerThread.addEntry(entry);
        }
    }

    public synchronized void addListener(final LogListener listener)
    {
        if (logListenerThread == null)
        {
            logListenerThread = new LogListenerThread();
            logListenerThread.start();
        }
        logListenerThread.addListener(listener);
    }

    public synchronized void removeListener(final LogListener listener)
    {
        if (logListenerThread != null)
        {
            logListenerThread.removeListener(listener);

            if (logListenerThread.getListenerCount() == 0)
            {
                logListenerThread.shutdown();
                logListenerThread = null;
            }
        }
    }

    public synchronized Enumeration<LogEntry> getEntries()
    {
        return new LogNodeEnumeration(headNode, tailNode);
    }

    private static final String[] FRAMEWORK_EVENT_MESSAGES =
    {
    	/*
    	 * TODO Parametrizzare i messaggi ?
    	 */
        "FrameworkEvent STARTED",
        "FrameworkEvent ERROR",
        "FrameworkEvent PACKAGES REFRESHED",
        "FrameworkEvent STARTLEVEL CHANGED",
        "FrameworkEvent WARNING",
        "FrameworkEvent INFO"
    };

    public void frameworkEvent(final FrameworkEvent event)
    {
        int eventType = event.getType();
        String message = null;

        for (int i = 0; message == null && i < FRAMEWORK_EVENT_MESSAGES.length; ++i)
        {
            if (eventType >> i == 1)
            {
                message = FRAMEWORK_EVENT_MESSAGES[i];
            }
        }

        LogEntry entry = new LogEntryImpl(event.getBundle(),
            null,
            (eventType == FrameworkEvent.ERROR) ? Logger.LOG_ERROR : Logger.LOG_INFO,
            message,
            event.getThrowable(),
            event.getSource()!=null ? event.getSource().getClass(): null);
        
        addEntry(entry);
    }

    private static final String[] BUNDLE_EVENT_MESSAGES =
    {    	
    	/*
    	 * TODO Parametrizzare i messaggi ?
    	 */
        "BundleEvent INSTALLED",
        "BundleEvent STARTED",
        "BundleEvent STOPPED",
        "BundleEvent UPDATED",
        "BundleEvent UNINSTALLED",
        "BundleEvent RESOLVED",
        "BundleEvent UNRESOLVED"
    };

    public void bundleChanged(final BundleEvent event)
    {
        int eventType = event.getType();
        String message = null;

        for (int i = 0; message == null && i < BUNDLE_EVENT_MESSAGES.length; ++i)
        {
            if (eventType >> i == 1)
            {
                message = BUNDLE_EVENT_MESSAGES[i];
            }
        }

        if (message != null)
        {
            LogEntry entry = new LogEntryImpl(event.getBundle(),
                null,
                Logger.LOG_INFO,
                message,
                null,
                null);
            
            addEntry(entry);
        }
    }

    private static final String[] SERVICE_EVENT_MESSAGES =
    {
    	/*
    	 * TODO Parametrizzare i messaggi ?
    	 */
        "ServiceEvent REGISTERED",
        "ServiceEvent MODIFIED",
        "ServiceEvent UNREGISTERING"
    };

    public void serviceChanged(final ServiceEvent event)
    {
        int eventType = event.getType();
        String message = null;

        for (int i = 0; message == null && i < SERVICE_EVENT_MESSAGES.length; ++i)
        {
            if (eventType >> i == 1)
            {
                message = SERVICE_EVENT_MESSAGES[i];
            }
        }

        LogEntry entry = new LogEntryImpl(event.getServiceReference().getBundle(),
            event.getServiceReference(),
            (eventType == ServiceEvent.MODIFIED) ? Logger.LOG_DEBUG : Logger.LOG_INFO,
            message,
            null,
            event.getSource()!=null ? event.getSource().getClass(): null);

        addEntry(entry);
    }
}