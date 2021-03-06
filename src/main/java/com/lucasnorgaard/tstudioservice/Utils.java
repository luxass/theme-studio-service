package com.lucasnorgaard.tstudioservice;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.lucasnorgaard.tstudioservice.models.Language;
import com.lucasnorgaard.tstudioservice.models.Mapping;
import com.lucasnorgaard.tstudioservice.models.ResponseIcon;
import org.kohsuke.github.GHContent;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

public class Utils {


    public static Optional<String> getFileExtension(String filename) {
        return Optional.ofNullable(filename).filter(f -> f.contains(".")).map(f -> f.substring(filename.lastIndexOf(".") + 1));
    }


    public static <K, V extends Comparable<V>> K maxUsingStreamAndLambda(Map<K, V> map) {
        Optional<Map.Entry<K, V>> maxEntry = map.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue()
                );

        return maxEntry.get().getKey();
    }

    public static ResponseIcon getCorrectFolderIcon(String name) {
        ResponseIcon icon = new ResponseIcon(
                "folder",
                "folder-open"
        );

        for (Map.Entry<String, List<String>> entry : Application.getFolderIconMap().entrySet()) {
            List<String> names = entry.getValue();
            String key = entry.getKey();
            if (names.contains(name)) {
                icon.url = key;
                icon.url_opened = key + "-open";
                break;
            }
        }
        return icon;
    }

    public static ResponseIcon getCorrectFileIcon(String name, String ext) {
        ResponseIcon icon = new ResponseIcon(
                "file",
                null
        );

        System.out.println(ext);
        System.out.println(name);

        if (!ext.equals("")) {
            System.out.println("Running");
            for (Map.Entry<String, List<String>> entry : Application.getFileIconExtMap().entrySet()) {
                List<String> exts = entry.getValue();
                String key = entry.getKey();
                System.out.println(exts);
                if (exts == null) return icon;

                if (exts.contains(ext)) {
                    icon.url = key;
                    icon.url_opened = null;
                    break;
                }
            }
        }
        System.out.println(icon);

        if (!name.equals("")) {
            System.out.println("Running 2");

            for (Map.Entry<String, List<String>> entry : Application.getFileIconNameMap().entrySet()) {
                List<String> names = entry.getValue();
                String key = entry.getKey();
                if (names == null) return icon;
                if (names.contains(name)) {
                    icon.url = key;
                    icon.url_opened = null;
                    break;
                }
            }
        }
        System.out.println(icon);

        return icon;
    }

    public static String getCorrectIcon(String ext, String name, boolean dir) {
        String icon = "";
        Map<String, List<String>> extMap = Application.getFileIconExtMap();
        Map<String, List<String>> nameMap = Application.getFileIconNameMap();
        Map<String, List<String>> folderNameMap = Application.getFolderIconMap();


        for (Map.Entry<String, List<String>> entry : extMap.entrySet()) {
            List<String> exts = entry.getValue();
            if (exts == null) return icon;
            if (exts.contains(ext)) {
                icon = entry.getKey();
                break;
            }
        }

        for (Map.Entry<String, List<String>> entry : nameMap.entrySet()) {
            List<String> names = entry.getValue();
            if (names == null) return icon;
            if (names.contains(name)) {
                icon = entry.getKey();
                break;
            }
        }

        if (dir) {
            for (Map.Entry<String, List<String>> entry : folderNameMap.entrySet()) {
                List<String> names = entry.getValue();
                if (names == null) return icon;
                if (names.contains(name)) {
                    icon = entry.getKey();
                    break;
                }
            }
        }

        if (icon.equals("")) {
            icon = dir ? "folder" : "file";
        }
        return icon;
    }



}
