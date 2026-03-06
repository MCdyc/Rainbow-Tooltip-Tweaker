package com.mcdyc.rainbowtooltip.tweaker;

import com.mcdyc.rainbowtooltip.Tags;
import com.mcdyc.rainbowtooltip.util.RainbowTextUtil;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.formatting.IFormattedText;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.oredict.IOreDictEntry;
import crafttweaker.api.tooltip.IngredientTooltips;
import crafttweaker.mc1120.formatting.FormattedString;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

/**
 * Rainbow Tooltip - CraftTweaker API
 * 提供 ZenScript 方法来为文本添加动态彩虹标记
 */
@ZenRegister
@ZenClass("mods.rainbowtooltip.RainbowTooltip")
public class RainbowTooltip {

    private static final Logger LOGGER = LogManager.getLogger(Tags.MOD_NAME + ":ZenScript");
    public static final RainbowTooltip INSTANCE = new RainbowTooltip();
    private static final long DEFAULT_RAINBOW_SPEED = 2000L;

    /**
     * 创建动态彩虹文本标记
     * 添加此标记的文本将在客户端以动态彩虹效果渲染
     *
     * @param text 输入文本
     * @return 带有彩虹标记的文本
     *
     * 使用示例:
     * var text = mods.rainbowtooltip.RainbowTooltip.format("Hello World!");
     */
    @ZenMethod
    public static String format(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }
        return RainbowTextUtil.RAINBOW_MARKER + text;
    }

    /**
     * 创建支持自定义速度的动态彩虹文本标记
     *
     * @param text 输入文本
     * @param speedMs 颜色循环周期（毫秒）
     * @return 带有彩虹标记的文本
     */
    @ZenMethod
    public static String formatWithSpeed(String text, int speedMs) {
        if (text == null || text.isEmpty()) {
            return "";
        }

        long safeSpeed = speedMs > 0 ? speedMs : DEFAULT_RAINBOW_SPEED;
        return RainbowTextUtil.RAINBOW_MARKER + "{" + safeSpeed + "}" + text;
    }

    /**
     * 为物品添加动态彩虹 tooltip
     *
     * @param item 物品堆叠
     * @param text 要显示的文本
     *
     * 使用示例:
     * mods.rainbowtooltip.RainbowTooltip.addTooltip(<minecraft:diamond>, "Legendary Diamond!");
     */
    @ZenMethod
    public static void addTooltip(IItemStack item, String text) {
        if (item == null || text == null || text.isEmpty()) {
            return;
        }

        addTooltipInternal(item, format(text));
        CraftTweakerAPI.logInfo("Added rainbow tooltip to " + item.getDisplayName());
    }

    /**
     * 为物品添加支持自定义速度的动态彩虹 tooltip
     *
     * @param item 物品堆叠
     * @param text 要显示的文本
     * @param speedMs 颜色循环周期（毫秒）
     */
    @ZenMethod
    public static void addTooltip(IItemStack item, String text, int speedMs) {
        if (item == null || text == null || text.isEmpty()) {
            return;
        }

        addTooltipInternal(item, formatWithSpeed(text, speedMs));
        CraftTweakerAPI.logInfo("Added rainbow tooltip with custom speed to " + item.getDisplayName());
    }

    /**
     * 为矿辞条目添加动态彩虹 tooltip
     *
     * @param oreDict 矿字典条目
     * @param text 要显示的文本
     *
     * 使用示例:
     * mods.rainbowtooltip.RainbowTooltip.addTooltip(<ore:ingotIron>, "Iron Collection");
     */
    @ZenMethod
    public static void addTooltip(IOreDictEntry oreDict, String text) {
        if (oreDict == null || text == null || text.isEmpty()) {
            return;
        }

        String rainbowText = format(text);
        addTooltipInternal(oreDict, rainbowText);
        CraftTweakerAPI.logInfo("Added rainbow tooltip for OreDict " + oreDict.getName() + ": " + rainbowText);
    }

    private static void addTooltipInternal(crafttweaker.api.item.IIngredient ingredient, String tooltipText) {
        IFormattedText formattedText = new FormattedString(tooltipText);
        IngredientTooltips.addTooltip(ingredient, formattedText);
    }

    /**
     * 检查文本是否包含彩虹标记
     *
     * @param text 输入文本
     * @return 是否包含彩虹标记
     */
    @ZenMethod
    public static boolean hasRainbowMarker(String text) {
        return RainbowTextUtil.hasRainbowMarker(text);
    }

    /**
     * 移除彩虹标记，返回纯文本
     *
     * @param text 输入文本
     * @return 移除标记后的文本
     */
    @ZenMethod
    public static String removeMarker(String text) {
        return RainbowTextUtil.removeMarkers(text);
    }
}
