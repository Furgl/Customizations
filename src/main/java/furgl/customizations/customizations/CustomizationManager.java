package furgl.customizations.customizations;

import java.util.ArrayList;

import com.google.common.collect.Lists;

import furgl.customizations.config.selectors.SelectableType;
import furgl.customizations.customizations.actions.Action;
import furgl.customizations.customizations.conditions.Condition;
import furgl.customizations.customizations.context.Context;
import furgl.customizations.customizations.triggers.Trigger;

public class CustomizationManager {

	private static ArrayList<Customization> customizations = Lists.newArrayList();

	public static void trigger(SelectableType type, Context... eventContext) {
		customizations.stream()
		// trigger type + context
		.filter(customization -> {
			for (Trigger trigger : customization.getTriggers())
				if (trigger.getType() == type)
					return trigger.test(eventContext); 
			return false;
		})
		// condition context
		.filter(customization -> {
			for (Condition condition : customization.getConditions())
				if (!condition.test(eventContext))
					return false; 
			return true;
		})
		// action context
		.filter(customization -> {
			for (Action action : customization.getActions())
				if (!action.test(eventContext))
					return false; 
				else
					action.activate(eventContext);
			return true;
		})
		.forEach(customization -> {
			/*Customizations.LOGGER.info("Activated Customization: "+customization);*/
		});
	}

	// GETTERS AND SETTERS

	public static void clearCustomizations() {
		customizations.clear();
	}

	public static void addCustomization(Customization customization) {
		customizations.add(customization);
	}

	public static void removeCustomization(String name) {
		customizations.removeIf(customization -> customization.name.equals(name));
	}

	public static void removeCustomization(Customization customization) {
		customizations.remove(customization);
	}

	public static ArrayList<Customization> getAllCustomizations() {
		return Lists.newArrayList(customizations);
	}

	public static int getNumberOfCustomizations() {
		return customizations.size();
	}

}