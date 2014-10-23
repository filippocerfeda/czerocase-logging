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

import org.czerocase.core.logging.Logger;
import org.czerocase.logging.services.LogServiceListener;
import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceReference;

public class LogServiceImpl implements Logger
{
    private final LogServiceListener logServiceListener;
    private final Bundle bundle;

    public LogServiceImpl(final LogServiceListener log, final Bundle bundle)
    {
        this.logServiceListener = log;
        this.bundle = bundle;
    }

    private void log(final int level, final String message, final Class<?> originalClass)
    {
        log(null, level, message, null, originalClass);
    }

    private void log(final int level,
        final String message,
        final Throwable exception, 
        final Class<?> originalClass)
    {
        log(null, level, message, exception, originalClass);
    }

//    private void log(final ServiceReference sr,
//        final int level,
//        final String message, 
//        final Class originalClass)
//    {
//        log(sr, level, message, null, originalClass);
//    }
    
    private void log(final ServiceReference sr,
        final int level,
        final String message,
        final Throwable exception,
        final Class<?> originalClass)
    {
        logServiceListener.addEntry(new LogEntryImpl((sr != null) ? sr.getBundle() : bundle,
            sr,
            level,
            message,
            exception,
            originalClass));
    }

	public void debug(Object message) {
		log(Logger.LOG_DEBUG,  message == null ? "" : message.toString(), null);
		
	}
	
	public void debug(Class<?> clazz, Object message) {
		 log(Logger.LOG_DEBUG, message == null ? "" : message.toString(), clazz);
		
	}

	public void debug(Class<?> clazz, Object message, Throwable t) {
		log(Logger.LOG_DEBUG, message == null ? "" : message.toString(), t, clazz);
		
	}

	public void info(Object message) {
		log(Logger.LOG_INFO, message == null ? "" : message.toString(), null);
		
	}
	
	public void info(Class<?> clazz, Object message) {
		log(Logger.LOG_INFO, message == null ? "" : message.toString(), clazz);
		
	}

	public void info(Class<?> clazz, Object message, Throwable t) {
		log(Logger.LOG_INFO, message == null ? "" : message.toString(), t, clazz);
		
	}

	public void warn(Class<?> clazz, Object message) {
		log(Logger.LOG_WARNING, message == null ? "" : message.toString(), clazz);
		
	}
	
	public void warn(Class<?> clazz, Object message, Throwable t) {
		log(Logger.LOG_WARNING, message == null ? "" : message.toString(), t, clazz);
		
	}

	public void error(Class<?> clazz, Object message) {
		log(Logger.LOG_ERROR, message == null ? "" : message.toString(), clazz);
		
	}

	public void error(Class<?> clazz, Object message, Throwable t) {
		log(Logger.LOG_ERROR, message == null ? "" : message.toString(), t, clazz);
		
	}

	public void fatal(Class<?> clazz, Object message) {
		log(Logger.LOG_FATAL, message == null ? "" : message.toString(), clazz);
		
	}

	public void fatal(Class<?> clazz, Object message, Throwable t) {
		log(Logger.LOG_FATAL, message == null ? "" : message.toString(), t, clazz);
		
	}

}