package cn.yzq25.portal;

import cn.nukkit.level.Position;

/**
 * Created by Yanziqing25
 */
public class XPortal extends Portal {
    private int width;

    public XPortal(String name, Position p1, Position p2, Position target) {
        super(name, p1, p2, target);
        this.width = this.p2.getFloorX() - this.p1.getFloorX() + 1;
        this.height = this.p2.getFloorY() - this.p1.getFloorY() + 1;
        this.volume = this.width * this.height;
    }

    @Override
    public boolean inside(Position playerPosition) {
        if (playerPosition.getFloorY() < this.p1.getFloorY() || playerPosition.getFloorY() >= this.p2.getFloorY()) {
            return false;
        }
        if (playerPosition.getFloorX() <= this.p1.getFloorX() || playerPosition.getFloorX() >= this.p2.getFloorX()) {
            return false;
        }
        if (playerPosition.getFloorZ() == this.p1.getFloorZ()) {
            return true;
        }
        return false;
    }
}
