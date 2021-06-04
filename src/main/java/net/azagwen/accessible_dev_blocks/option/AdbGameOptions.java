package net.azagwen.accessible_dev_blocks.option;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import net.azagwen.accessible_dev_blocks.AdbMain;
import net.azagwen.accessible_dev_blocks.screen.StructureVoidRenderMode;
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
    public double structVoidBoxColorRed;
    public double structVoidBoxColorGreen;
    public double structVoidBoxColorBlue;
    public final File optionsFile;

    public AdbGameOptions(MinecraftClient client, File optionsFile) {
        this.reset();
        this.optionsFile = new File(optionsFile, "config/adb_options.json");
        this.client = client;
    }

    public void reset() {
        this.structVoidRenderMode = AdbDefaultOptions.defaultStructVoidRenderMode;
        this.structVoidRenderDiameter = AdbDefaultOptions.defaultStructVoidRenderDiameter;
        this.structVoidFadeBorders = AdbDefaultOptions.defaultStructVoidFadeBorders;
        this.structVoidBoxColorRed = AdbDefaultOptions.defaultStructVoidBoxColorRed;
        this.structVoidBoxColorGreen = AdbDefaultOptions.defaultStructVoidBoxColorGreen;
        this.structVoidBoxColorBlue = AdbDefaultOptions.defaultStructVoidBoxColorBlue;
    }

    public void write() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject jsonObject = new JsonObject();
        JsonArray colorJsonArray = new JsonArray();

        colorJsonArray.add((int) this.structVoidBoxColorRed);
        colorJsonArray.add((int) this.structVoidBoxColorGreen);
        colorJsonArray.add((int) this.structVoidBoxColorBlue);

        jsonObject.addProperty("structure_void_visibility", this.structVoidVisibility);
        jsonObject.addProperty("structure_void_render_mode", this.structVoidRenderMode.getId());
        jsonObject.addProperty("structure_void_render_diameter", (int) this.structVoidRenderDiameter);
        jsonObject.addProperty("structure_void_box_outline_fade_borders", this.structVoidFadeBorders);
        jsonObject.add("structure_void_box_outline_color", colorJsonArray);

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
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                JsonReader reader = new JsonReader(new FileReader(optionsFile));
                JsonParser parser = new JsonParser();
                JsonObject json = parser.parse(reader).getAsJsonObject();

                boolean isVisible = json.has("structure_void_visibility") && json.get("structure_void_visibility").getAsBoolean();
                int renderMode = json.has("structure_void_render_mode") ? json.get("structure_void_render_mode").getAsInt() : 0;
                double renderDiameter = json.has("structure_void_render_diameter") ? json.get("structure_void_render_diameter").getAsDouble() : 8.0D;
                boolean fadeBorders = json.has("structure_void_box_outline_fade_borders") && json.get("structure_void_box_outline_fade_borders").getAsBoolean();
                double colorRed = 255.0D;
                double colorGreen = 255.0D;
                double colorBlue = 255.0D;

                if (json.has("structure_void_box_outline_color")) {
                    JsonArray colors = json.get("structure_void_box_outline_color").getAsJsonArray();
                    colorRed = colors.get(0).getAsDouble();
                    colorGreen = colors.get(1).getAsDouble();
                    colorBlue = colors.get(2).getAsDouble();
                }

                this.structVoidVisibility = isVisible;
                this.structVoidRenderMode = StructureVoidRenderMode.values()[renderMode];
                this.structVoidRenderDiameter = renderDiameter;
                this.structVoidFadeBorders = fadeBorders;
                this.structVoidBoxColorRed = colorRed;
                this.structVoidBoxColorGreen = colorGreen;
                this.structVoidBoxColorBlue = colorBlue;

            } catch (FileNotFoundException e) {
                AdbMain.LOGGER.info("An error occurred reading " + optionsFile.getName());
                e.printStackTrace();
            }
        }
    }
}
