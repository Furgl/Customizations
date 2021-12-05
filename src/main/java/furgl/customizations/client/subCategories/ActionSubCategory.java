package furgl.customizations.client.subCategories;

import java.util.List;

import com.google.common.collect.Lists;

import furgl.customizations.client.elements.ConfigElement;
import furgl.customizations.client.selectors.ActionSelector;
import furgl.customizations.common.customizations.Customization;
import furgl.customizations.common.customizations.actions.Action;
import net.minecraft.text.Text;

public class ActionSubCategory extends SubCategory {

	private Action action;

	public ActionSubCategory(Customization customization, Action action) {
		super("action", customization);
		this.action = action;
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