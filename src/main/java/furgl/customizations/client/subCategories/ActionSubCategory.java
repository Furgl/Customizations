package furgl.customizations.client.subCategories;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import furgl.customizations.client.elements.ConfigElement;
import furgl.customizations.client.selectors.ActionSelector;
import furgl.customizations.common.customizations.Customization;
import furgl.customizations.common.customizations.actions.Action;
import furgl.customizations.common.customizations.context.Context;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import net.minecraft.text.Text;

public class ActionSubCategory extends SubCategory {

	private Action action;

	public ActionSubCategory(Customization customization, Action action) {
		super("action", customization);
		this.action = action;
	}

	@Override
	protected List<AbstractConfigListEntry> addToConfig(ConfigBuilder builder) {
		// remove unneeded contexts
		ArrayList<Context> neededContexts = this.getRelatedContexts();
		this.action.getContext().removeIf(context -> !neededContexts.contains(context));
		
		return super.addToConfig(builder);
	}

	@Override
	public Text getName() {
		return Text.of(this.action.name);
	}

	@Override
	public void updateName(String name) {
		super.updateName(name);
		this.action.name = name;
	}

	@Override
	protected List<ConfigElement> createChildren() {
		return Lists.newArrayList(new ActionSelector(this.getCustomization(), this.action));
	}

}