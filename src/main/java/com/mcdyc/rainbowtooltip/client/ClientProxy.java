package com.mcdyc.rainbowtooltip.client;

import com.mcdyc.rainbowtooltip.Tags;
import com.mcdyc.rainbowtooltip.common.CommonProxy;
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

        LOGGER.info("Client proxy initialized - Rainbow tooltip events registered");
    }
}
