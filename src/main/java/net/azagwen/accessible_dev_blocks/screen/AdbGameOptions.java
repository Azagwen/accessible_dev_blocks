package net.azagwen.accessible_dev_blocks.screen;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import net.azagwen.accessible_dev_blocks.AdbMain;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;

import java.io.*;

@Environment(EnvType.CLIENT)
public class AdbGameOptions {
    protected MinecraftClient client;
    public boolean structVoidVisibility;
    public StructureVoidRenderMode structVoidRenderMode;
    public double structVoidRenderDiameter;
    public boolean structVoidFadeBorders;
    public final File optionsFile;

    public AdbGameOptions(MinecraftClient client, File optionsFile) {
        this.structVoidVisibility = false;
        this.structVoidRenderMode = StructureVoidRenderMode.PARTICLE;
        this.structVoidRenderDiameter = 16;
        this.structVoidFadeBorders = true;
        this.optionsFile = new File(optionsFile, "config/adb_options.json");
        this.client = client;
    }

    public void write() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("structure_void_visibility", structVoidVisibility);
        jsonObject.addProperty("structure_void_render_mode", structVoidRenderMode.getId());
        jsonObject.addProperty("structure_void_render_diameter", (int) structVoidRenderDiameter);
        jsonObject.addProperty("structure_void_box_outline_fade_borders", structVoidFadeBorders);

        AdbMain.LOGGER.info(gson.toJson(jsonObject));

        try {
            if (optionsFile.createNewFile()) {
                AdbMain.LOGGER.info(optionsFile.getName() + " created.");
            } else {
                AdbMain.LOGGER.info(optionsFile.getName() + " exists, skipping.");
            }
        } catch (IOException e) {
            AdbMain.LOGGER.info("An error occurred creating " + optionsFile.getName());
            e.printStackTrace();
        }

        try {
            FileWriter writer = new FileWriter(optionsFile);
            writer.write(gson.toJson(jsonObject));
            writer.close();
            AdbMain.LOGGER.info("Successfully wrote options to " + optionsFile.getName());
        } catch (IOException e) {
            AdbMain.LOGGER.info("An error occurred writing to " + optionsFile.getName());
            e.printStackTrace();
        }
    }

    public void read() {
        if (optionsFile.exists()) {
            try {
                JsonReader reader = new JsonReader(new FileReader(optionsFile));
                JsonParser parser = new JsonParser();
                JsonObject json = parser.parse(reader).getAsJsonObject();

                boolean isVisible = json.has("structure_void_visibility") && json.get("structure_void_visibility").getAsBoolean();
                int renderMode = json.has("structure_void_render_mode") ? json.get("structure_void_render_mode").getAsInt() : 0;
                double renderDiameter = json.has("structure_void_render_diameter") ? json.get("structure_void_render_diameter").getAsDouble() : 8.0D;
                boolean fadeBorders = json.has("structure_void_box_outline_fade_borders") && json.get("structure_void_box_outline_fade_borders").getAsBoolean();

                this.structVoidVisibility = isVisible;
                this.structVoidRenderMode = StructureVoidRenderMode.values()[renderMode];
                this.structVoidRenderDiameter = renderDiameter;
                this.structVoidFadeBorders = fadeBorders;

            } catch (FileNotFoundException e) {
                AdbMain.LOGGER.info("An error occurred reading " + optionsFile.getName());
                e.printStackTrace();
            }
        }
    }
}
