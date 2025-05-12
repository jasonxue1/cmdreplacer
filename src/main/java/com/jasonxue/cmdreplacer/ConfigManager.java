package com.jasonxue.cmdreplacer;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ConfigManager {
    private static File configFile;
    private static List<Entry> entries = new ArrayList<>();
    private static Gson gson = new Gson();

    public static void init(File configDir) {
        configFile = new File(configDir, "cmdreplacer.json");
    }

    public static void load() {
        if (!configFile.exists()) {
            save();
            return;
        }
        try (Reader r = new FileReader(configFile)) {
            Type listType = new TypeToken<List<Entry>>(){}.getType();
            entries = gson.fromJson(r, listType);
            if (entries == null) entries = new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void save() {
        try (Writer w = new FileWriter(configFile)) {
            gson.toJson(entries, w);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Entry> getAll() {
        return entries;
    }

    public static void add(Entry e) {
        entries.add(e);
        save();
    }

    public static void update(int idx, Entry e) {
        entries.set(idx, e);
        save();
    }

    public static void remove(int idx) {
        entries.remove(idx);
        save();
    }

    public static class Entry {
        public String original;
        public String replacement;

        public Entry() {}
        public Entry(String o, String r) {
            this.original = o;
            this.replacement = r;
        }
    }
}
