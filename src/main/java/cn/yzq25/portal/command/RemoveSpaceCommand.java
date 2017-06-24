package cn.yzq25.portal.command;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandExecutor;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginCommand;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.utils.TextFormat;
import cn.yzq25.portal.PortalMain;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Yanziqing25
 */
public class RemoveSpaceCommand extends PluginCommand<PortalMain> implements CommandExecutor {
    private PortalMain portal;

    public RemoveSpaceCommand() {
        super("removespace", PortalMain.getInstance());
        this.setExecutor(this);
        Map<String, CommandParameter[]> commandParameters = new LinkedHashMap<>();
        commandParameters.put("default", new CommandParameter[]{new CommandParameter("name", false)});
        this.setCommandParameters(commandParameters);
        this.setAliases(new String[]{"rs"});
        this.setPermission("portal.command.removespace");
        this.setDescription("移除一个传送舱");
        this.portal = PortalMain.getInstance();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!this.testPermission(sender)) {
            return false;
        }
        if (!portal.spacesConfig.exists(args[0])) {
            sender.sendMessage(TextFormat.RED + "传送舱[" + args[0] + "]不存在!");
            return true;
        }
        if (args.length != 1) {
            return false;
        }
        if (portal.removeSpace(args[0])) {
            sender.sendMessage(TextFormat.DARK_GREEN + "传送舱[" + args[0] + "]移除成功!");
            return true;
        } else {
            sender.sendMessage(TextFormat.RED + "传送舱[" + args[0] + "]移除失败!");
            return true;
        }
    }
}