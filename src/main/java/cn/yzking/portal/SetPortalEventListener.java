package cn.yzking.portal;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.level.Position;
import cn.nukkit.utils.TextFormat;

import java.util.Map;

/**
 * Created by Yanziqing25
 */
public class SetPortalEventListener implements Listener {
    private final PortalMain plugin;

    public SetPortalEventListener(PortalMain plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = false)
    public void onSettingPortal(PlayerInteractEvent event) {
        if (event.getAction() == PlayerInteractEvent.Action.LEFT_CLICK_AIR || event.getAction() == PlayerInteractEvent.Action.RIGHT_CLICK_AIR)
            return;
        Player player = event.getPlayer();
        String xuid = player.getLoginChainData().getXUID();
        if (!plugin.setter.containsKey(xuid)) return;
        Map<String, String> info = plugin.setter.get(xuid);
        String step = info.get("step");
        switch (step) {
            case "1" -> {
                Position p = event.getBlock();
                //设置方块
                info.put("w", p.getLevel().getName().toLowerCase());
                info.put("x1", String.valueOf(p.getFloorX()));
                info.put("y1", String.valueOf(p.getFloorY()));
                info.put("z1", String.valueOf(p.getFloorZ()));
                info.put("step", "2");
                player.sendTip(TextFormat.BLUE + "请点击方块2");
            }
            case "2" -> {
                Position p = event.getBlock();
                int x1 = Integer.parseInt(info.get("x1"));
                int y1 = Integer.parseInt(info.get("y1"));
                int z1 = Integer.parseInt(info.get("z1"));

                int x2 = p.getFloorX();
                int y2 = p.getFloorY();
                int z2 = p.getFloorZ();

                //交换数据,要求方块2数据 >= 方块1
                if (x1 > x2) {
                    x1 = x1 ^ x2;
                    x2 = x1 ^ x2;
                    x1 = x1 ^ x2;
                }
                if (y1 > y2) {
                    x1 = x1 ^ x2;
                    x2 = x1 ^ x2;
                    x1 = x1 ^ x2;
                }
                if (z1 > z2) {
                    x1 = x1 ^ x2;
                    x2 = x1 ^ x2;
                    x1 = x1 ^ x2;
                }

                if (y2 - y1 <= 1) {
                    player.sendTip(TextFormat.RED + "传送门高度至少为1!");
                    info.put("step", "2");
                    return;
                }
                if (x2 - x1 <= 1 && z2 - z1 <= 1) {
                    player.sendTip(TextFormat.RED + "传送门宽度至少为1!");
                    info.put("step", "2");
                    return;
                }

                String type = info.get("type");
                if (type.equals("1")) {
                    info.put("x1", String.valueOf(x1));
                    info.put("y1", String.valueOf(y1));
                    info.put("z1", String.valueOf(z1));

                    info.put("x2", String.valueOf(x2));
                    info.put("y2", String.valueOf(y2));
                    info.put("z2", String.valueOf(z2));
                    info.put("step", "3");
                    player.sendTip(TextFormat.GREEN + "请点击目标地点");
                } else if (type.equals("2")) {
                    String name = info.get("name");
                    String w = info.get("w");
                    Position p1 = new Position(x1, y1, z1, plugin.getServer().getLevelByName(w));
                    Position p2 = new Position(x2, y2, z2, plugin.getServer().getLevelByName(w));
                    String address = info.get("address");
                    int port = Integer.parseInt(info.get("port"));
                    if (plugin.addTransferPortal(name, p1, p2, address, port)) {
                        plugin.savePortalConfig();
                        player.sendTip(TextFormat.DARK_GREEN + "传送门[" + name + "]设置成功!");
                    } else {
                        player.sendTip(TextFormat.RED + "传送门[" + name + "]设置失败!");
                    }
                    plugin.setter.remove(player.getLoginChainData().getXUID());
                }
            }
            case "3" -> {
                Position target = event.getBlock();
                target.y += 1;
                String name = info.get("name");
                String w = info.get("w");
                Position p1 = new Position(Double.parseDouble(info.get("x1")), Double.parseDouble(info.get("y1")), Double.parseDouble(info.get("z1")), plugin.getServer().getLevelByName(w));
                Position p2 = new Position(Double.parseDouble(info.get("x2")), Double.parseDouble(info.get("y2")), Double.parseDouble(info.get("z2")), plugin.getServer().getLevelByName(w));
                if (plugin.addTeleportPortal(name, p1, p2, target)) {
                    plugin.savePortalConfig();
                    player.sendTip(TextFormat.DARK_GREEN + "传送门[" + name + "]设置成功!");
                } else {
                    player.sendTip(TextFormat.RED + "传送门[" + name + "]设置失败!");
                }
                plugin.setter.remove(player.getLoginChainData().getXUID());
            }
        }
    }
}
