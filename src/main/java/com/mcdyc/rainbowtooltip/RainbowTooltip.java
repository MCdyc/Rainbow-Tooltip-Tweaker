package com.mcdyc.rainbowtooltip;

import com.mcdyc.rainbowtooltip.common.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Rainbow Tooltip Tweaker - CraftTweaker 附属模组
 * 允许通过 ZenScript 为物品添加动态彩虹字体
 */
@Mod(modid = Tags.MOD_ID, name = Tags.MOD_NAME, version = Tags.VERSION)
public class RainbowTooltip
{

    public static final Logger LOGGER = LogManager.getLogger(Tags.MOD_NAME);
    public static RainbowTooltip instance;

    @SidedProxy(
        clientSide = "com.mcdyc.rainbowtooltip.client.ClientProxy",
        serverSide = "com.mcdyc.rainbowtooltip.server.ServerProxy"
    )
    public static CommonProxy proxy;

    public RainbowTooltip() {
        instance = this;
    }


    /**
     * 模组预初始化
     */
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOGGER.info("{} is loading...", Tags.MOD_NAME);
    }

    /**
     * 模组初始化 - 注册代理
     */
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        try {
            // 初始化代理（注册客户端事件）
            if (proxy != null) {
                proxy.init(event);
            }

            LOGGER.info("Proxy initialized: {}", proxy.getClass().getName());
        } catch (Exception e) {
            LOGGER.error("Failed to initialize mod", e);
        }
    }

    /**
     * 模组后初始化
     */
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        LOGGER.info("{} loaded successfully!", Tags.MOD_NAME);
    }
}
