package furgl.customizations.config.selectors;

import java.util.List;
import java.util.function.BiFunction;

import com.google.common.collect.Lists;

import furgl.customizations.Customizations;
import furgl.customizations.config.elements.ConfigElement;
import furgl.customizations.customizations.Customization;
import furgl.customizations.customizations.context.Context;
import furgl.customizations.customizations.context.ContextHolder;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class SelectableType implements Selectable {

	private String id;
	private ItemStack stack;
	private BiFunction<Customization, ContextHolder, List<ConfigElement>> relatedElements;

	public SelectableType(String id, ItemStack stack) {
		this(id, stack, (customization, contextHolder) -> Lists.newArrayList());
	}

	public SelectableType(String id, ItemStack stack, BiFunction<Customization, ContextHolder, List<ConfigElement>> relatedElements) {
		this.id = id;
		this.stack = stack;
		this.relatedElements = relatedElements;
		if (!"misc.blank".equals(id))
			Selectables.ALL_SELECTABLES.add(this);
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public Text getName() {
		return new TranslatableText("config."+Customizations.MODID+"."+this.getId()+".name");
	}

	@Override
	public Text getTooltip() {
		return new TranslatableText("config."+Customizations.MODID+"."+this.getId()+".tooltip");
	}

	@Override
	public ItemStack getStack() {
		return this.stack;
	}

	@Override
	public List<ConfigElement> createRelatedElements(Customization customization, ContextHolder contextHolder) {
		return this.relatedElements.apply(customization, contextHolder);
	}

	/**Activate this action*/
	public void activate(Context[] actionContexts, Context[] triggerContexts) {}

}