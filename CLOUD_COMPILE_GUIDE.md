# 云编译设置指南 - GitHub Codespaces

## ✅ 已完成的步骤

1. Git 仓库已初始化
2. 所有文件已提交到本地仓库
3. 分支设置为 `main`

## 🚀 下一步：创建 GitHub 仓库并使用 Codespaces

### 方法1：通过 GitHub 网页创建（推荐）

1. **创建新仓库**
   - 访问：https://github.com/new
   - 仓库名：`android-health-monitor`
   - 设为 Public 或 Private 都可以
   - **不要**勾选 "Initialize this repository with a README"
   - 点击 "Create repository"

2. **连接本地仓库**
   ```bash
   cd /home/david/.openclaw/workspace/android-health-monitor
   
   # 添加远程仓库（替换 YOUR_USERNAME 为你的 GitHub 用户名）
   git remote add origin https://github.com/YOUR_USERNAME/android-health-monitor.git
   
   # 推送代码
   git branch -M main
   git push -u origin main
   ```

3. **在 GitHub 上开启 Codespaces**
   - 打开你的仓库页面
   - 点击绿色的 "Code" 按钮
   - 选择 "Codespaces" 标签
   - 点击 "Create codespace"
   - 等待环境创建（约1-2分钟）

4. **在 Codespaces 中编译**
   - Codespaces 会自动打开 VS Code（在浏览器中）
   - 打开终端（`Ctrl + \`` 或 View → Terminal）
   - 运行以下命令：
     ```bash
     # 首次运行需要接受许可
     ./gradlew assembleDebug
     
     # 或编译 Release 版本
     ./gradlew assembleRelease
     ```

5. **下载 APK**
   - 编译完成后，APK 在：`app/build/outputs/apk/debug/app-debug.apk`
   - 在 VS Code 左侧文件管理器中右键点击 APK 文件
   - 选择 "Download" 下载到本地

### 方法2：使用 GitHub CLI（需要安装）

如果你安装了 GitHub CLI：
```bash
# 安装 GitHub CLI（如果未安装）
curl -fsSL https://cli.github.com/packages/githubcli-archive-keyring.gpg | sudo dd of=/usr/share/keyrings/githubcli-archive-keyring.gpg
echo "deb [arch=$(dpkg --print-architecture) signed-by=/usr/share/keyrings/githubcli-archive-keyring.gpg] https://cli.github.com/packages stable main" | sudo tee /etc/apt/sources.list.d/github-cli.list > /dev/null
sudo apt update
sudo apt install gh

# 登录 GitHub
gh auth login

# 创建仓库并推送
gh repo create android-health-monitor --public --source=. --remote=origin --push
```

## 🎮 使用 Codespaces 编译

### Codespaces 优势
- ✅ 免费使用（每月 60 小时）
- ✅ 预装所有开发工具
- ✅ 云端编译，速度快
- ✅ 自动配置 Android SDK
- ✅ 支持完整的 Android Studio 功能

### 编译步骤
```bash
# 1. 打开 Codespaces 终端
# 2. 进入项目目录
cd /workspaces/android-health-monitor

# 3. 编译 Debug 版本
./gradlew assembleDebug

# 4. 编译 Release 版本
./gradlew assembleRelease

# 5. 查看编译结果
ls -lh app/build/outputs/apk/debug/
```

### 下载 APK
1. 在 VS Code 左侧文件浏览器中找到 APK
2. 右键 → Download
3. 保存到本地电脑
4. 通过 USB 传输到手机安装

## 📱 安装到手机

### 方法1：ADB 安装
```bash
# 连接手机并启用 USB 调试
adb devices
adb install app-debug.apk
```

### 方法2：直接安装
1. 将 APK 传输到手机
2. 在文件管理器中打开
3. 点击安装

## 💾 备份和同步

### 从 GitHub 拉取最新代码
```bash
git pull origin main
```

### 提交修改到 GitHub
```bash
git add .
git commit -m "描述你的修改"
git push origin main
```

## 🆘 常见问题

### Q: Codespaces 用完免费额度怎么办？
A: 可以继续使用，但会产生费用。建议在 Codespaces 完成工作后及时删除。

### Q: 如何删除 Codespace？
A: 在 GitHub 仓库页面 → Codespaces → 点击三个点 → Delete

### Q: 编译失败怎么办？
A: 1. 检查错误信息；2. 查看 BUILD_INSTRUCTIONS.md；3. 在 Codespaces 中查看日志

### Q: 可以在其他设备上使用 Codespaces 吗？
A: 可以！只要有浏览器就能访问，所有文件都保存在云端。

## 🎯 下一步

1. 创建 GitHub 仓库
2. 推送代码
3. 启动 Codespaces
4. 开始编译！

需要我帮你检查推送后的代码吗？或者有其他问题随时问我！😊
