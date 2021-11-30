package furgl.customizations.config.subCategories;

import java.util.List;

import com.google.common.collect.Lists;

import furgl.customizations.config.Config;
import furgl.customizations.config.elements.ConfigElement;
import furgl.customizations.config.selectors.TriggerSelector;
import furgl.customizations.customizations.Customization;
import furgl.customizations.customizations.triggers.Trigger;
import net.minecraft.text.Text;

public class TriggerSubCategory extends SubCategory {

	private Trigger trigger;

	public TriggerSubCategory(Customization customization, Trigger trigger) {
		super("trigger", customization);
		this.trigger = trigger;
	}
	
	@Override
	public Text getName() {
		return Text.of(Config.SUB_CATEGORY_FORMATTING+this.trigger.name);
	}

	@Override
	public void updateName(String name) {
		super.updateName(name);
		this.trigger.name = name;
	}
	
	@Override
	protected List<ConfigElement> createChildren() {
		return Lists.newArrayList(new TriggerSelector(this.getCustomization(), this.trigger));
	}

}