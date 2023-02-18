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

public class CommandContext {

    private String[] args;
    private String command;

    public CommandContext(String command, String[] args) {
        this.command = command;
        this.args = args;
    }

    public String[] getArgs() {
        return args;
    }

    public String getCommand() {
        return command;
    }

    public String getArg(int index) {
        return args[index];
    }

    public int getArgCount() {
        return args.length;
    }

    public boolean hasArgs() {
        return args.length > 0;
    }

    public boolean hasArg(int index) {
        return args.length > index;
    }

    public boolean hasArg(String arg) {
        for (String s : args) {
            if (s.equalsIgnoreCase(arg)) {
                return true;
            }
        }
        return false;
    }

    public int getArgIndex(String arg) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equalsIgnoreCase(arg)) {
                return i;
            }
        }
        return -1;
    }

    public String getArg(String arg) {
        for (String s : args) {
            if (s.equalsIgnoreCase(arg)) {
                return s;
            }
        }
        return null;
    }
}
