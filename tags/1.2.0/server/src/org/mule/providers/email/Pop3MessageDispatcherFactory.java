/*
 * $Header: /home/projects/mule/scm/mule/providers/email/src/java/org/mule/providers/email/Pop3MessageDispatcherFactory.java,v 1.3 2005/06/03 01:20:31 gnt Exp $
 * $Revision: 1.3 $
 * $Date: 2005/06/03 01:20:31 $
 * ------------------------------------------------------------------------------------------------------
 *
 * Copyright (c) SymphonySoft Limited. All rights reserved.
 * http://www.symphonysoft.com
 *
 * The software in this package is published under the terms of the BSD
 * style license a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 */
package org.mule.providers.email;

import org.mule.umo.UMOException;
import org.mule.umo.provider.UMOConnector;
import org.mule.umo.provider.UMOMessageDispatcher;
import org.mule.umo.provider.UMOMessageDispatcherFactory;

/**
 * <code>Pop3MessageDispatcherFactory</code> creates a Pop3 Message
 * dispatcher. For Pop3 connections the dispatcher can only be used to receive
 * message (as apposed to listening for them). Trying to send or dispatch will
 * throw an UnsupportedOperationException.
 * 
 * @author <a href="mailto:ross.mason@symphonysoft.com">Ross Mason</a>
 * @version $Revision: 1.3 $
 */

public class Pop3MessageDispatcherFactory implements UMOMessageDispatcherFactory
{
    public UMOMessageDispatcher create(UMOConnector connector) throws UMOException
    {
        return new Pop3MessageDispatcher((Pop3Connector) connector);
    }
}