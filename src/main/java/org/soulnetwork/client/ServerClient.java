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

import com.google.common.hash.Hashing;
import org.soulnetwork.encryption.Sha512;
import org.soulnetwork.server.ServerConfig;

import java.io.File;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;

public class ServerClient {

    private final HttpClient client;

    public ServerClient() {
        client = HttpClient.newHttpClient();
    }

    public HttpResponse<String> request(
            UUID key,
            UUID playerUUID,
            String playerName,
            String command
    )
    {
        ServerRequestBuilder builder = new ServerRequestBuilder()
                .keyHash(
                        Sha512
                        .S512hsh(
                                key.toString(),
                                StaticServerConfig.hashSalt,
                                StaticServerConfig.hashDelimiter
                        )
                )
                .playerName(playerName)
                .playerUUID(playerUUID)
                .command(command);

        try {
            HttpRequest request = builder.build();
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public HttpClient getClient() {
        return client;
    }

    public static void main(String[] args) throws IOException {
        ServerConfig config = new ServerConfig();
        config.load(new File("serverConfig.json"));
        StaticServerConfig.load(config);

        ServerClient client = new ServerClient();

        client.request(
                StaticServerConfig.key,
                UUID.fromString("c0a8a0a0-0000-0000-0000-000000000000"),
                "test",
                "test"
        );
    }



}
