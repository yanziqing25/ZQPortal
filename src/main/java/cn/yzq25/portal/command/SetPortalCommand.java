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
public class SetPortalCommand extends PluginCommand<PortalMain> implements CommandExecutor {

    public SetPortalCommand() {
        super("setportal", PortalMain.getInstance());
        this.setExecutor(this);
        this.setCommandParameters(new LinkedHashMap<String, CommandParameter[]>(){{put("default", new CommandParameter[]{new CommandParameter("传送门名称", CommandParamType.STRING, false), new CommandParameter("IP地址:端口", CommandParamType.STRING, true)});}});
        this.setAliases(new String[]{"sp", "添加传送门"});
        this.setPermission("portal.command.setportal");
        this.setDescription("进入传送门设置模式");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage(TextFormat.RED + "请在游戏中使用此命令!");
            return false;
        }
        if (getPlugin().settingStatus != 0) {
            sender.sendMessage(TextFormat.RED + "你已处于设置模式下,如需中途取消请输入命令\"/cancelportal\"或\"/cp\"");
            return true;
        }
        if (args.length == 1) getPlugin().type = 1;
        else if (args.length == 2) {
            getPlugin().type = 2;
            getPlugin().address = args[1];
        }
        else return false;
        if (getPlugin().portalsConfig.exists(args[0])) {
            sender.sendMessage(TextFormat.RED + "传送门[" + args[0] + "]已存在!");
            return true;
        }
        getPlugin().portalName = args[0];
        getPlugin().settingStatus = 1;
        getPlugin().setter = sender.getName();
        sender.sendMessage(TextFormat.GREEN + "你已处于设置模式下!");
        sender.sendMessage("请点击方块1");
        return true;
    }
}
