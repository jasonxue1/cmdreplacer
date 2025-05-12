# Command Replacer (cmdreplacer)

一个针对 Minecraft 1.8.9 Forge 客户端的**命令替换**模组，允许你把常用的原指令替换成自定义指令并自动发送。

---

## 特性

* 在客户端拦截聊天输入，以 `/原指令` 开头的消息自动替换为 `/替换指令` 并发送
* 内建简易 GUI，一键添加／编辑／删除映射
* 完全客户端运行，不依赖服务器端支持

---

## 安装

1. 安装 Minecraft Forge 1.8.9-11.15.1.2318
2. 将生成好的 JAR（例如 `cmdreplacer_1.8.9_1.0.0.jar`）放入你的
   `.minecraft/mods/`
3. 启动 Minecraft，确保启动日志中出现 `Command Replacer`

---

## 配置

1. **打开配置 GUI**
   在游戏中按下默认按键 **`R`**（可在代码中修改）
2. **管理映射**

   * 点击 **增加**：添加一条“原指令 → 替换指令”
   * 点击 **编辑**：修改已选中的映射
   * 点击 **删除**：删除已选中的映射
   * 点击 **完成**：退出 GUI，生效当前所有映射
3. 所有设置保存在
   `config/cmdreplacer.json`
   下次启动自动加载

---

## 使用

* 在聊天框输入：

  ```
  /原指令 [参数...]
  ```
* 客户端会自动将其替换为：

  ```
  /替换指令 [相同参数...]
  ```
* 如果多个映射存在，按**添加顺序**依次匹配，第一次匹配成功就会停止

---

## 构建 & 发布（可选）

本项目自带 GitHub Actions 工作流，自动在你打上 Tag（如 `1.0.0`）并推送后：

1. **编译**：使用系统 Gradle & Java 11 构建
2. **打包**：生成 `build/libs/cmdreplacer_1.8.9_<TAG>.jar`
3. **发布**：在 GitHub Releases 创建同名 Release 并上传该 JAR

### 本地编译

```bash
# 准备
git clone https://github.com/yourname/cmdreplacer.git
cd cmdreplacer

# 编译
gradle build --quiet

# 输出
mv build/libs/cmdreplacer-*.jar cmdreplacer_1.8.9_1.0.0.jar
```

---

## 常见问题

* **为何按键不生效？**
  确认你没有占用 `R` 键。

* **配置文件丢失？**
  首次运行会自动生成空的 `config/cmdreplacer.json`。

* **映射不生效？**
  检查“原指令”格式是否带 `/`，且不要和 Minecraft 原生命令冲突。

---

> 如果有任何问题或建议欢迎提交 Issue 或 PR，也可在 Discussions 中交流。
