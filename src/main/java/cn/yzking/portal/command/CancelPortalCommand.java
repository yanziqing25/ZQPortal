package cn.yzking.portal.command;

import cn.nukkit.Player;
import cn.nukkit.command.*;
import cn.nukkit.utils.TextFormat;
import cn.yzking.portal.PortalMain;

import java.util.HashMap;

/**
 * Created by Yanziqing25
 */
public class CancelPortalCommand extends PluginCommand<PortalMain> implements CommandExecutor {

    public CancelPortalCommand() {
        super("cancelportal", PortalMain.getInstance());
        this.setExecutor(this);
        this.setCommandParameters(new HashMap<>());
        this.setAliases(new String[]{"cp", "取消设置传送门"});
        this.setPermission("portal.command.cancelportal");
        this.setDescription("取消传送门设置模式");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage(TextFormat.RED + "请在游戏中使用此命令！");
            return false;
        }
        if (args.length != 0) {
            return false;
        }
        Player player = (Player) sender;
        String xuid = player.getLoginChainData().getXUID();
        if (getPlugin().setter.containsKey(xuid)) {
            getPlugin().setter.remove(xuid);
            player.sendTip(TextFormat.GREEN + "取消设置模式成功!");
        } else {
            player.sendTip(TextFormat.RED + "你不在设置模式中,无需取消!");
        }
        return true;
    }
}
