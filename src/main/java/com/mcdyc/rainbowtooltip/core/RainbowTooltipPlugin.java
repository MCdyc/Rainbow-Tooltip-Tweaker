package com.mcdyc.rainbowtooltip.core;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import zone.rong.mixinbooter.IEarlyMixinLoader;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Coremod 插件 - 用于在游戏极早期注册 Mixin 配置
 * FontRenderer 属于 Minecraft 核心类，必须在极早期注入，否则会报 MixinTargetAlreadyLoadedException。
 */
@IFMLLoadingPlugin.MCVersion("1.12.2")
public class RainbowTooltipPlugin implements IFMLLoadingPlugin, IEarlyMixinLoader {

    @Override
    public List<String> getMixinConfigs() {
        return Collections.singletonList("mixins.rainbowtooltip.json");
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[0];
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Nullable
    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
