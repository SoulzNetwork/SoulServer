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

package org.soulnetwork;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.soulnetwork.api.key.KeyManager;
import org.soulnetwork.client.ServerClient;
import org.soulnetwork.command.CommandManager;
import org.soulnetwork.command.cmds.CommandStop;
import org.soulnetwork.discord.DiscordCli;
import org.soulnetwork.discord.DiscordConfig;
import org.soulnetwork.discord.ServerMapper;
import org.soulnetwork.logs.Logging;
import org.soulnetwork.server.Server;
import org.soulnetwork.server.ServerConfig;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;
import java.util.UUID;

public class Main {

    private KeyManager keyManager;
    private Server server;
    private static Main instance;
    private DiscordCli discordCli;
    private ServerMapper mapper;
    private CommandManager commandManager;
    private Logging logging;

    public static void main(String[] args) throws Exception {
        new Main().start();
    }

    public void start() throws Exception {
        instance = this;
        logging = new Logging(new File("logs"));
        logging.findLogs();
        logging.saveTimer();

        commandManager = new CommandManager();
        commandManager.reg("stop", "exit", "shutdown");
        commandManager.getCommandMap().register(new CommandStop(), "stop", "exit", "shutdown");

        mapper = new ServerMapper();

        File mapperFile = new File("server-mapper.json");
        if (!mapperFile.exists()) mapperFile.createNewFile();

        JsonObject mapperConfig = new Gson().fromJson(new FileReader(mapperFile), JsonObject.class);

        mapper.load(mapperConfig);

        ServerConfig config = new ServerConfig();
        DiscordConfig discordConfig = new DiscordConfig();
        config.load(new File("serverConfig.json"));
        discordConfig.load(new File("discordConfig.json"));


        server = new Server(config);
        server.start();

        keyManager = new KeyManager();
        keyManager.load("keys.json");

        System.out.println(keyManager.getKeys());

        discordCli = new DiscordCli(discordConfig,mapper);

        discordCli.init();


        Scanner scanner = new Scanner(System.in);
        while (true) {
            String command = scanner.nextLine();
            if (commandManager.execute(command)) {
                break;
            }
        }
    }

    public void stop() throws IOException {
        server.getServer().stop(0);
        logging.saveLog();
        logging.close();
    }



    public KeyManager getKeyManager() {
        return keyManager;
    }

    public Server getServer() {
        return server;
    }

    public static Main getInstance() {
        return instance;
    }

    public Logging getLogging() {
        return logging;
    }
}