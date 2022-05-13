package cn.yzking.portal.command;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandExecutor;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginCommand;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.utils.TextFormat;
import cn.yzking.portal.PortalMain;

/**
 * Created by Yanziqing25
 */
public class DeletePortalCommand extends PluginCommand<PortalMain> implements CommandExecutor {

    public DeletePortalCommand() {
        super("deleteportal", PortalMain.getInstance());
        this.setExecutor(this);
        CommandParameter[] commandParameters = {CommandParameter.newType("传送门名称", CommandParamType.STRING)};
        this.addCommandParameters("default", commandParameters);
        this.setAliases(new String[]{"dp", "删除传送门"});
        this.setPermission("portal.command.deleteportal");
        this.setDescription("删除一个传送门");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1) {
            return false;
        }
        if (!getPlugin().isPortalExists(args[0])) {
            sender.sendMessage(TextFormat.RED + "传送门[" + args[0] + "]不存在!");
            return true;
        }
        if (getPlugin().deletePortal(args[0])) {
            getPlugin().savePortalConfig();
            sender.sendMessage(TextFormat.DARK_GREEN + "传送门[" + args[0] + "]移除成功!");
        } else {
            sender.sendMessage(TextFormat.RED + "传送门[" + args[0] + "]移除失败!");
        }
        return true;
    }
}
