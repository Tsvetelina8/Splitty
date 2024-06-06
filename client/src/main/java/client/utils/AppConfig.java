package client.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;

public class AppConfig {
    private Properties configProperties;
    
    /**
     * Constructor for AppConfig class
     */
    public AppConfig() {
        loadProperties();
    }

    /**
     * Method to load config properties from config file
     * The file is located under client/src/main/resources/config/config.properties
     */
    private void loadProperties() {
        configProperties = new Properties();
        try (InputStream input = AppConfig.class.getClassLoader().
            getResourceAsStream("config/config.properties")) {
                
            if (input == null) {
                System.out.println("Unable to find config.properties");
                return;
            }
            configProperties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Method to get the string coresponding to selected locale
     * (in the format of "en_US")
     * 
     * @return array of Strings
     */
    public String getSelectedLocale() {
        return configProperties.getProperty("selectedLocale");
    }

    /**
     * Method to get the array of strings corresponding to available locales
     * (in the format of "en_US")
     * 
     * @return array of Strings
     */
    public String[] getAvailableLocales() {
        String localesStr = configProperties.getProperty("locales");
        return localesStr.split(",");
    }

    /**
     * Method to get the server URL from the config
     *
     * @return server url
     */
    public String getServerUrl() {
        return configProperties.getProperty("serverUrl");
    }

    /**
     * Method to get the web socket URL from the config
     *
     * @return web socket url
     */
    public String getWebSocketUrl() {
        return configProperties.getProperty("webSocketUrl");
    }

    /**
     * Method to get the user email
     *
     * @return email (for gmail)
     */
    public String getUserEmail() {
        return configProperties.getProperty("email");
    }

    /**
     * Method to get the user email password
     *
     * @return password
     */
    public String getUserPassword() {
        return configProperties.getProperty("password");
    }

    /**
     * Method to save the selection of locale in the config
     * 
     * @param locale Locale to be saved as selected
     */
    public void saveSelectedLocale(Locale locale) {
        configProperties.setProperty("selectedLocale", locale.toString());
        
        saveProperties();
    }

    /**
     * Method to add a new available translation to config
     * (in case it's a new translation, appends it to existing list)
     * 
     * @param localeStr locale string (like en_GB)
     */
    public void addAvailableLocale(String localeStr) {
        String currentLocales = configProperties.getProperty("locales", "");
        Set<String> localesSet = new HashSet<>(Arrays.asList(currentLocales.split(",")));

        if (localesSet.add(localeStr)) {
            String updatedLocalesStr = String.join(",", localesSet);
            configProperties.setProperty("locales", updatedLocalesStr);
            
            saveProperties();
        }
    }

    /**
     * Method to save properties to file
     */
    private void saveProperties() {
        String pathToConfig = "config/config.properties";

        try (OutputStream output = new FileOutputStream(AppConfig.class.getClassLoader().
                getResource(pathToConfig).toURI().getPath())) {

            configProperties.store(output, null);
        } catch (IOException | URISyntaxException io) {
            io.printStackTrace();
        }
    }
}