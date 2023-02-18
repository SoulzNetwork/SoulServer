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

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.soulnetwork.Main;
import org.soulnetwork.api.key.KeyManager;
import org.soulnetwork.discord.DiscordCli;
import org.soulnetwork.logs.Logging;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class ServerHttpHandler implements HttpHandler {

    /**
     * @param exchange the exchange containing the request from the
     *                 client and used to send the response
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        long received = System.currentTimeMillis();
        KeyManager keyManager = Main.getInstance().getKeyManager();
        Headers headers = exchange.getRequestHeaders();
        String keyNId = headers.getFirst("Authorization");
        String clientName = headers.getFirst("client-name");
        String clientVersion = headers.getFirst("client-version");
        InputStream body = exchange.getRequestBody();

        DiscordCli cli = DiscordCli.getInstance();

        System.out.println("Received request from " + clientName + " " + clientVersion + " address: " + exchange.getRemoteAddress().getAddress().getHostAddress());

        if (!keyManager.getKeys().contains(keyNId)) {
            exchange.sendResponseHeaders(401, 0);
            exchange.close();
            return;
        }

        if (clientName == null || clientVersion == null) {
            exchange.sendResponseHeaders(400, 0);
            exchange.close();
            return;
        }

        String ofString = new String(body.readAllBytes());
        String playerName = ofString.split("&")[0].split("=")[1];
        UUID playerUUID = UUID.fromString(ofString.split("&")[1].split("=")[1]);
        String command = ofString.split("&")[2].split("=")[1];
        String serverName = ofString.split("&")[3].split("=")[1];
        String color = ofString.split("&")[4].split("=")[1];
        long sendTime = Long.parseLong(ofString.split("&")[5].split("=")[1]);
        Color c = hex2C(color);

        ServerRequest request = new ServerRequest(
                playerName,
                playerUUID,
                command,
                serverName,
                clientName,
                clientVersion,
                c,
                sendTime,
                received
        );

        try {
            cli.log(request);
            long requestID = new Random().nextLong(0, Long.MAX_VALUE);
            long time = Math.abs(request.getReceiveTime() - request.getSendTime());

            Logging log = Main.getInstance().getLogging();

            log.log("POST X: "
                    + requestID + " "
                    + request.getPlayerName() + " "
                    + request.getPlayerId() + " "
                    + request.getCommand() + " "
                    + request.getServerName() + " "
                    + request.getClientName() + " "
                    + request.getClientVersion() + " "
                    + request.getColor() + " "
                    + exchange.getRemoteAddress().getHostString() + " "
                    + "TOOK " + time + "ms"
            );

        } catch (InterruptedException e) {
            exchange.sendResponseHeaders(500, 0);
            exchange.getResponseBody().close();
            return;
        }

        String response = "{\"message\": \"Authorized\"}";
        exchange.sendResponseHeaders(200, response.length());
        exchange.getResponseBody().write(response.getBytes());
        exchange.getResponseBody().close();
    }


    public static Color hex2C(String colorStr) {
        return new Color(
                Integer.valueOf( colorStr.substring( 1, 3 ), 16 ),
                Integer.valueOf( colorStr.substring( 3, 5 ), 16 ),
                Integer.valueOf( colorStr.substring( 5, 7 ), 16 ) );
    }

}
