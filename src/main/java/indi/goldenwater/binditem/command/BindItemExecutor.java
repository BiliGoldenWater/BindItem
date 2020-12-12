package indi.goldenwater.binditem.command;

import indi.goldenwater.binditem.BindItem;
import indi.goldenwater.binditem.module.CheckPermissions;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;

public class BindItemExecutor {
    public static void registerCommandBindItem(BindItem plugin){
        plugin.getServer().getPluginCommand("binditem").setExecutor(new CommandExecutor() {
            @Override
            public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
                if ((commandSender instanceof Player)){
                    return executorCommand(commandSender, args);
                } else {
                    commandSender.sendMessage("该命令必须由玩家使用.");
                    return true;
                }
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
                            ItemStack itemStack = player.getItemInHand();
                            if(itemStack.getType() != Material.AIR){
                                itemStack.addEnchantment(Enchantment.getByName("bind_item"),-1);
                            } else {
                                sender.sendMessage("§c不能绑定空气");
                            }
//                            sender.sendMessage("Unfinished.");
                        }
                        return true;
                    case "unbind":
                        if(CheckPermissions.hasPermission_Tips(sender,"binditem.commands.unbind")){
                            Player player = (Player)sender;
                            ItemStack itemStack = player.getItemInHand();
                            if(itemStack.getType() != Material.AIR){
                                ItemMeta itemMeta = itemStack.getItemMeta();
                                Map<String,Object> data = itemMeta.serialize();
                                data.forEach((k,v)->{
                                    sender.sendMessage(k + ":" + v.toString());
                                });
                            } else {
                                sender.sendMessage("§c不能绑定空气");
                            }
//                            sender.sendMessage("Unfinished.");
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
                            sender.sendMessage("Unfinished.");
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
