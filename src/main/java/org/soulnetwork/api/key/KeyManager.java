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

package org.soulnetwork.api.key;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.soulnetwork.Main;
import org.soulnetwork.encryption.Sha512;
import org.soulnetwork.server.ServerConfig;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class KeyManager {

    private final List<String> keyHashes = new ArrayList<>();
    public static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    public void registerKey(UUID key)
    {
        keyHashes.add(hashUUID(key));
    }

    public String hashUUID(UUID key)
    {
        ServerConfig config = Main.getInstance().getServer().getConfig();
        JsonObject conf = config.getConfig().getAsJsonObject("hash");

        String salt = conf.get("salt").getAsString();
        String delimiter = conf.get("delimiter").getAsString();

        return Sha512.S512hsh(key.toString(), salt, delimiter);
    }

    public void unregister(UUID key) {
        keyHashes.remove(hashUUID(key));
    }

    public boolean isValid(UUID key) {
        return keyHashes.contains(hashUUID(key));
    }

    public UUID randomKey() {
        UUID key = UUID.randomUUID();
        registerKey(key);
        return key;
    }

    public List<String> getKeys() {
        return keyHashes;
    }

    public void load(String file) throws IOException {
        File f = new File(file);
        if (!f.exists()) {
            return;
        }
        FileReader reader = new FileReader(f);
        String[] keys = GSON.fromJson(reader, String[].class);

        for (String key : keys) {
            UUID uuid = UUID.fromString(key);
            registerKey(uuid);
        }
        reader.close();
    }
}
