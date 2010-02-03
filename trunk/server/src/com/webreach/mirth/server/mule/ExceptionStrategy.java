/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * http://www.mirthcorp.com
 *
 * The software in this package is published under the terms of the MPL
 * license a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 */

package com.webreach.mirth.server.mule;

import org.mule.impl.DefaultComponentExceptionStrategy;
import org.mule.providers.AbstractMessageReceiver;
import org.mule.providers.ConnectException;
import org.mule.providers.FatalConnectException;

import com.webreach.mirth.model.SystemEvent;
import com.webreach.mirth.server.controllers.ControllerFactory;
import com.webreach.mirth.server.controllers.EventController;
import com.webreach.mirth.server.util.StackTracePrinter;

public class ExceptionStrategy extends DefaultComponentExceptionStrategy {

	// ast: call to checkForConnectException
	protected void defaultHandler(Throwable t) {
		checkForConnectException(t);
		super.defaultHandler(t);
	}

	// ast: if the exception is a ConnectException, and the object is a
	// receiver, then, the component is stopped.
	protected void checkForConnectException(Object obj) {
		if (obj instanceof ConnectException) {
			ConnectException connectException = (ConnectException) obj;
			checkForConnectException(connectException.getComponent());
		} else if (obj instanceof AbstractMessageReceiver) {
			AbstractMessageReceiver abstractMessageReceiver = (AbstractMessageReceiver) obj;
			
			try {
				logger.error("Stopping channel " + abstractMessageReceiver);
				abstractMessageReceiver.getComponent().stop();
			} catch (Throwable t2) {
				logger.error("Error stopping channel " + abstractMessageReceiver + " \n" + t2);
			}
		} else if (obj instanceof FatalConnectException) {
			FatalConnectException fatalConnectException = (FatalConnectException) obj;
			checkForConnectException(fatalConnectException.getComponent());
		}
	}

	protected void logException(Throwable t) {
		EventController systemLogger = ControllerFactory.getFactory().createEventController();
		SystemEvent event = new SystemEvent("Exception occured in channel.");
		event.setDescription(StackTracePrinter.stackTraceToString(t));
		systemLogger.logSystemEvent(event);
	}
}
