package furgl.customizations.common.customizations.selectables;

import java.util.List;
import java.util.function.BiFunction;

import com.google.common.collect.Lists;

import furgl.customizations.client.elements.ConfigElement;
import furgl.customizations.common.Customizations;
import furgl.customizations.common.customizations.Customization;
import furgl.customizations.common.customizations.context.ContextHelper;
import furgl.customizations.common.customizations.context.holders.ConfigContextHolder;
import furgl.customizations.common.customizations.context.holders.EventContextHolder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

public class Selectable {

	/**Context holders for getting placeholders*/
	public List<EventContextHolder> placeholderContextHolders = Lists.newArrayList();
	private String id;
	private ItemStack stack;
	private BiFunction<Customization, ConfigContextHolder, List<ConfigElement>> relatedElements;

	public Selectable(String id, ItemStack stack) {
		this(id, stack, (customization, contextHolder) -> Lists.newArrayList());
	}

	public Selectable(String id, ItemStack stack, BiFunction<Customization, ConfigContextHolder, List<ConfigElement>> relatedElements) {
		this.id = id;
		this.stack = stack;
		this.relatedElements = relatedElements;
		if (!"misc.blank".equals(id))
			Selectables.ALL_SELECTABLES.add(this);
	}

	public String getId() {
		return this.id;
	}

	public MutableText getName() {
		return new TranslatableText("config." + Customizations.MODID + "." + this.getId() + ".name");
	}

	/** Get tooltip to display when this is hovered over in drop down menu */
	public MutableText getTooltip() {
		MutableText tooltip = new TranslatableText("config." + Customizations.MODID + "." + this.getId() + ".tooltip");
		// add placeholders to tooltip
		String placeholderText = ContextHelper.getPlaceholderText(this.placeholderContextHolders);
		if (!placeholderText.isEmpty()) {
			if (Screen.hasShiftDown()) 
				tooltip.append("\n\n" + Formatting.GOLD + Formatting.UNDERLINE + 
						new TranslatableText("config." + Customizations.MODID + ".placeholders.tooltip").getString() + "\n" + placeholderText);
			else
				tooltip = tooltip.append("\n").append(new TranslatableText("config."+Customizations.MODID+".placeholders.holdShift", Formatting.DARK_GRAY, Formatting.ITALIC));
		}
		return tooltip;
	}

	/** Get item stack to represent this selection in drop down menu */
	public ItemStack getStack() {
		return this.stack;
	}

	/** Create other config elements when this selection is picked */
	public List<ConfigElement> createRelatedElements(Customization customization, ConfigContextHolder contextHolder) {
		return this.relatedElements.apply(customization, contextHolder);
	}

}