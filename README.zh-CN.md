# 数字滑块拼图

**中文** | [English](README.md) | [Deutsch](README.de.md)

一个使用 Java Swing 编写的经典滑块拼图游戏。既可以玩数字拼图，也可以将自己的图片变成拼图。

## 当前状态

**v1.2.0 — 多语言界面与图片错误处理优化**

项目现已提供可玩的数字拼图和图片拼图模式。界面可在中文、英文和德文之间切换，且不会重置当前游戏。加载无效或无法读取的图片时，程序会显示本地化的错误提示。

## 已实现功能

- 3 × 3、4 × 4、5 × 5 三种棋盘大小
- 通过合法移动随机打乱，保证生成的拼图可解
- 数字拼图与图片拼图模式
- 支持上传 JPG、JPEG 和 PNG 图片
- 自动缩放为 600 × 600 的无缝图片棋盘
- 不丢失当前棋盘状态即可在图片和数字显示之间切换
- 中文、英文、德文三种界面语言
- 合法移动判断与步数统计
- 难度选择与重新开始当前游戏
- 完成检测与提示窗口
- Java Swing 桌面图形界面，游戏逻辑与界面分离

## 环境要求

- Java Development Kit（JDK）8 或更高版本；为正确显示 UTF-8 翻译内容，建议使用 JDK 9 或更高版本
- BlueJ（可选）

检查 Java 是否已安装：

```bash
java --version
javac --version
```

## 从源码运行

克隆仓库：

```bash
git clone https://github.com/zhouxu121/sliding_puzzle.git
cd sliding_puzzle/SlidingPuzzle
```

编译并启动：

```bash
javac *.java
java Main
```

## 使用 BlueJ 运行

1. 打开 BlueJ。
2. 选择 **Open Project（打开项目）**。
3. 选择 `SlidingPuzzle` 文件夹。
4. 编译全部类。
5. 右键点击 `Main`，运行 `void main(String[] args)`。

## 游戏玩法

1. 在难度菜单中选择棋盘大小。
2. 点击与空格直接相邻的方块。
3. 方块移动到空格中，步数随之增加。
4. 按从左到右、从上到下的顺序排列全部方块，即可完成拼图。

要玩图片拼图，请在菜单中选择“上传图片”，然后选择 JPG、JPEG 或 PNG 文件。当前排列和步数不会改变；选择“显示数字”可切回数字拼图显示。可通过“语言”菜单切换界面语言。

3 × 3 拼图的完成状态：

```text
0  1  2
3  4  5
6  7  [ ]
```

只有空格上、下、左、右四个方向中紧邻的方块可以移动。

## 项目结构

```text
sliding_puzzle/
├── SlidingPuzzle/
│   ├── Main.java          # 程序入口
│   ├── GameJFrame.java    # Swing 图形界面
│   ├── GameModel.java     # 拼图规则和棋盘状态
│   ├── Messages*.properties # 多语言界面文本
│   └── package.bluej      # BlueJ 项目配置
├── .gitignore
├── README.md              # 英文
├── README.zh-CN.md        # 中文
└── README.de.md           # 德文
```

## 主要类说明

### `GameModel`

包含游戏规则：创建完成状态的棋盘、通过合法移动打乱棋盘、判断方块是否可以移动、保存棋盘状态，以及检查是否完成拼图。

### `GameJFrame`

包含 Swing 图形界面：显示数字或图片方块、处理图片加载、显示模式和语言切换、提供游戏菜单，以及显示完成提示。

### `Main`

在 Swing 的事件分派线程中启动程序。

## 许可

本项目仅用于学习和教学目的。
