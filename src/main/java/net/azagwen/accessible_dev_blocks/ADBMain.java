package net.azagwen.accessible_dev_blocks;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_G;

public class ADBMain implements ModInitializer {
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
		AutoConfig.register(ADBConfig.class, JanksonConfigSerializer::new);
	}
}
