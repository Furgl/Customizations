package furgl.customizations.client.config;

import java.util.HashSet;

import com.google.common.collect.Sets;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

import furgl.customizations.client.elements.ConfigElement;
import furgl.customizations.client.elements.DebugModeElement;
import furgl.customizations.client.elements.ShowTipsElement;
import furgl.customizations.client.lists.CustomizationList;
import furgl.customizations.common.Customizations;
import furgl.customizations.common.PacketManager;
import furgl.customizations.common.config.FileConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.gui.ClothConfigScreen;
import me.shedaniel.clothconfig2.gui.entries.TextListEntry;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class Config implements ModMenuApi { 

	public static Text customizationsCategoryText;
	public static ClothConfigScreen currentScreen;
	public static HashSet<ConfigElement> currentElements = Sets.newHashSet();
	public static boolean editingServerConfig;

	public static boolean isMultiplayer() {
		return MinecraftClient.getInstance().player != null && 
				!MinecraftClient.getInstance().isInSingleplayer() && 
				!MinecraftClient.getInstance().isIntegratedServerRunning();
	}

	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return parent -> {
			// if multiplayer, wait for packet from server and either get config from server or 
			if (isMultiplayer()) {
				ClientPlayNetworking.send(PacketManager.CONFIG_WAITING_SCREEN, PacketByteBufs.empty());
				return new WaitingScreen(parent, new TranslatableText("config."+Customizations.MODID+".multiplayerWaitingScreen.waiting"));
			}
			// else open single player's config
			else
				return createConfigScreen(parent, false);
		};
	}

	public static Screen createConfigScreen(Screen parent, boolean writeToServer) {
		currentElements.clear();
		// init builder
		ConfigBuilder builder = ConfigBuilder.create()
				.setParentScreen(parent)
				.setShouldListSmoothScroll(true)
				.setTitle(isMultiplayer() ? 
						new TranslatableText("config."+Customizations.MODID+".multiplayer.name") :
							new TranslatableText("config."+Customizations.MODID+".singleplayer.name"))
				.setSavingRunnable(() -> {
					if (writeToServer) {
						PacketByteBuf buf = PacketByteBufs.create();
						buf.writeString(FileConfig.writeToString()); // send config 
						ClientPlayNetworking.send(PacketManager.SEND_CONFIG_TO_SERVER, buf);
					}
					else
						FileConfig.writeToFile(false);
				});
		// category
		customizationsCategoryText = new TranslatableText("config."+Customizations.MODID+".category.customizations");
		ConfigCategory category = builder.getOrCreateCategory(customizationsCategoryText);
		// show tips
		category.addEntry(new ShowTipsElement().getOrCreateMainConfigEntry(builder));
		// debug mode
		category.addEntry(new DebugModeElement().getOrCreateMainConfigEntry(builder));
		// tip
		TextListEntry entry = null;
		if ((entry = ConfigHelper.addTip(builder, "addCustomization")) != null)
			category.addEntry(entry);
		if ((entry = ConfigHelper.addTip(builder, "lists", ConfigHelper.LIST_FORMATTING)) != null)
			category.addEntry(entry);
		// customizations - create list and then add subcategories to main category
		CustomizationList list = new CustomizationList();
		list.getOrCreateConfigEntries(builder).forEach(entry2 -> category.addEntry(entry2));
		list.getSubCategories().forEach(subCategory -> category.addEntry(subCategory.getMainConfigEntry()));
		// set current screen
		Screen screen = builder.build();
		if (screen instanceof ClothConfigScreen) {
			currentScreen = (ClothConfigScreen)screen;
			editingServerConfig = writeToServer;
		}
		return screen;
	}

}