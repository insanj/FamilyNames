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
    public static final String TOOLTIP_KEY = "tooltip";
    public static final String FAMILY_NAMES_KEY = "family_name";
    public static final String FIRST_NAMES_KEY = "first_name";
    public static final String MALE_KEY = "male";
    public static final String FEMALE_KEY = "female";
    public static final String SURNAMES_KEY = "surname";
    public static final String PLAYERS_KEY = "player";
    public static final String GENDER_KEY = "gender";

    // private const keys (used only internally, such as for commands)
    private final String FAMILY_NAME_MALE_FIRST_NAME_TYPE = "male_first_name";
    private final String FAMILY_NAME_FEMALE_FIRST_NAME_TYPE = "female_first_name";
    private final String FAMILY_NAME_SURNAME_TYPE = "surname";

    // private vars & constructor (use getters to get values from outside)
    private boolean enabledEntry;
    private boolean debugEntry;
    private boolean tooltipEntry;

    private List<String> maleFirstNameEntries;
    private List<String> femaleFirstNameEntries;
    private List<String> surnameEntries;
    private Map<String, PlayerEntry> playerEntries;

    public static class PlayerEntry {
        public final String name, gender, firstName, surname, tooltip;
        public PlayerEntry(String name, String gender, String firstName, String surname, String tooltip) {
            this.name = name;
            this.gender = gender;
            this.firstName = firstName;
            this.surname = surname;
            this.tooltip = tooltip;

            if (name == null || firstName == null || surname == null) {
                FamilyNamesPlugin.warning("FamilyNames player needs to be saved with a name, first name, and surname! Not doing this is highly inadvisable.");
            }
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
        tooltipEntry = configFile.getBoolean(FamilyNamesConfig.TOOLTIP_KEY);

        String maleFirstNameConfigSectionPath = String.format("%s.%s.%s", FamilyNamesConfig.FAMILY_NAMES_KEY, FamilyNamesConfig.FIRST_NAMES_KEY, FamilyNamesConfig.MALE_KEY);
        maleFirstNameEntries = (List<String>)configFile.get(maleFirstNameConfigSectionPath);

        String femaleFirstNameConfigSectionPath = String.format("%s.%s.%s", FamilyNamesConfig.FAMILY_NAMES_KEY, FamilyNamesConfig.FIRST_NAMES_KEY, FamilyNamesConfig.FEMALE_KEY);
        femaleFirstNameEntries = (List<String>)configFile.get(femaleFirstNameConfigSectionPath);

        String surnameConfigSectionPath = String.format("%s.%s", FamilyNamesConfig.FAMILY_NAMES_KEY, FamilyNamesConfig.SURNAMES_KEY);
        surnameEntries = (List<String>)configFile.get(surnameConfigSectionPath);

        ConfigurationSection playersSection = configFile.getConfigurationSection(FamilyNamesConfig.PLAYERS_KEY);
        HashMap<String, PlayerEntry> players = new HashMap<String, PlayerEntry>();
        if (playersSection != null) {
            Map<String,Object> playersSectionMap = (Map<String, Object>)playersSection.getValues(true);

            for (String name : playersSectionMap.keySet()) {
                Map<String, String> playerMetaMap = (Map<String, String>)playersSectionMap.get(name);
                PlayerEntry entry = new PlayerEntry(name, playerMetaMap.get(FamilyNamesConfig.GENDER_KEY), playerMetaMap.get(FamilyNamesConfig.FIRST_NAMES_KEY), playerMetaMap.get(FamilyNamesConfig.SURNAMES_KEY), playerMetaMap.get(FamilyNamesConfig.TOOLTIP_KEY));
                players.put(name, entry);
            }
        }

        playerEntries = players;
    }

    // public getters
    public boolean getEnabled() {
        return enabledEntry;
    }

    public boolean getDebug() {
        return debugEntry;
    }

    public boolean getTooltip() {
        return tooltipEntry;
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

        String genderSelector = String.format("%s.%s", selectorForPlayer, FamilyNamesConfig.GENDER_KEY);
        configFile.set(genderSelector, playerEntry.gender);

        String firstNameSelector = String.format("%s.%s", selectorForPlayer, FamilyNamesConfig.FIRST_NAMES_KEY);
        configFile.set(firstNameSelector, playerEntry.firstName);

        String surnameSelector = String.format("%s.%s", selectorForPlayer, FamilyNamesConfig.SURNAMES_KEY);
        configFile.set(surnameSelector, playerEntry.surname);

        String tooltipSelector = String.format("%s.%s", selectorForPlayer, FamilyNamesConfig.TOOLTIP_KEY);
        configFile.set(tooltipSelector, playerEntry.tooltip);

        plugin.saveConfig();
        reload();
    }

    public boolean removePlayerEntry(String playerName) { // -onFamilyRemovePCommand
        String selectorForPlayer = String.format("%s.%s", FamilyNamesConfig.PLAYERS_KEY, playerName);   
        FileConfiguration configFile = plugin.getConfig();
        boolean exists = configFile.get(selectorForPlayer) != null;
        if (exists == false) {
            return false;
        }
        configFile.set(selectorForPlayer, null);
        plugin.saveConfig();
        reload();

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
            return true;
        } else {
            return false;
        }
    }

    public boolean removeFamilyName(String typeString, String string) { // -onFamilyRemoveCommand
        FileConfiguration configFile = plugin.getConfig();

        if (typeString.equals(FAMILY_NAME_MALE_FIRST_NAME_TYPE)) {
            String maleFirstNameSelector = String.format("%s.%s.%s", FamilyNamesConfig.FAMILY_NAMES_KEY, FamilyNamesConfig.FIRST_NAMES_KEY, FamilyNamesConfig.MALE_KEY);

            List<String> existingMaleFirstNames = (List<String>) configFile.get(maleFirstNameSelector);
            if (existingMaleFirstNames == null || existingMaleFirstNames.contains(string) == false) {
                return false;
            }

            ArrayList<String> removedMaleFirstNames = new ArrayList<String>(existingMaleFirstNames);
            removedMaleFirstNames.remove(string);

            configFile.set(maleFirstNameSelector, removedMaleFirstNames);
            plugin.saveConfig();
            reload();
            return true;
        } else if (typeString.equals(FAMILY_NAME_FEMALE_FIRST_NAME_TYPE)) {
            String femaleFirstNameSelector = String.format("%s.%s.%s", FamilyNamesConfig.FAMILY_NAMES_KEY, FamilyNamesConfig.FIRST_NAMES_KEY, FamilyNamesConfig.FEMALE_KEY);

            List<String> existingFemaleFirstNames = (List<String>) configFile.get(femaleFirstNameSelector);
            if (existingFemaleFirstNames == null || existingFemaleFirstNames.contains(string) == false) {
                return false;
            }

            ArrayList<String> removedFemaleFirstNames = new ArrayList<String>(existingFemaleFirstNames);
            removedFemaleFirstNames.remove(string);

            configFile.set(femaleFirstNameSelector, removedFemaleFirstNames);
            plugin.saveConfig();
            reload();
            return true;
        } else if (typeString.equals(FAMILY_NAME_SURNAME_TYPE)) {
            String surnameSelector = String.format("%s.%s", FamilyNamesConfig.FAMILY_NAMES_KEY, FamilyNamesConfig.SURNAMES_KEY);

            List<String> existingSurnames = (List<String>) configFile.get(surnameSelector);
            if (existingSurnames == null || existingSurnames.contains(string) == false) {
                return false;
            }

            ArrayList<String> removedSurnames = new ArrayList<String>(existingSurnames);
            removedSurnames.remove(string);

            configFile.set(surnameSelector, removedSurnames);
            plugin.saveConfig();
            reload();
            return true;
        } else {
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
