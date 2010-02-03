/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * http://www.mirthcorp.com
 *
 * The software in this package is published under the terms of the MPL
 * license a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 */

package com.webreach.mirth.server.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.webreach.mirth.model.converters.ObjectXMLSerializer;
import com.webreach.mirth.server.controllers.ChannelStatisticsController;
import com.webreach.mirth.server.controllers.ControllerFactory;

public class ChannelStatisticsServlet extends MirthServlet {
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (!isUserLoggedIn(request)) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		} else {
			try {
				ChannelStatisticsController statisticsController = ControllerFactory.getFactory().createChannelStatisticsController();
				ObjectXMLSerializer serializer = new ObjectXMLSerializer();
				PrintWriter out = response.getWriter();
				String operation = request.getParameter("op");
				String channelId = request.getParameter("id");

				if (operation.equals("getStatistics")) {
					response.setContentType("application/xml");
					out.println(serializer.toXML(statisticsController.getStatistics(channelId)));
				} else if (operation.equals("clearStatistics")) {
                    boolean deleteReceived = Boolean.valueOf(request.getParameter("deleteReceived"));
                    boolean deleteFiltered = Boolean.valueOf(request.getParameter("deleteFiltered"));
                    boolean deleteQueued = Boolean.valueOf(request.getParameter("deleteQueued"));
                    boolean deleteSent = Boolean.valueOf(request.getParameter("deleteSent"));
                    boolean deleteErrored = Boolean.valueOf(request.getParameter("deleteErrored"));
                    boolean deleteAlerted = Boolean.valueOf(request.getParameter("deleteAlerted"));
					statisticsController.clearStatistics(channelId, deleteReceived, deleteFiltered, deleteQueued, deleteSent, deleteErrored, deleteAlerted);
				}
			} catch (Exception e) {
				throw new ServletException(e);
			}
		}
	}
}
