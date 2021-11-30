package furgl.customizations.config.parts;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.google.common.collect.Lists;

import furgl.customizations.customizations.Customization;
import furgl.customizations.customizations.context.Context;
import furgl.customizations.customizations.context.ContextHolder;
import furgl.customizations.customizations.context.Contexts;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;

public class UUIDPart extends Part {

	public UUIDPart(Customization customization, ContextHolder contextHolder) {
		super("uuid", customization, contextHolder);
	}

	@Override
	public ArrayList<Context> getRelatedContexts() {
		return Lists.newArrayList(Contexts.SELECTED_ENTITY);
	}

	@Override
	protected List<AbstractConfigListEntry> addToConfig(ConfigBuilder builder) {
		this.mainConfigEntry = builder.entryBuilder()
				.startStrField(this.getName(), this.contextHolder.getOrAddContext(Contexts.SELECTED_ENTITY).uuid == null ? "" : this.contextHolder.getOrAddContext(Contexts.SELECTED_ENTITY).uuid.toString())
				.setSaveConsumer(value -> {
					try {
						this.contextHolder.getOrAddContext(Contexts.SELECTED_ENTITY).uuid = UUID.fromString(value);
					}
					catch (Exception e) {};
				})
				.setTooltip(this.getTooltip())
				.build();
		return Lists.newArrayList(this.mainConfigEntry);
	}

}