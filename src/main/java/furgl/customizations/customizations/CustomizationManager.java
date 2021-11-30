package furgl.customizations.customizations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;

import furgl.customizations.Customizations;
import furgl.customizations.config.FileConfig;
import furgl.customizations.config.FileConfig.DebugMode;
import furgl.customizations.config.selectors.SelectableType;
import furgl.customizations.customizations.actions.Action;
import furgl.customizations.customizations.conditions.Condition;
import furgl.customizations.customizations.context.Context;
import furgl.customizations.customizations.triggers.Trigger;

public class CustomizationManager {

	private static ArrayList<Customization> customizations = Lists.newArrayList();

	public static void trigger(SelectableType type, Object... contexts) {
		// read in contexts
		List<Context> list = Lists.newArrayList();
		for (Object obj : contexts)
			if (obj instanceof Context)
				list.add((Context) obj);
			else if (obj instanceof Collection)
				list.addAll((Collection<? extends Context>) obj);
		Context[] eventContext = list.toArray(new Context[0]);
		customizations.stream()
		// trigger type + context
		.filter(customization -> {
			for (Trigger trigger : customization.getTriggers())
				if (trigger.getType() == type) {
					boolean passed = trigger.test(eventContext); 
					if (FileConfig.debugMode == DebugMode.DETAILED) {
						Customizations.LOGGER.info("Triggered Customization: "+customization+"...\nWith context: "+Arrays.toString(eventContext));
						Customizations.LOGGER.info(" - Testing Trigger: "+trigger+"... "+(passed ? "PASSED" : "FAILED"));
					}
					return passed;
				}
			return false;
		})
		// condition context
		.filter(customization -> {
			for (Condition condition : customization.getConditions()) {
				boolean passed = condition.test(eventContext); 
				if (FileConfig.debugMode == DebugMode.DETAILED)
					Customizations.LOGGER.info(" - Testing Condition: "+condition+"... "+(passed ? "PASSED" : "FAILED"));
				if (!passed)
					return false;
			}
			return true;
		})
		// action context
		.filter(customization -> {
			for (Action action : customization.getActions())
				if (!action.test(eventContext))
					return false; 
				else {
					if (FileConfig.debugMode == DebugMode.DETAILED)
						Customizations.LOGGER.info(" - Activating Action: "+action+"...");
					action.activate(eventContext);
				}
			return true;
		})
		.forEach(customization -> {
			if (FileConfig.debugMode == DebugMode.BASIC || FileConfig.debugMode == DebugMode.DETAILED)
				Customizations.LOGGER.info("Activated Customization: "+customization+"\nWith context: "+Arrays.toString(eventContext));
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