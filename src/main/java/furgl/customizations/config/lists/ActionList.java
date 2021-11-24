package furgl.customizations.config.lists;

import java.util.List;
import java.util.stream.Collectors;

import furgl.customizations.config.subCategories.ActionSubCategory;
import furgl.customizations.config.subCategories.SubCategory;
import furgl.customizations.customizations.Customization;
import furgl.customizations.customizations.actions.Action;

public class ActionList extends CList {

	public ActionList(Customization customization) {
		super("actions", customization);
	}

	@Override
	public List<String> createItems() {
		return this.getCustomization().getActions().stream()
				.map(action -> action.name)
				.collect(Collectors.toList());
	}

	@Override
	public int getNumberOfItems() {
		return this.getCustomization().getActions().size();
	}

	@Override
	protected void addItem(String item) {
		this.getCustomization().addAction(new Action(this.getDefaultInstanceName()));
	}

	@Override
	protected void deleteItem(String item) {
		this.getCustomization().removeAction(item);
	}

	@Override
	public List<SubCategory> createSubCategories() {
		return this.getCustomization().getActions().stream()
				.map(action -> new ActionSubCategory(this.getCustomization(), action))
				.collect(Collectors.toList());
	}

}