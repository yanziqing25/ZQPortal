package cn.yzq25.portal;

import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;
import cn.yzq25.portal.command.CancelPortalCommand;
import cn.yzq25.portal.command.RemovePortalCommand;
import cn.yzq25.portal.command.SetPortalCommand;
import cn.yzq25.portal.event.portal.PortalLoadedEvent;
import cn.yzq25.portal.task.PortalPlayerInsideTask;
import cn.yzq25.utils.ZQUtils;

import java.net.InetSocketAddress;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * ZQPortal插件主类
 *
 * @author Yanziqing25
 */
public class PortalMain extends PluginBase {
    private static PortalMain instance;
    protected String checkingMode;
    public Config portalsConfig;
    public int settingStatus;
    public String portalName;
    public String setter;
    public int type;
    public String address;

    public LinkedHashMap<String, Portal> portalsMap;

    public PortalMain() {
    }

    public static PortalMain getInstance() {
        return instance;
    }

    @Override
    public void onLoad() {
        instance = this;
        saveDefaultConfig();
        saveResource("portals.yml");
    }

    @Override
    public void onEnable() {
        if (getConfig().getBoolean("check_update", true)) {
            ZQUtils.checkPluginUpdate(this);
        }
        this.checkingMode = getConfig().getString("checking_mode", "event");
        this.portalsConfig = new Config(getDataFolder().getPath() + "/portals.yml", Config.YAML);
        this.settingStatus = 0;
        this.portalName = null;
        this.setter = null;
        this.type = 0;
        this.address = null;
        this.portalsMap = new LinkedHashMap();
        getServer().getPluginManager().registerEvents(new PortalEventListener(), this);
        getServer().getCommandMap().register("portal", new SetPortalCommand(), "setportal");
        getServer().getCommandMap().register("portal", new RemovePortalCommand(), "removeportal");
        getServer().getCommandMap().register("portal", new CancelPortalCommand(), "cancelportal");
        loadPortals();
        if (this.checkingMode.equals("task")) {
            getServer().getScheduler().scheduleRepeatingTask(new PortalPlayerInsideTask(this), 20);
        }
        getLogger().info(TextFormat.GREEN + "插件加载成功! By:Yanziqing25");
    }

    @Override
    public void onDisable() {
        this.portalsConfig.save();
        getLogger().info(TextFormat.RED + "插件已关闭!");
    }

    /**
     * 添加一个非跨服传送门
     *
     * @param name 传送门名称
     * @param x1 传送门第一个点X坐标
     * @param y1 传送门第一个点Y坐标
     * @param z1 传送门第一个点Z坐标
     * @param x2 传送门第二个点X坐标
     * @param y2 传送门第二个点Y坐标
     * @param z2 传送门第二个点Z坐标
     * @param portalWorld 传送门所在世界
     * @param tx 目标所在点X坐标
     * @param ty 目标所在点Y坐标
     * @param tz 目标所在点Z坐标
     * @param targetWorld 目标所在世界
     *
     * @return 是否添加成功boolean
     */
    public synchronized boolean addTeleportPortal(String name, int x1, int y1, int z1, int x2, int y2, int z2, Level portalWorld, int tx, int ty, int tz, Level targetWorld) {
        Map<String, Object> args = new LinkedHashMap();
        args.put("type", 1);
        args.put("x1", x1);
        args.put("y1", y1);
        args.put("z1", z1);
        args.put("x2", x2);
        args.put("y2", y2);
        args.put("z2", z2);
        args.put("world", portalWorld.getName());
        args.put("tx", tx);
        args.put("ty", ty);
        args.put("tz", tz);
        args.put("target", targetWorld.getName());

        if (this.portalsConfig.exists(name)) {
            return false;
        }
        //保存并加载传送门
        this.portalsConfig.set(name, args);
        if (this.portalsConfig.save()) {
            this.loadPortal(name);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 添加一个跨服传送门
     *
     * @param name 传送门名称
     * @param x1 传送门第一个点X坐标
     * @param y1 传送门第一个点Y坐标
     * @param z1 传送门第一个点Z坐标
     * @param x2 传送门第二个点X坐标
     * @param y2 传送门第二个点Y坐标
     * @param z2 传送门第二个点Z坐标
     * @param portalWorld 传送门所在世界
     * @param target 目标服务器IP地址:端口
     *
     * @return 是否添加成功boolean
     */
    public synchronized boolean addTransferPortal(String name, int x1, int y1, int z1, int x2, int y2, int z2, Level portalWorld, String target) {
        Map<String, Object> args = new LinkedHashMap();
        args.put("type", 2);
        args.put("x1", x1);
        args.put("y1", y1);
        args.put("z1", z1);
        args.put("x2", x2);
        args.put("y2", y2);
        args.put("z2", z2);
        args.put("world", portalWorld.getName());
        args.put("target", target);

        if (this.portalsConfig.exists(name)) {
            return false;
        }
        //保存并加载传送门
        this.portalsConfig.set(name, args);
        if (this.portalsConfig.save()) {
            this.loadPortal(name);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 移除一个传送门
     *
     * @param name 传送门名称
     * @return 是否移除成功boolean
     */
    public synchronized boolean removePortal(String name) {
        if (!this.portalsConfig.exists(name)) {
            return false;
        }
        this.portalsConfig.remove(name);
        if (this.portalsConfig.save()) {
            this.unloadPortal(name);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 加载配置文件中的所有传送门
     */
    public synchronized void loadPortals() {
        Map<String, Object> config = this.portalsConfig.getAll();
        config.forEach((name, info) -> {
            if (!isLoaded(name)) {
                loadPortal(name);
                getServer().getLogger().info(TextFormat.DARK_GREEN + "成功加载传送门[" + name + "]");
            }
        });
    }

    /**
     * 检测传送门是否已被加载
     *
     * @param name 传送门名称
     *
     * @return boolean
     */
    public boolean isLoaded(String name) {
        return this.portalsMap.containsKey(name);
    }

    /**
     * 获取传送门对象
     *
     * @param name 传送门名称
     *
     * @return Portal传送门的对象
     */
    public Portal getPortal(String name) {
        if (!isLoaded(name)) {
            return null;
        }
        return this.portalsMap.get(name);
    }

    private synchronized void loadPortal(String name) {
        Map<String, Object> info = (Map<String, Object>)this.portalsConfig.get(name);
        Portal portal = null;
        switch ((int)info.get("type")) {
            case 1:
                portal = new TeleportPortal(name, new Position((int) (info.get("x1")), (int) (info.get("y1")), (int) (info.get("z1")), getServer().getLevelByName(((String) info.get("world")))), new Position((int) (info.get("x2")), (int) (info.get("y2")), (int) (info.get("z2")), getServer().getLevelByName(((String) info.get("world")))), new Position((int) (info.get("tx")), (int) (((Map) info).get("ty")), (int) (info.get("tz")), getServer().getLevelByName(((String) info.get("target")))));
                break;
            case 2:
                InetSocketAddress inetSocketAddress = new InetSocketAddress(((String) info.get("target")).split(":")[0], Integer.valueOf(((String) info.get("target")).split(":")[1]));
                portal = new TransferPortal(name, new Position((int) (info.get("x1")), (int) (info.get("y1")), (int) (info.get("z1")), getServer().getLevelByName(((String) info.get("world")))), new Position((int) (info.get("x2")), (int) (info.get("y2")), (int) (info.get("z2")), getServer().getLevelByName(((String) info.get("world")))), inetSocketAddress);
                break;
        }
        PortalLoadedEvent portalLoadedEvent = new PortalLoadedEvent(portal);
        if (portalLoadedEvent.isCancelled()) {
            return;
        }
        this.portalsMap.put(portalLoadedEvent.getPortal().getName(), portalLoadedEvent.getPortal());
    }

    private synchronized void unloadPortal(String name) {
        this.portalsMap.remove(name);
        getServer().getLogger().info(TextFormat.DARK_GREEN + "成功卸载传送门[" + name + "]");
    }
}