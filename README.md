# ZQPortal

### 介绍

:bangbang: 2.0.0版本传送门插件卷土归来！

重大重构工程，更好的性能，更方便的设置......

:bangbang: 注意：此插件需要前置插件[ZQExtension](https://github.com/yanziqing25/ZQExtension)

### 功能

- 进入传送门即可传送到预定好的目标位置
- 开发者需要的API请查看主类"PortalMain"和传送门类"Portal"

## 配置文件

### config.yml:

```yaml
# 语言，目前仅"zh-cn"
language: zh-cn
# 插件更新检测开关，"true"或"false"
check_update: true
# 检测玩家是否在传送门里时使用的模式，可选"event"或"task"
checking_mode: task
# task模式检测间隔，单位：秒（仅"checking_mode"为task时生效）
interval: 1
```

### checking_mode解释

人数较多时建议使用“task”模式和加大检测间隔"interval"以获得更好的性能（本人目测）
具体性能和内存占用自测~

- "event"使用玩家移动事件监听来检测玩家的位置
- "task"使用计时器来检测玩家的位置

## 指令

| 命令            | 别名  | 参数                                           | 说明          | 权限等级 |
|---------------|-----|----------------------------------------------|-------------|------|
| /addportal    | ap  | <string: name>                               | 进入普通传送门设置模式 | OP   |
| /addportal    | ap  | <string: name> [string: address] [int: port] | 进入跨服传送门设置模式 | OP   |
| /deleteportal | dp  | <string: name>                               | 移除一个传送门     | OP   |
| /cancelportal | cp  |                                              | 取消设置传送门模式   | OP   |

## 演示（LiteLoaderBDS版）

### 普通传送门

![Teleport](https://www.z4a.net/images/2022/04/26/teleport.gif)

### 跨服传送门

![Transfer](https://www.z4a.net/images/2022/04/26/transfer.gif)