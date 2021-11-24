package furgl.customizations.config.parts;

import java.util.List;

import com.google.common.collect.Lists;

import furgl.customizations.customizations.Customization;
import furgl.customizations.customizations.context.ContextHolder;
import furgl.customizations.customizations.context.Contexts;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;

public class ServerCommandPart extends Part {

	public ServerCommandPart(Customization customization, ContextHolder contextHolder) {
		super("serverCommand", customization, contextHolder);
	}

	@Override
	protected List<AbstractConfigListEntry> addToConfig(ConfigBuilder builder) {
		this.mainConfigEntry = builder.entryBuilder()
				.startStrField(this.getName(), this.contextHolder.getOrAddContext(Contexts.SERVER_COMMAND).command)
				.setSaveConsumer(command -> this.contextHolder.getOrAddContext(Contexts.SERVER_COMMAND).command = command)
				.setTooltip(this.getTooltip())
				.build();
		return Lists.newArrayList(this.mainConfigEntry);
	}

}