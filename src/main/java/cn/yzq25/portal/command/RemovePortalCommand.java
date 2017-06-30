package cn.yzq25.portal.command;

import cn.nukkit.command.*;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.utils.TextFormat;
import cn.yzq25.portal.PortalMain;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Yanziqing25
 */
public class RemovePortalCommand extends PluginCommand<PortalMain> implements CommandExecutor {
    private PortalMain portal;

    public RemovePortalCommand() {
        super("removeportal", PortalMain.getInstance());
        this.setExecutor(this);
        Map<String, CommandParameter[]> commandParameters = new LinkedHashMap<>();
        commandParameters.put("default", new CommandParameter[]{new CommandParameter("name", false)});
        this.setCommandParameters(commandParameters);
        this.setAliases(new String[]{"rp"});
        this.setPermission("portal.command.removeportal");
        this.setDescription("移除一个传送门");
        this.portal = PortalMain.getInstance();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!this.testPermission(sender)) {
            return false;
        }
        if (args.length != 1) {
            return false;
        }
        if (!portal.portalsConfig.exists(args[0])) {
            sender.sendMessage(TextFormat.RED + "传送门[" + args[0] + "]不存在!");
            return true;
        }
        if (portal.removePortal(args[0])) {
            sender.sendMessage(TextFormat.DARK_GREEN + "传送门[" + args[0] + "]移除成功!");
            return true;
        } else {
            sender.sendMessage(TextFormat.RED + "传送门[" + args[0] + "]移除失败!");
            return true;
        }
    }
}
