/*
 * Copyright (c) 2020 by Golden_Water. All Right Reserved.
 * ProjectName: BindItem
 * FileName: BindItemExecutor.java
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

package indi.goldenwater.binditem.command;

import indi.goldenwater.binditem.BindItem;
import indi.goldenwater.binditem.listener.OnItemEvents;
import indi.goldenwater.binditem.module.CheckPermissions;
import indi.goldenwater.binditem.module.ItemBindAndUnbind;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

import static org.bukkit.Bukkit.getServer;

public class BindItemExecutor {
    public static void registerCommandBindItem(BindItem plugin){
        plugin.getServer().getPluginCommand("binditem").setExecutor((commandSender, command, s, args) -> { // CommandSender commandSender, Command command, String s, String[] args
            if ((commandSender instanceof Player)){
                return executorCommand(commandSender, args);
            } else {
                commandSender.sendMessage("该命令必须由玩家使用.");
                return true;
            }
        });
    }

    public static boolean executorCommand(CommandSender sender, String[] args){
        switch (args.length){
            case 0:
                if(CheckPermissions.hasPermission_Tips(sender,"binditem.commands.help")){
                    sendHelpMessage(sender, HelpMessageType.pluginInfo);
                }
                return true;
            case 1:
                switch (args[0]){
                    case "help":
                        if(CheckPermissions.hasPermission_Tips(sender,"binditem.commands.help")){
                            sendHelpMessage(sender, HelpMessageType.fullHelp);
                        }
                        return true;
                    case "bind":
                        if(CheckPermissions.hasPermission_Tips(sender,"binditem.commands.bind.self")){
                            Player player = (Player)sender;
                            ItemStack item = player.getItemInHand();
                            if(item.getType() != Material.AIR){
                                ItemBindAndUnbind.bindItem(player, item);
//                                System.out.println(Arrays.toString(ItemBindAndUnbind.bindType(item).toArray()));
                            } else {
                                sender.sendMessage("§c不能绑定空气");
                            }
                        }
                        return true;
                    case "unbind":
                        if(CheckPermissions.hasPermission_Tips(sender,"binditem.commands.unbind")){
                            Player player = (Player)sender;
                            ItemStack item = player.getItemInHand();
                            if(item.getType() != Material.AIR){
//                                sender.sendMessage(String.valueOf(item.getEnchantmentLevel(Enchantment.getByName("bind_item"))));
                                ItemBindAndUnbind.unBindItem(player, item);
//                                sender.sendMessage(String.valueOf(item.getEnchantmentLevel(Enchantment.getByName("bind_item"))));
                            } else {
                                sender.sendMessage("§c不能解绑空气");
                            }
                        }
                        return true;
                    case "bindonpickup":
                        if(CheckPermissions.hasPermission_Tips(sender,"binditem.commands.bindonpickup")){
                            sender.sendMessage("Unfinished.");
                        }
                        return true;
                    case "bindonuse":
                        if(CheckPermissions.hasPermission_Tips(sender,"binditem.commands.bindonuse")){
                            sender.sendMessage("Unfinished.");
                        }
                        return true;
                    case "bindonequip":
                        if(CheckPermissions.hasPermission_Tips(sender,"binditem.commands.bindonequip")){
                            sender.sendMessage("Unfinished.");
                        }
                        return true;
                    case "reload":
                        if(CheckPermissions.hasPermission_Tips(sender,"binditem.commands.reload")){
                            BindItem.getInstance().reloadConfig();
                            sender.sendMessage("Configuration reload finished.");
                        }
                        return true;
                }
                break;
            case 2:
                switch (args[0]){
                    case "help":
                        switch (args[1]){
                            case "help":
                                sendHelpMessage(sender, HelpMessageType.help);
                                return true;
                            case "bind":
                                sendHelpMessage(sender, HelpMessageType.bind);
                                return true;
                            case "unbind":
                                sendHelpMessage(sender, HelpMessageType.unbind);
                                return true;
                            case "bindonpickup":
                                sendHelpMessage(sender, HelpMessageType.bindonpickup);
                                return true;
                            case "bindonuse":
                                sendHelpMessage(sender, HelpMessageType.bindonuse);
                                return true;
                            case "bindonequip":
                                sendHelpMessage(sender, HelpMessageType.bindonequip);
                                return true;
                        }
                        break;
                    case "bind":
                        if(CheckPermissions.hasPermission_Tips(sender,"binditem.commands.bind.other")){
                            Player player = null;
                            for (Player i : getServer().getOnlinePlayers()){
                                if (i.getName().equals(args[1])){
                                    player = i;
                                    break;
                                }
                            }

                            if (player==null){
                                sender.sendMessage("§c玩家不在线.");
                                return true;
                            }

                            ItemStack item = ((Player) sender).getItemInHand();
                            if(item.getType() != Material.AIR){
                                ItemBindAndUnbind.bindItem(player, item);
//                                System.out.println(Arrays.toString(ItemBindAndUnbind.bindType(item).toArray()));
                            } else {
                                sender.sendMessage("§c不能绑定空气.");
                            }
                        }
                        return true;
                }
                break;
        }
        return false;
    }

    public static void sendHelpMessage(CommandSender sender, int helpType){
        Map<String,String> helpMessages = new HashMap<>();
        helpMessages.put("pluginSignature", "BindItem by§7.§eGolden§7_§bWater§r");
        helpMessages.put("commandAlias", "指令别名§7:§d/bi§7, §d/bind§7, §d/bd§r");
        helpMessages.put("commandHelp_simple", "§d/binditem§b help§r 获取帮助信息");
        helpMessages.put("commandBind_simple", "§d/binditem§b bind§r 绑定手上的物品");
        helpMessages.put("commandUnbind_simple", "§d/binditem§b unbind§r 解绑手上的物品");
        helpMessages.put("commandBindOnPickUp_simple", "§d/binditem§b bindonpickup§r 将手上物品设定为捡起后绑定");
        helpMessages.put("commandBindOnUse_simple", "§d/binditem§b bindonuse§r 将手上物品设定为使用后绑定");
        helpMessages.put("commandBindOnEquip_simple", "§d/binditem§b bindonequip§r 将手上物品设定为装备后绑定");
        helpMessages.put("commandHelp_detail", "§d/binditem§b help [command]§r \n" +
                "获取帮助信息, 如果填写了指令则显示那个指令的详细信息\n" +
                "需要权限§7:§r binditem.commands.help");
        helpMessages.put("commandBind_detail", "§d/binditem§b bind [player]§r \n" +
                "绑定手上的物品, 如果填写了玩家名则绑定到那个玩家\n" +
                "绑定手上物品需要权限§7:§r binditem.commands.bind.self\n" +
                "绑定到其他玩家需要权限§7:§r bind.commands.bind.other");
        helpMessages.put("commandUnbind_detail", "§d/binditem§b unbind§r \n" +
                "获取帮助信息, 如果填写了指令则显示那个指令的详细信息\n" +
                "需要权限§7:§r binditem.commands.unbind\n" +
                "解绑他人物品需要权限§7:§r binditem.bypass.unbindlimit");
        helpMessages.put("commandBindOnPickUp_detail", "§d/binditem§b bindonpickup§r \n" +
                "将手上物品设定为捡起后绑定\n" +
                "需要权限§7:§r binditem.commands.bindonpickup");
        helpMessages.put("commandBindOnUse_detail", "§d/binditem§b bindonuse§r \n" +
                "将手上物品设定为使用后绑定\n" +
                "需要权限§7:§r binditem.commands.bindonuse");
        helpMessages.put("commandBindOnEquip_detail", "§d/binditem§b bindonequip§r \n" +
                "将手上物品设定为装备后绑定\n" +
                "需要权限§7:§r binditem.commands.bindonequip");

        switch (helpType){
            case HelpMessageType.pluginInfo:
                sender.sendMessage(helpMessages.get("pluginSignature"));
                sender.sendMessage(helpMessages.get("commandAlias"));
                sender.sendMessage(helpMessages.get("commandHelp_simple"));
                break;
            case HelpMessageType.fullHelp:
                sender.sendMessage(helpMessages.get("pluginSignature"));
                sender.sendMessage(helpMessages.get("commandAlias"));
                sender.sendMessage(helpMessages.get("commandHelp_simple"));
                sender.sendMessage(helpMessages.get("commandBind_simple"));
                sender.sendMessage(helpMessages.get("commandUnbind_simple"));
                sender.sendMessage(helpMessages.get("commandBindOnPickUp_simple"));
                sender.sendMessage(helpMessages.get("commandBindOnUse_simple"));
                sender.sendMessage(helpMessages.get("commandBindOnEquip_simple"));
                break;
            case HelpMessageType.help:
                sender.sendMessage(helpMessages.get("pluginSignature"));
                sender.sendMessage(helpMessages.get("commandHelp_detail"));
                break;
            case HelpMessageType.bind:
                sender.sendMessage(helpMessages.get("pluginSignature"));
                sender.sendMessage(helpMessages.get("commandBind_detail"));
                break;
            case HelpMessageType.unbind:
                sender.sendMessage(helpMessages.get("pluginSignature"));
                sender.sendMessage(helpMessages.get("commandUnbind_detail"));
                break;
            case HelpMessageType.bindonpickup:
                sender.sendMessage(helpMessages.get("pluginSignature"));
                sender.sendMessage(helpMessages.get("commandBindOnPickUp_detail"));
                break;
            case HelpMessageType.bindonuse:
                sender.sendMessage(helpMessages.get("pluginSignature"));
                sender.sendMessage(helpMessages.get("commandBindOnUse_detail"));
                break;
            case HelpMessageType.bindonequip:
                sender.sendMessage(helpMessages.get("pluginSignature"));
                sender.sendMessage(helpMessages.get("commandBindOnEquip_detail"));
                break;
        }
    }

    public static class HelpMessageType {
        public static final int pluginInfo = 0;
        public static final int fullHelp = 1;
        public static final int help = 2;
        public static final int bind = 3;
        public static final int unbind = 4;
        public static final int bindonpickup = 5;
        public static final int bindonuse = 6;
        public static final int bindonequip = 7;
    }
}
