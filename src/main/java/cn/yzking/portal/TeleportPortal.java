package cn.yzking.portal;

import cn.nukkit.Player;
import cn.nukkit.level.Position;

public class TeleportPortal extends Portal {
    private final Position target;

    public TeleportPortal(String name, Position p1, Position p2, Position target) {
        super(name, p1, p2);
        this.target = target;
    }

    /**
     * 获取传送目标地点
     *
     * @return 目标地点的Position
     */
    public Position getTarget() {
        return this.target;
    }

    @Override
    public boolean teleport(Player player) {
        return player.teleport(this.target);
    }
}