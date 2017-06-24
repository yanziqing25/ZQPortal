package cn.yzq25.portal;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerMoveEvent;
import cn.nukkit.level.Level;
import cn.nukkit.utils.TextFormat;

/**
 * Created by Yanziqing25
 */
public class PortalEventListener implements Listener {
    private PortalMain mainclass;

    private int x1 = 0;
    private int y1 = 0;//高度
    private int z1 = 0;
    private int x2 = 0;
    private int y2 = 0;//高度
    private int z2 = 0;
    private Level portalWorld;

    private int tx = 0;
    private int ty = 0;
    private int tz = 0;
    private Level targetWorld;

    private int t = 0;//临时数据

    public PortalEventListener(PortalMain mainclass) {
        this.mainclass = mainclass;
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = false)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.isCancelled()) {
            return;
        }
        Player player = event.getPlayer();
        if (!player.getName().equals(mainclass.setter)) {
            return;
        }
        switch (mainclass.settingStatus) {
            case 0:
                break;
            case 1:
                x1 = event.getBlock().getFloorX();
                y1 = event.getBlock().getFloorY();
                z1 = event.getBlock().getFloorZ();
                mainclass.settingStatus = 2;
                player.sendMessage(TextFormat.BLUE + "请点击方块2");
                return;
            case 2:
                x2 = event.getBlock().getFloorX();
                y2 = event.getBlock().getFloorY();
                z2 = event.getBlock().getFloorZ();
                portalWorld = event.getBlock().getLevel();

                //交换数据,要求方块2数据 >= 方块1
                if (x1 > x2) {
                    t = x1;
                    x1 = x2;
                    x2 = t;
                }
                if (y1 > y2) {
                    t = y1;
                    y1 = y2;
                    y2 = t;
                }
                if (z1 > z2) {
                    t = z1;
                    z1 = z2;
                    z2 = t;
                }

                if (y2 - y1 <= 1) {
                    player.sendMessage(TextFormat.RED + "传送门高度至少为1!");
                    mainclass.settingStatus = 2;
                    return;
                }

                if (x2 - x1 <= 1 && z2 - z1 <= 1) {
                    player.sendMessage(TextFormat.RED + "传送门宽度至少为1!");
                    mainclass.settingStatus = 2;
                    return;
                }
                mainclass.settingStatus = 3;
                player.sendMessage(TextFormat.GREEN + "请点击目标地点");
                return;
            case 3:
                tx = event.getBlock().getFloorX();
                ty = event.getBlock().getFloorY();
                tz = event.getBlock().getFloorZ();
                targetWorld = event.getBlock().getLevel();
                String portalName = mainclass.portalName;

                if ((x2 - x1 > 1 && z2 == z1) || (z2 - z1 > 1 && x2 == x1)) {
                    if (mainclass.addPortal(portalName, x1, y1, z1, x2, y2, z2, portalWorld, tx, ty, tz, targetWorld)) {
                        player.sendMessage(TextFormat.DARK_GREEN + "传送门[" + portalName + "]设置成功!");
                    } else {
                        player.sendMessage(TextFormat.RED + "传送门[" + portalName + "]设置失败!");
                    }
                }

                if (x2 - x1 > 1 && z2 - z1 > 1) {
                    if (mainclass.addSpace(portalName, x1, y1, z1, x2, y2, z2, portalWorld, tx, ty, tz, targetWorld)) {
                        player.sendMessage(TextFormat.DARK_GREEN + "传送舱[" + portalName + "]设置成功!");
                    } else {
                        player.sendMessage(TextFormat.RED + "传送舱[" + portalName + "]设置失败!");
                    }
                }
                mainclass.settingStatus = 0;
                mainclass.portalName = null;
                mainclass.setter = null;
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = false)
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.isCancelled() || mainclass.portalsMap.isEmpty()) {
            return;
        }
        Player player = event.getPlayer();
        mainclass.portalsMap.forEach((name, portal) -> {
            if (portal.inside(event.getTo())) {
                portal.teleport(player);
            }
        });
    }
}
