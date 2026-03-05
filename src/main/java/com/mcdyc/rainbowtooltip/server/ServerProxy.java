package com.mcdyc.rainbowtooltip.server;

import com.mcdyc.rainbowtooltip.common.CommonProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

/**
 * 服务器代理
 */
public class ServerProxy extends CommonProxy {

    /**
     * 初始化方法
     */
    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        // 服务器端不需要特殊初始化
    }
}
