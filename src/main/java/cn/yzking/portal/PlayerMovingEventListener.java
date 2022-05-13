package cn.yzking.portal;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerMoveEvent;

/**
 * Created by Yanziqing25
 */
public class PlayerMovingEventListener implements Listener {
    private final PortalMain plugin;

    public PlayerMovingEventListener(PortalMain plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        for (Portal portal : plugin.portals) {
            if (portal.inside(event.getTo())) {
                portal.teleport(event.getPlayer());
                break;
            }
        }
    }
}
