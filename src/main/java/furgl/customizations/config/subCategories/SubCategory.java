package furgl.customizations.config.subCategories;

import java.util.List;

import com.google.common.collect.Lists;

import furgl.customizations.config.Config;
import furgl.customizations.config.ConfigElement;
import furgl.customizations.customizations.Customization;
import furgl.customizations.impl.IAbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import net.minecraft.text.Text;

public abstract class SubCategory extends ConfigElement {

	private List<ConfigElement> children;

	protected SubCategory(String id, Customization customization) {
		super(id, customization);
	}

	@Override
	protected List<AbstractConfigListEntry> addToConfig(ConfigBuilder builder) {
		System.out.println("adding to config: "+this.getName().getString()+" ("+this.getClass().getSimpleName()+")"); // TODO remove
		List<AbstractConfigListEntry> children = Lists.newArrayList();
		this.getChildren().stream()
		.map(element -> element.getOrCreateConfigEntries(builder))
		.forEach(list -> children.addAll(list));
		this.mainConfigEntry = builder.entryBuilder()
				.startSubCategory(this.getName(), children)
				.build();

		return Lists.newArrayList(this.mainConfigEntry);
	}

	/**Get other ConfigElements that are in this sub category*/
	public List<ConfigElement> getChildren() {
		if (this.children == null)
			this.children = this.createChildren();
		return this.children;
	}

	/**Create other ConfigElements that are in this sub category*/
	protected abstract List<ConfigElement> createChildren();

	/**Called when this controlling list item is renamed*/
	public void updateName(String name) {
		((IAbstractConfigListEntry)this.getMainConfigEntry()).setFieldName(Text.of(Config.SUB_CATEGORY_FORMATTING+name));
	}

	@Override
	public Text getName() {
		return Text.of(Config.SUB_CATEGORY_FORMATTING+super.getName().getString());
	}

}