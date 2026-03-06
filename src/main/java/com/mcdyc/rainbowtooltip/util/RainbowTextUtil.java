package com.mcdyc.rainbowtooltip.util;

import net.minecraft.client.Minecraft;

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
     * 渐变标记前缀
     */
    public static final String GRADIENT_MARKER = "[GRADIENT]";

    /**
     * 动态彩虹颜色计算 (HSB 模型)
     *
     * @param characterIndex 字符在文本中的位置索引
     * @return 动态计算的 RGB 颜色值
     */
    public static int getDynamicRainbowColor(int characterIndex) {
        long time = Minecraft.getSystemTime();

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
        long time = Minecraft.getSystemTime();
        float hue = (time % speed) / (float) speed + (characterIndex * density);
        return Color.HSBtoRGB(hue, 1.0F, 1.0F);
    }

    /**
     * 获取渐变颜色 (静态)
     *
     * @param startRGB 起始颜色 RGB 值
     * @param endRGB 结束颜色 RGB 值
     * @param currentIndex 当前字符索引
     * @param totalChars 总字符数
     * @return 渐变 RGB 颜色值
     */
    public static int getGradientColor(int startRGB, int endRGB, int currentIndex, int totalChars) {
        if (totalChars <= 1) {
            return startRGB;
        }

        float ratio = (float) currentIndex / (totalChars - 1);

        int startR = (startRGB >> 16) & 0xFF;
        int startG = (startRGB >> 8) & 0xFF;
        int startB = startRGB & 0xFF;

        int endR = (endRGB >> 16) & 0xFF;
        int endG = (endRGB >> 8) & 0xFF;
        int endB = endRGB & 0xFF;

        int r = (int) (startR + (endR - startR) * ratio);
        int g = (int) (startG + (endG - startG) * ratio);
        int b = (int) (startB + (endB - startB) * ratio);

        return (r << 16) | (g << 8) | b;
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
     * 检查文本是否包含渐变标记
     *
     * @param text 输入文本
     * @return 是否包含渐变标记
     */
    public static boolean hasGradientMarker(String text) {
        return text != null && text.toUpperCase().contains(GRADIENT_MARKER);
    }

    /**
     * 移除动态彩虹标记
     *
     * @param text 输入文本
     * @return 移除标记后的文本
     */
    public static String removeMarkers(String text) {
        if (text == null) return "";
        return text.replaceAll("\u00A7z", "")
                   .replaceAll("(?i)\\[GRADIENT\\]", "");
    }
}
