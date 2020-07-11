package cn.yzq25.portal.command;

import cn.nukkit.command.*;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.utils.TextFormat;
import cn.yzq25.portal.PortalMain;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Yanziqing25
 */
public class RemovePortalCommand extends PluginCommand<PortalMain> implements CommandExecutor {

    public RemovePortalCommand() {
        super("removeportal", PortalMain.getInstance());
        this.setExecutor(this);
        Map<String, CommandParameter[]> commandParameters = new LinkedHashMap<>();
        this.setCommandParameters(new LinkedHashMap<String, CommandParameter[]>(){{put("default", new CommandParameter[]{new CommandParameter("传送门名称", CommandParamType.STRING, false)});}});
        this.setCommandParameters(commandParameters);
        this.setAliases(new String[]{"rp", "移除传送门"});
        this.setPermission("portal.command.removeportal");
        this.setDescription("移除一个传送门");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1) {
            return false;
        }
        if (!getPlugin().portalsConfig.exists(args[0])) {
            sender.sendMessage(TextFormat.RED + "传送门[" + args[0] + "]不存在!");
            return true;
        }
        if (getPlugin().removePortal(args[0])) {
            sender.sendMessage(TextFormat.DARK_GREEN + "传送门[" + args[0] + "]移除成功!");
            return true;
        } else {
            sender.sendMessage(TextFormat.RED + "传送门[" + args[0] + "]移除失败!");
            return true;
        }
    }
}
