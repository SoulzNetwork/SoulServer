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

import java.awt.*;
import java.sql.Timestamp;
import java.util.UUID;

public class ServerRequest {

    private final String playerName;
    private final UUID playerId;
    private final String command;
    private final String serverName;
    private final String clientName;
    private final String clientVersion;
    private final Color color;
    private final long sendTime;
    private final long receiveTime;

    public ServerRequest(String playerName, UUID playerId, String command, String serverName, String clientName, String clientVersion, Color color
            , long sendTime, long receiveTime) {
        this.playerName = playerName;
        this.playerId = playerId;
        this.command = command;
        this.serverName = serverName;
        this.clientName = clientName;
        this.clientVersion = clientVersion;
        this.color = color;
        this.sendTime = sendTime;
        this.receiveTime = receiveTime;
    }

    public String getPlayerName() {
        return playerName;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public String getCommand() {
        return command;
    }

    public String getServerName() {
        return serverName;
    }

    public String getClientName() {
        return clientName;
    }

    public String getClientVersion() {
        return clientVersion;
    }

    public Color getColor() {
        return color;
    }

    public long getSendTime() {
        return sendTime;
    }

    public long getReceiveTime() {
        return receiveTime;
    }
}
