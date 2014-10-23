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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.czerocase.core.logging.LogEntry;
import org.czerocase.core.logging.LogListener;

final class LogListenerThread extends Thread {
	private boolean stoppingFlag = false;
	private final List<LogEntry> entriesToDeliver = new ArrayList<LogEntry>();
	private final List<LogListener> listeners = new ArrayList<LogListener>();

	void addEntry(final LogEntry entry) {
		synchronized (entriesToDeliver) {
			entriesToDeliver.add(entry);
			entriesToDeliver.notifyAll();
		}
	}

	void addListener(final LogListener listener) {
		synchronized (listeners) {
			listeners.add(listener);
		}
	}

	void removeListener(final LogListener listener) {
		synchronized (listeners) {
			listeners.remove(listener);
		}
	}

	int getListenerCount() {
		return listeners.size();
	}

	void shutdown() {
		synchronized (entriesToDeliver) {
			stoppingFlag = true;
			entriesToDeliver.notifyAll();
		}
	}

	public void run() {
		boolean stop = false;

		for (; !stop;) {
			synchronized (entriesToDeliver) {
				if (!entriesToDeliver.isEmpty()) {
					LogEntry entry = (LogEntry) entriesToDeliver.remove(0);

					synchronized (listeners) {
						Iterator<LogListener> listenerIt = listeners.iterator();
						while (listenerIt.hasNext()) {
							try {
								LogListener listener = (LogListener) listenerIt
										.next();
								listener.logged(entry);
							} catch (Throwable t) {
							}
						}
					}
				}

				if (entriesToDeliver.isEmpty()) {
					try {
						entriesToDeliver.wait();
					} catch (InterruptedException e) {
					}
				}
			}

			if (stoppingFlag) {
				stop = true;
			}
		}
	}
}