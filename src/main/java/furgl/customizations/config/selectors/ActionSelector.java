package furgl.customizations.config.selectors;

import com.google.common.collect.Lists;

import furgl.customizations.customizations.Customization;
import furgl.customizations.customizations.actions.Action;

public class ActionSelector extends Selector<SelectableType> {

	public ActionSelector(Customization customization, Action action) {
		super("actionSelector", customization, action, action.getType(), 
				Lists.newArrayList(
						Selectables.ACTION_CONSOLE_MESSAGE,
						Selectables.ACTION_SERVER_COMMAND,
						Selectables.ACTION_PLAYER_COMMAND,
						Selectables.ACTION_HEAL_OR_DAMAGE),
				name -> (SelectableType) Selectables.getTypeByName((String) name), 
				type -> ((SelectableType) type).getName(),
				type -> action.setType(type));
	}

}