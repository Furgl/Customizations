package furgl.customizations.client.selectors;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import com.google.common.collect.Lists;

import furgl.customizations.client.config.ConfigHelper;
import furgl.customizations.client.elements.ConfigElement;
import furgl.customizations.common.customizations.Customization;
import furgl.customizations.common.customizations.context.Context;
import furgl.customizations.common.customizations.context.holders.ConfigContextHolder;
import furgl.customizations.common.customizations.selectables.Selectable;
import furgl.customizations.common.customizations.selectables.Selectables;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import net.minecraft.text.Text;

public abstract class Selector<T extends Selectable> extends ConfigElement {

	protected Object previousValue;
	private ConfigContextHolder contextHolder;
	protected T selection;
	protected Function<String, T> stringToSelection;
	protected Function<T, Text> selectionToText;
	private Consumer<T> saveConsumer;
	private Iterable<T> selections;
	private List<ConfigElement> relatedElements;

	protected Selector(String id, Customization customization, ConfigContextHolder contextHolder, T selection, Iterable<T> selections, 
			Function<String, T> stringToSelection, Function<T, Text> selectionToText, Consumer<T> saveConsumer) {
		super(id, customization);
		this.contextHolder = contextHolder;
		this.selection = selection == null ? (T) Selectables.BLANK : selection;
		this.selections = selections;
		this.stringToSelection = stringToSelection;
		this.selectionToText = selectionToText;
		this.saveConsumer = saveConsumer;
	}

	@Override
	protected List<AbstractConfigListEntry> addToConfig(ConfigBuilder builder) {
		this.mainConfigEntry = ConfigHelper.createDropdownMenu(builder, this.getName(), this.getTooltip(), 
				this.selection, this.stringToSelection, this.selectionToText)
				.setSuggestionMode(false)
				.setSaveConsumer(this.saveConsumer)
				.setSelections(this.selections)
				.build();
		List<AbstractConfigListEntry> list = Lists.newArrayList(this.mainConfigEntry);
		// add related elements
		this.getRelatedElements().stream()
		.map(element -> ((ConfigElement)element).getOrCreateConfigEntries(builder))
		.forEach(elements -> list.addAll((List<AbstractConfigListEntry>) elements));
		return list;
	}

	public List<ConfigElement> getRelatedElements() {
		if (this.relatedElements == null)
			this.relatedElements = this.selection.createRelatedElements(this.getCustomization(), this.contextHolder);
		return this.relatedElements;
	}

	@Override
	public ArrayList<Context> getRelatedContexts() {
		ArrayList<Context> contexts = Lists.newArrayList();
		for (ConfigElement element : this.getRelatedElements())
			contexts.addAll(element.getRelatedContexts());
		return contexts;
	}

	@Override
	public boolean checkForChanges() { 
		if (this.previousValue == null)
			this.previousValue = this.getMainConfigEntry().getValue();
		if (this.previousValue != this.mainConfigEntry.getValue()) 
			return true;
		else
			return false;
	}

}