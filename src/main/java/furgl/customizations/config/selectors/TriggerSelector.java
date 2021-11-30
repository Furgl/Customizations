package furgl.customizations.config.selectors;

import com.google.common.collect.Lists;

import furgl.customizations.customizations.Customization;
import furgl.customizations.customizations.triggers.Trigger;

public class TriggerSelector extends Selector<SelectableType> {

	public TriggerSelector(Customization customization, Trigger trigger) {
		super("triggerSelector", customization, trigger, trigger.getType(), 
				Lists.newArrayList(
						Selectables.TRIGGER_BREAK_BLOCK,
						Selectables.TRIGGER_KILL_ENTITY),
				name -> (SelectableType) Selectables.getTypeByName((String) name), 
				type -> ((SelectableType) type).getName(),
				type -> trigger.setType(type));
	}

}