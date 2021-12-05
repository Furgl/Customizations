package furgl.customizations.client.subCategories;

import java.util.List;

import com.google.common.collect.Lists;

import furgl.customizations.client.elements.ConfigElement;
import furgl.customizations.client.lists.ActionList;
import furgl.customizations.client.lists.ConditionList;
import furgl.customizations.client.lists.TriggerList;
import furgl.customizations.common.customizations.Customization;
import net.minecraft.text.Text;

public class CustomizationSubCategory extends SubCategory {

	public CustomizationSubCategory(Customization customization) {
		super("customization", customization);
	}

	@Override
	public Text getName() {
		return Text.of(this.getCustomization().name);
	}

	@Override
	protected List<ConfigElement> createChildren() {
		List<ConfigElement> list = Lists.newArrayList();
		TriggerList triggerList = new TriggerList(this.getCustomization());
		list.add(triggerList);
		list.addAll(triggerList.getSubCategories()); 
		ConditionList conditionList = new ConditionList(this.getCustomization());
		list.add(conditionList);
		list.addAll(conditionList.getSubCategories());
		ActionList actionList = new ActionList(this.getCustomization());
		list.add(actionList);
		list.addAll(actionList.getSubCategories());
		return list;
	}

	@Override
	public void updateName(String name) {
		super.updateName(name);
		this.getCustomization().name = name;
	}

}