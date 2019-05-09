package com.insanj.familynames.util;

import java.util.Map;
import java.util.HashMap;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.bukkit.World;
import org.bukkit.Location;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.file.FileConfiguration;

import com.insanj.familynames.FamilyNamesPlugin;

public class FamilyNamesConfig {
    // public const keys (used for permissions or elsewhere)
    public static final String ENABLED_KEY = "enabled";
    public static final String DEBUG_KEY = "debug";
    public static final String SHOW_USERNAME_ON_HOVER_KEY = "show_username_on_hover";

    public static final String FAMILY_NAMES_KEY = "family_name";
    public static final String FIRST_NAMES_KEY = "first_name";
    public static final String MALE_KEY = "male";
    public static final String FEMALE_KEY = "female";
    public static final String SURNAMES_KEY = "surname";
    public static final String PLAYERS_KEY = "player";

    // private const keys (used only internally, such as for commands)
    private final String FAMILY_NAME_MALE_FIRST_NAME_TYPE = "male_first_name";
    private final String FAMILY_NAME_FEMALE_FIRST_NAME_TYPE = "female_first_name";
    private final String FAMILY_NAME_SURNAME_TYPE = "surname";

    // private vars & constructor (use getters to get values from outside)
    private boolean enabledEntry;
    private boolean debugEntry;
    private boolean showUsernameOnHoverEntry;

    private List<String> maleFirstNameEntries;
    private List<String> femaleFirstNameEntries;
    private List<String> surnameEntries;
    private Map<String, PlayerEntry> playerEntries;

    public static class PlayerEntry {
        public final String name, firstName, surname;
        public PlayerEntry(String name, String firstName, String surname) {
            this.name = name;
            this.firstName = firstName;
            this.surname = surname;

            if (name == null || firstName == null || surname == null) {
                FamilyNamesPlugin.warning("FamilyNames player needs to be saved with a name, first name, and surname! Not doing this is highly inadvisable.");
            }
        }

        public static PlayerEntry fromPlayerMap(String name, Map<String, Object> map) {
            String firstName = null, surname = null;

            Object firstNameObj = map.get(FamilyNamesConfig.FIRST_NAMES_KEY);
            if (firstNameObj != null && firstNameObj instanceof String) {
                firstName = (String)firstNameObj;
            }

            Object surnameObj = map.get(FamilyNamesConfig.SURNAMES_KEY);
            if (surnameObj != null && surnameObj instanceof String) {
                surname = (String)surnameObj;
            }

            return new PlayerEntry(name, firstName, surname);
        }
    }

    // util vars
    private FamilyNamesPlugin plugin;

    public FamilyNamesConfig(FamilyNamesPlugin plugin) {
        this.plugin = plugin;
    }

    // reload from disk (replaces anything in memory, which should already have been written to disk)
    public void reload() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();

        FileConfiguration configFile = plugin.getConfig();
        enabledEntry = configFile.getBoolean(FamilyNamesConfig.ENABLED_KEY);
        debugEntry = configFile.getBoolean(FamilyNamesConfig.DEBUG_KEY);
        showUsernameOnHoverEntry = configFile.getBoolean(FamilyNamesConfig.SHOW_USERNAME_ON_HOVER_KEY);

        String maleFirstNameConfigSectionPath = String.format("%s.%s.%s", FamilyNamesConfig.FAMILY_NAMES_KEY, FamilyNamesConfig.FIRST_NAMES_KEY, FamilyNamesConfig.MALE_KEY);
        maleFirstNameEntries = (List<String>)configFile.get(maleFirstNameConfigSectionPath);

        String femaleFirstNameConfigSectionPath = String.format("%s.%s.%s", FamilyNamesConfig.FAMILY_NAMES_KEY, FamilyNamesConfig.FIRST_NAMES_KEY, FamilyNamesConfig.FEMALE_KEY);
        femaleFirstNameEntries = (List<String>)configFile.get(femaleFirstNameConfigSectionPath);

        String surnameConfigSectionPath = String.format("%s.%s", FamilyNamesConfig.FAMILY_NAMES_KEY, FamilyNamesConfig.SURNAMES_KEY);
        surnameEntries = (List<String>)configFile.get(surnameConfigSectionPath);

        ConfigurationSection playersSection = configFile.getConfigurationSection(FamilyNamesConfig.PLAYERS_KEY);
        HashMap<String, PlayerEntry> players = new HashMap<String, PlayerEntry>();
        if (playersSection != null) {
            Map<String,Object> playersSectionMap = (Map<String, Object>)playersSection.getValues(false);

            for (String name : playersSectionMap.keySet()) {
                ConfigurationSection individualPlayerSection = configFile.getConfigurationSection(String.format("%s.%s", FamilyNamesConfig.PLAYERS_KEY, name));
                Map<String, Object> playerMetaMap = (Map<String, Object>)individualPlayerSection.getValues(true);

                PlayerEntry entry = PlayerEntry.fromPlayerMap(name, playerMetaMap);
                players.put(name, entry);
            }
        }

        playerEntries = players;

        plugin.info("Finished reloading config from config.yml!");
    }

    // public getters
    public boolean getEnabled() {
        return enabledEntry;
    }

    public boolean getDebug() {
        return debugEntry;
    }

    public boolean getShowUsernameOnHover() {
        return showUsernameOnHoverEntry;
    }

    public List<String> getFamilyMaleFirstNames() {
        return maleFirstNameEntries;
    }

    public List<String> getFamilyFemaleFirstNames() {
        return femaleFirstNameEntries;
    }

    public List<String> getFamilySurnames() {
        return surnameEntries;
    }

    public PlayerEntry getPlayerEntry(String playerName) {
        return playerEntries.get(playerName);
    }

    // player command funcs
    public void addPlayerEntry(PlayerEntry playerEntry) { // -onFamilySetCommand
        String selectorForPlayer = String.format("%s.%s", FamilyNamesConfig.PLAYERS_KEY, playerEntry.name);   
        FileConfiguration configFile = plugin.getConfig();

        String firstNameSelector = String.format("%s.%s", selectorForPlayer, FamilyNamesConfig.FIRST_NAMES_KEY);
        configFile.set(firstNameSelector, playerEntry.firstName);

        String surnameSelector = String.format("%s.%s", selectorForPlayer, FamilyNamesConfig.SURNAMES_KEY);
        configFile.set(surnameSelector, playerEntry.surname);

        plugin.saveConfig();
        reload();

        plugin.info("Added player entry: " + playerEntry.toString());
    }

    public boolean removePlayerEntry(String playerName) { // -onFamilyRemovePCommand
        String selectorForPlayer = String.format("%s.%s", FamilyNamesConfig.PLAYERS_KEY, playerName);   
        FileConfiguration configFile = plugin.getConfig();
        boolean exists = configFile.get(selectorForPlayer) != null;
        if (exists == false) {
            plugin.info("Unable to remove player entry for player which does not have an entry in the Family Names config: " + playerName);
            return false;
        }

        configFile.set(selectorForPlayer, null);
        plugin.saveConfig();
        reload();

        plugin.info("Removed player entry: " + playerName);
        return true;
    }

    public boolean addFamilyName(String typeString, String string) { // -onFamilyAddCommand
        FileConfiguration configFile = plugin.getConfig();

        if (typeString.equals(FAMILY_NAME_MALE_FIRST_NAME_TYPE)) {
            String maleFirstNameSelector = String.format("%s.%s.%s", FamilyNamesConfig.FAMILY_NAMES_KEY, FamilyNamesConfig.FIRST_NAMES_KEY, FamilyNamesConfig.MALE_KEY);

            List<String> existingMaleFirstNames = (List<String>) configFile.get(maleFirstNameSelector);
            if (existingMaleFirstNames == null) {
                existingMaleFirstNames = new ArrayList<String>();
            }

            ArrayList<String> addedMaleFirstNames = new ArrayList<String>(existingMaleFirstNames);
            addedMaleFirstNames.add(string);

            configFile.set(maleFirstNameSelector, addedMaleFirstNames);
            plugin.saveConfig();
            reload();

            plugin.info("Adding Family Name entry for Male First Name: " + string);
            return true;
        } else if (typeString.equals(FAMILY_NAME_FEMALE_FIRST_NAME_TYPE)) {
            String femaleFirstNameSelector = String.format("%s.%s.%s", FamilyNamesConfig.FAMILY_NAMES_KEY, FamilyNamesConfig.FIRST_NAMES_KEY, FamilyNamesConfig.FEMALE_KEY);

            List<String> existingFemaleFirstNames = (List<String>) configFile.get(femaleFirstNameSelector);
            if (existingFemaleFirstNames == null) {
                existingFemaleFirstNames = new ArrayList<String>();
            }

            ArrayList<String> addedFemaleFirstNames = new ArrayList<String>(existingFemaleFirstNames);
            addedFemaleFirstNames.add(string);

            configFile.set(femaleFirstNameSelector, addedFemaleFirstNames);
            plugin.saveConfig();
            reload();

            plugin.info("Adding Family Name entry for Female First Name: " + string);
            return true;
        } else if (typeString.equals(FAMILY_NAME_SURNAME_TYPE)) {
            String surnameSelector = String.format("%s.%s", FamilyNamesConfig.FAMILY_NAMES_KEY, FamilyNamesConfig.SURNAMES_KEY);

            List<String> existingSurnames = (List<String>) configFile.get(surnameSelector);
            if (existingSurnames == null) {
                existingSurnames = new ArrayList<String>();
            }

            ArrayList<String> addedSurnames = new ArrayList<String>(existingSurnames);
            addedSurnames.add(string);

            configFile.set(surnameSelector, addedSurnames);
            plugin.saveConfig();
            reload();

            plugin.info("Adding Family Name entry for Surname: " + string);
            return true;
        } else {
            plugin.info("Unable to add Family Name entry, needs proper type (male_first_name, female_first_name, or surname)");
            return false;
        }
    }

    public boolean removeFamilyName(String typeString, String string) { // -onFamilyRemoveCommand
        FileConfiguration configFile = plugin.getConfig();

        if (typeString.equals(FAMILY_NAME_MALE_FIRST_NAME_TYPE)) {
            String maleFirstNameSelector = String.format("%s.%s.%s", FamilyNamesConfig.FAMILY_NAMES_KEY, FamilyNamesConfig.FIRST_NAMES_KEY, FamilyNamesConfig.MALE_KEY);

            List<String> existingMaleFirstNames = (List<String>) configFile.get(maleFirstNameSelector);
            if (existingMaleFirstNames == null || existingMaleFirstNames.contains(string) == false) {
                plugin.info("Unable to remove male first name entry which does not exist in the Family Names config: " + string);
                return false;
            }

            ArrayList<String> removedMaleFirstNames = new ArrayList<String>(existingMaleFirstNames);
            removedMaleFirstNames.remove(string);

            configFile.set(maleFirstNameSelector, removedMaleFirstNames);
            plugin.saveConfig();
            reload();

            plugin.info("Removing Family Name entry for Male First Name: " + string);
            return true;
        } else if (typeString.equals(FAMILY_NAME_FEMALE_FIRST_NAME_TYPE)) {
            String femaleFirstNameSelector = String.format("%s.%s.%s", FamilyNamesConfig.FAMILY_NAMES_KEY, FamilyNamesConfig.FIRST_NAMES_KEY, FamilyNamesConfig.FEMALE_KEY);

            List<String> existingFemaleFirstNames = (List<String>) configFile.get(femaleFirstNameSelector);
            if (existingFemaleFirstNames == null || existingFemaleFirstNames.contains(string) == false) {
                plugin.info("Unable to remove female first name entry which does not exist in the Family Names config: " + string);
                return false;
            }

            ArrayList<String> removedFemaleFirstNames = new ArrayList<String>(existingFemaleFirstNames);
            removedFemaleFirstNames.remove(string);

            configFile.set(femaleFirstNameSelector, removedFemaleFirstNames);
            plugin.saveConfig();
            reload();

            plugin.info("Removing Family Name entry for Felame First Name: " + string);
            return true;
        } else if (typeString.equals(FAMILY_NAME_SURNAME_TYPE)) {
            String surnameSelector = String.format("%s.%s", FamilyNamesConfig.FAMILY_NAMES_KEY, FamilyNamesConfig.SURNAMES_KEY);

            List<String> existingSurnames = (List<String>) configFile.get(surnameSelector);
            if (existingSurnames == null || existingSurnames.contains(string) == false) {
                plugin.info("Unable to remove surname entry which does not exist in the Family Names config: " + string);
                return false;
            }

            ArrayList<String> removedSurnames = new ArrayList<String>(existingSurnames);
            removedSurnames.remove(string);

            configFile.set(surnameSelector, removedSurnames);
            plugin.saveConfig();
            reload();

            plugin.info("Removing Family Name entry for Surname: " + string);
            return true;
        } else {
            plugin.info("Unable to remove Family Name entry, needs proper type (male_first_name, female_first_name, or surname)");
            return false;
        }
    }

    // player join funcs
    // convenience rando menthods
    public int random(int min, int max) {
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    public String getRandomMaleFirstName() {
        List<String> maleFirstNames = getFamilyMaleFirstNames();
        int randomMaleIdx = random(0, maleFirstNames.size()-1);
        return maleFirstNames.get(randomMaleIdx);
    }

    public String getRandomFemaleFirstName() {
        List<String> femaleFirstNames = getFamilyFemaleFirstNames();
        int randomFemaleIdx = random(0, femaleFirstNames.size()-1);
        return femaleFirstNames.get(randomFemaleIdx);   
    }

    public String getRandomSurname() {
        List<String> surnames = getFamilySurnames();
        int randomSurnameIdx = random(0, surnames.size()-1);
        return surnames.get(randomSurnameIdx);   
    }
}
