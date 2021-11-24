package furgl.customizations.mixin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.google.common.collect.Lists;
import com.terraformersmc.modmenu.ModMenu;

import furgl.customizations.Customizations;
import furgl.customizations.config.Config;
import furgl.customizations.config.ConfigElement;
import furgl.customizations.impl.IClothConfigScreen;
import me.shedaniel.clothconfig2.api.AbstractConfigEntry;
import me.shedaniel.clothconfig2.api.Expandable;
import me.shedaniel.clothconfig2.gui.ClothConfigScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
@Mixin(ClothConfigScreen.class)
public class ClothConfigScreenMixin implements IClothConfigScreen {

	// saved data for closing and re-opening screen to add more options
	/**Strings to represent categories that were expanded before re-opening the screen*/
	@Unique
	private ArrayList<String> expandedCategories = Lists.newArrayList();
	@Unique
	private double scroll;

	@Inject(method = "render", at = @At(value = "HEAD"))
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
		if (((ClothConfigScreen)(Object)this) == Config.currentScreen) {
			boolean reload = false;
			for (ConfigElement element : Config.currentElements)
				reload |= element.checkForChanges();
			if (reload) 
				reload();
		}
	}

	@Override
	public void reload() {
		Map<Text, List<AbstractConfigEntry<?>>> map = ((ClothConfigScreen)(Object)this).getCategorizedEntries();
		List<AbstractConfigEntry<?>> list = map.get(Config.categoryText);
		// save currently expanded categories
		expandedCategories.clear();
		saveExpandedCategories("main", list);
		// save scroll
		this.scroll = ((ClothConfigScreen)(Object)this).listWidget.getScroll();

		// save and reopen config screen
		((ClothConfigScreen)(Object)this).saveAll(false);
		Screen screen = ModMenu.getConfigScreen(Customizations.MODID, ((IAbstractConfigScreen)this).getParent());
		MinecraftClient.getInstance().openScreen(screen);

		if (screen instanceof ClothConfigScreen) {
			// re-set expanded categories
			map = ((ClothConfigScreen)screen).getCategorizedEntries();
			list = map.get(Config.categoryText);
			if (list != null) 
				setExpandedCategories("main", list);
			// re-set scroll
			((ClothConfigScreen)screen).listWidget.capYPosition(this.scroll);
		}

		return;
	}

	/**Save which categories are currently expanded*/
	private void saveExpandedCategories(String trace, List<? extends Element> list) {
		for (Element e : list)
			if (e instanceof Expandable && e instanceof AbstractConfigEntry) {
				Expandable expandable = (Expandable) e; 
				AbstractConfigEntry entry = (AbstractConfigEntry) e; 
				if (expandable.isExpanded()) {
					String newTrace = trace+"->"+entry.getFieldName().getString();
					expandedCategories.add(newTrace);
					saveExpandedCategories(newTrace, entry.children());
				}
			}
	}

	/**Expand categories that were previously expanded*/
	private void setExpandedCategories(String trace, List<? extends Element> list) {
		for (Element e : list)
			if (e instanceof Expandable && e instanceof AbstractConfigEntry) {
				Expandable expandable = (Expandable) e; 
				AbstractConfigEntry entry = (AbstractConfigEntry) e; 
				String newTrace = trace+"->"+entry.getFieldName().getString();
				if (expandedCategories.contains(newTrace)) {
					expandable.setExpanded(true);
					setExpandedCategories(newTrace, entry.children());
				}
			}
	}

}