package furgl.customizations.config.lists;

import java.util.List;
import java.util.stream.Collectors;

import furgl.customizations.config.subCategories.CustomizationSubCategory;
import furgl.customizations.config.subCategories.SubCategory;
import furgl.customizations.customizations.Customization;
import furgl.customizations.customizations.CustomizationManager;
import net.minecraft.text.Text;

public class CustomizationList extends CList {

	public CustomizationList() {
		super("customizations", null);
	}

	@Override
	public List<String> createItems() {
		return CustomizationManager.getAllCustomizations().stream()
				.map(customization -> customization.name)
				.collect(Collectors.toList());
	}

	@Override
	public int getNumberOfItems() {
		return CustomizationManager.getNumberOfCustomizations();
	}

	@Override
	protected void addItem(String item) {
		CustomizationManager.addCustomization(new Customization(this.getDefaultInstanceName()));
	}

	@Override
	protected void deleteItem(String item) {
		CustomizationManager.removeCustomization(item);
	}

	@Override
	protected List<SubCategory> createSubCategories() {
		return CustomizationManager.getAllCustomizations().stream()
				.map(customization -> new CustomizationSubCategory(customization))
				.collect(Collectors.toList());
	}
	
	@Override
	public Text getTooltip() {
		return Text.of("");
	}

}