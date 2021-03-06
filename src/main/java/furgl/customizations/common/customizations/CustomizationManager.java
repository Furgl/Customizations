package furgl.customizations.common.customizations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import furgl.customizations.common.Customizations;
import furgl.customizations.common.config.FileConfig;
import furgl.customizations.common.config.FileConfig.DebugMode;
import furgl.customizations.common.customizations.actions.Action;
import furgl.customizations.common.customizations.conditions.Condition;
import furgl.customizations.common.customizations.context.Context;
import furgl.customizations.common.customizations.context.holders.Cause;
import furgl.customizations.common.customizations.context.holders.Other;
import furgl.customizations.common.customizations.context.holders.Subject;
import furgl.customizations.common.customizations.context.holders.Target;
import furgl.customizations.common.customizations.selectables.SelectableTrigger;
import furgl.customizations.common.customizations.triggers.Trigger;

public class CustomizationManager {

	private static ArrayList<Customization> customizations = Lists.newArrayList(); 

	/**Trigger this trigger with these contexts*/
	public static void trigger(SelectableTrigger type, Cause cause, @Nullable Target target, @Nullable Other other) { 
		// only trigger on server
		if (Customizations.server != null && !Customizations.server.getOverworld().isClient) {
			List<Context> list = Lists.newArrayList();
			list.addAll(cause.getContext());
			if (target != null)
				list.addAll(target.getContext());
			if (other != null)
				list.addAll(other.getContext());
			Context[] eventContexts = list.toArray(new Context[0]);
			customizations.stream()
			// trigger type + context
			.filter(customization -> {
				for (Trigger trigger : customization.getTriggers())
					if (trigger.getType() == type) {
						boolean passed = trigger.test(eventContexts); 
						if (FileConfig.debugMode == DebugMode.DETAILED) { 
							Customizations.LOGGER.info("Triggered Customization: "+customization.name+"...");
							Customizations.LOGGER.info(" > Cause: "+cause);
							if (target != null)
								Customizations.LOGGER.info(" > Target: "+target);
							if (other != null)
								Customizations.LOGGER.info(" > Other: "+other);
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
				Set<Subject> subjects = Sets.newHashSet();
				for (Action action : customization.getActions()) {
					boolean passed = action.test(eventContexts);
					if (FileConfig.debugMode == DebugMode.DETAILED)
						Customizations.LOGGER.info(" - Testing Action: "+action+"... "+(passed ? "PASSED" : "FAILED"));
					if (!passed)
						return false;
					if (FileConfig.debugMode == DebugMode.DETAILED)
						Customizations.LOGGER.info(" - Activating Action: "+action+"...", action);
					subjects = action.activate(eventContexts);
				}
				if (FileConfig.debugMode == DebugMode.BASIC || FileConfig.debugMode == DebugMode.DETAILED) {
					Customizations.LOGGER.info("Activated Customization: "+customization.name);
					if (FileConfig.debugMode == DebugMode.BASIC) {
						Customizations.LOGGER.info(" > Cause: "+cause);
						if (target != null)
							Customizations.LOGGER.info(" > Target: "+target);
						if (other != null)
							Customizations.LOGGER.info(" > Other: "+other);
					}
					if (!subjects.isEmpty())
						Customizations.LOGGER.info(" > Subjects: "+Arrays.toString(subjects.toArray(new Subject[0])));
				}
				return true;
			})
			.count();
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