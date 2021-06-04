package net.azagwen.accessible_dev_blocks.screen;

import net.azagwen.accessible_dev_blocks.AdbMain;
import net.minecraft.text.TranslatableText;

public class AdbOptionTranslatableText extends TranslatableText{

    public AdbOptionTranslatableText(String key) {
        super(AdbMain.ADB_NAMESPACE + ".options." + key);
    }

    public AdbOptionTranslatableText(String key, Object... args) {
        super(AdbMain.ADB_NAMESPACE + ".options." + key, args);
    }
}
