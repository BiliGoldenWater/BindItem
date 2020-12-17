/*
 * Copyright (c) 2020 by Golden_Water. All Right Reserved.
 * ProjectName: BindItem
 * FileName: BindItem.java
 * Author: Golden_Water
 * Email: 2439577268@qq.com
 * LastModified: 2020/12/15 00:31:15
 *
 * Commercial use is prohibited without permission.
 * Author must be noted for use,
 * The signature must be in a place where everyone using it can easily see it.
 * Any consequence arising out of the use of the code shall be borne by itself.
 *
 * Using or downloading code represents agreement to the protocol.
 *
 * 未经允许禁止商业使用。
 * 使用必须注明作者，
 * 署名必须在每个使用的人能都简单地看到的地方。
 * 使用代码造成的任何后果均自行承担。
 *
 * 使用或下载代码则代表同意该协议。
 */

package indi.goldenwater.binditem;

import indi.goldenwater.binditem.command.BindItemExecutor;
import indi.goldenwater.binditem.enchant.RegisterEnchantBindItem;
import indi.goldenwater.binditem.listener.OnDeathAndRespawnEvents;
import indi.goldenwater.binditem.listener.OnItemEvents;
import indi.goldenwater.binditem.module.DBOperator;
import org.bukkit.configuration.Configuration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class BindItem extends JavaPlugin {
    private static BindItem instance;
    private static boolean useUUID;
    private static DBOperator enchantLvlDatabase;
    private static String pluginPath;
    private static Configuration config;
    private static String tableName;
    public static Map<String, List<ItemStack>> deathPlayersItems = new HashMap<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        useUUID = getServer().getOnlineMode();
        pluginPath = ".\\"+getDataFolder().getPath()+"\\";
        saveDefaultConfig();
        config = getConfig();
        tableName = "enchantData";

        enchantLvlDatabase = new DBOperator(pluginPath + "data.db");

        getServer().getPluginManager().registerEvents(new OnItemEvents(), this);
        if(config.getBoolean("takeOverDeathDrop")){
            getServer().getPluginManager().registerEvents(new OnDeathAndRespawnEvents(), this);
        }

        getLogger().info("init database.");
        initEnchantLvlDatabase(enchantLvlDatabase);
        RegisterEnchantBindItem.registerEnchantBindItem();
        BindItemExecutor.registerCommandBindItem(this);
        getLogger().info("BindItem Enabled");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        saveConfig();
        getLogger().info("BindItem Disabled");
    }

    private void initEnchantLvlDatabase(DBOperator enchantLvlDatabase){
        Map<String, String> tableStyle = new HashMap<>();
        tableStyle.put("enchantLvl", "int");
        tableStyle.put("playerName", "text");
        tableStyle.put("bindNum", "int");
        enchantLvlDatabase.createTable(tableName, tableStyle);

        List<String> columns = new ArrayList<>(), values = new ArrayList<>();
        columns.add("enchantLvl");
        columns.add("playerName");
        columns.add("bindNum");

        Map<Integer, String> preSettings = new HashMap<>();
        preSettings.put(1, "\"bindonpickup\"");
        preSettings.put(2, "\"bindonuse\"");
        preSettings.put(3, "\"bindonequip\"");
        preSettings.put(4, "\"none\"");
        for(int i = 1;i<=10;i++){
            values.clear();
            values.add(String.valueOf(i));
            values.add(preSettings.get(Math.min(i, 4)));
            values.add("0");
            if(enchantLvlDatabase.select(tableName, "enchantLvl", "enchantLvl=" + i, 1).get(1).size()>0){
                enchantLvlDatabase.update(tableName, columns, values, "enchantLvl=" + i);
            } else {
                enchantLvlDatabase.insert(tableName, columns, values);
            }
        }

        if(getConfig().getBoolean("removeNonBindPlayerOnStartUp")) {
            List<Object> noBindEnchant = enchantLvlDatabase.select(tableName, "enchantLvl", "bindNum<=0", 1).get(1);
            noBindEnchant.forEach((value) -> {
                if ((int) value > 10) {
                    enchantLvlDatabase.delete(tableName, "enchantLvl=" + value);
                }
            });
        }
    }

    public static Configuration getPluginConfig(){
        return config;
    }

    public static BindItem getInstance(){
        return instance;
    }

    public static boolean isUsingUUID(){
        return useUUID;
    }

    public static DBOperator getEnchantLvlDatabase(){
        return enchantLvlDatabase;
    }

    public static String getPluginPath(){
        return pluginPath;
    }

    public static String getTableName(){
        return tableName;
    }
}
