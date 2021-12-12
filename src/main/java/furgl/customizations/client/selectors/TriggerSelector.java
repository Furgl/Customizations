package furgl.customizations.client.selectors;

import com.google.common.collect.Lists;

import furgl.customizations.common.customizations.Customization;
import furgl.customizations.common.customizations.selectables.Selectable;
import furgl.customizations.common.customizations.selectables.Selectables;
import furgl.customizations.common.customizations.triggers.Trigger;

public class TriggerSelector extends Selector<Selectable> {

	public TriggerSelector(Customization customization, Trigger trigger) {
		super("triggerSelector", customization, trigger, trigger.getType(), 
				Lists.newArrayList(Selectables.ALL_TRIGGERS),
				name -> Selectables.getTypeByName((String) name), 
				type -> type.getName(),
				type -> trigger.setType(type));
	}

}