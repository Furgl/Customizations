package furgl.customizations.client.parts;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import furgl.customizations.common.customizations.Customization;
import furgl.customizations.common.customizations.context.Context;
import furgl.customizations.common.customizations.context.Contexts;
import furgl.customizations.common.customizations.context.holders.ConfigContextHolder;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;

public class PositionPart extends Part {

	public PositionPart(Customization customization, ConfigContextHolder contextHolder) {
		super("position", customization, contextHolder);
	}
	
	@Override
	public ArrayList<Context> getRelatedContexts() {
		return Lists.newArrayList(Contexts.SELECTED_ENTITY);
	}

	@Override
	protected List<AbstractConfigListEntry> addToConfig(ConfigBuilder builder) {
		List<AbstractConfigListEntry> list = Lists.newArrayList();
		list.add(this.mainConfigEntry = builder.entryBuilder()
				.startFloatField(this.getName("x"), this.contextHolder.getOrAddContext(Contexts.SELECTED_ENTITY).x)
				.setSaveConsumer(value -> this.contextHolder.getOrAddContext(Contexts.SELECTED_ENTITY).x = value)
				.setTooltip(this.getTooltip("x"))
				.build());
		list.add(builder.entryBuilder()
				.startFloatField(this.getName("y"), this.contextHolder.getOrAddContext(Contexts.SELECTED_ENTITY).y)
				.setSaveConsumer(value -> this.contextHolder.getOrAddContext(Contexts.SELECTED_ENTITY).y = value)
				.setTooltip(this.getTooltip("y"))
				.build());
		list.add(builder.entryBuilder()
				.startFloatField(this.getName("z"), this.contextHolder.getOrAddContext(Contexts.SELECTED_ENTITY).z)
				.setSaveConsumer(value -> this.contextHolder.getOrAddContext(Contexts.SELECTED_ENTITY).z = value)
				.setTooltip(this.getTooltip("z"))
				.build());
		return list;
	}

}