package cn.yzking.portal.command;

import cn.nukkit.Player;
import cn.nukkit.command.*;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.utils.TextFormat;
import cn.yzking.portal.PortalMain;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yanziqing25
 */
public class SetPortalCommand extends PluginCommand<PortalMain> implements CommandExecutor {

    public SetPortalCommand() {
        super("setportal", PortalMain.getInstance());
        this.setExecutor(this);
        CommandParameter[] commandParameters = {CommandParameter.newType("传送门名称", CommandParamType.STRING), CommandParameter.newType("IP地址", true, CommandParamType.STRING), CommandParameter.newType("端口", true, CommandParamType.INT)};
        this.addCommandParameters("default", commandParameters);
        this.setAliases(new String[]{"sp", "添加传送门"});
        this.setPermission("portal.command.setportal");
        this.setDescription("进入传送门设置模式");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage(TextFormat.RED + "请在游戏中使用此命令！");
            return false;
        }
        Player player = (Player) sender;
        String xuid = player.getLoginChainData().getXUID();
        if (getPlugin().setter.containsKey(xuid)) {
            player.sendMessage(TextFormat.RED + "你已处于设置模式下，如需中途取消请输入命令\"/cancelportal\"或\"/cp\"");
            return true;
        }
        if (getPlugin().isPortalExists(args[0])) {
            player.sendTip(TextFormat.RED + "传送门[" + args[0] + "]已存在!");
            return true;
        }
        Map<String, String> info = new HashMap<>();
        info.put("name", args[0]);
        if (args.length == 1) info.put("type", "1");
        else if (args.length == 2) {
            info.put("type", "2");
            info.put("address", args[1]);
            info.put("port", "19132");
        } else if (args.length == 3) {
            info.put("type", "2");
            info.put("address", args[1]);
            info.put("port", args[2]);
        } else return false;
        info.put("step", "1");
        getPlugin().setter.put(xuid, info);
        player.sendTip(TextFormat.GREEN + "请点击方块1");
        return true;
    }
}
