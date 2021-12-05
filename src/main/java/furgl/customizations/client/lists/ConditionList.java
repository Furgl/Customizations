package furgl.customizations.client.lists;

import java.util.List;
import java.util.stream.Collectors;

import furgl.customizations.client.subCategories.ConditionSubCategory;
import furgl.customizations.client.subCategories.SubCategory;
import furgl.customizations.common.customizations.Customization;
import furgl.customizations.common.customizations.conditions.Condition;

public class ConditionList extends CList {

	public ConditionList(Customization customization) {
		super("conditions", customization);
	}

	@Override
	public List<String> createItems() {
		return this.getCustomization().getConditions().stream()
				.map(condition -> condition.name)
				.collect(Collectors.toList());
	}
	
	@Override
	public int getNumberOfItems() {
		return this.getCustomization().getConditions().size();
	}

	@Override
	protected void addItem(String item) {
		this.getCustomization().addCondition(new Condition(this.getDefaultInstanceName()));
	}

	@Override
	protected void deleteItem(String item) {
		this.getCustomization().removeCondition(item);
	}

	@Override
	public List<SubCategory> createSubCategories() {
		return this.getCustomization().getConditions().stream()
				.map(condition -> new ConditionSubCategory(this.getCustomization(), condition))
				.collect(Collectors.toList());
	}

}