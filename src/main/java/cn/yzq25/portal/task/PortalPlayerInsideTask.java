package cn.yzq25.portal.task;

import cn.nukkit.scheduler.PluginTask;
import cn.yzq25.portal.PortalMain;

/**
 * Created by Yanziqing25
 */
public class PortalPlayerInsideTask extends PluginTask<PortalMain> {
    private PortalMain portalMain;

    public PortalPlayerInsideTask(PortalMain plugin) {
        super(plugin);
        this.portalMain = plugin;
    }

    @Override
    public void onRun(int currentTick) {
        if (portalMain.portalsMap.isEmpty()) {
            return;
        }

        portalMain.portalsMap.forEach((name, portal) -> portalMain.getServer().getOnlinePlayers().forEach((uuid, player) -> {
            if (portal.inside(player.getPosition())) {
                portal.teleport(player);
            }
        }));
    }
}
