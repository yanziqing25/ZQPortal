package cn.yzq25.portal;

import cn.nukkit.Player;
import cn.nukkit.level.Position;

import java.net.InetSocketAddress;

public class TransferPortal extends Portal {
    private InetSocketAddress target;

    public TransferPortal(String name, Position p1, Position p2, InetSocketAddress target) {
        super(name, p1, p2);
        this.target = target;
    }

    /**
     * 获取传送目标地点
     *
     * @return 目标地点的Position
     */
    public InetSocketAddress getTarget() {
        return this.target;
    }

    @Override
    public boolean teleport(Player player) {
        player.teleport(player.getLevel().getSafeSpawn());

        player.transfer(target);
        return true;
    }
}
