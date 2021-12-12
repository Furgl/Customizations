package furgl.customizations.client.selectors;

import com.google.common.collect.Lists;

import furgl.customizations.common.customizations.Customization;
import furgl.customizations.common.customizations.conditions.Condition;
import furgl.customizations.common.customizations.selectables.Selectable;
import furgl.customizations.common.customizations.selectables.Selectables;

public class ConditionSelector extends Selector<Selectable> {

	public ConditionSelector(Customization customization, Condition condition) {
		super("conditionSelector", customization, condition, condition.getType(), 
				Lists.newArrayList(Selectables.ALL_CONDITIONS),
				name -> Selectables.getTypeByName((String) name), 
				type -> type.getName(),
				type -> condition.setType(type));
	}

}