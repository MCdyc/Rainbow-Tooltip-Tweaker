package com.mcdyc.rainbowtooltip.mixin;

import com.mcdyc.rainbowtooltip.util.RainbowTextUtil;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mixin(FontRenderer.class)
public abstract class MixinFontRenderer {

    @Unique
    private static final Pattern rainbowtooltip$SPEED_MARKER_PATTERN = Pattern.compile("\\u00A7z\\{(\\d{1,9})}");

    @Unique
    private static final long rainbowtooltip$DEFAULT_SPEED = 2000L;

    @Shadow private float alpha;

    @Unique
    private boolean rainbowtooltip$isRainbow = false;

    @Unique
    private boolean rainbowtooltip$isShadow = false;

    @Unique
    private int rainbowtooltip$rainbowOffset = 0;

    @Unique
    private long rainbowtooltip$rainbowSpeed = rainbowtooltip$DEFAULT_SPEED;

    /**
     * 将 §z 替换为一个不可见的 Unicode 字符 \uFFFF
     * 这样做可以利用原版的字符解析机制安全地触发我们的状态切换。
     *
     * renderStringAtPos 方法签名: private void renderStringAtPos(String text, boolean shadow)
     */
    @ModifyVariable(method = "renderStringAtPos", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private String rainbowtooltip$modifyText(String text) {
        rainbowtooltip$rainbowSpeed = rainbowtooltip$DEFAULT_SPEED;

        if (text != null) {
            Matcher matcher = rainbowtooltip$SPEED_MARKER_PATTERN.matcher(text);
            if (matcher.find()) {
                long speed = rainbowtooltip$DEFAULT_SPEED;
                try {
                    speed = Long.parseLong(matcher.group(1));
                } catch (NumberFormatException ignored) {
                    // 保持默认值
                }
                rainbowtooltip$rainbowSpeed = speed > 0L ? speed : rainbowtooltip$DEFAULT_SPEED;
                return matcher.replaceFirst("\uFFFF");
            }
        }

        if (text != null && text.contains("\u00A7z")) {
            return text.replace("\u00A7z", "\uFFFF");
        }
        return text;
    }

    /**
     * 在每次开始渲染一段文字前，重置状态。
     * 注意: renderStringAtPos 返回 void，所以用 CallbackInfo 而非 CallbackInfoReturnable
     */
    @Inject(method = "renderStringAtPos", at = @At("HEAD"))
    private void rainbowtooltip$onRenderStringAtPosHead(String text, boolean shadow, CallbackInfo ci) {
        rainbowtooltip$isShadow = shadow;
        rainbowtooltip$isRainbow = false;
        rainbowtooltip$rainbowOffset = 0;
    }

    /**
     * 当原版遇到合法的颜色代码（如 §c 或 §r）时，它会调用 GlStateManager.color()
     * 此时我们需要关闭彩虹模式，让后续的普通颜色正常生效！
     */
    @Inject(method = "renderStringAtPos", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;color(FFFF)V"))
    private void rainbowtooltip$onVanillaColorChange(String text, boolean shadow, CallbackInfo ci) {
        rainbowtooltip$isRainbow = false;
    }

    /**
     * 拦截英文字符渲染的入口，实时应用当前的彩虹颜色，并处理原版阴影的变暗。
     * renderDefaultChar 签名: protected float renderDefaultChar(int ch, boolean italic)
     */
    @Inject(method = "renderDefaultChar", at = @At("HEAD"))
    private void rainbowtooltip$onRenderDefaultChar(int ch, boolean italic, CallbackInfoReturnable<Float> cir) {
        rainbowtooltip$applyRainbowColor();
    }

    /**
     * 拦截中文字符/特殊字符渲染的入口，并监听隐形的触发符 \uFFFF。
     * renderUnicodeChar 签名: protected float renderUnicodeChar(char ch, boolean italic)
     */
    @Inject(method = "renderUnicodeChar", at = @At("HEAD"), cancellable = true)
    private void rainbowtooltip$onRenderUnicodeChar(char ch, boolean italic, CallbackInfoReturnable<Float> cir) {
        if (ch == '\uFFFF') {
            rainbowtooltip$isRainbow = true;
            rainbowtooltip$rainbowOffset = 0; // 重置渐变位置，让新段落从头开始渐变
            cir.setReturnValue(0.0F); // 返回宽度 0，让这个触发符彻底隐形
            return;
        }
        rainbowtooltip$applyRainbowColor();
    }

    @Unique
    private void rainbowtooltip$applyRainbowColor() {
        if (rainbowtooltip$isRainbow) {
            int rgb = RainbowTextUtil.getDynamicRainbowColor(rainbowtooltip$rainbowOffset++, rainbowtooltip$rainbowSpeed, 0.05F);

            float r = ((rgb >> 16) & 0xFF) / 255.0F;
            float g = ((rgb >> 8) & 0xFF) / 255.0F;
            float b = (rgb & 0xFF) / 255.0F;

            // 完美还原原版阴影效果：如果是画阴影，将 RGB 各自除以 4
            if (rainbowtooltip$isShadow) {
                r /= 4.0F;
                g /= 4.0F;
                b /= 4.0F;
            }

            GlStateManager.color(r, g, b, this.alpha);
        }
    }
}
