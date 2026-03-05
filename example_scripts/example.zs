# Rainbow Tooltip Tweaker 示例脚本
# 这个模组允许通过 ZenScript 添加动态彩虹字体

# ===== 基础用法 =====

# 1. 为文本添加动态彩虹效果
var rainbowText = mods.rainbowtooltip.RainbowTooltip.format("Hello World!");
print(rainbowText);

# 2. 将物品的全局展示名变更为彩虹色（掉在地上、拿在手里都可见）
<minecraft:diamond>.displayName = rainbowText;

# 3. 将彩虹文本仅仅追加到物品 tooltip 的末尾
<minecraft:diamond>.addTooltip(rainbowText);

# ===== 物品名称 & Tooltip 示例 =====

# 为钻石修改传说彩虹名
<minecraft:diamond>.displayName = mods.rainbowtooltip.RainbowTooltip.format("✦ LEGENDARY DIAMOND ✦");
<minecraft:diamond>.addTooltip("");
<minecraft:diamond>.addTooltip(mods.rainbowtooltip.RainbowTooltip.format("It flows with ancient power..."));

# 为金苹果保留原名，只加动态彩虹 tooltip
<minecraft:golden_apple>.addTooltip("");
<minecraft:golden_apple>.addTooltip(mods.rainbowtooltip.RainbowTooltip.format("★ GODLY APPLE ★"));

# 为龙蛋也彻底改名为彩虹名
<minecraft:dragon_egg>.displayName = mods.rainbowtooltip.RainbowTooltip.format("✨ DRAGON EGG ✨");

# ===== 批量处理 =====

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

# 为所有矿物添加彩虹标记
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

# ===== 矿辞示例 =====

# 为铁锭矿辞添加彩虹 tooltip
mods.rainbowtooltip.RainbowTooltip.addTooltip(<ore:ingotIron>, "IRON COLLECTION");

# ===== 高级用法 =====

# 检查文本是否包含彩虹标记
if (mods.rainbowtooltip.RainbowTooltip.hasRainbowMarker(rainbowText)) {
    print("Text has rainbow marker!");
}

# 移除彩虹标记，获取纯文本
var plainText = mods.rainbowtooltip.RainbowTooltip.removeMarker(rainbowText);
print("Plain text: " + plainText);

# ===== 组合使用示例 =====

# 为附魔书添加动态彩虹效果
<minecraft:enchanted_book>.addTooltip("");
<minecraft:enchanted_book>.addTooltip(mods.rainbowtooltip.RainbowTooltip.format("✧ MAGIC ✧"));

# 为末影珍珠添加彩虹效果
<minecraft:ender_pearl>.addTooltip("");
<minecraft:ender_pearl>.addTooltip(mods.rainbowtooltip.RainbowTooltip.format("◈ VOID ENERGY ◈"));

# ===== 工具类示例 =====

# 创建一个彩虹文本函数
function addRainbowTooltip(item, text) {
    item.addTooltip("");
    item.addTooltip(mods.rainbowtooltip.RainbowTooltip.format(text));
}

# 使用函数
addRainbowTooltip(<minecraft:bedrock>, "✓ INDESTRUCTIBLE ✓");
addRainbowTooltip(<minecraft:command_block>, "⚙ ADMIN ONLY ⚙");

print("Rainbow Tooltip Tweaker example script loaded!");
