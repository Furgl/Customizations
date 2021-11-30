package furgl.customizations.config.selectors;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import com.google.common.collect.Lists;

import furgl.customizations.config.Config;
import furgl.customizations.config.ConfigHelper;
import furgl.customizations.config.elements.ConfigElement;
import furgl.customizations.customizations.Customization;
import furgl.customizations.customizations.context.ContextHolder;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import net.minecraft.text.Text;

public abstract class Selector<T extends Selectable> extends ConfigElement {

	protected Object previousValue;
	private ContextHolder contextHolder;
	protected T selection;
	protected Function<String, T> stringToSelection;
	protected Function<T, Text> selectionToText;
	private Consumer<T> saveConsumer;
	private Iterable<T> selections;

	protected Selector(String id, Customization customization, ContextHolder contextHolder, T selection, Iterable<T> selections, 
			Function<String, T> stringToSelection, Function<T, Text> selectionToText, Consumer<T> saveConsumer) {
		super(id, customization);
		this.contextHolder = contextHolder;
		this.selection = selection;
		this.selections = selections;
		this.stringToSelection = stringToSelection;
		this.selectionToText = selectionToText;
		this.saveConsumer = saveConsumer;
	}

	@Override
	protected List<AbstractConfigListEntry> addToConfig(ConfigBuilder builder) {
		this.mainConfigEntry = ConfigHelper.createFixedDropdownMenu(builder, this.getName(), this.getTooltip(), 
				this.selection, this.stringToSelection, this.selectionToText)
				.setSuggestionMode(false)
				.setSaveConsumer(this.saveConsumer)
				.setSelections(this.selections)
				.build();
		List<AbstractConfigListEntry> list = Lists.newArrayList(this.mainConfigEntry);
		List<ConfigElement> relatedElements = this.selection.createRelatedElements(this.getCustomization(), this.contextHolder);
		// remove unneeded contexts (doesn't work perfectly - removes subcategory/related elements' contexts)
		//ArrayList<Context> contexts = Lists.newArrayList();
		//relatedElements.forEach(element -> contexts.addAll(element.getRelatedContexts()));
		//this.contextHolder.getContext().removeIf(context -> !contexts.contains(context));
		// add related elements
		relatedElements.stream()
		.map(element -> ((ConfigElement)element).getOrCreateConfigEntries(builder))
		.forEach(elements -> list.addAll((List<AbstractConfigListEntry>) elements));
		return list;
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

	@Override
	public Text getName() {
		return Text.of(Config.SELECTOR_FORMATTING+super.getName().getString());
	}

}