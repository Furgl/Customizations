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

public class RadiusPart extends Part {

	public RadiusPart(Customization customization, ContextHolder contextHolder) {
		super("radius", customization, contextHolder);
	}
	
	@Override
	public ArrayList<Context> getRelatedContexts() {
		return Lists.newArrayList(Contexts.SELECTED_ENTITY);
	}

	@Override
	protected List<AbstractConfigListEntry> addToConfig(ConfigBuilder builder) {
		List<AbstractConfigListEntry> list = Lists.newArrayList();
		list.add(this.mainConfigEntry = builder.entryBuilder()
				.startFloatField(this.getName(), this.contextHolder.getOrAddContext(Contexts.SELECTED_ENTITY).radius)
				.setSaveConsumer(value -> this.contextHolder.getOrAddContext(Contexts.SELECTED_ENTITY).radius = value)
				.build());
		return list;
	}

}