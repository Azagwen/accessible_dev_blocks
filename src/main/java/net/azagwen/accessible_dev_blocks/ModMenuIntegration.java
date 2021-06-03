package net.azagwen.accessible_dev_blocks;

import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfig;
import net.azagwen.accessible_dev_blocks.cloth_config.AdbAutoConfig;
import net.azagwen.accessible_dev_blocks.cloth_config.AdbConfigTest;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ModMenuIntegration implements ModMenuApi {
    private static final boolean useAutoConfig = true;

    @Override
    public String getModId() {
        return AdbMain.ADB_NAMESPACE;
    }

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            if (useAutoConfig) {
                return AutoConfig.getConfigScreen(AdbAutoConfig.class, parent).get();
            } else {
                return AdbConfigTest.getConfigBuilder().setParentScreen(parent).build();
            }
        };
    }
}
