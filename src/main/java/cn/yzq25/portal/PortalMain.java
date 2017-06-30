package cn.yzq25.portal;

import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;
import cn.nukkit.utils.Utils;
import cn.yzq25.portal.command.CancelPortalCommand;
import cn.yzq25.portal.command.RemovePortalCommand;
import cn.yzq25.portal.command.SetPortalCommand;
import cn.yzq25.portal.event.portal.PortalLoadedEvent;
import cn.yzq25.portal.task.PortalPlayerInsideTask;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
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
        checkUpdate();
        this.checkingMode = getConfig().getString("checking-mode", "event");
        this.portalsConfig = new Config(getDataFolder().getPath() + "/portals.yml", Config.YAML);
        this.settingStatus = 0;
        this.portalName = null;
        this.setter = null;
        this.portalsMap = new LinkedHashMap();
        getServer().getPluginManager().registerEvents(new PortalEventListener(this), this);
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
        getLogger().info(TextFormat.RED + "插件已关闭!");
    }

    private JSONObject getServerJsonObject() {
        try {
            URL url = new URL("http://www.mcel.cn:80/plugins/ZQPortal/update.json");
            url.openConnection().setConnectTimeout(30000);
            url.openConnection().setReadTimeout(30000);
            InputStream in = url.openConnection().getInputStream();
            return new JSONObject(Utils.readFile(in));
        } catch (IOException e) {
            return null;
        }
    }

    private void checkUpdate() {
        if (getConfig().getBoolean("check-update", true)) {
            if (getServerJsonObject() != null) {
                if (!getDescription().getVersion().equals(getServerJsonObject().getString("version"))) {
                    getLogger().info(TextFormat.YELLOW + "插件有更新!最新版本为" + TextFormat.BLUE + getServerJsonObject().getString("version"));
                    getLogger().info(TextFormat.YELLOW + "下载地址:" + getServerJsonObject().getString("download"));
                } else {
                    getLogger().info(TextFormat.BLUE + "本插件为最新版本!版本号" + getDescription().getVersion());
                }
            } else {
                getLogger().warning("更新检查失败!");
            }
        }
    }

    /**
     * 添加一个传送门
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
    public boolean addPortal(String name, int x1, int y1, int z1, int x2, int y2, int z2, Level portalWorld, int tx, int ty, int tz, Level targetWorld) {
        Map<String, Object> args = new LinkedHashMap();
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
    public boolean removePortal(String name) {
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
    public void loadPortals() {
        Map<String, Object> config = this.portalsConfig.getAll();
        config.forEach((name, info) -> {
            if (!isLoaded(name)) {
                this.portalsMap.put(name, new Portal(name, new Position((int) (((Map) info).get("x1")), (int) (((Map) info).get("y1")), (int) (((Map) info).get("z1")), getServer().getLevelByName(((String)((Map) info).get("world")))), new Position((int) (((Map) info).get("x2")), (int) (((Map) info).get("y2")), (int) (((Map) info).get("z2")), getServer().getLevelByName(((String)((Map) info).get("world")))), new Position((int) (((Map) info).get("tx")), (int) (((Map) info).get("ty")), (int) (((Map) info).get("tz")), getServer().getLevelByName(((String)((Map) info).get("target"))))));
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

    private void loadPortal(String name) {
        Map<String, Object> info = (Map<String, Object>)this.portalsConfig.get(portalName);
        Portal portal = new Portal(name, new Position((int)(info.get("x1")), (int)(info.get("y1")), (int)(info.get("z1")), getServer().getLevelByName(((String)info.get("world")))), new Position((int)(info.get("x2")), (int)(info.get("y2")), (int)(info.get("z2")), getServer().getLevelByName(((String)info.get("world")))), new Position((int)(info.get("tx")), (int)(((Map) info).get("ty")), (int)(info.get("tz")), getServer().getLevelByName(((String)info.get("target")))));
        PortalLoadedEvent portalLoadedEvent = new PortalLoadedEvent(portal);
        if (portalLoadedEvent.isCancelled()) {
            return;
        }
        this.portalsMap.put(name, portal);
    }

    private void unloadPortal(String name) {
        this.portalsMap.remove(name);
        getServer().getLogger().info(TextFormat.DARK_GREEN + "成功卸载传送门[" + name + "]");
    }
}