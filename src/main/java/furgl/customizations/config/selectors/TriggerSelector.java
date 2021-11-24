package furgl.customizations.config.selectors;

import furgl.customizations.customizations.Customization;
import furgl.customizations.customizations.triggers.Trigger;
import furgl.customizations.customizations.triggers.TriggerTypes;

public class TriggerSelector extends Selector<SelectableType> {

	public TriggerSelector(Customization customization, Trigger trigger) {
		super("triggerSelector", customization, trigger, trigger.getType(), 
				TriggerTypes.ALL_TYPES,
				name -> TriggerTypes.getTypeByName((String) name), 
				type -> ((SelectableType) type).getName(),
				type -> trigger.setType(type));
	}

}