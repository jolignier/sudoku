package view.language;

import javafx.util.Pair;
import java.util.Locale;
import java.util.ResourceBundle;


/**
 * Date  18/04/2020 <br>
 *  Represent a Language Manager
 *  Language Manager purpose is to give all the needed informations
 *  about current and available languages to display on the GUI
 *  Allows the application to be international and comfortable to use
 */
public class LanguageManager {

    private ResourceBundle labels;
    private final String bundlePath = "view/language/Labels";


    /**
     * Constructor
     * @param lang the default language
     */
    public LanguageManager(String lang) {
        Locale loc = new Locale(lang);
        this.labels = ResourceBundle.getBundle(bundlePath, loc);
    }

    /**
     * @param key the key of the wanted value
     * @return the corresponding value
     */
    public String get(String key) {
        return this.labels.getString(key);
    }

    /**
     * @return A list of all translated difficulties
     */
    public String[] getDifficulties() {
        String[] difficulties = getDifficultyKeys();
        for (int i = 0; i < difficulties.length; i++) {
            difficulties[i] = this.get("difficulty."+difficulties[i]);
        }
        return difficulties;
    }

    /**
     * @return the name of the current language
     */
    public String getLanguage() {
        return this.get("name");
    }

    /**
     * @param value the value of the wanted key
     * @return the corresponding key
     */
    public String getKey(String value){
        String key = "";
        for (String k: labels.keySet()) {
            if (this.get(k) == value){
                key = k;
            }
        }
        return key;
    }

    /**
     * @return A pair of Language translated name and corresponding flag
     */
    public Pair<String,String>[] getLanguageListCell(){
        String[] locales = getAvailbleLocales();
        Pair<String,String>[] iconListLanguage = new Pair[locales.length];
        for (int i = 0; i < locales.length; i++) {
            ResourceBundle bl = ResourceBundle.getBundle(bundlePath, new Locale(locales[i]));
            iconListLanguage[i] = new Pair<>(bl.getString("name"), bl.getString("flag"));
        }
        return iconListLanguage;
    }

    /**
     * @param name translated name of current language
     * @return locale of current language
     */
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

    /**
     * @return the different themes translated values of given language
     */
    public String[] getThemes() {
        String[] themes = getThemesKeys();
        for (int i = 0; i < themes.length; i++) {
            themes[i] = this.get("theme."+themes[i]);
        }
        return themes;
    }

    /**
     * @return the different themes keys available
     */
    private String[] getThemesKeys() {
        return new String[] {
            "light-blue", "light-green", "light-red", "light-orange", "light-pink",
            "dark-blue", "dark-green", "dark-red", "dark-orange", "dark-pink",
        };
    }
    /**
     * @return the different difficulty keys available
     */
    private String[] getDifficultyKeys() {
        return new String[] {"easy", "medium", "hard", "very_hard"};
    }
    /**
     * @return the different languages keys available
     */
    private String[] getAvailableLanguages() {
        return new String[] {"french", "english", "spanish"};
    }
    /**
     * @return the different locales keys available
     */
    private String[] getAvailbleLocales() {
        return new String[] {"fr", "en", "es"};
    }
}
