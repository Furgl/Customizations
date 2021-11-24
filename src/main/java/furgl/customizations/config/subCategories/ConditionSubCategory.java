package furgl.customizations.config.subCategories;

import java.util.List;

import com.google.common.collect.Lists;

import furgl.customizations.config.Config;
import furgl.customizations.config.ConfigElement;
import furgl.customizations.config.selectors.ConditionSelector;
import furgl.customizations.customizations.Customization;
import furgl.customizations.customizations.conditions.Condition;
import net.minecraft.text.Text;

public class ConditionSubCategory extends SubCategory {

	private Condition condition;

	public ConditionSubCategory(Customization customization, Condition condition) {
		super("condition", customization);
		this.condition = condition;
	}
	
	@Override
	public Text getName() {
		return Text.of(Config.SUB_CATEGORY_FORMATTING+this.condition.name);
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