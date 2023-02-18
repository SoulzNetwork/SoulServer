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

package org.soulnetwork.logs;

import com.google.common.annotations.VisibleForTesting;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.PriorityQueue;

public class Logging {

    private Thread logThread;
    private volatile File directory;
    private final ArrayDeque<File> queue;
    private final transient ArrayDeque<String> logQueue;
    public Logging(File directory) {
        this.directory = directory;
        if (!directory.exists()) directory.mkdirs();
        queue = new ArrayDeque<>();
        logQueue = new ArrayDeque<>();
        logThread = new LogThread(this);
        logThread.start();
    }

    public void findLogs() {
        File[] files = directory.listFiles();
        if (files == null) return;
        for (File file : files) {
            if (file.isDirectory()) continue;
            queue.add(file);
        }
    }

    public void close() {
        logThread.interrupt();
    }

    public void log(String message) {
        LogThread thread = (LogThread) logThread;
        thread.logBuffer[++thread.bufferIndex] = message;
        System.out.println(message);
    }

    public void saveLog() throws IOException {
        int logLength = queue.size();
        File file = new File(directory, "log-" + "(" + logLength + ")" + ".log");
        if (!file.exists()) file.createNewFile();
        FileWriter writer = new FileWriter(file);
        StringBuilder log = new StringBuilder();
        for (int i = 0; i < logQueue.size(); i++) {
            log.append(logQueue.poll()).append("\n");
            logQueue.remove();
        }
        writer.write(log.toString());
        writer.close();
    }


    public Thread getLogThread() {
        return logThread;
    }

    public File getDirectory() {
        return directory;
    }

    public void setDirectory(File directory) {
        this.directory = directory;
    }

    public ArrayDeque<File> getQueue() {
        return queue;
    }


    public ArrayDeque<String> getLogQueue() {
        return logQueue;
    }

    public void saveTimer() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000 * 60 * 5);
                    saveLog();
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
