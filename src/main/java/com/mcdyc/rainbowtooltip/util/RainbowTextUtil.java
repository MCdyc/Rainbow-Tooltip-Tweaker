package com.mcdyc.rainbowtooltip.util;

import java.awt.Color;

/**
 * 彩虹文本工具类 - 支持动态彩虹效果
 */
public class RainbowTextUtil {

    /**
     * 动态彩虹标记前缀
     */
    public static final String RAINBOW_MARKER = "\u00A7z";

    /**
     * 动态彩虹颜色计算 (HSB 模型)
     *
     * @param characterIndex 字符在文本中的位置索引
     * @return 动态计算的 RGB 颜色值
     */
    public static int getDynamicRainbowColor(int characterIndex) {
        long time = System.currentTimeMillis();

        // HSB 平滑算法：利用时间和字符位置计算色相 (Hue)
        // time % 2000L / 2000.0F - 时间因子，控制整体颜色循环速度
        // characterIndex * 0.05F - 位置因子，控制波浪的密集度
        float hue = (time % 2000L) / 2000.0F + (characterIndex * 0.05F);

        return Color.HSBtoRGB(hue, 1.0F, 1.0F);
    }

    /**
     * 获取动态彩虹颜色 (可自定义速度和密度)
     *
     * @param characterIndex 字符在文本中的位置索引
     * @param speed 颜色变化速度 (默认 2000ms 为一个周期)
     * @param density 颜色波浪密度 (默认 0.05)
     * @return 动态计算的 RGB 颜色值
     */
    public static int getDynamicRainbowColor(int characterIndex, long speed, float density) {
        if (speed <= 0L) {
            speed = 2000L;
        }
        long time = System.currentTimeMillis();
        float hue = (time % speed) / (float) speed + (characterIndex * density);
        return Color.HSBtoRGB(hue, 1.0F, 1.0F);
    }



    /**
     * 解析颜色字符串为 RGB 值
     *
     * @param colorCode Minecraft 颜色代码 (如 "§c")
     * @return RGB 颜色值
     */
    public static int parseColorCode(String colorCode) {
        if (colorCode == null || colorCode.length() < 2) {
            return 0xFFFFFF; // 默认白色
        }

        char code = colorCode.charAt(1);
        switch (code) {
            case '0': return 0x000000; // 黑色
            case '1': return 0x0000AA; // 深蓝
            case '2': return 0x00AA00; // 深绿
            case '3': return 0x00AAAA; // 深青
            case '4': return 0xAA0000; // 深红
            case '5': return 0xAA00AA; // 深紫
            case '6': return 0xFFAA00; // 金色
            case '7': return 0xAAAAAA; // 灰色
            case '8': return 0x555555; // 深灰
            case '9': return 0x5555FF; // 蓝色
            case 'a': return 0x55FF55; // 绿色
            case 'b': return 0x55FFFF; // 青色
            case 'c': return 0xFF5555; // 红色
            case 'd': return 0xFF55FF; // 紫色
            case 'e': return 0xFFFF55; // 黄色
            case 'f': return 0xFFFFFF; // 白色
            default: return 0xFFFFFF;
        }
    }

    /**
     * 检查文本是否包含动态彩虹标记
     *
     * @param text 输入文本
     * @return 是否包含彩虹标记
     */
    public static boolean hasRainbowMarker(String text) {
        return text != null && text.contains(RAINBOW_MARKER);
    }

    /**
     * 移除动态彩虹标记
     *
     * @param text 输入文本
     * @return 移除标记后的文本
     */
    public static String removeMarkers(String text) {
        if (text == null) return "";
        return text.replace("\u00A7z", "");
    }
}
