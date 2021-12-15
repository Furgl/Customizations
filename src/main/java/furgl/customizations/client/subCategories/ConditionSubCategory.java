package furgl.customizations.client.subCategories;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import furgl.customizations.client.elements.ConfigElement;
import furgl.customizations.client.selectors.ConditionSelector;
import furgl.customizations.common.customizations.Customization;
import furgl.customizations.common.customizations.conditions.Condition;
import furgl.customizations.common.customizations.context.Context;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import net.minecraft.text.Text;

public class ConditionSubCategory extends SubCategory {

	private Condition condition;

	public ConditionSubCategory(Customization customization, Condition condition) {
		super("condition", customization);
		this.condition = condition;
	}
	
	@Override
	protected List<AbstractConfigListEntry> addToConfig(ConfigBuilder builder) {
		// remove unneeded contexts
		ArrayList<Context> neededContexts = this.getRelatedContexts();
		this.condition.getContext().removeIf(context -> !neededContexts.contains(context));
		
		return super.addToConfig(builder);
	}
	
	@Override
	public Text getName() {
		return Text.of(this.condition.name);
	}

	@Override
	public void updateName(String name) {
		super.updateName(name);
		this.condition.name = name;
	}
	
	@Override
	protected List<ConfigElement> createChildren() {
		return Lists.newArrayList(new ConditionSelector(this.getCustomization(), this.condition));
	}

}