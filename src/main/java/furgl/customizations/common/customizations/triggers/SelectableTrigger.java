package furgl.customizations.common.customizations.triggers;

import java.util.List;
import java.util.function.BiFunction;

import com.google.common.collect.Lists;

import furgl.customizations.client.elements.ConfigElement;
import furgl.customizations.client.selectors.Selectable;
import furgl.customizations.client.selectors.Selectables;
import furgl.customizations.common.Customizations;
import furgl.customizations.common.customizations.Customization;
import furgl.customizations.common.customizations.context.Context;
import furgl.customizations.common.customizations.context.ContextHelper;
import furgl.customizations.common.customizations.context.ContextHolder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

public abstract class SelectableTrigger extends Selectable {

	public SelectableTrigger(String id, ItemStack stack, BiFunction<Customization, ContextHolder, List<ConfigElement>> relatedElements) {
		super(id, stack, relatedElements);
		Selectables.ALL_TRIGGERS.add(this);
	}

	/**Context that will be referenced to get placeholders for this trigger*/
	public abstract List<Context> getContextForPlaceholders();

	@Override
	public MutableText getTooltip() {
		MutableText tooltip = super.getTooltip();
		// add placeholders to tooltip
		if (Screen.hasShiftDown()) {
			List<String> placeholders = Lists.newArrayList();
			this.getContextForPlaceholders().forEach(ctx -> placeholders.addAll(ctx.getPlaceholders().keySet()));
			if (!placeholders.isEmpty()) {
				tooltip = tooltip.append("\n\n"+Formatting.GOLD+new TranslatableText("config."+Customizations.MODID+".placeholders.tooltip").getString());
				for (String placeholder : placeholders) 
					tooltip = tooltip.append(new LiteralText("\n - "+Formatting.GOLD+ContextHelper.PLACEHOLDER_CHAR+placeholder+ContextHelper.PLACEHOLDER_CHAR).formatted(Formatting.GOLD));
			}
		}
		return tooltip;
	}

}