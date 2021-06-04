package net.azagwen.accessible_dev_blocks;

import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import net.azagwen.accessible_dev_blocks.screen.AdbOptionScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ModMenuIntegration implements ModMenuApi {

    @Override
    public String getModId() {
        return AdbMain.ADB_NAMESPACE;
    }

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> new AdbOptionScreen(parent, AdbClient.settings);
    }
}
