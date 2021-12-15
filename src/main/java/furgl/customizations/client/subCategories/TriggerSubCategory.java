package furgl.customizations.client.subCategories;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import furgl.customizations.client.elements.ConfigElement;
import furgl.customizations.client.selectors.TriggerSelector;
import furgl.customizations.common.customizations.Customization;
import furgl.customizations.common.customizations.context.Context;
import furgl.customizations.common.customizations.triggers.Trigger;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import net.minecraft.text.Text;

public class TriggerSubCategory extends SubCategory {

	private Trigger trigger;

	public TriggerSubCategory(Customization customization, Trigger trigger) {
		super("trigger", customization);
		this.trigger = trigger;
	}
	
	@Override
	protected List<AbstractConfigListEntry> addToConfig(ConfigBuilder builder) {
		// remove unneeded contexts
		ArrayList<Context> neededContexts = this.getRelatedContexts();
		this.trigger.getContext().removeIf(context -> !neededContexts.contains(context));
		
		return super.addToConfig(builder);
	}
	
	@Override
	public Text getName() {
		return Text.of(this.trigger.name);
	}

	@Override
	public void updateName(String name) {
		super.updateName(name);
		this.trigger.name = name;
	}
	
	@Override
	protected List<ConfigElement> createChildren() {
		return Lists.newArrayList(new TriggerSelector(this.getCustomization(), this.trigger));
	}

}