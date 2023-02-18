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

import kotlin.collections.AbstractMap;
import kotlin.collections.AbstractMutableList;

import java.util.*;

public class CommandMap {

    private ArrayDeque<ServerCommand> executeList;
    private HashMap<String, ServerCommand> commandMap;

    public CommandMap() {
        executeList = new ArrayDeque<>();
        commandMap = new HashMap<>();
    }

    public void reg(String... cmd) {
        for (String s : cmd) {
            commandMap.put(s, new ServerCommand());
        }
    }

    public CommandRegister getServerCommand(String reg) {
        return new CommandRegister(this, reg);
    }

    public boolean execute(String cmd, CommandContext context) {
        if (!commandMap.containsKey(cmd)) return false;
        if (commandMap.get(cmd).getExecutor() == null) return false;
        executeList.add(commandMap.get(cmd));
        return executeList.poll().getExecutor().execute(context);
    }

    public void register(IServerCommand executor, String... cmd) {
        for (String s : cmd) {
            commandMap.put(s, new ServerCommand(s, executor));
        }
    }

    public ArrayDeque<ServerCommand> getExecuteList() {
        return executeList;
    }

    public void setExecuteList(ArrayDeque<ServerCommand> executeList) {
        this.executeList = executeList;
    }

    public HashMap<String, ServerCommand> getCommandMap() {
        return commandMap;
    }

    public void setCommandMap(HashMap<String, ServerCommand> commandMap) {
        this.commandMap = commandMap;
    }
}
