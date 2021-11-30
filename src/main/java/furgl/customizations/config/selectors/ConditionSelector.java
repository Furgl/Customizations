package furgl.customizations.config.selectors;

import com.google.common.collect.Lists;

import furgl.customizations.customizations.Customization;
import furgl.customizations.customizations.conditions.Condition;

public class ConditionSelector extends Selector<SelectableType> {

	public ConditionSelector(Customization customization, Condition condition) {
		super("conditionSelector", customization, condition, condition.getType(), 
				Lists.newArrayList(
						Selectables.CONDITION_RANDOM),
				name -> (SelectableType) Selectables.getTypeByName((String) name), 
				type -> ((SelectableType) type).getName(),
				type -> condition.setType(type));
	}

}