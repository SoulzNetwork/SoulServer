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

package org.soulnetwork.client;


import com.google.gson.JsonObject;
import org.soulnetwork.server.ServerConfig;

import java.util.UUID;

public class StaticServerConfig {
    public static int serverPort = 25576;
    public static String serverHost = "localhost";
    public static String serverSubDirectory = "server";
    public static UUID key = UUID.randomUUID();
    public static String serverName = "SERVER_NAME";
    public static String clientName = "CLIENT_NAME";
    public static String clientVersion = "1.0.0";
    public static String hashSalt = "SALT";
    public static String hashDelimiter = "DELIMITER";
    public static String color = "#ffffff";

    public static String url = "http://" + serverHost + ":" + serverPort;

    public static void load(ServerConfig config) {
        final JsonObject serverConfig = config.getConfig().getAsJsonObject("server");
        final JsonObject hashConf = config.getConfig().getAsJsonObject("hash");

        serverPort = serverConfig.get("port").getAsInt();
        serverHost = serverConfig.get("host").getAsString();
        serverSubDirectory = serverConfig.get("sub-directory").getAsString();

        key = UUID
                .fromString(
                        "59700c3e-8276-4074-9d7c-46118ec8a1b3"
                );

        serverName = "Hub";
        clientName = "Hub";

        clientVersion = "1.0.0";

        hashSalt = hashConf
                .get("salt")
                .getAsString();
        hashDelimiter = hashConf
                .get("delimiter")
                .getAsString();

        color = "#ffffff";

        url = "http://" + serverHost + ":" + serverPort;
    }
}
