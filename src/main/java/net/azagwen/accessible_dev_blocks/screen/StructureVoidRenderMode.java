package net.azagwen.accessible_dev_blocks.screen;

import net.minecraft.text.Text;

public enum StructureVoidRenderMode {
    PARTICLE(0, "particle"),
    BOX_OUTLINE(1, "box_outline");

    private final int id;
    private final String name;

    StructureVoidRenderMode(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public StructureVoidRenderMode next()
    {
        //loop back to start if on last enum
        if(ordinal() + 1 == values().length)
        {
            return values()[0];
        }
        return values()[ordinal() + 1];
    }

    public Text getTranslatableName() {
        return new AdbOptionTranslatableText("renderMode." + this.name);
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }
}
