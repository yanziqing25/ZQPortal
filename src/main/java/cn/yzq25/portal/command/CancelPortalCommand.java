package cn.yzq25.portal.command;

import cn.nukkit.command.*;
import cn.nukkit.utils.TextFormat;
import cn.yzq25.portal.PortalMain;

import java.util.HashMap;

/**
 * Created by Yanziqing25
 */
public class CancelPortalCommand extends PluginCommand<PortalMain> implements CommandExecutor {

    public CancelPortalCommand() {
        super("cancelportal", PortalMain.getInstance());
        this.setExecutor(this);
        this.setCommandParameters(new HashMap());
        this.setAliases(new String[]{"cp", "取消设置传送门"});
        this.setPermission("portal.command.cancelportal");
        this.setDescription("取消传送门设置模式");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 0) {
            return false;
        }
        if (getPlugin().settingStatus != 0) {
            getPlugin().settingStatus = 0;
            getPlugin().portalName = null;
            getPlugin().setter = null;
            getPlugin().type = 0;
            getPlugin().address = null;
            sender.sendMessage(TextFormat.GREEN + "取消设置模式成功!");
            return true;
        } else {
            sender.sendMessage(TextFormat.RED + "你不在设置模式中,无需取消!");
            return true;
        }
    }
}
