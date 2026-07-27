# 数字滑块拼图

一个使用 Java Swing 编写的经典数字滑块拼图游戏。

本项目是 Java 编程课程作业，游戏规则与图形界面相互分离。目前已经实现可玩的数字拼图模式。

## 当前状态

**v0.1.0 — 首个可玩版本**

数字拼图模式已经可以游玩。“上传图片”菜单目前只能选择图片文件，尚未实现将图片切分为拼图块的功能。

## 已实现功能

- 3 × 3、4 × 4、5 × 5 三种棋盘大小
- 通过合法移动随机打乱，保证生成的拼图可解
- 合法移动判断
- 步数统计
- 难度选择
- 重新开始当前难度
- 完成检测与提示窗口
- Java Swing 桌面图形界面
- 游戏逻辑与界面分离

## 计划中的功能

- 将用户选择的图片切分为拼图块
- 图片拼图模式
- 优化界面视觉设计
- 提供可执行发布包和原生安装程序

## 环境要求

- Java Development Kit（JDK）8 或更高版本
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
2. 点击与空格直接相邻的数字方块。
3. 方块移动到空格中，步数随之增加。
4. 按从左到右、从上到下的顺序排列全部数字方块，即可完成拼图。

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
│   └── package.bluej      # BlueJ 项目配置
├── .gitignore
├── README.md
└── README.zh-CN.md
```

## 主要类说明

### `GameModel`

包含游戏规则：创建完成状态的棋盘、通过合法移动打乱棋盘、判断方块是否可以移动、保存棋盘状态，以及检查是否完成拼图。

### `GameJFrame`

包含 Swing 图形界面：显示棋盘和步数、处理方块点击、提供难度与重新开始菜单，以及显示完成提示。

### `Main`

在 Swing 的事件分派线程中启动程序。

## 许可

本项目仅用于学习和教学目的。

[English version](README.md)
