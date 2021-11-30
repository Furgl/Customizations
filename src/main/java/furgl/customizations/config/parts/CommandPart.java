package furgl.customizations.config.parts;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import furgl.customizations.customizations.Customization;
import furgl.customizations.customizations.context.Context;
import furgl.customizations.customizations.context.ContextHolder;
import furgl.customizations.customizations.context.Contexts;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;

public class CommandPart extends Part {

	public CommandPart(Customization customization, ContextHolder contextHolder) {
		super("command", customization, contextHolder);
	}
	
	@Override
	public ArrayList<Context> getRelatedContexts() {
		return Lists.newArrayList(Contexts.COMMAND);
	}

	@Override
	protected List<AbstractConfigListEntry> addToConfig(ConfigBuilder builder) {
		this.mainConfigEntry = builder.entryBuilder()
				.startStrField(this.getName(), this.contextHolder.getOrAddContext(Contexts.COMMAND).command)
				.setSaveConsumer(command -> this.contextHolder.getOrAddContext(Contexts.COMMAND).command = command)
				.setTooltip(this.getTooltip())
				.build();
		return Lists.newArrayList(this.mainConfigEntry);
	}

}