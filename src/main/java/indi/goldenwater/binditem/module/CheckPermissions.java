/*
 * Copyright (c) 2020 by Golden_Water. All Right Reserved.
 * ProjectName: BindItem
 * FileName: CheckPermissions.java
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

package indi.goldenwater.binditem.module;

import org.bukkit.command.CommandSender;

public final class CheckPermissions {
    public static boolean hasPermission_Tips(CommandSender sender, String perm,String messageOnNoPerm){
        boolean hasPerm;
        hasPerm = sender.hasPermission(perm);
        if(!hasPerm){
            sender.sendMessage(messageOnNoPerm.replaceAll("%perm",perm));
        }
        return hasPerm;
    }
    public static boolean hasPermission_Tips(CommandSender sender, String perm){
        boolean hasPerm;
        hasPerm = sender.hasPermission(perm);
        if(!hasPerm){
            sender.sendMessage("§cYou don't have permission "+perm+".");
        }
        return hasPerm;
    }
}
