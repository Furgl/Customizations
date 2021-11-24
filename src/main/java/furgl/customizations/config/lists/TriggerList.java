package furgl.customizations.config.lists;

import java.util.List;
import java.util.stream.Collectors;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Lists;

import furgl.customizations.config.ConfigHelper;
import furgl.customizations.config.subCategories.SubCategory;
import furgl.customizations.config.subCategories.TriggerSubCategory;
import furgl.customizations.customizations.Customization;
import furgl.customizations.customizations.triggers.Trigger;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.gui.entries.TextListEntry;

public class TriggerList extends CList {

	public TriggerList(Customization customization) {
		super("triggers", customization);
	}

	@Override
	protected List<AbstractConfigListEntry> addToConfig(ConfigBuilder builder) {
		List<AbstractConfigListEntry> list = Lists.newArrayList();
		TextListEntry tip = ConfigHelper.addTip(builder, "customizationNeeds");
		if (tip != null)
			list.add(tip);
		list.addAll(super.addToConfig(builder));
		return list;
	}

	@Override
	public List<String> createItems() {
		return this.getCustomization().getTriggers().stream()
				.map(trigger -> trigger.name)
				.collect(Collectors.toList());
	}

	@Override
	public int getNumberOfItems() {
		return this.getCustomization().getTriggers().size();
	}

	@Override
	protected void addItem(String item) {
		this.getCustomization().addTrigger(new Trigger(this.getDefaultInstanceName()));
	}

	@Override
	protected void deleteItem(String item) {
		this.getCustomization().removeTrigger(item);
	}

	@Override
	public List<SubCategory> createSubCategories() {
		return this.getCustomization().getTriggers().stream()
				.map(trigger -> new TriggerSubCategory(this.getCustomization(), trigger))
				.collect(Collectors.toList());
	}

}