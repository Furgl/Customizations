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

public class NumberOfEntitiesPart extends Part {

	public NumberOfEntitiesPart(Customization customization, ContextHolder contextHolder) {
		super("numberOfEntities", customization, contextHolder);
	}
	
	@Override
	public ArrayList<Context> getRelatedContexts() {
		return Lists.newArrayList(Contexts.SELECTED_ENTITY);
	}

	@Override
	protected List<AbstractConfigListEntry> addToConfig(ConfigBuilder builder) {
		List<AbstractConfigListEntry> list = Lists.newArrayList();
		list.add(this.mainConfigEntry = builder.entryBuilder()
				.startIntField(this.getName(), this.contextHolder.getOrAddContext(Contexts.SELECTED_ENTITY).numberOfEntities)
				.setSaveConsumer(value -> this.contextHolder.getOrAddContext(Contexts.SELECTED_ENTITY).numberOfEntities = value)
				.build());
		return list;
	}

}