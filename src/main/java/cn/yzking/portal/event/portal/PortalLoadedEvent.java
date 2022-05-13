package cn.yzking.portal.event.portal;

import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.yzking.portal.Portal;
import cn.yzking.portal.event.PortalPluginEvent;

/**
 * Created by Yanziqing25
 */
public class PortalLoadedEvent extends PortalPluginEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final Portal portal;

    public PortalLoadedEvent(Portal portal) {
        this.portal = portal;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }

    public Portal getPortal() {
        return portal;
    }
}
