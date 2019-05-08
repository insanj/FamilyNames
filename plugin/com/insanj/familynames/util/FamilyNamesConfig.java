package com.insanj.familynames.util;

import java.util.Map;
import java.util.HashMap;
import java.util.UUID;

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

public class FamilyNamesConfig {
    // const keys
    static final String ENABLED_KEY = "enabled";
    static final String DEBUG_KEY = "debug";
    static final String TOOLTIP_KEY = "tooltip";
    static final String FAMILY_NAMES_KEY = "family_name";
    static final String FIRST_NAMES_KEY = "first_name";
    static final String MALE_KEY = "male";
    static final String FEMALE_KEY = "female";
    static final String SURNAMES_KEY = "surname";
    static final String PLAYERS_KEY = "player";
    static final String GENDER_KEY = "gender";

    // private vars & constructor (use getters to get values from outside)
    private boolean enabledEntry;
    private boolean debugEntry;
    private boolean tooltipEntry;

    private List<String> maleFirstNameEntries;
    private List<String> femaleFirstNameEntries;
    private List<String> surnameEntries;
    private Map<String, PlayerEntry> playerEntries;

    class PlayerEntry {
        public final String name, gender, firstName, surname, tooltip;
        public PlayerEntry(String name, String gender, String firstName, String familyNameSurname, String tooltip) {
            this.name = name;
            this.gender = gender;
            this.firstName = firstName;
            this.surname = surname;
            this.tooltip = tooltip;
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
        maleFirstNameEntries = configFile.getConfigurationSection(maleFirstNameConfigSectionPath).getValues(true);

        String femaleFirstNameConfigSectionPath = String.format("%s.%s.%s", FamilyNamesConfig.FAMILY_NAMES_KEY, FamilyNamesConfig.FIRST_NAMES_KEY, FamilyNamesConfig.FEMALE_KEY);
        femaleFirstNameEntries = configFile.getConfigurationSection(femaleFirstNameConfigSectionPath).getValues(true);

        String surnameConfigSectionPath = String.format("%s.%s", FamilyNamesConfig.FAMILY_NAMES_KEY, FamilyNamesConfig.SURNAMES_KEY);
        surnameEntries = configFile.getConfigurationSection(surnameConfigSectionPath).getValues(true);

        ConfigurationSection playersSection = configFile.getConfigurationSection(FamilyNamesConfig.PLAYERS_KEY);
        HashMap<PlayerEntry> players = new HashMap<PlayerEntry>();
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

    public void addPlayerEntry(PlayerEntry playerEntry) {
        configFile.getConfigurationSection().

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

    // convenience rando menthods
    public String getRandomMaleFirstName() {
        List<String> maleFirstNames = getFamilyMaleFirstNames();
        int randomMaleIdx = Math.random(maleFirstNames.size());
        return maleFirstNames.get(randomMaleIdx);
    }

    public String getRandomFemaleFirstName() {
        List<String> femaleFirstNames = getFamilyFemaleFirstNames();
        int randomFemaleIdx = Math.random(femaleFirstNames.size());
        return femaleFirstNames.get(randomFemaleIdx);   
    }

    public String getRandomSurname() {
        List<String> surnames = getFamilySurnames();
        int randomSurnameIdx = Math.random(surnames.size());
        return surnames.get(randomSurnameIdx);   
    }
}
