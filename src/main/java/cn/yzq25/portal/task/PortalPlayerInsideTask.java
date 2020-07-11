package cn.yzq25.portal.task;

import cn.nukkit.scheduler.PluginTask;
import cn.yzq25.portal.PortalMain;

/**
 * Created by Yanziqing25
 */
public class PortalPlayerInsideTask extends PluginTask<PortalMain> {

    public PortalPlayerInsideTask(PortalMain plugin) {
        super(plugin);
    }

    @Override
    public void onRun(int currentTick) {
        if (getOwner().portalsMap.isEmpty()) {
            return;
        }

        getOwner().portalsMap.forEach((name, portal) -> getOwner().getServer().getOnlinePlayers().forEach((uuid, player) -> {
            if (portal.inside(player.getPosition())) {
                portal.teleport(player);
            }
        }));
    }
}
