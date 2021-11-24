package furgl.customizations.config.selectors;

import furgl.customizations.customizations.Customization;
import furgl.customizations.customizations.conditions.Condition;
import furgl.customizations.customizations.conditions.ConditionTypes;

public class ConditionSelector extends Selector<SelectableType> {

	public ConditionSelector(Customization customization, Condition condition) {
		super("conditionSelector", customization, condition, condition.getType(), 
				ConditionTypes.ALL_TYPES,
				name -> ConditionTypes.getTypeByName((String) name), 
				type -> ((SelectableType) type).getName(),
				type -> condition.setType(type));
	}

}