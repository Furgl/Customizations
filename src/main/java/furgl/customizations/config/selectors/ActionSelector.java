package furgl.customizations.config.selectors;

import furgl.customizations.customizations.Customization;
import furgl.customizations.customizations.actions.Action;
import furgl.customizations.customizations.actions.ActionTypes;

public class ActionSelector extends Selector<SelectableType> {

	public ActionSelector(Customization customization, Action action) {
		super("actionSelector", customization, action, action.getType(), 
				ActionTypes.ALL_TYPES,
				name -> ActionTypes.getTypeByName((String) name), 
				type -> ((SelectableType) type).getName(),
				type -> action.setType(type));
	}

}