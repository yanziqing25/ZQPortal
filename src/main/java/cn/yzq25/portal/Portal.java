package cn.yzq25.portal;

import cn.nukkit.Player;
import cn.nukkit.level.Position;

/**
 * Portal类
 *
 * @author Yanziqing25
 */
public class Portal {
    private String name;
    private Position p1;
    private Position p2;
    private Position target;
    private int height;
    private int volume;

    public Portal(String name, Position p1, Position p2, Position target) {
        this.name = name;
        this.p1 = p1;
        this.p2 = p2;
        this.target = target;
        this.height = this.p2.getFloorY() - this.p1.getFloorY() + 1;
        this.volume = (this.p2.getFloorX() - this.p1.getFloorX() + 1) * (this.p2.getFloorZ() - this.p1.getFloorZ() + 1) * this.height;
    }

    /**
     * 获取传送门名称
     *
     * @return 传送门的名称String
     */
    public String getName() {
        return this.name;
    }

    /**
     * 获取传送目标地点
     *
     * @return 目标地点的Position
     */
    public Position getTarget() {
        return this.target;
    }

    /**
     * 获取传送门高度
     *
     * @return 传送门的高度int
     */
    public int getHeight(){
        return this.height;
    }

    /**
     * 获取传送门体积
     *
     * @return 传送门的体积int
     */
    public int getVolume() {
        return this.volume;
    }

    public boolean teleport(Player player) {
        return player.teleport(this.target);
    }

    public boolean inside(Position playerPosition) {
        return playerPosition.getFloorY() >= this.p1.getFloorY() && playerPosition.getFloorY() < this.p2.getFloorY() &&
                playerPosition.getFloorX() >= this.p1.getFloorX() && playerPosition.getFloorX() <= this.p2.getFloorX() &&
                playerPosition.getFloorZ() >= this.p1.getFloorZ() && playerPosition.getFloorZ() <= this.p2.getFloorZ();
    }
}
