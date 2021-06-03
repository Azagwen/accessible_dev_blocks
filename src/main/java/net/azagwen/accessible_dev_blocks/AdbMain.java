package net.azagwen.accessible_dev_blocks;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import me.shedaniel.autoconfig.serializer.YamlConfigSerializer;
import net.azagwen.accessible_dev_blocks.cloth_config.AdbAutoConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AdbMain implements ModInitializer {
	public static final Logger LOGGER  = LogManager.getLogger();
	public static final String ADB_NAMESPACE = "adb";

	public static Identifier ADB_ID(String path) {
		return new Identifier(ADB_NAMESPACE, path);
	}

	public static ItemGroup ADB_GROUP = FabricItemGroupBuilder.create(
			ADB_ID("group"))
			.icon(() -> new ItemStack(Items.STRUCTURE_BLOCK))
			.appendItems((itemStacks -> {
				itemStacks.add(new ItemStack(Items.COMMAND_BLOCK));
				itemStacks.add(new ItemStack(Items.CHAIN_COMMAND_BLOCK));
				itemStacks.add(new ItemStack(Items.REPEATING_COMMAND_BLOCK));
				itemStacks.add(new ItemStack(Items.STRUCTURE_BLOCK));
				itemStacks.add(new ItemStack(Items.JIGSAW));
				itemStacks.add(new ItemStack(Items.COMMAND_BLOCK_MINECART));
				itemStacks.add(new ItemStack(Items.STRUCTURE_VOID));
				itemStacks.add(new ItemStack(Items.BARRIER));
				itemStacks.add(new ItemStack(Items.DEBUG_STICK));
			}))
			.build();

	@Override
	public void onInitialize() {
		LOGGER.info("Accessible Developer blocks initialised !");
		AutoConfig.register(AdbAutoConfig.class, GsonConfigSerializer::new);
		AutoConfig.getConfigHolder(AdbAutoConfig.class).save();
	}
}
