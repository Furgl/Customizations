package furgl.customizations.client.parts;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import furgl.customizations.client.config.ConfigHelper;
import furgl.customizations.common.customizations.Customization;
import furgl.customizations.common.customizations.context.Context;
import furgl.customizations.common.customizations.context.Contexts;
import furgl.customizations.common.customizations.context.holders.ConfigContextHolder;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;

public class CommandPart extends Part {

	public CommandPart(Customization customization, ConfigContextHolder contextHolder) {
		super("command", customization, contextHolder);
	}
	
	@Override
	public ArrayList<Context> getRelatedContexts() {
		return Lists.newArrayList(Contexts.COMMAND);
	}

	@Override
	protected List<AbstractConfigListEntry> addToConfig(ConfigBuilder builder) {
		this.mainConfigEntry = ConfigHelper.createStrField(builder, this.getName(), 
				this.contextHolder.getOrAddContext(Contexts.COMMAND).command, 
				command -> this.contextHolder.getOrAddContext(Contexts.COMMAND).command = command, 
				this.getTooltip(),
				true, this.getCustomization())
				.build();
		return Lists.newArrayList(this.mainConfigEntry);
	}

}