# Rainbow Tooltip Tweaker

一个 CraftTweaker 附属模组，允许整合包作者通过 ZenScript 添加**动态**彩虹字体到物品 tooltip。

![Rainbow Tooltip Demo](https://i.imgur.com/placeholder.png)

## 功能特性

- 🌈 **动态彩虹效果** - 文本颜色会随时间平滑变化，呈现流动的彩虹效果
- ⚡ **高性能渲染** - 逐字符渲染，性能影响微小
- 🎨 **HSB 颜色模型** - 使用色相、饱和度、亮度模型，颜色过渡自然平滑
- 📝 **简单 ZenScript API** - 一行代码即可添加彩虹效果
- 🎯 **支持物品和矿辞** - 可以为单个物品或整类矿辞添加彩虹 tooltip

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

### 渲染流程

```
ZenScript: format("Hello")
         ↓
生成: "[RAINBOW]Hello"
         ↓
ItemTooltipEvent (LOWEST)
         ↓
替换为 "\u00A7uHello" (特殊标记)
         ↓
原版渲染白色文本 (\u00A7u 被忽略)
         ↓
RenderTooltipEvent.PostText
         ↓
检测标记，逐字符渲染彩虹色
         ↓
最终显示: 动态流动的彩虹文字
```

## 安装

1. 将模组 jar 文件放入 Minecraft 的 `mods` 文件夹
2. 确保已安装 [CraftTweaker 2](https://www.curseforge.com/minecraft/mc-mods/236222-crafttweaker-2)
3. 将 ZenScript 脚本放入 `scripts` 文件夹

## ZenScript API

### 基础用法

```zenscript
# 为文本添加动态彩虹效果
var rainbowText = mods.rainbowtooltip.RainbowTooltip.format("Hello World!");

# 将彩虹文本添加到物品 tooltip
<minecraft:diamond>.addTooltip(rainbowText);
```

### 完整示例

```zenscript
# 为钻石添加传奇彩虹 tooltip
<minecraft:diamond>.addTooltip("");
<minecraft:diamond>.addTooltip(mods.rainbowtooltip.RainbowTooltip.format("✦ LEGENDARY DIAMOND ✦"));

# 为金苹果添加动态彩虹 tooltip
<minecraft:golden_apple>.addTooltip("");
<minecraft:golden_apple>.addTooltip(mods.rainbowtooltip.RainbowTooltip.format("★ GODLY APPLE ★"));

# 为龙蛋添加彩虹 tooltip
<minecraft:dragon_egg>.addTooltip("");
<minecraft:dragon_egg>.addTooltip(mods.rainbowtooltip.RainbowTooltip.format("✨ DRAGON EGG ✨"));
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

### 性能

- 逐字符渲染对现代硬件性能影响**可忽略**
- 仅在物品悬停时触发，不影响游戏主场景帧率
- HSB 颜色计算每帧执行，但计算量很小

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

1. 动态彩虹效果仅在**客户端**可见
2. 使用 `<item>.addTooltip(text)` 添加彩虹文本，而不是 `<item>.tooltip.add(text)`
3. 颜色代码（如 `§c`）会重置彩虹效果的偏移量
4. 完整的 tooltip 渲染由客户端事件处理器处理

## 许可证

本项目采用 MIT 许可证。

## 致谢

- [CraftTweaker 2](https://github.com/jaredlll08/CraftTweaker2) - ZenScript 框架
- [Minecraft Forge](https://github.com/MinecraftForge/MinecraftForge) - 事件系统
- [CleanroomMC](https://github.com/CleanroomMC) - 构建工具链

## 问题反馈

如有问题或建议，请在 [GitHub Issues](https://github.com/yourname/Rainbow-Tooltip-Tweaker/issues) 中反馈。
