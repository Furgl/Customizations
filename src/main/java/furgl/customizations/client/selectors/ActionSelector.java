package furgl.customizations.client.selectors;

import com.google.common.collect.Lists;

import furgl.customizations.common.customizations.Customization;
import furgl.customizations.common.customizations.actions.Action;

public class ActionSelector extends Selector<Selectable> {

	public ActionSelector(Customization customization, Action action) {
		super("actionSelector", customization, action, action.getType(), 
				Lists.newArrayList(Selectables.ALL_ACTIONS),
				name -> Selectables.getTypeByName((String) name), 
				type -> type.getName(),
				type -> action.setType(type));
	}

}