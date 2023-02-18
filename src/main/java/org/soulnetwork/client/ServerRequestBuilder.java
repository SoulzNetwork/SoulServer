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


import java.net.URI;
import java.net.http.HttpRequest;
import java.util.UUID;

public class ServerRequestBuilder {
    private String keyHash;
    private UUID playerUUID = UUID.randomUUID();
    private String playerName = "TestPlayer";
    private String command = "/testcommand";

    public ServerRequestBuilder keyHash(String keyHash) {
        this.keyHash = keyHash;
        return this;
    }

    public ServerRequestBuilder playerUUID(UUID playerUUID) {
        this.playerUUID = playerUUID;
        return this;
    }

    public ServerRequestBuilder playerName(String playerName) {
        this.playerName = playerName;
        return this;
    }

    public ServerRequestBuilder command(String command) {
        this.command = command;
        return this;
    }

    public HttpRequest build() {
        final String body = "playerName=" + playerName +
                "&playerUUID=" + playerUUID.toString() +
                "&command=" + command +
                "&serverName=" + StaticServerConfig.serverName +
                "&color=" + StaticServerConfig.color +
                "&time=" + System.nanoTime();

        return HttpRequest.newBuilder()
                .uri(URI.create(
                        StaticServerConfig.url +
                                StaticServerConfig.serverSubDirectory
                ))
                .header("Content-Type", "application/json")
                .header("Authorization", keyHash)
                .header("client-name", StaticServerConfig.clientName)
                .header("client-version", StaticServerConfig.clientVersion)
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
    }





}
