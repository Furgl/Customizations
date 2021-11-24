package furgl.customizations.config.lists;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Lists;

import furgl.customizations.Customizations;
import furgl.customizations.config.Config;
import furgl.customizations.config.ConfigElement;
import furgl.customizations.config.subCategories.SubCategory;
import furgl.customizations.customizations.Customization;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.gui.entries.StringListListEntry;
import me.shedaniel.clothconfig2.gui.entries.StringListListEntry.StringListCell;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

/**String list that controls adding, deleting, and renaming Parts*/
public abstract class CList extends ConfigElement {

	protected StringListListEntry list;
	protected List<String> previousValues;
	private List<SubCategory> subCategories;

	protected CList(String id, @Nullable Customization customization) {
		super(id, customization);
	}

	@Override
	protected List<AbstractConfigListEntry> addToConfig(ConfigBuilder builder) {
		System.out.println("adding to config: "+this.getName().getString()+" ("+this.getClass().getSimpleName()+")"); // TODO remove

		// create list
		this.mainConfigEntry = this.list = builder.entryBuilder()
				.startStrList(this.getName(), createItems())
				.setAddButtonTooltip(new TranslatableText("config."+Customizations.MODID+".addButton.tooltip").append(getName()))
				.setDeleteButtonEnabled(true)
				.setCellErrorSupplier(str -> {
					int matches = 0;
					for (String str2 : this.list.getValue())
						if (str.equals(str2))
							++matches;
					if (matches > 1)
						return Optional.of(new TranslatableText("config."+Customizations.MODID+".duplicate"));
					else
						return Optional.empty();
				})
				.setCreateNewInstance(entry -> new StringListCell(this.getDefaultInstanceName(), entry))
				.setInsertInFront(false)
				.build();

		this.getSubCategories().forEach(subCategory -> subCategory.getOrCreateConfigEntries(builder));

		return Lists.newArrayList(this.mainConfigEntry);
	}

	/**Do not call directly - use getSubCategories()*/
	protected abstract List<SubCategory> createSubCategories();
	
	public List<SubCategory> getSubCategories() {
		if (this.subCategories == null)
			this.subCategories = this.createSubCategories();
		return this.subCategories;
	}

	@Override
	public boolean checkForChanges() {
		boolean reload = false;
		List<String> values = this.list.getValue();
		if (previousValues != null) {
			// items were added/deleted
			if (previousValues.size() != values.size()) {
				// get added/deleted values
				ArrayList<String> addedValues = Lists.newArrayList();
				ArrayList<String> deletedValues = Lists.newArrayList();
				for (String value : values)
					if (!previousValues.contains(value)) {
						System.out.println("added: "+value); // TODO remove
						addedValues.add(value);
					}
				for (String previousValue : previousValues)
					if (!values.contains(previousValue)) {
						System.out.println("deleted: "+previousValue); // TODO remove
						deletedValues.add(previousValue);
					}
				// add/delete them
				for (String addedValue : addedValues) 
					this.addItem(addedValue);
				for (String deletedValue : deletedValues)
					this.deleteItem(deletedValue);
				reload = true;
			}
			// update names
			else {
				for (int i=0; i<values.size(); ++i) {
					String previousValue = previousValues.get(i);
					String value = values.get(i);
					if (!value.equals(previousValue)) {
						System.out.println("previousValues: "+previousValues+" values: "+values); // TODO remove
						System.out.println("name change from: "+previousValue+" to: "+value); // TODO remove
						this.updateName(i, value);
						this.mainConfigEntry.save();
					}
				}
			}
		}
		previousValues = values;
		return reload;
	}

	/**Called when an item is renamed*/
	protected void updateName(int index, String name) {
		if (index >= 0 && index < this.subCategories.size()) 
			this.subCategories.get(index).updateName(name);
	}

	/**Get number of items in this list*/
	public abstract int getNumberOfItems();

	/**Get name of new items added to this list*/
	public String getDefaultInstanceName() {
		int num = (this.getNumberOfItems()+1);
		String name;
		do 
			name = new TranslatableText("config."+Customizations.MODID+"."+this.getId()+".instance.name").getString()+" "+num++;
		while (this.previousValues.contains(name));
		return name;	
	}

	@Override
	public Text getName() {
		return Text.of(Config.LIST_FORMATTING+super.getName().getString());
	}

	/**Called when new item is added to the list*/
	protected abstract void addItem(String addedValue);

	/**Called when item is deleted from the list*/
	protected abstract void deleteItem(String deletedValue);

	/**Get the names of items in this list*/
	public abstract List<String> createItems();

}