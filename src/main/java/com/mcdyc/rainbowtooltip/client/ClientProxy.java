package com.mcdyc.rainbowtooltip.client;

import com.mcdyc.rainbowtooltip.Tags;
import com.mcdyc.rainbowtooltip.common.CommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 客户端代理
 * 负责注册客户端专用的事件处理器
 */
public class ClientProxy extends CommonProxy {

    private static final Logger LOGGER = LogManager.getLogger(Tags.MOD_NAME + ":Client");

    /**
     * 初始化客户端事件处理器
     */
    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);

        // 注册 Tooltip 事件处理器
        TooltipEventHandler tooltipHandler = new TooltipEventHandler();
        MinecraftForge.EVENT_BUS.register(tooltipHandler);

        LOGGER.info("Client proxy initialized - Rainbow tooltip events registered");
    }

    /**
     * 获取 Minecraft 实例
     */
    public static Minecraft getMC() {
        return Minecraft.getMinecraft();
    }

    /**
     * 获取字体渲染器
     */
    public static FontRenderer getFontRenderer() {
        return getMC().fontRenderer;
    }
}
