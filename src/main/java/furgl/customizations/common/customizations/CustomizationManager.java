package furgl.customizations.common.customizations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;

import furgl.customizations.common.Customizations;
import furgl.customizations.common.config.FileConfig;
import furgl.customizations.common.config.FileConfig.DebugMode;
import furgl.customizations.common.customizations.actions.Action;
import furgl.customizations.common.customizations.conditions.Condition;
import furgl.customizations.common.customizations.context.Context;
import furgl.customizations.common.customizations.triggers.SelectableTrigger;
import furgl.customizations.common.customizations.triggers.Trigger;

public class CustomizationManager {

	private static ArrayList<Customization> customizations = Lists.newArrayList();

	/**Trigger this trigger with these contexts*/
	public static void trigger(SelectableTrigger type, Object... contexts) {
		// only trigger on server
		if (Customizations.server != null && !Customizations.server.getOverworld().isClient) {
			// read in contexts
			List<Context> list = Lists.newArrayList();
			for (Object obj : contexts)
				if (obj instanceof Context)
					list.add((Context) obj);
				else if (obj instanceof Collection)
					list.addAll((Collection<? extends Context>) obj);
			Context[] eventContexts = list.toArray(new Context[0]);
			customizations.stream()
			// trigger type + context
			.filter(customization -> {
				for (Trigger trigger : customization.getTriggers())
					if (trigger.getType() == type) {
						boolean passed = trigger.test(eventContexts); 
						if (FileConfig.debugMode == DebugMode.DETAILED) { 
							Customizations.LOGGER.info("Triggered Customization: "+customization+"...\nWith context: "+Arrays.toString(eventContexts));
							Customizations.LOGGER.info(" - Testing Trigger: "+trigger+"... "+(passed ? "PASSED" : "FAILED"));
						}
						return passed;
					}
				return false;
			})
			// condition context
			.filter(customization -> {
				for (Condition condition : customization.getConditions()) {
					boolean passed = condition.test(eventContexts); 
					if (FileConfig.debugMode == DebugMode.DETAILED)
						Customizations.LOGGER.info(" - Testing Condition: "+condition+"... "+(passed ? "PASSED" : "FAILED"));
					if (!passed)
						return false;
				}
				return true;
			})
			// action context
			.filter(customization -> {
				for (Action action : customization.getActions()) {
					boolean passed = action.test(eventContexts);
					if (FileConfig.debugMode == DebugMode.DETAILED)
						Customizations.LOGGER.info(" - Testing Action: "+action+"... "+(passed ? "PASSED" : "FAILED"));
					if (!passed)
						return false;
					if (FileConfig.debugMode == DebugMode.DETAILED)
						Customizations.LOGGER.info(" - Activating Action: "+action+"...", action);
					action.activate(eventContexts);
				}
				return true;
			})
			.forEach(customization -> {
				if (FileConfig.debugMode == DebugMode.BASIC || FileConfig.debugMode == DebugMode.DETAILED)
					Customizations.LOGGER.info("Activated Customization: "+customization+"...\nWith context: "+Arrays.toString(eventContexts));
			});
		}
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