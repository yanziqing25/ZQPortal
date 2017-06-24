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

        portalMain.getServer().getOnlinePlayers().forEach((UUID, Player) -> {
            portalMain.portalsMap.forEach((name, portal) -> {
                if (portal.inside(Player.getPosition())) {
                    portal.teleport(Player);
                }
            });
        });
    }
}
