package net.azagwen.accessible_dev_blocks.option;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import net.azagwen.accessible_dev_blocks.AdbMain;
import net.azagwen.accessible_dev_blocks.screen.StructureVoidRenderMode;
import net.azagwen.accessible_dev_blocks.utils.AdbUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;

import java.awt.*;
import java.io.*;

@Environment(EnvType.CLIENT)
public class AdbGameOptions {
    protected MinecraftClient client;
    public boolean structVoidVisibility;
    public StructureVoidRenderMode structVoidRenderMode;
    public double structVoidRenderDiameter;
    public boolean structVoidFadeBorders;
    public double structVoidColorRed;
    public double structVoidColorGreen;
    public double structVoidColorBlue;
    public String structVoidColor;
    public final File optionsFile;
    public final boolean logDebugInfo = false;

    public AdbGameOptions(MinecraftClient client, File optionsFile) {
        this.reset();
        this.optionsFile = new File(optionsFile, "config/adb_options.json");
        this.client = client;
    }

    public void reset() {
        this.structVoidRenderMode = AdbDefaultOptions.defaultStructVoidRenderMode;
        this.structVoidRenderDiameter = AdbDefaultOptions.defaultStructVoidRenderDiameter;
        this.structVoidFadeBorders = AdbDefaultOptions.defaultStructVoidFadeBorders;
        this.structVoidColorRed = Color.decode(AdbDefaultOptions.defaultStructVoidColor).getRed();
        this.structVoidColorGreen = Color.decode(AdbDefaultOptions.defaultStructVoidColor).getGreen();
        this.structVoidColorBlue = Color.decode(AdbDefaultOptions.defaultStructVoidColor).getBlue();
        this.structVoidColor = AdbDefaultOptions.defaultStructVoidColor;
    }

    private void setColorFromSliders () {
        this.structVoidColor = AdbUtils.getHexFromColor(
                new Color(
                        (int) this.structVoidColorRed,
                        (int) this.structVoidColorGreen,
                        (int) this.structVoidColorBlue
                )
        );
    }

    public void write() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject jsonObject = new JsonObject();

        //Populate the JSON
        jsonObject.addProperty("structure_void_visibility", this.structVoidVisibility);
        jsonObject.addProperty("structure_void_render_mode", this.structVoidRenderMode.getId());
        jsonObject.addProperty("structure_void_render_diameter", (int) this.structVoidRenderDiameter);
        jsonObject.addProperty("structure_void_box_outline_fade_borders", this.structVoidFadeBorders);
        jsonObject.addProperty("structure_void_box_outline_color", this.structVoidColor);

        //Create the File
        try {
            if (optionsFile.createNewFile()) {
                if (logDebugInfo)
                    AdbMain.LOGGER.info(optionsFile.getName() + " created.");
            } else {
                if (logDebugInfo)
                    AdbMain.LOGGER.info(optionsFile.getName() + " exists, skipping.");
            }
        } catch (IOException e) {
            AdbMain.LOGGER.info("An error occurred creating " + optionsFile.getName());
            e.printStackTrace();
        }

        //Write in the File
        try {
            FileWriter writer = new FileWriter(optionsFile);
            writer.write(gson.toJson(jsonObject));
            writer.close();
            if (logDebugInfo)
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

                //Search and Read values from the JSON, and assign them to temp fields.
                boolean isVisible = json.has("structure_void_visibility") && json.get("structure_void_visibility").getAsBoolean();
                int renderMode = json.has("structure_void_render_mode") ? json.get("structure_void_render_mode").getAsInt() : 0;
                double renderDiameter = json.has("structure_void_render_diameter") ? json.get("structure_void_render_diameter").getAsDouble() : 8.0D;
                boolean fadeBorders = json.has("structure_void_box_outline_fade_borders") && json.get("structure_void_box_outline_fade_borders").getAsBoolean();
                String boxColorStr = json.has("structure_void_box_outline_color") ? json.get("structure_void_box_outline_color").getAsString() : "#ffffff";
                Color boxColor = Color.decode(boxColorStr);

                //Write values from the JSON to the fields in this class.
                this.structVoidVisibility = isVisible;
                this.structVoidRenderMode = StructureVoidRenderMode.values()[renderMode];
                this.structVoidRenderDiameter = renderDiameter;
                this.structVoidFadeBorders = fadeBorders;
                this.structVoidColorRed = boxColor.getRed();
                this.structVoidColorGreen = boxColor.getGreen();
                this.structVoidColorBlue = boxColor.getBlue();
                this.structVoidColor = boxColorStr;

            } catch (FileNotFoundException e) {
                AdbMain.LOGGER.info("An error occurred reading " + optionsFile.getName());
                e.printStackTrace();
            }
        }
    }
}
