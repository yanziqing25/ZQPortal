package cn.yzking.portal;

import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;
import cn.yzking.portal.task.CheckPlayerInsideTask;
import cn.yzking.utils.ZQUtils;
import cn.yzking.portal.command.CancelPortalCommand;
import cn.yzking.portal.command.DeletePortalCommand;
import cn.yzking.portal.command.SetPortalCommand;
import cn.yzking.portal.event.portal.PortalLoadedEvent;

import java.net.InetSocketAddress;
import java.util.*;

/**
 * ZQPortal插件主类
 *
 * @author Yanziqing25
 */
public class PortalMain extends PluginBase {
    private static PortalMain instance;
    private Config portalsConfig;
    public Set<Portal> portals;
    public Map<String, Map<String, String>> setter;

    public PortalMain() {

    }

    public static PortalMain getInstance() {
        return instance;
    }

    @Override
    public void onLoad() {
        instance = this;
        this.portals = new HashSet<>();
        this.setter = new HashMap<>();
        saveDefaultConfig();
        saveResource("portals.yml");
    }

    @Override
    public void onEnable() {
        if (getConfig().getBoolean("check_update", true)) {
            ZQUtils.checkPluginUpdate(this);
        }
        String checkingMode = getConfig().getString("checking_mode", "task");
        this.portalsConfig = new Config(getDataFolder().getPath() + "/portals.yml", Config.YAML);
        loadPortalConfig();
        getServer().getPluginManager().registerEvents(new SetPortalEventListener(this), this);
        if (checkingMode.equals("task")) {
            getServer().getScheduler().scheduleRepeatingTask(new CheckPlayerInsideTask(this), getConfig().getInt("interval", 1) * 20, true);
        } else {
            getServer().getPluginManager().registerEvents(new PlayerMovingEventListener(this), this);
        }
        getServer().getCommandMap().register("portal", new SetPortalCommand(), "setportal");
        getServer().getCommandMap().register("portal", new DeletePortalCommand(), "deleteportal");
        getServer().getCommandMap().register("portal", new CancelPortalCommand(), "cancelportal");
        getLogger().info(TextFormat.GREEN + "插件加载成功! By:Yanziqing25");
    }

    @Override
    public void onDisable() {
        this.portalsConfig.save();
        getLogger().info(TextFormat.RED + "插件已关闭!");
    }

    /**
     * 检测传送门是否已被加载
     *
     * @param name 传送门名称
     * @return boolean
     */
    public boolean isPortalExists(String name) {
        for (Portal portal : this.portals) {
            if (portal.getName().equalsIgnoreCase(name)) return true;
        }
        return false;
    }

    /**
     * 添加一个非跨服传送门
     *
     * @param name   传送门名称
     * @param p1     传送门第一个方块
     * @param p2     传送门第二个方块
     * @param target 目标所在服务器
     * @return 是否添加成功boolean
     */
    public boolean addTeleportPortal(String name, Position p1, Position p2, Position target) {
        if (isPortalExists(name)) return false;
        TeleportPortal portal = new TeleportPortal(name, p1, p2, target);
        PortalLoadedEvent portalLoadedEvent = new PortalLoadedEvent(portal);
        if (portalLoadedEvent.isCancelled()) return false;
        this.portals.add(portal);
        return true;
    }

    /**
     * 添加一个跨服传送门
     *
     * @param name   传送门名称
     * @param p1     传送门第一个方块
     * @param p2     传送门第二个方块
     * @param target 目标所在服务器
     * @return 是否添加成功boolean
     */
    public boolean addTransferPortal(String name, Position p1, Position p2, InetSocketAddress target) {
        if (isPortalExists(name)) return false;
        TransferPortal portal = new TransferPortal(name, p1, p2, target);
        PortalLoadedEvent portalLoadedEvent = new PortalLoadedEvent(portal);
        if (portalLoadedEvent.isCancelled()) return false;
        this.portals.add(portal);
        return true;
    }

    /**
     * 添加一个跨服传送门
     *
     * @param name        传送门名称
     * @param p1          传送门第一个方块
     * @param p2          传送门第二个方块
     * @param address     目标所在服务器地址
     * @param port        目标所在服务器端口
     * @return 是否添加成功boolean
     */
    public boolean addTransferPortal(String name, Position p1, Position p2, String address, int port) {
        return addTransferPortal(name, p1, p2, new InetSocketAddress(address, port));
    }

    /**
     * 移除一个传送门
     *
     * @param name 传送门名称
     * @return 是否移除成功boolean
     */
    public boolean deletePortal(String name) {
        if (!isPortalExists(name)) return false;
        portals.removeIf(portal -> portal.getName().equalsIgnoreCase(name));
        return true;
    }

    public void loadPortalConfig() {
        this.portalsConfig.getAll().forEach((name, v) -> {
            Map<String, Object> info = (Map<String, Object>) v;
            Level w = getServer().getLevelByName((String) info.get("w"));
            int x1 = (int) (info.get("x1"));
            int y1 = (int) (info.get("y1"));
            int z1 = (int) (info.get("z1"));
            int x2 = (int) (info.get("x2"));
            int y2 = (int) (info.get("y2"));
            int z2 = (int) (info.get("z2"));
            Position p1 = new Position(x1, y1, z1, w);
            Position p2 = new Position(x2, y2, z2, w);
            int type = (int) (info.get("type"));
            if (type == 1) {
                Level tw = getServer().getLevelByName((String) info.get("tw"));
                int tx = (int) (info.get("tx"));
                int ty = (int) (info.get("ty"));
                int tz = (int) (info.get("tz"));
                Position tp = new Position(tx, ty, tz, tw);
                addTeleportPortal(name, p1, p2, tp);
            } else {
                String rawServer = (String) info.get("server");
                String[] rawServerArray = rawServer.split(":");
                String address = rawServerArray[0];
                int port = Integer.parseInt(rawServerArray[1]);
                addTransferPortal(name, p1, p2, address, port);
            }
        });
    }

    public void savePortalConfig() {
        LinkedHashMap<String, Object> cfg = new LinkedHashMap<>();
        for (Portal portal : portals) {
            LinkedHashMap<String, Object> info = new LinkedHashMap<>();
            info.put("w", portal.getPosition1().getLevel().getName().toLowerCase());
            info.put("x1", portal.getPosition1().getFloorX());
            info.put("y1", portal.getPosition1().getFloorY());
            info.put("z1", portal.getPosition1().getFloorZ());
            info.put("x2", portal.getPosition2().getFloorX());
            info.put("y2", portal.getPosition2().getFloorY());
            info.put("z2", portal.getPosition2().getFloorZ());

            if (portal instanceof TeleportPortal teleportPortal) {
                info.put("type", 1);
                info.put("tw", teleportPortal.getTarget().getLevel().getName().toLowerCase());
                info.put("tx", teleportPortal.getTarget().getFloorX());
                info.put("ty", teleportPortal.getTarget().getFloorY());
                info.put("tz", teleportPortal.getTarget().getFloorZ());
            } else {
                TransferPortal transferPortal = (TransferPortal) portal;
                InetSocketAddress target = transferPortal.getTarget();
                info.put("type", 2);
                info.put("server", target.getHostName() + ":" + target.getPort());
            }
            cfg.put(portal.getName(), info);
        }
        portalsConfig.setAll(cfg);
        portalsConfig.save();
    }

    /**
     * 获取传送门对象
     *
     * @param name 传送门名称
     * @return Portal传送门的对象
     */
    public Portal getPortal(String name) {
        for (Portal portal : this.portals) {
            if (portal.getName().equalsIgnoreCase(name)) return portal;
        }
        return null;
    }
}