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

package org.soulnetwork.command;

import org.soulnetwork.server.Server;
import org.soulnetwork.server.ServerConfig;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Stream;

public class CommandManager {

    private CommandMap commandMap;

    public CommandManager() {
        commandMap = new CommandMap();
    }

    public CommandRegister getServerCommand(String reg) {
        return commandMap.getServerCommand(reg);
    }

    public boolean execute(String command) {
        String[] args = command.split(" ");
        String cmd = args[0];
        args = Stream.of(args).skip(1).toArray(String[]::new);
        return commandMap.execute(cmd, new CommandContext(cmd, args));
    }

    public void reg(String... cmd) {
        commandMap.reg(cmd);
    }

    public CommandMap getCommandMap() {
        return commandMap;
    }




}
