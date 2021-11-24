package furgl.customizations.config.subCategories;

import java.util.List;

import com.google.common.collect.Lists;

import furgl.customizations.config.Config;
import furgl.customizations.config.ConfigElement;
import furgl.customizations.config.selectors.ActionSelector;
import furgl.customizations.customizations.Customization;
import furgl.customizations.customizations.actions.Action;
import net.minecraft.text.Text;

public class ActionSubCategory extends SubCategory {

	private Action action;

	public ActionSubCategory(Customization customization, Action action) {
		super("action", customization);
		this.action = action;
	}
	
	@Override
	public Text getName() {
		return Text.of(Config.SUB_CATEGORY_FORMATTING+this.action.name);
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