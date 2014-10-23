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


public final class LogNodeEnumeration implements Enumeration<LogEntry>
{
    private LogNode next;
    private final LogNode last;

    public LogNodeEnumeration(final LogNode start, final LogNode end)
    {
        next = start;
        last = end;
    }

    public boolean hasMoreElements()
    {
        return next != null;
    }

    public LogEntry nextElement()
    {
        LogEntry result = null;

        if (next == last)
        {
            result = next.getEntry();
            next = null;
        }
        else if (next != null)
        {
            result = next.getEntry();
            next = next.getNextNode();
        }

        return result;
    }
}