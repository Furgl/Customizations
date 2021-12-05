package furgl.customizations.client.selectors;

import java.util.ArrayList;

import com.google.common.collect.Lists;

import furgl.customizations.common.customizations.Customization;
import furgl.customizations.common.customizations.context.Context;
import furgl.customizations.common.customizations.context.ContextHolder;
import furgl.customizations.common.customizations.context.Contexts;

public class EntitySelector<T extends Selectable> extends Selector {

	public EntitySelector(Customization customization, ContextHolder contextHolder) {
		super("entity", customization, contextHolder, contextHolder.getOrAddContext(Contexts.SELECTED_ENTITY).type, 
				Lists.newArrayList(
						Selectables.ENTITY_CAUSE,
						Selectables.ENTITY_TARGET,
						Selectables.ENTITY_NEAREST,
						Selectables.ENTITY_SPECIFIC,
						Selectables.ENTITY_ALL_PLAYERS),
				value -> Selectables.getTypeByName((String) value), 
				value -> ((Selectable)value).getName(), 
				value -> contextHolder.getOrAddContext(Contexts.SELECTED_ENTITY).type = (Selectable) value);
	}
	
	@Override
	public ArrayList<Context> getRelatedContexts() {
		return Lists.newArrayList(Contexts.SELECTED_ENTITY);
	}
	
}