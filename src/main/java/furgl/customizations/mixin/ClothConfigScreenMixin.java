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
import furgl.customizations.config.elements.ConfigElement;
import furgl.customizations.impl.IClothConfigScreen;
import me.shedaniel.clothconfig2.api.AbstractConfigEntry;
import me.shedaniel.clothconfig2.api.Expandable;
import me.shedaniel.clothconfig2.gui.AbstractTabbedConfigScreen;
import me.shedaniel.clothconfig2.gui.ClothConfigScreen;
import me.shedaniel.clothconfig2.gui.ClothConfigScreen.ListWidget;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
@Mixin(ClothConfigScreen.class)
public abstract class ClothConfigScreenMixin extends AbstractTabbedConfigScreen implements IClothConfigScreen {

	// saved data for closing and re-opening screen to add more options
	/**Strings to represent categories that were expanded before re-opening the screen*/
	@Unique
	private ArrayList<String> expandedCategories = Lists.newArrayList();
	@Unique
	private double scrollTo = -1;

	protected ClothConfigScreenMixin(Screen parent, Text title, Identifier backgroundLocation) {
		super(parent, title, backgroundLocation);
	}

	@Unique
	public boolean isScrolledToBottom() {
		ListWidget<AbstractConfigEntry<AbstractConfigEntry<?>>> listWidget = ((ClothConfigScreen)(Object)this).listWidget;
		double scroll = listWidget.getScroll(); // save prev scroll
		listWidget.capYPosition(Double.MAX_VALUE); // scroll to max
		boolean atBottom = ((scroll+200) == listWidget.getScroll()) || (scroll == listWidget.getScroll()); // if same, then we were at bottom already
		if (!atBottom) // if not at bottom, scroll back to where we were
			listWidget.capYPosition(scroll);
		return atBottom;
	}

	/**If scrolled to bottom and page expands, scroll to bottom again*/
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		ListWidget<AbstractConfigEntry<AbstractConfigEntry<?>>> listWidget = ((ClothConfigScreen)(Object)this).listWidget;
		boolean atBottom = isScrolledToBottom();
		boolean ret = super.mouseClicked(mouseX, mouseY, button);
		if (atBottom) // if we were at bottom, scroll to bottom again
			this.scrollTo = Double.MAX_VALUE;
			//listWidget.capYPosition(Double.MAX_VALUE);
		return ret;
	}

	@Inject(method = "render", at = @At(value = "HEAD"))
	public void renderHead(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
		if (((ClothConfigScreen)(Object)this) == Config.currentScreen) {
			// check for reload
			boolean reload = false;
			for (ConfigElement element : Config.currentElements)
				reload |= element.checkForChanges();
			if (reload) 
				reload();
		}
	}
	
	@Inject(method = "render", at = @At(value = "TAIL"))
	public void renderTail(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
		if (((ClothConfigScreen)(Object)this) == Config.currentScreen) {
			// scroll after reload
			if (scrollTo >= 0) {
				((ClothConfigScreen)(Object)this).listWidget.scrollTo(scrollTo, true);
				scrollTo = -1;
			}
		}
	}

	@Override
	public void reload() {
		Map<Text, List<AbstractConfigEntry<?>>> map = ((ClothConfigScreen)(Object)this).getCategorizedEntries();
		List<AbstractConfigEntry<?>> list = map.get(Config.customizationsCategoryText);
		// save currently expanded categories
		expandedCategories.clear();
		saveExpandedCategories("main", list);
		// save scroll
		double scroll = isScrolledToBottom() ? Double.MAX_VALUE : ((ClothConfigScreen)(Object)this).listWidget.getScroll();

		// save and reopen config screen
		((ClothConfigScreen)(Object)this).saveAll(false);
		Screen screen = ModMenu.getConfigScreen(Customizations.MODID, ((AbstractConfigScreenAccessor)this).getParent());
		MinecraftClient.getInstance().openScreen(screen);

		if (screen instanceof ClothConfigScreen) {
			// re-set expanded categories
			map = ((ClothConfigScreen)screen).getCategorizedEntries();
			list = map.get(Config.customizationsCategoryText);
			if (list != null) 
				setExpandedCategories("main", list);
			// re-set scroll
			((IClothConfigScreen)screen).setScrollTo(scroll);
		}

		return;
	}
	
	@Unique
	@Override
	public void setScrollTo(double scroll) {
		this.scrollTo = scroll;
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