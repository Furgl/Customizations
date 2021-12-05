package furgl.customizations.client.subCategories;

import java.util.List;

import com.google.common.collect.Lists;

import furgl.customizations.client.elements.ConfigElement;
import furgl.customizations.client.selectors.ConditionSelector;
import furgl.customizations.common.customizations.Customization;
import furgl.customizations.common.customizations.conditions.Condition;
import net.minecraft.text.Text;

public class ConditionSubCategory extends SubCategory {

	private Condition condition;

	public ConditionSubCategory(Customization customization, Condition condition) {
		super("condition", customization);
		this.condition = condition;
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