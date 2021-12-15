package furgl.customizations.client.elements;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Lists;

import furgl.customizations.client.config.Config;
import furgl.customizations.common.Customizations;
import furgl.customizations.common.customizations.Customization;
import furgl.customizations.common.customizations.context.Context;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public abstract class ConfigElement {

	private String id;
	/**Only null for CustomizationList*/
	@Nullable
	private Customization customization;
	@Nullable
	protected List<AbstractConfigListEntry> configEntries;
	@Nullable
	protected AbstractConfigListEntry mainConfigEntry;

	protected ConfigElement(String id, @Nullable Customization customization) {
		this.id = id;
		this.customization = customization;
		Config.currentElements.add(this);
	}
	
	public List<AbstractConfigListEntry> getConfigEntries() {
		return getOrCreateConfigEntries(null);
	}

	public List<AbstractConfigListEntry> getOrCreateConfigEntries(@Nullable ConfigBuilder builder) {
		if (this.configEntries == null) {
			if (builder == null)
				return Lists.newArrayList();
			else
				this.configEntries = this.addToConfig(builder);
		}
		return this.configEntries;
	}

	/**Add this to the config*/
	protected abstract List<AbstractConfigListEntry> addToConfig(ConfigBuilder builder);

	// GETTERS AND SETTERS
	
	@Nullable
	public AbstractConfigListEntry getOrCreateMainConfigEntry(@Nullable ConfigBuilder builder) {
		if (this.mainConfigEntry == null && builder != null) 
				this.addToConfig(builder);
		return this.mainConfigEntry;
	}

	@Nullable
	public AbstractConfigListEntry getMainConfigEntry() {
		return this.getOrCreateMainConfigEntry(null);
	}

	/**Only null for CustomizationList*/
	public Customization getCustomization() {
		return this.customization;
	}

	public String getId() {
		return this.id;
	}

	public Text getName() {
		return new TranslatableText("config."+Customizations.MODID+"."+this.getId()+".name");
	}

	public Text getTooltip() {
		return new TranslatableText("config."+Customizations.MODID+"."+this.getId()+".tooltip");
	}

	/**Check for add, deleted, or renamed items
	 * @return if config should reload*/
	public boolean checkForChanges() {
		return false;
	}
	
	/**Get contexts that are used in this element (and children/related elements)*/
	public ArrayList<Context> getRelatedContexts() {
		return Lists.newArrayList();
	}

}