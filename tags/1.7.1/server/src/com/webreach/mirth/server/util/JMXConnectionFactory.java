/* ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is Mirth.
 *
 * The Initial Developer of the Original Code is
 * WebReach, Inc.
 * Portions created by the Initial Developer are Copyright (C) 2006
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s):
 *   Gerald Bortis <geraldb@webreachinc.com>
 *
 * ***** END LICENSE BLOCK ***** */

package com.webreach.mirth.server.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.management.remote.JMXConnector;

import com.webreach.mirth.util.PropertyLoader;

public class JMXConnectionFactory {
	public static JMXConnection createJMXConnection() throws Exception {
		Properties properties = PropertyLoader.loadProperties("mirth");
		return createJMXConnection(PropertyLoader.getProperty(properties, "configuration.id"));
	}

	public static JMXConnection createJMXConnection(String domain) throws Exception {
		Properties properties = PropertyLoader.loadProperties("mirth");
		String port = PropertyLoader.getProperty(properties, "jmx.port");
		String jmxUrl = "service:jmx:rmi:///jndi/rmi://localhost:" + port + "/server";

		String password = PropertyLoader.getProperty(properties, "jmx.password");
		Map environment = new HashMap<String, String>();
		String[] credentials = { "admin", password };
		environment.put(JMXConnector.CREDENTIALS, credentials);
		return new JMXConnection(jmxUrl, domain, environment);
	}
}