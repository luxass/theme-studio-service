package com.lucasnorgaard.tstudioservice;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.lucasnorgaard.tstudioservice.models.Language;
import com.lucasnorgaard.tstudioservice.models.TStudioPreset;
import org.kohsuke.github.GHContent;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Helpers {

    public Map<String, TStudioPreset> getPresets() {

        GitHub gitHub = Application.getGitHub();
        Map<String, TStudioPreset> presets = new HashMap<>();
        try {
            GHRepository repository = gitHub.getRepository("DeprecatedLuxas/tstudio-presets");
            List<GHContent> contents = repository.getDirectoryContent("presets")
                    .stream()
                    .filter(f -> f.getName().contains(".tstudio-preset")).collect(Collectors.toList());


            for (GHContent content : contents) {
                try {
                    String fileContent = new String(content.read().readAllBytes(), StandardCharsets.UTF_8);
                    TStudioPreset tStudioPreset = Application.GSON.fromJson(fileContent, TStudioPreset.class);
                    presets.put(content.getName().replace(".tstudio-preset", ""), tStudioPreset);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return presets;
    }

    public Map<String, Language> getLanguages() {
        System.out.println("Fetching Languages");
        Map<String, Language> languages = new HashMap<>();

        try {
            GHRepository repo = Application.getGitHub().getRepository("github/linguist");
            GHContent content = repo.getFileContent("lib/linguist/languages.yml");
            Yaml yaml = new Yaml();
            Type langMapType = new TypeToken<Map<String, Language>>() {
            }.getType();
            Object json = yaml.loadAs(content.read(), Object.class);

            languages = Application.GSON.fromJson(Application.GSON.toJson(json), langMapType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Updated Languages");
        return languages;
    }



}
