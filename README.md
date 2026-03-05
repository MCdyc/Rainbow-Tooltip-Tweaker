# Rainbow Tooltip Tweaker

一个 CraftTweaker 附属模组，允许整合包作者通过 ZenScript 添加**动态**彩虹字体到物品 tooltip。

![Rainbow Tooltip Demo](https://i.imgur.com/placeholder.png)

## 功能特性

- 🌈 **全局动态彩虹效果** - 可以在任何支持颜色代码的地方（如物品标题、Tooltip、甚至是聊天栏如果是被服务端发送的带有 NBT 名字的物品）使用，产生波浪般流动的彩虹效果！
- ⚡ **零成本无损性能** - 基于强力底层的字节码注入（Mixin），仅在包含彩虹标记时被触发，其他原生渲染 100% 毫无损耗。
- 🎨 **HSB 原生颜色模型** - 自动处理饱和度和色相平滑渐变，颜色纯正。
- 📝 **极简 ZenScript API** - 一行代码，既能改 Tooltip，也能彻底改变物品的全局展示名称 `displayName`。
- 🎯 **矿辞及批量支持** - 支持给单一物品或一整个 OreDict 矿辞统一贴彩虹标签。

## 工作原理

### 动态彩虹算法

模组使用 HSB 颜色模型计算动态彩虹颜色：

```java
hue = (time % 2000L) / 2000.0F + (characterIndex * 0.05F)
rgb = Color.HSBtoRGB(hue, 1.0F, 1.0F)
```

| 参数 | 作用 |
|------|------|
| `time` | 当前系统时间（毫秒），用于实现动画效果 |
| `speed` | 颜色变化周期（默认 2000ms = 2 秒） |
| `characterIndex` | 字符在文本中的位置，用于实现波浪效果 |
| `density` | 颜色波浪密度（默认 0.05） |

### 渲染机制重构 (Mixin 注入)

过去受限于事件，彩虹字只能在悬浮 Tooltip 被画出来时强行覆盖一层颜色。
**现在**，模组利用 Mixin 技术深度劫持了 Minecraft 的主画笔 `FontRenderer`。

```
ZenScript: format("Hello")
         ↓
生成: "\u00A7zHello" (特殊的原版隐形颜色代码 §z)
         ↓
Minecraft FontRenderer (底层)
         ↓
拦截到 §z (或被替换的隐形标记) → 瞬间开启彩虹画笔模式
         ↓
渲染出动态流动的彩虹文字，遇到普通颜色代码自动恢复！
```

**【最大优势】：** 由于我们劫持了主画笔，彩虹效果不再局限于 Tooltip！你现在可以直接通过 `item.displayName` 将一个物品彻底变成彩虹名。无论它是拿在手里、掉在地上，还是你在背包里用鼠标指着它看，全部都是原汁原味的动态彩虹色！

## 安装

1. 将模组 jar 文件放入 Minecraft 的 `mods` 文件夹
2. 确保已安装 [CraftTweaker 2](https://www.curseforge.com/minecraft/mc-mods/236222-crafttweaker-2)
3. 将 ZenScript 脚本放入 `scripts` 文件夹

## ZenScript API

### 基础用法

```zenscript
# 为文本添加动态彩虹效果
var rainbowText = mods.rainbowtooltip.RainbowTooltip.format("Hello World!");

# 【新特性】将物品原本的名字彻底替换为彩虹名！
<minecraft:diamond>.displayName = rainbowText;

# 将彩虹文本附加到物品悬浮提示 Tooltip
<minecraft:diamond>.addTooltip(rainbowText);
```

### 完整示例

```zenscript
# 为钻石修改【全局名称】并添加彩虹 tooltip
<minecraft:diamond>.displayName = mods.rainbowtooltip.RainbowTooltip.format("✦ 传奇彩虹钻石 ✦");
<minecraft:diamond>.addTooltip("");
<minecraft:diamond>.addTooltip(mods.rainbowtooltip.RainbowTooltip.format("此物品流光溢彩"));

# 为金苹果保留原名，只加动态彩虹 tooltip
<minecraft:golden_apple>.addTooltip("");
<minecraft:golden_apple>.addTooltip(mods.rainbowtooltip.RainbowTooltip.format("★ 神圣苹果 ★"));

# 为龙蛋彻底改名
<minecraft:dragon_egg>.displayName = mods.rainbowtooltip.RainbowTooltip.format("✨ 灭世龙蛋 ✨");
```

### 批量处理

```zenscript
# 为多个物品批量添加彩虹文本
var legendaryItems = [
    <minecraft:diamond>,
    <minecraft:emerald>,
    <minecraft:diamond_block>,
    <minecraft:emerald_block>
];

for item in legendaryItems {
    item.addTooltip("");
    item.addTooltip(mods.rainbowtooltip.RainbowTooltip.format("★ LEGENDARY ★"));
}

# 为矿物添加彩虹标记
var ores = [
    <minecraft:diamond_ore>,
    <minecraft:emerald_ore>,
    <minecraft:gold_ore>,
    <minecraft:iron_ore>
];

for ore in ores {
    ore.addTooltip("");
    ore.addTooltip(mods.rainbowtooltip.RainbowTooltip.format("✨ RARE ORE ✨"));
}
```

### 使用便捷方法

```zenscript
# 直接调用 addTooltip 方法
mods.rainbowtooltip.RainbowTooltip.addTooltip(<minecraft:diamond>, "Legendary Diamond!");
mods.rainbowtooltip.RainbowTooltip.addTooltip(<minecraft:bedrock>, "✓ INDESTRUCTIBLE ✓");

# 矿辞支持
mods.rainbowtooltip.RainbowTooltip.addTooltip(<ore:ingotIron>, "IRON COLLECTION");
```

### 检查和移除标记

```zenscript
# 检查文本是否包含彩虹标记
if (mods.rainbowtooltip.RainbowTooltip.hasRainbowMarker(text)) {
    print("Text has rainbow marker!");
}

# 移除彩虹标记，获取纯文本
var plainText = mods.rainbowtooltip.RainbowTooltip.removeMarker(text);
```

## 颜色代码参考

| 代码 | 颜色 | 代码 | 颜色 |
|------|------|------|------|
| `§0` | 黑色 | `§8` | 深灰 |
| `§1` | 深蓝 | `§9` | 蓝色 |
| `§2` | 深绿 | `§a` | 绿色 |
| `§3` | 深青 | `§b` | 青色 |
| `§4` | 深红 | `§c` | 红色 |
| `§5` | 深紫 | `§d` | 紫色 |
| `§6` | 金色 | `§e` | 黄色 |
| `§7` | 灰色 | `§f` | 白色 |

## 技术细节

### 性能分析

基于完美的前置判断：
`if (text.contains("\u00A7z"))`

- **99.99% 的原版文字：** 在渲染第一毫秒就被跳过，不会产生任何 Mixin 负担，实现 **完全零开销**。
- **带有彩虹标记的文字：** CPU 计算极速的位运算和单次 HSB 取色，耗时以纳秒计，连阴影覆盖渲染也顺道完美融合，不影响任何帧率。

### 兼容性

- Minecraft 1.12.2
- Forge 14.23.5.2859
- CraftTweaker 2 1.12-4.+

### 事件系统

- `ItemTooltipEvent` (LOWEST 优先级) - 检测和替换彩虹标记
- `RenderTooltipEvent.PostText` - 覆盖渲染，实现动态彩虹效果

## 示例脚本

完整示例脚本位于 `example_scripts/example.zs`

## 构建

```bash
./gradlew build
```

构建产物位于 `build/libs/` 目录。

## 开发

### 环境要求

- JDK 8+
- Gradle 4+

### 构建命令

```bash
# 清理并构建
./gradlew clean build

# 运行客户端
./gradlew runClient
```

## 注意事项

1. 所有的动态渲染逻辑发生在 **客户端**。服务端无需知道彩虹逻辑，它眼里这只是夹带了特殊乱码符的普通名字。
2. 不一定非要调用本模组的 `addTooltip` 方法修改；既然它现在是全局系统，你可以使用原版 CT 的任何字符串拼接方式把 `RainbowTooltip.format("文字")` 拼进去。
3. 遇到原版颜色代码（如 `§c`），彩虹色会被**立刻截断重置为纯色**。所以如果要拼接普通彩色字，直接放在彩虹字符串后面没问题。

## 许可证

本项目采用 MIT 许可证。

## 致谢

- [CraftTweaker 2](https://github.com/jaredlll08/CraftTweaker2) - ZenScript 框架
- [Minecraft Forge](https://github.com/MinecraftForge/MinecraftForge) - 事件系统
- [CleanroomMC](https://github.com/CleanroomMC) - 构建工具链

## 问题反馈

如有问题或建议，请在 [GitHub Issues](https://github.com/yourname/Rainbow-Tooltip-Tweaker/issues) 中反馈。
