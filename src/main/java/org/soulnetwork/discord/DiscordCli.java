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

package org.soulnetwork.discord;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.FileUpload;
import net.dv8tion.jda.api.utils.TimeUtil;
import net.dv8tion.jda.api.utils.Timestamp;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.soulnetwork.Main;
import org.soulnetwork.server.ServerRequest;

import javax.imageio.ImageIO;
import javax.swing.text.DateFormatter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.TemporalAccessor;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class DiscordCli {

    private static DiscordCli instance;
    private JDA jda;
    private DiscordConfig config;
    private ServerMapper serverMapper;
    private HttpClient client;

    public DiscordCli(DiscordConfig config, ServerMapper serverMapper) {
        instance = this;
        this.config = config;
        this.serverMapper = serverMapper;
        client = HttpClient.newHttpClient();
    }

    public void init() {
        String token = config.getConfig().get("token").getAsString();
        jda = JDABuilder.createDefault(token)
                .setEnabledIntents(GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS))
                .setActivity(Activity.competing("with wyvern"))
                .addEventListeners(new DiscordListener())
                .build();
    }


    public void log(ServerRequest e) throws IOException, InterruptedException {
        long guildId = config.getConfig().get("guildId").getAsLong();
        if (!serverMapper.hasServer(e.getServerName())) return;
        long serverId = serverMapper.getServerId(e.getServerName());


        EmbedBuilder builder = new EmbedBuilder()


                .setAuthor(
                        e.getPlayerName(),
                        null,
                        "https://crafatar.com/avatars/" + e.getPlayerId().toString()
                )

                .setDescription(e.getCommand() + "\n\n--> "
                        + e.getServerName()
                        + " - " + e.getPlayerName()
                        + "\n"
                        + "Send: " + new Date(e.getSendTime())
                        + "\n"
                        + "Receive: " + new Date(e.getReceiveTime())
                        + "\n"
                        + "TOOK -X POST " + Math.abs(e.getReceiveTime() - e.getSendTime()) + "ms"
                )
                .setColor(e.getColor())
                .setThumbnail("https://crafatar.com/avatars/" + e.getPlayerId().toString()
                )
                        .setTitle("player " + e.getPlayerName() + " executed command " + e.getCommand() + " on server " + e.getServerName());


        Objects.requireNonNull(
                        Objects.requireNonNull(
                                        jda.getGuildById(guildId))
                                .getTextChannelById(serverId))
                .sendMessageEmbeds(builder.build())
                .queue();
    }

    public BufferedImage requestAvatar(UUID player) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://crafatar.com/avatars/" + player.toString()))
                .build();

        HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());

        BufferedImage image;
        try {
            image = ImageIO.read(new ByteArrayInputStream(response.body()));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return image;
    }

    public static DiscordCli getInstance() {
        return instance;
    }

    public JDA getJda() {
        return jda;
    }

    public DiscordConfig getConfig() {
        return config;
    }

    public ServerMapper getServerMapper() {
        return serverMapper;
    }

    public HttpClient getClient() {
        return client;
    }

}
