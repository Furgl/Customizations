package furgl.customizations.client.lists;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Lists;

import furgl.customizations.client.elements.ConfigElement;
import furgl.customizations.client.subCategories.SubCategory;
import furgl.customizations.common.Customizations;
import furgl.customizations.common.customizations.Customization;
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
				.setTooltipSupplier(() -> this.getTooltip().getString().isEmpty() ? Optional.empty() : Optional.of(new Text[] {this.getTooltip()}))
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
					if (!previousValues.contains(value)) 
						addedValues.add(value);
				for (String previousValue : previousValues)
					if (!values.contains(previousValue)) 
						deletedValues.add(previousValue);
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
					if (!value.equals(previousValue)) 
						this.updateName(i, value);
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
	
	/**Called when new item is added to the list*/
	protected abstract void addItem(String addedValue);

	/**Called when item is deleted from the list*/
	protected abstract void deleteItem(String deletedValue);

	/**Get the names of items in this list*/
	public abstract List<String> createItems();

}