package furgl.customizations.config;

import java.util.HashSet;

import com.google.common.collect.Sets;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

import furgl.customizations.Customizations;
import furgl.customizations.config.lists.CustomizationList;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.gui.ClothConfigScreen;
import me.shedaniel.clothconfig2.gui.entries.TextListEntry;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

public class Config implements ModMenuApi {

	public static final Formatting LIST_FORMATTING = Formatting.BLUE;
	public static final Formatting SUB_CATEGORY_FORMATTING = Formatting.GREEN;
	public static final Formatting SELECTOR_FORMATTING = Formatting.YELLOW;
	
	public static Text categoryText;
	public static ClothConfigScreen currentScreen;
	public static HashSet<ConfigElement> currentElements = Sets.newHashSet();

	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return parent -> {
			currentElements.clear();
			// init builder
			ConfigBuilder builder = ConfigBuilder.create()
					.setParentScreen(parent)
					.setTitle(new TranslatableText("config."+Customizations.MODID+".name"))
					.setSavingRunnable(() -> FileConfig.writeToFile(false));
			// category
			categoryText = new TranslatableText("config."+Customizations.MODID+".category.customizations");
			ConfigCategory category = builder.getOrCreateCategory(categoryText);
			// tip
			TextListEntry entry = null;
			if ((entry = ConfigHelper.addTip(builder, "addCustomization")) != null)
				category.addEntry(entry);
			if ((entry = ConfigHelper.addTip(builder, "lists", Config.LIST_FORMATTING)) != null)
				category.addEntry(entry);
			// customizations - create list and then add subcategories to main category
			CustomizationList list = new CustomizationList();
			list.getOrCreateConfigEntries(builder).forEach(entry2 -> category.addEntry(entry2));
			list.getSubCategories().forEach(subCategory -> category.addEntry(subCategory.getMainConfigEntry()));
			// show tips
			category.addEntry(new ShowTipsElement().getOrCreateMainConfigEntry(builder));
			Screen screen = builder.build();
			if (screen instanceof ClothConfigScreen)
				currentScreen = (ClothConfigScreen)screen;
			return screen;
		};
	}

}