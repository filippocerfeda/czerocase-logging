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
package org.czerocase.logging.impl;


import org.czerocase.core.logging.LogEntry;
import org.czerocase.core.logging.LogReaderService;
import org.czerocase.core.logging.LogListener;
import org.czerocase.logging.services.LogServiceListener;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;


public class LogReaderServiceImpl implements LogReaderService
{
    private final LogServiceListener log;
    private final List<LogListener> listeners = new Vector<LogListener>();

    public LogReaderServiceImpl(final LogServiceListener log)
    {
        this.log = log;
    }

    public synchronized void addLogListener(final LogListener listener)
    {
        listeners.add(listener);
        log.addListener(listener);
    }

    public synchronized void removeLogListener(final LogListener listener)
    {
        listeners.remove(listener);
        log.removeListener(listener);
    }

    public Enumeration<LogEntry> getLog()
    {
        return log.getEntries();
    }

    public synchronized void removeAllLogListeners()
    {
        Iterator<LogListener> listenerIt = listeners.iterator();
        while (listenerIt.hasNext())
        {
            LogListener listener = listenerIt.next();
            log.removeListener(listener);
        }
    }
}