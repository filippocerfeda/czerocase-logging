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
import org.czerocase.core.logging.exceptions.*;
import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceReference;

public class LogEntryImpl implements LogEntry
{
	private final Bundle bundle;
	private final Throwable thrownException;
	private final int level;
	private final String message;
	private final ServiceReference serviceReference;
	private final long timeInMillis;
	private final Class<?> originalClass;

	public LogEntryImpl(final Bundle bundle,
			final ServiceReference sr,
			final int level,
			final String message,
			final Throwable exception,
			final Class<?> originalClass)
			{
		this.bundle = bundle;
		this.thrownException = LogException.getException(exception);
		this.level = level;
		this.message = message;
		this.serviceReference = sr;
		this.timeInMillis = System.currentTimeMillis();
		this.originalClass = originalClass;
			}

	public Bundle getBundle()
	{
		return bundle;
	}

	public ServiceReference getServiceReference()
	{
		return serviceReference;
	}

	public int getLevel()
	{
		return level;
	}

	public String getMessage()
	{
		return message;
	}

	public Throwable getException()
	{
		return thrownException;
	}

	public long getTime()
	{
		return timeInMillis;
	}

	public java.lang.Class<?> getOrginalClass() {
		return originalClass;
	}
}