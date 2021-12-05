package furgl.customizations.client.parts;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import furgl.customizations.common.customizations.Customization;
import furgl.customizations.common.customizations.context.Context;
import furgl.customizations.common.customizations.context.ContextHolder;
import furgl.customizations.common.customizations.context.Contexts;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;

public class PlayerNamePart extends Part {

	public PlayerNamePart(Customization customization, ContextHolder contextHolder) {
		super("playerName", customization, contextHolder);
	}
	
	@Override
	public ArrayList<Context> getRelatedContexts() {
		return Lists.newArrayList(Contexts.SELECTED_ENTITY);
	}

	@Override
	protected List<AbstractConfigListEntry> addToConfig(ConfigBuilder builder) {
		this.mainConfigEntry = builder.entryBuilder()
				.startStrField(this.getName(), this.contextHolder.getOrAddContext(Contexts.SELECTED_ENTITY).playerName)
				.setSaveConsumer(value -> this.contextHolder.getOrAddContext(Contexts.SELECTED_ENTITY).playerName = value)
				.setTooltip(this.getTooltip())
				.build();
		return Lists.newArrayList(this.mainConfigEntry);
	}

}