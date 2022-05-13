package cn.yzking.portal.task;

import cn.nukkit.Player;
import cn.nukkit.scheduler.PluginTask;
import cn.yzking.portal.Portal;
import cn.yzking.portal.PortalMain;

/**
 * Created by Yanziqing25
 */
public class CheckPlayerInsideTask extends PluginTask<PortalMain> {

    public CheckPlayerInsideTask(PortalMain plugin) {
        super(plugin);
    }

    @Override
    public void onRun(int currentTick) {
        for (Player player : getOwner().getServer().getOnlinePlayers().values()) {
            for (Portal portal : getOwner().portals) {
                if (portal.inside(player.getPosition())) {
                    portal.teleport(player);
                    break;
                }
            }
        }
    }
}
