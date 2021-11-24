package furgl.customizations.config.selectors;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import com.google.common.collect.Lists;

import furgl.customizations.Customizations;
import furgl.customizations.config.Config;
import furgl.customizations.config.ConfigElement;
import furgl.customizations.config.ConfigHelper;
import furgl.customizations.customizations.Customization;
import furgl.customizations.customizations.context.ContextHolder;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public abstract class Selector<T extends Selectable> extends ConfigElement {
	
	protected Object previousValue;
	private ContextHolder contextHolder;
	protected T selection;
	protected Function<String, T> stringToSelection;
	protected Function<T, Text> selectionToString;
	private Consumer<T> saveConsumer;
	private Iterable<T> selections;
	
	protected Selector(String id, Customization customization, ContextHolder contextHolder, T selection, Iterable<T> selections, Function<String, T> stringToSelection, Function<T, Text> selectionToString, Consumer<T> saveConsumer) {
		super(id, customization);
		this.contextHolder = contextHolder;
		this.selection = selection;
		this.selections = selections;
		this.stringToSelection = stringToSelection;
		this.selectionToString = selectionToString;
		this.saveConsumer = saveConsumer;
	}

	@Override
	protected List<AbstractConfigListEntry> addToConfig(ConfigBuilder builder) {
		this.mainConfigEntry = ConfigHelper.createFixedDropdownMenu(builder, this.getName(), this.getTooltip(), 
				this.selection, this.selections, this.stringToSelection, this.selectionToString, this.saveConsumer)
				.setSuggestionMode(false)
				.build();
		List<AbstractConfigListEntry> list = Lists.newArrayList(this.mainConfigEntry);
		this.selection.createRelatedElements(this.getCustomization(), this.contextHolder).stream()
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