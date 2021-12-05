package furgl.customizations.client.selectors;

import java.util.ArrayList;

import com.google.common.collect.Lists;

import furgl.customizations.common.customizations.Customization;
import furgl.customizations.common.customizations.context.Context;
import furgl.customizations.common.customizations.context.ContextHolder;
import furgl.customizations.common.customizations.context.Contexts;

public class PositionSelector<T extends Selectable> extends Selector {

	public PositionSelector(Customization customization, ContextHolder contextHolder) {
		super("position", customization, contextHolder, contextHolder.getOrAddContext(Contexts.SELECTED_ENTITY).positionType, 
				Lists.newArrayList(
						Selectables.POSITION_CAUSE,
						Selectables.POSITION_TARGET,
						Selectables.POSITION_FIXED),
				value -> Selectables.getTypeByName((String) value), 
				value -> ((Selectable)value).getName(), 
				value -> contextHolder.getOrAddContext(Contexts.SELECTED_ENTITY).positionType = (Selectable) value);
	}
	
	@Override
	public ArrayList<Context> getRelatedContexts() {
		return Lists.newArrayList(Contexts.SELECTED_ENTITY);
	}
	
}