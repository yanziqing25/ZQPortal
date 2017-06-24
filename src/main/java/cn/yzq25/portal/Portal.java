package cn.yzq25.portal;

import cn.nukkit.Player;
import cn.nukkit.level.Position;

/**
 * Portal类
 *
 * @author Yanziqing25
 */
public abstract class Portal {
    protected String name;
    protected Position p1;
    protected Position p2;
    protected Position target;
    protected int height;
    protected int volume;

    public Portal(String name, Position p1, Position p2, Position target) {
        this.name = name;
        this.p1 = p1;
        this.p2 = p2;
        this.target = target;
    }

    /**
     * 获取传送门名称
     *
     * @return 传送门的名称String
     */
    public String getName() {
        return this.name;
    }

    public boolean teleport(Player player) {
        return player.teleport(this.target);
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

    /**
     * 检测传送门是否已被加载
     *
     * @return boolean
     */
    public boolean isLoaded() {
        return PortalMain.getInstance().portalsMap.containsKey(this.name);
    }

    /**
     * 检测传送门是否已被加载
     *
     * @param name 传送门名称
     *
     * @return boolean
     */
    public static boolean isLoaded(String name) {
        return PortalMain.getInstance().portalsMap.containsKey(name);
    }

    /**
     * 获取传送门对象
     *
     * @param name 传送门名称
     *
     * @return Portal传送门的对象
     */
    public static Portal getPortal(String name) {
        if (!isLoaded(name)) {
            return null;
        }
        return PortalMain.getInstance().portalsMap.get(name);
    }

    public abstract boolean inside(Position playerPosition);
}
