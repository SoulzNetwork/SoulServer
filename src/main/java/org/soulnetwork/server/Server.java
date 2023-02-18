/*
 * Copyright (c) 2023. SoulzNetwork
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.soulnetwork.server;

import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpServer;

import java.io.File;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public class Server {

    private ServerConfig config;
    private HttpServer server;

    public Server(ServerConfig config) {
        this.config = config;
    }

    public void start() throws Exception {
        server = HttpServer.create();
        JsonObject serverConfig = config.getConfig().getAsJsonObject("server");


        int port = serverConfig.get("port").getAsInt();
        String host = serverConfig.get("host").getAsString();


        InetAddress address = InetAddress.getByName(host);
        InetSocketAddress socketAddress = new InetSocketAddress(address, port);

        server.bind(socketAddress, 0);
        server.createContext("/discord/logging/", new ServerHttpHandler());
        server.start();
    }

    public ServerConfig getConfig() {
        return config;
    }

    public HttpServer getServer() {
        return server;
    }
}
