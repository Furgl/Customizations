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
import furgl.customizations.common.config.FileConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.gui.ClothConfigScreen;
import me.shedaniel.clothconfig2.gui.entries.TextListEntry;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class Config implements ModMenuApi {

	public static Text customizationsCategoryText;
	public static ClothConfigScreen currentScreen;
	public static HashSet<ConfigElement> currentElements = Sets.newHashSet();

	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return parent -> {
			currentElements.clear();
			// init builder
			ConfigBuilder builder = ConfigBuilder.create()
					.setParentScreen(parent)
					.setShouldListSmoothScroll(true)
					.setTitle(new TranslatableText("config."+Customizations.MODID+".name"))
					.setSavingRunnable(() -> FileConfig.writeToFile(false));
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
			if (screen instanceof ClothConfigScreen)
				currentScreen = (ClothConfigScreen)screen;
			return screen;
		};
	}

}