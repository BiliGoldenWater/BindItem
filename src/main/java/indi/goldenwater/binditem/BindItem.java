/*
 * Copyright (c) 2020. Golden_Water All Right Reserved.
 * ProjectName: BindItem
 * FileName: BindItem.java
 * Author: Golden_Water
 * LastModified: 2020/12/15 上午12:07
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
import indi.goldenwater.binditem.module.DBOperator;
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

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        useUUID = getServer().getOnlineMode();
        pluginPath = ".\\"+getServer().getPluginManager().getPlugin("BindItem").getDataFolder().getPath()+"\\";
        saveDefaultConfig();
        enchantLvlDatabase = new DBOperator(pluginPath + "data.db");

//        getServer().getPluginManager().registerEvents(,this);

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
        enchantLvlDatabase.createTable("enchantData", tableStyle);

        List<String> columns = new ArrayList<>(), values = new ArrayList<>();
        columns.add("enchantLvl");
        columns.add("playerName");
        columns.add("bindNum");

        Map<Integer, String> preSettings = new HashMap<>();
        preSettings.put(0, "\"bindonpickup\"");
        preSettings.put(1, "\"bindonuse\"");
        preSettings.put(2, "\"bindonequip\"");
        preSettings.put(3, "\"none\"");
        for(int i = 0;i<=9;i++){
            values.clear();
            values.add(String.valueOf(i));
            values.add(preSettings.get(Math.min(i, 3)));
            values.add("0");
            if(enchantLvlDatabase.select("enchantData", "enchantLvl", "enchantLvl=" + i, 1).get(1).size()>0){
                enchantLvlDatabase.update("enchantData", columns, values, "enchantLvl=" + i);
            } else {
                enchantLvlDatabase.insert("enchantData", columns, values);
            }
        }

        List<Object> noBindEnchant = enchantLvlDatabase.select("enchantData", "enchantLvl", "bindNum<=0", 1).get(1);
        noBindEnchant.forEach((value)->{
            if((int)value>9){
                enchantLvlDatabase.delete("enchantData", "enchantLvl="+value);
            }
        });
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
}
