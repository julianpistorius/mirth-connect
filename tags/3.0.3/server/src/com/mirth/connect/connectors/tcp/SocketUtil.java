/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * 
 * http://www.mirthcorp.com
 * 
 * The software in this package is published under the terms of the MPL license a copy of which has
 * been included with this distribution in the LICENSE.txt file.
 */

package com.mirth.connect.connectors.tcp;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import org.apache.commons.lang3.StringUtils;

import com.mirth.connect.util.TcpUtil;

public class SocketUtil {

    public static StateAwareSocket createSocket() throws UnknownHostException, IOException {
        return createSocket(null);
    }

    public static StateAwareSocket createSocket(String localAddr) throws UnknownHostException, IOException {
        return createSocket(localAddr, 0);
    }

    /**
     * Creates a socket and connects it to the specified remote host on the specified remote port.
     * 
     * @param host
     *            - The remote host to connect on.
     * @param port
     *            - The remote port to connect on.
     * @param localAddr
     *            - The local address to bind the socket to.
     * @param localPort
     *            - The local port to bind the socket to.
     * @param timeout
     *            - The socket timeout to use when connecting.
     * @return The bound and connected StateAwareSocket.
     * @throws UnknownHostException
     *             if the IP address of the host could not be determined
     * @throws IOException
     *             if an I/O error occurs when creating the socket
     */
    public static StateAwareSocket createSocket(String localAddr, int localPort) throws UnknownHostException, IOException {
        StateAwareSocket socket = new StateAwareSocket();

        if (StringUtils.isNotEmpty(localAddr)) {
            InetAddress localAddress = InetAddress.getByName(TcpUtil.getFixedHost(localAddr));
            socket.bind(new InetSocketAddress(localAddress, localPort));
        }

        return socket;
    }

    public static void connectSocket(StateAwareSocket socket, String host, int port, int timeout) throws UnknownHostException, IOException {
        socket.connect(new InetSocketAddress(InetAddress.getByName(TcpUtil.getFixedHost(host)), port), timeout);
    }

    public static void closeSocket(StateAwareSocket socket) throws IOException {
        if (socket != null) {
            /*
             * MIRTH-2984: The shutdownInput() and shutdownOutput() methods are no longer being
             * called. On Windows this causes a RST packet to be sent to the remote side if there
             * are still bytes available in the socket's input stream.
             */
            socket.close();
        }
    }

    public static String getInetAddress(StateAwareSocket socket) {
        String inetAddress = socket == null || socket.getInetAddress() == null ? "" : socket.getInetAddress().toString() + ":" + socket.getPort();

        if (inetAddress.startsWith("/")) {
            inetAddress = inetAddress.substring(1);
        }

        return inetAddress;
    }

    public static String getLocalAddress(StateAwareSocket socket) {
        String localAddress = socket == null || socket.getLocalAddress() == null ? "" : socket.getLocalAddress().toString() + ":" + socket.getLocalPort();

        // If addresses begin with a slash "/", remove it.
        if (localAddress.startsWith("/")) {
            localAddress = localAddress.substring(1);
        }

        return localAddress;
    }
}
