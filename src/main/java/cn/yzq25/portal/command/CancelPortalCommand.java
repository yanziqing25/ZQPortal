package cn.yzq25.portal.command;

import cn.nukkit.command.*;
import cn.nukkit.utils.TextFormat;
import cn.yzq25.portal.PortalMain;

/**
 * Created by Yanziqing25
 */
public class CancelPortalCommand extends PluginCommand<PortalMain> implements CommandExecutor {
    private PortalMain portal;

    public CancelPortalCommand() {
        super("cancelportal", PortalMain.getInstance());
        this.setExecutor(this);
        this.setAliases(new String[]{"cp"});
        this.setPermission("portal.command.cancelportal");
        this.setDescription("取消传送门设置模式");

        this.portal = PortalMain.getInstance();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (portal.settingStatus != 0) {
            portal.settingStatus = 0;
            sender.sendMessage(TextFormat.GREEN + "取消设置模式成功!");
            return true;
        } else {
            sender.sendMessage(TextFormat.RED + "你不在设置模式中,无需取消!");
            return true;
        }
    }
}
