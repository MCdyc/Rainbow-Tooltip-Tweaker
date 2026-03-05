package com.mcdyc.rainbowtooltip.tweaker;

import com.mcdyc.rainbowtooltip.Tags;
import com.mcdyc.rainbowtooltip.util.RainbowTextUtil;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.oredict.IOreDictEntry;
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

        String rainbowText = format(text);

        try {
            // 使用 minetweaker 的 addTooltip 方法
            // 通过反射调用，因为 CraftTweaker API 1.12.2 版本中 addTooltip 是实例方法
            Class<?> itemStackClass = item.getClass();
            java.lang.reflect.Method addTooltipMethod = itemStackClass.getMethod("addTooltip", String.class);
            addTooltipMethod.invoke(item, rainbowText);

            CraftTweakerAPI.logInfo("Added rainbow tooltip to " + item.getDisplayName());
        } catch (Exception e) {
            // 如果 addTooltip 不存在，记录错误
            LOGGER.error("Failed to add tooltip via reflection", e);
            CraftTweakerAPI.logError("Unable to add rainbow tooltip. " +
                "Please use: <item>.addTooltip(mods.rainbowtooltip.RainbowTooltip.format(\"text\"))");
        }
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
        CraftTweakerAPI.logInfo("Added rainbow tooltip for OreDict " + oreDict.getName() + ": " + rainbowText);

        // 注意: OreDict 条目需要遍历包含的物品分别添加 tooltip
        try {
            Class<?> oreDictClass = oreDict.getClass();
            java.lang.reflect.Method getItemsMethod = oreDictClass.getMethod("getItems");
            Object[] items = (Object[]) getItemsMethod.invoke(oreDict);

            if (items != null) {
                for (Object itemObj : items) {
                    if (itemObj instanceof IItemStack) {
                        IItemStack item = (IItemStack) itemObj;
                        addTooltip(item, text);
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("Failed to add tooltip to OreDict items", e);
        }
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
