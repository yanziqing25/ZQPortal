package cn.yzking.portal;

import cn.nukkit.Player;
import cn.nukkit.level.Position;

/**
 * Portal类
 *
 * @author Yanziqing25
 */
public abstract class Portal {
    private final String name;
    private final Position p1;
    private final Position p2;
    private final int height;
    private final int volume;

    public Portal(String name, Position p1, Position p2) {
        this.name = name;
        this.p1 = p1;
        this.p2 = p2;
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
     * 传送门的第一个方块
     *
     * @return 传送门的第一个方块Position
     */
    public Position getPosition1() {
        return this.p1;
    }

    /**
     * 传送门的第二个方块
     *
     * @return 传送门的第二个方块Position
     */
    public Position getPosition2() {
        return this.p2;
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

    public boolean inside(Position playerPosition) {
        return playerPosition.getLevel().getName().equals(p1.getLevel().getName()) &&
                playerPosition.getFloorY() >= this.p1.getFloorY() && playerPosition.getFloorY() < this.p2.getFloorY() &&
                playerPosition.getFloorX() >= this.p1.getFloorX() && playerPosition.getFloorX() <= this.p2.getFloorX() &&
                playerPosition.getFloorZ() >= this.p1.getFloorZ() && playerPosition.getFloorZ() <= this.p2.getFloorZ();
    }

    public abstract boolean teleport(Player player);
}