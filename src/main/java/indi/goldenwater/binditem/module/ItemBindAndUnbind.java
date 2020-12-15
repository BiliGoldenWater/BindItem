/*
 * Copyright (c) 2020 by Golden_Water. All Right Reserved.
 * ProjectName: BindItem
 * FileName: ItemBindAndUnbind.java
 * Author: Golden_Water
 * Email: 2439577268@qq.com
 * LastModified: 2020/12/15 12:09:15
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

package indi.goldenwater.binditem.module;

import indi.goldenwater.binditem.BindItem;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ItemBindAndUnbind {
    public static void bindItem(Player player, ItemStack item){
        List<String> bindInfo = bindType(item);
        switch (bindInfo.get(0)){
            case "none":
                if (bind(player.getName(),item)){
                    player.sendMessage("绑定成功.");
                } else {
                    player.sendMessage("§c绑定失败, 记录已满.");
                }
                break;
            case "bindToPlayer":
                if (bindInfo.get(1).equals(player.getName())){
                    player.sendMessage("物品已绑定.");
                } else {
                    player.sendMessage("该物品已被绑定到玩家: "+bindInfo.get(1)+".");
                }
                break;
            case "bindOnPickup":
                player.sendMessage("物品已被设置为拾取后绑定.");
                break;
            case "bindOnUse":
                player.sendMessage("物品已被设置为使用后绑定.");
                break;
            case "bindOnEquip":
                player.sendMessage("物品已被设置为装备后绑定.");
                break;
            case "needRemove":
                item.removeEnchantment(Enchantment.getByName("bind_item"));
                if (bind(player.getName(),item)){
                    player.sendMessage("绑定成功.");
                } else {
                    player.sendMessage("§c绑定失败, 记录已满.");
                }
                break;
        }
    }

    public static void unBindItem(Player player, ItemStack item){
        List<String> bindInfo = bindType(item);
        switch (bindInfo.get(0)){
            case "none":
                player.sendMessage("该物品未被绑定.");
                break;
            case "bindToPlayer":
                if(bindInfo.get(1).equals(player.getName())){
                    if (unbind(item.getEnchantmentLevel(Enchantment.getByName("bind_item")),item)){
                        player.sendMessage("物品已解绑.");
                    } else {
                        player.sendMessage("解绑失败.");
                    }
                } else {
                    player.sendMessage("这个物品不是你的.");
                }
                break;
            case "bindOnPickup":
            case "bindOnUse":
            case "bindOnEquip":
                if(CheckPermissions.hasPermission_Tips(player,"binditem.bypass.unsetlimit", "§c你没有权限 %perm 来解除设定.")){
                    if (unbind(item.getEnchantmentLevel(Enchantment.getByName("bind_item")),item)){
                        player.sendMessage("解除设定成功.");
                    } else {
                        player.sendMessage("解除失败.");
                    }
                }
                break;
            case "needRemove":
                item.removeEnchantment(Enchantment.getByName("bind_item"));
                player.sendMessage("该物品的绑定已经失效, 清除绑定!");
                break;
        }
    }

    private static boolean bind(String playerName, ItemStack item){
        DBOperator database = BindItem.getEnchantLvlDatabase();

        int maxBindNum = 0;
        List<Object> numList = database.select(BindItem.getTableName(), "bindNum", "playerName=\""+nameEncode(playerName)+"\"", 1).get(1);
        for (Object i : numList) {
            if ((int) i>maxBindNum){
                maxBindNum= (int) i;
            }
        }

        List<String> columns = new ArrayList<>(), values = new ArrayList<>();
        int enchantLVL;
        columns.add("enchantLvl");
        columns.add("playerName");
        columns.add("bindNum");

        if (isHaveRecord(maxBindNum, playerName, numList.size())){
            enchantLVL = (int) database.select(BindItem.getTableName(),
                    "enchantLvl",
                    "playerName=\""+nameEncode(playerName)+"\" and bindNum="+maxBindNum,
                    1)
                    .get(1).get(0);
            values.add(String.valueOf(enchantLVL));
        } else {
            int availableLvl = getAvailableLvl(database);
            if(availableLvl<Short.MAX_VALUE){
                values.add(String.valueOf(availableLvl));
            } else {
                BindItem.getInstance().getLogger().warning("记录数已满");
                return false;
            }
            enchantLVL = availableLvl;
        }

        values.add("\""+nameEncode(playerName)+"\"");

        if(isHaveRecord(maxBindNum, playerName, numList.size())){
            values.add(String.valueOf(++maxBindNum));
            database.update(BindItem.getTableName(), columns, values, "enchantLvl="+enchantLVL);
        } else {
            values.add("1");
            database.insert(BindItem.getTableName(), columns, values);
        }

        item.addEnchantment(Enchantment.getByName("bind_item"), enchantLVL);

        return true;
    }

    private static boolean isHaveRecord(int maxBindNum, String playerName, int recordNum){
        boolean isNoKeywords = (
                !Objects.equals(playerName, "bindonpickup") &&
                        !Objects.equals(playerName, "bindonuse") &&
                        !Objects.equals(playerName, "bindonequip") &&
                        !Objects.equals(playerName, "none"));
        boolean isHaveBind = maxBindNum>0;
        boolean isHaveRecord = recordNum>0;

        return (isNoKeywords && isHaveRecord) || isHaveBind;
    }

    private static boolean unbind(int enchantLvl, ItemStack item){
        DBOperator database = BindItem.getEnchantLvlDatabase();

        List<String> columns = new ArrayList<>(), values = new ArrayList<>();
        columns.add("enchantLvl");
        columns.add("playerName");
        columns.add("bindNum");

        Map<Integer, List<Object>> bindInfo = database.select(
                BindItem.getTableName(),
                "playerName, bindNum",
                "enchantLvl="+enchantLvl,
                2);

        values.add(String.valueOf(enchantLvl));
        values.add("\""+ bindInfo.get(1).get(0) +"\"");

        int bindNum = (int) bindInfo.get(2).get(0)-1;
        values.add(String.valueOf(Math.max(bindNum, 0)));

        database.update(BindItem.getTableName(), columns, values, "enchantLvl="+enchantLvl);

        item.removeEnchantment(Enchantment.getByName("bind_item"));

        return true;
    }

    private static String nameEncode(String name){
        try {
            return URLEncoder.encode(name, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return name;
        }
    }

    private static String nameDecode(String name){
        try {
            return URLDecoder.decode(name, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return name;
        }
    }

    private static int getAvailableLvl(DBOperator database){
        for (int i = 11;i<Short.MAX_VALUE;i++){
            if (database.select(
                    BindItem.getTableName(),
                    "enchantLvl",
                    "enchantLvl="+i,
                    1)
                    .get(1).size()<1){
                return i;
            }
        }
        return Short.MAX_VALUE;
    }
    
    public static List<String> bindType(ItemStack item){
        List<String> type = new ArrayList<>();
        Map<Enchantment, Integer> enchants = item.getEnchantments();
        DBOperator database = BindItem.getEnchantLvlDatabase();

        if (enchants.get(Enchantment.getByName("bind_item")) != null) {
            int enchantLvl = enchants.get(Enchantment.getByName("bind_item"));

            List<Object> nameList = database.select(BindItem.getTableName(), "playerName", "enchantLvl=" + enchantLvl, 1).get(1);
            if(enchantLvl > 10 && nameList.size()==0){
                type.add("needRemove");
                type.add("none");
            }else if (enchantLvl > 10){
                type.add("bindToPlayer");
                type.add(nameDecode(nameList.get(0).toString()));
            } else {
                switch (enchantLvl){
                    case 1:
                        type.add("bindOnPickup");
                        break;
                    case 2:
                        type.add("bindOnUse");
                        break;
                    case 3:
                        type.add("bindOnEquip");
                        break;
                    default:
                        type.add("needRemove");
                        break;
                }
                type.add("none");
            }
        } else {
            type.add("none");
            type.add("none");
        }
        return type;
    }
}
