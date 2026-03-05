package com.mcdyc.rainbowtooltip.client;

import com.mcdyc.rainbowtooltip.Tags;
import com.mcdyc.rainbowtooltip.util.RainbowTextUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraftforge.client.event.RenderTooltipEvent;

import java.awt.Color;
import java.util.List;

/**
 * 客户端 Tooltip 事件处理器
 * 处理物品 Tooltip 中的动态彩虹文本渲染
 */
@SideOnly(Side.CLIENT)
public class TooltipEventHandler {

    private static final Logger LOGGER = LogManager.getLogger("RainbowTooltip");

    /**
     * 处理物品 Tooltip 事件
     * 检测并渲染包含彩虹标记的文本
     */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onItemTooltip(ItemTooltipEvent event) {
        if (event.isCanceled()) {
            return;
        }

        List<String> tooltip = event.getToolTip();

        if (tooltip == null) {
            return;
        }

        // 检测并处理每一行 tooltip 文本
        boolean hasRainbow = false;
        for (int i = 0; i < tooltip.size(); i++) {
            String line = tooltip.get(i);

            if (RainbowTextUtil.hasRainbowMarker(line)) {
                // 替换为去除标记的文本
                // 实际渲染由 Minecraft 的 tooltip 渲染系统处理
                // 由于需要逐字符渲染不同颜色，需要使用自定义渲染方式
                String strippedText = RainbowTextUtil.removeMarkers(line);
                tooltip.set(i, "\u00A7u" + strippedText); // 使用特殊的不可见颜色代码作为标记
                hasRainbow = true;
            }
        }
    }

    /**
     * 在 Tooltip 文本绘制后进行动态彩虹效果覆盖
     */
    @SubscribeEvent
    public void onRenderTooltipText(RenderTooltipEvent.PostText event) {
        if (event.getLines() == null || event.getLines().isEmpty()) return;

        int tooltipX = event.getX();
        int tooltipY = event.getY();
        FontRenderer font = event.getFontRenderer();

        for (int i = 0; i < event.getLines().size(); i++) {
            String line = event.getLines().get(i);
            
            if (line != null && line.contains("\u00A7u")) {
                // 去除所有颜色代码和自定义标记以获取纯文本进行彩虹渲染
                String cleanText = line.replaceAll("\u00A7.", "");
                // 绘制带阴影效果以匹配原版（因为原版已经绘制了一次默认颜色的文本及阴影，我们通过带阴影覆盖可以完美契合）
                // 为了避免和原版的字体重叠产生锯齿，我们在彩虹绘制时直接覆盖对应的位置。
                
                // 由于原版在 RenderTooltipEvent.PostText 之前已经使用了 font.drawStringWithShadow 画过了该行文字，
                // 而且我们的文本之前被设为了 "\u00A7u..."，原版的 fontRenderer 会跳过未知的 \u00A7u 继续渲染文本（通常是白色）。
                // 所以我们现在只需要在这里重新用彩虹色绘制即可覆盖白色。
                // 如果需要连阴影一起重画彩虹，可以先偏移 1, 1 画深色彩虹或者黑色，然后再画本体。但原版的黑色阴影已经存在了，我们只需画本体覆盖。
                
                // 为了增强覆盖效果并消除可能的白色边缘，可以在渲染前设置 zLevel 或直接叠加。
                GlStateManager.pushMatrix();
                // 稍微向前移一点以覆盖原文本
                GlStateManager.translate(0.0F, 0.0F, 0.01F);
                
                renderDynamicRainbowText(cleanText, tooltipX, tooltipY, font);
                
                GlStateManager.popMatrix();
            }

            if (i == 0) {
                tooltipY += 2;
            }
            tooltipY += 10;
        }
    }

    /**
     * 渲染动态彩虹文本
     * 这个方法需要在自定义的渲染器中调用
     *
     * @param text 要渲染的文本
     * @param x X 坐标
     * @param y Y 坐标
     * @param fontRenderer 字体渲染器
     * @return 渲染的总宽度
     */
    public static int renderDynamicRainbowText(String text, float x, float y, FontRenderer fontRenderer) {
        if (text == null || text.isEmpty() || fontRenderer == null) {
            return 0;
        }

        float currentX = x;
        int rainbowOffset = 0;

        // 逐个字符绘制，每个字符应用动态计算的颜色
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            // 跳过 Minecraft 颜色代码符号
            if (c == '\u00A7') {
                if (i + 1 < text.length()) {
                    rainbowOffset = 0; // 遇到颜色代码时重置彩虹偏移
                    i++; // 跳过颜色代码字符
                }
                continue;
            }

            // 计算当前字符的动态彩虹颜色
            int rgb = RainbowTextUtil.getDynamicRainbowColor(rainbowOffset);
            rainbowOffset++;

            // 分离 R, G, B
            float r = ((rgb >> 16) & 0xFF) / 255.0F;
            float g = ((rgb >> 8) & 0xFF) / 255.0F;
            float b = (rgb & 0xFF) / 255.0F;

            // 设置 OpenGL 颜色
            GlStateManager.color(r, g, b, 1.0F);

            // 绘制单个字符 - cast to int for x, y
            String charStr = String.valueOf(c);
            fontRenderer.drawString(charStr, (int) currentX, (int) y, rgb);
            currentX += fontRenderer.getStringWidth(charStr);
        }

        // 重置颜色为白色
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        return (int) (currentX - x);
    }

    /**
     * 渲染自定义颜色的彩虹文本
     *
     * @param text 要渲染的文本
     * @param x X 坐标
     * @param y Y 坐标
     * @param fontRenderer 字体渲染器
     * @param speed 颜色变化速度
     * @param density 颜色波浪密度
     * @return 渲染的总宽度
     */
    public static int renderDynamicRainbowText(String text, float x, float y, FontRenderer fontRenderer, long speed, float density) {
        if (text == null || text.isEmpty() || fontRenderer == null) {
            return 0;
        }

        float currentX = x;
        int rainbowOffset = 0;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            if (c == '\u00A7') {
                if (i + 1 < text.length()) {
                    rainbowOffset = 0;
                    i++;
                }
                continue;
            }

            int rgb = RainbowTextUtil.getDynamicRainbowColor(rainbowOffset, speed, density);
            rainbowOffset++;

            float r = ((rgb >> 16) & 0xFF) / 255.0F;
            float g = ((rgb >> 8) & 0xFF) / 255.0F;
            float b = (rgb & 0xFF) / 255.0F;

            GlStateManager.color(r, g, b, 1.0F);

            String charStr = String.valueOf(c);
            fontRenderer.drawString(charStr, (int) currentX, (int) y, rgb);
            currentX += fontRenderer.getStringWidth(charStr);
        }

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        return (int) (currentX - x);
    }
}
