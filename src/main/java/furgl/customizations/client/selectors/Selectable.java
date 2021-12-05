package furgl.customizations.client.selectors;

import java.util.List;
import java.util.function.BiFunction;

import com.google.common.collect.Lists;

import furgl.customizations.client.elements.ConfigElement;
import furgl.customizations.common.Customizations;
import furgl.customizations.common.customizations.Customization;
import furgl.customizations.common.customizations.context.ContextHolder;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableText;


public class Selectable {

	private String id;
	private ItemStack stack;
	private BiFunction<Customization, ContextHolder, List<ConfigElement>> relatedElements;

	public Selectable(String id, ItemStack stack) {
		this(id, stack, (customization, contextHolder) -> Lists.newArrayList());
	}

	public Selectable(String id, ItemStack stack, BiFunction<Customization, ContextHolder, List<ConfigElement>> relatedElements) {
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
		return new TranslatableText("config."+Customizations.MODID+"."+this.getId()+".name");
	}

	/**Get tooltip to display when this is hovered over in drop down menu*/
	public MutableText getTooltip() {
		return new TranslatableText("config."+Customizations.MODID+"."+this.getId()+".tooltip");
	}

	/**Get item stack to represent this selection in drop down menu*/
	public ItemStack getStack() {
		return this.stack;
	}

	/**Create other config elements when this selection is picked*/
	public List<ConfigElement> createRelatedElements(Customization customization, ContextHolder contextHolder) {
		return this.relatedElements.apply(customization, contextHolder);
	}

}