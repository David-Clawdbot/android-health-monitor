# 编译指南 - Android Health Monitor

## 已完成的配置

✅ 项目配置文件已补全：
- settings.gradle.kts - 项目设置
- build.gradle.kts - 根项目构建配置
- gradle.properties - Gradle 属性配置
- gradle/wrapper/gradle-wrapper.properties - Gradle Wrapper 配置
- gradlew / gradlew.bat - Gradle Wrapper 脚本
- .gitignore - Git 忽略文件

✅ 资源文件已创建：
- res/values/strings.xml - 字符串资源
- res/values/colors.xml - 颜色资源
- res/values/themes.xml - 主题资源
- res/layout/activity_main.xml - 主界面布局

## 编译前准备

### 1. 安装必要工具

#### 在 Windows/Mac 上：
- 安装 Android Studio
- 下载并安装 JDK 17 或更高版本
- 配置 JAVA_HOME 环境变量

#### 在 Linux 上：
```bash
# 安装 JDK 17
sudo apt update
sudo apt install openjdk-17-jdk

# 设置 JAVA_HOME
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
echo 'export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64' >> ~/.bashrc
```

### 2. 获取 Gradle Wrapper Jar

**方法1：使用 Gradle 自动生成（推荐）**
```bash
# 如果系统已安装 gradle
cd /home/david/.openclaw/workspace/android-health-monitor
gradle wrapper

# 如果系统没有安装 gradle，下载 Gradle
wget https://services.gradle.org/distributions/gradle-8.2-bin.zip
unzip gradle-8.2-bin.zip
./gradle-8.2/bin/gradle wrapper
```

**方法2：直接下载 jar 文件**
```bash
cd /home/david/.openclaw/workspace/android-health-monitor/gradle/wrapper
wget https://github.com/gradle/gradle/raw/v8.2.0/gradle/wrapper/gradle-wrapper.jar -O gradle-wrapper.jar
```

**方法3：使用 Android Studio（最简单）**
1. 打开 Android Studio
2. 选择 File → Open → 选择项目目录
3. Android Studio 会自动下载并配置 Gradle Wrapper

## 编译步骤

### 在 Windows/Mac 上使用 Android Studio：
1. File → Open → 选择项目目录
2. 等待 Gradle 同步完成
3. Build → Build Bundle(s) / APK(s) → Build APK(s)
4. APK 文件位置：`app/build/outputs/apk/debug/app-debug.apk`

### 使用命令行编译：
```bash
cd /home/david/.openclaw/workspace/android-health-monitor

# 编译 Debug 版本
./gradlew assembleDebug

# 编译 Release 版本
./gradlew assembleRelease

# 清理构建
./gradlew clean
```

### 编译输出位置
- Debug APK: `app/build/outputs/apk/debug/app-debug.apk`
- Release APK: `app/build/outputs/apk/release/app-release.apk`

## 安装到设备

### 使用 ADB：
```bash
# 安装 Debug 版本
adb install app/build/outputs/apk/debug/app-debug.apk

# 安装 Release 版本
adb install app/build/outputs/apk/release/app-release.apk

# 卸载
adb uninstall com.healthmonitor
```

### 直接复制到手机：
将 APK 文件复制到手机存储，然后在文件管理器中打开安装。

## 常见问题

### 问题1：找不到 JAVA_HOME
```bash
export JAVA_HOME=/path/to/java
# 永久设置
echo 'export JAVA_HOME=/path/to/java' >> ~/.bashrc
```

### 问题2：gradlew 权限不足
```bash
chmod +x gradlew
```

### 问题3：缺少 Android SDK
在 Android Studio 中：
- File → Settings → Appearance & Behavior → System Settings → Android SDK
- 安装所需的 SDK Platform 和 Build Tools

### 问题4：Gradle 下载失败
修改 `gradle/wrapper/gradle-wrapper.properties`，使用国内镜像：
```
distributionUrl=https\://mirrors.cloud.tencent.com/gradle/gradle-8.2-bin.zip
```

## 项目信息

- 应用包名：com.healthmonitor
- 最小 SDK：26 (Android 8.0)
- 目标 SDK：34 (Android 14)
- 版本号：1.0.0 (Version Code: 1)
