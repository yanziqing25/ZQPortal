# ZQPortal
### 介绍
本插件是一个可自定义的简单易用的传送门插件!
注意：此插件需要前置插件—ZQExtension.jar
### 功能
- 进入传送门即可传送到预定好的目标位置
- 开发者需要的API请查看主类"PortalMain"和传送门类"Portal"
### 配置文件
#### "check-update"
- 插件自动更新开关,"true"或"false"
#### checking-mode"
- 检测玩家是否在传送门里时使用的模式,"event"或"task"
- "event"使用事件监听来检测玩家的位置,此模式有占用资源少的优点
- "task"使用计时器来检测玩家的位置,此模式有准确传送的优点
### 指令
- `/setportal <string: name> [string: address]` 进入设置传送门模式,分别点击传送门的两角,(点击传送的目标)即可
- `/removeportal <string: name>` 移除一个传送门
- `/cancelportal` 取消设置传送门模式
### 权限
		portal.command.setportal:
		default: op
		portal.command.removeportal:
		default: op
		portal.command.cancelportal:
		default: op