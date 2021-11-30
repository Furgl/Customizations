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

public class ConsoleMessagePart extends Part {

	public ConsoleMessagePart(Customization customization, ContextHolder contextHolder) {
		super("consoleMessage", customization, contextHolder);
	}
	
	@Override
	public ArrayList<Context> getRelatedContexts() {
		return Lists.newArrayList(Contexts.CONSOLE_MESSAGE);
	}

	@Override
	protected List<AbstractConfigListEntry> addToConfig(ConfigBuilder builder) {
		this.mainConfigEntry = builder.entryBuilder()
				.startStrField(this.getName(), this.contextHolder.getOrAddContext(Contexts.CONSOLE_MESSAGE).message)
				.setSaveConsumer(message -> this.contextHolder.getOrAddContext(Contexts.CONSOLE_MESSAGE).message = message)
				.setTooltip(this.getTooltip())
				.build();
		return Lists.newArrayList(this.mainConfigEntry);
	}

}