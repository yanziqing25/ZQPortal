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
public class SetPortalCommand extends PluginCommand<PortalMain> implements CommandExecutor {
    private PortalMain portal;

    public SetPortalCommand() {
        super("setportal", PortalMain.getInstance());
        this.setExecutor(this);
        Map<String, CommandParameter[]> commandParameters = new LinkedHashMap<>();
        commandParameters.put("default", new CommandParameter[]{new CommandParameter("name", false)});
        this.setCommandParameters(commandParameters);
        this.setAliases(new String[]{"sp"});
        this.setPermission("portal.command.setportal");
        this.setDescription("进入传送门设置模式");
        this.portal = PortalMain.getInstance();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage(TextFormat.RED + "请在游戏中使用此命令!");
            return false;
        }
        if (args.length != 1) {
            return false;
        }
        if (portal.portalsConfig.exists(args[0])) {
            sender.sendMessage(TextFormat.RED + "传送门[" + args[0] + "]已存在!");
            return true;
        }
        if (portal.settingStatus != 0) {
            sender.sendMessage(TextFormat.RED + "你已处于设置模式下,如需中途取消请输入命令\"/cancelportal\"");
            return true;
        }
        portal.portalName = args[0];
        portal.settingStatus = 1;
        portal.setter = sender.getName();
        sender.sendMessage(TextFormat.GREEN + "你已处于设置模式下!");
        sender.sendMessage("请点击方块1");
        return true;
    }
}
