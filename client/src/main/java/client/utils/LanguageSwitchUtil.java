package client.utils;

import java.util.Locale;
import java.util.Locale.Builder;

import client.scenes.StartScreenCtrl;
import javafx.scene.control.Menu;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class LanguageSwitchUtil {

    /**
     * Method to set the country flag on language indicator
     * 
     * @param languageIndicator indicator menu
     * @param languageStr       name of language
     */
    public static void setCountryFlag(Menu languageIndicator, String languageStr) {
        String iconPath = "/client/flags/";
        switch (languageStr) {
            case "English":
            case "Dutch":
            case "Russian":
                iconPath += languageStr;
                break;
            default:
                return;
        }

        iconPath += ".png";

        Image icon = new Image(StartScreenCtrl.class.getResourceAsStream(iconPath));
        ImageView iconView = new ImageView(icon);

        iconView.setFitHeight(15);
        iconView.setFitWidth(15);

        languageIndicator.setGraphic(iconView);
    }

    /**
     * Wrapper of setCountryFlag. Allows to pass locale directly to this method
     * 
     * @param languageIndicator  indicator menu
     * @param locale             locale
     */
    public static void setCountryFlag(Menu languageIndicator, Locale locale) {
        Builder builder = new Locale.Builder();

        Locale inLocale = builder.setLanguage("en").setRegion("US").build();
        String languageStr;
        if (locale == null)
            languageStr = inLocale.getDisplayLanguage(inLocale);
        else {
            languageStr = locale.getDisplayLanguage(inLocale);
        }
        setCountryFlag(languageIndicator, languageStr);
    }

    /**
     * Method to initialize the indicator
     * 
     * @param languageIndicator  indicator menu
     * @param newLocale          locale of choice to be initilized
     */
    public static void initIndicator(Menu languageIndicator, Locale newLocale) {
        Builder builder = new Locale.Builder();

        Locale inLocale = builder.setLanguage("en").setRegion("US").build();

        String languageStr;
        if (newLocale == null)
            languageStr = inLocale.getDisplayLanguage(inLocale);
        else {
            languageStr = newLocale.getDisplayLanguage(inLocale);
        }

        languageIndicator.setText(languageStr);
        setCountryFlag(languageIndicator, languageStr);
    }
}
