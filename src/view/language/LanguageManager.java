package view.language;

import javafx.util.Pair;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

public class LanguageManager {

    private ResourceBundle labels;
    private final String bundlePath = "view/language/Labels";

    public LanguageManager(String lang) {
        Locale loc = new Locale(lang);
        this.labels = ResourceBundle.getBundle(bundlePath, loc);
    }

    public String get(String key) {
        return this.labels.getString(key);
    }

    public String[] getDifficulties() {
        String[] difficulties = getDifficultyKeys();
        for (int i = 0; i < difficulties.length; i++) {
            difficulties[i] = this.get("difficulty."+difficulties[i]);
        }
        return difficulties;
    }

    public String getLanguage() {
        return this.get("name");
    }

    public String getTranslatedLanguage() {
        return this.get("translated_name");
    }

    public String getKey(String value){
        String key = "";
        for (String k: labels.keySet()) {
            if (this.get(k) == value){
                key = k;
            }
        }
        return key;
    }

    public String[] getAllTranslatedLanguages() {
        String[] langs = getAvailableLanguages();
        for (int i = 0; i < langs.length; i++) {
            langs[i] = this.get("language."+langs[i]);
        }
        return langs;
    }

    public Pair<String,String>[] getLanguageListCell(){
        String[] locales = getAvailbleLocales();
        Pair<String,String>[] iconListLanguage = new Pair[locales.length];
        for (int i = 0; i < locales.length; i++) {
            ResourceBundle bl = ResourceBundle.getBundle(bundlePath, new Locale(locales[i]));
            iconListLanguage[i] = new Pair<>(bl.getString("name"), bl.getString("flag"));
        }
        return iconListLanguage;
    }

    public String getLocale(String name){
        String[] locale = getAvailbleLocales();
        String res = "";
        for (int i = 0; i < locale.length; i++) {
            ResourceBundle bl = ResourceBundle.getBundle(bundlePath, new Locale(locale[i]));
            String blName = bl.getString("name");
            if ( blName.equals(name)) {
                res = bl.getString("locale");
            }
        }
        return res;
    }

    public String[] getThemes() {
        String[] themes = getThemesKeys();
        for (int i = 0; i < themes.length; i++) {
            themes[i] = this.get("theme."+themes[i]);
        }
        return themes;
    }

    private String[] getThemesKeys() {
        return new String[] {
            "light-blue", "light-green", "light-red", "light-orange", "light-pink",
            "dark-blue", "dark-green", "dark-red", "dark-orange", "dark-pink",
        };
    }

    private String[] getDifficultyKeys() {
        return new String[] {"easy", "medium", "hard", "very_hard"};
    }

    private String[] getAvailableLanguages() {
        return new String[] {"french", "english", "spanish"};
    }

    private String[] getAvailbleLocales() {
        return new String[] {"fr", "en", "es"};
    }
}
