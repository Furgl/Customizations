package furgl.customizations.common.customizations;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import furgl.customizations.common.customizations.actions.Action;
import furgl.customizations.common.customizations.conditions.Condition;
import furgl.customizations.common.customizations.triggers.Trigger;

public class Customization {

	public String name;

	private List<Trigger> triggers = Lists.newArrayList();
	private List<Condition> conditions = Lists.newArrayList();
	private List<Action> actions = Lists.newArrayList();

	public Customization(String name) {
		this.name = name;
	}
	
	public List<Trigger> getTriggers() {
		return this.triggers;
	}
	
	public List<Condition> getConditions() {
		return this.conditions;
	}
	
	public List<Action> getActions() {
		return this.actions;
	}

	public void addTrigger(Trigger trigger) {
		if (trigger != null)
			this.triggers.add(trigger);
	}

	public void removeTrigger(String name) {
		this.triggers.removeIf(trigger -> trigger.name.equals(name));
	}

	public void addCondition(Condition condition) {
		if (condition != null)
			this.conditions.add(condition);
	}

	public void removeCondition(String name) {
		this.conditions.removeIf(condition -> condition.name.equals(name));
	}

	public void addAction(Action action) {
		if (action != null)
			this.actions.add(action);
	}

	public void removeAction(String name) {
		this.actions.removeIf(action -> action.name.equals(name));
	}

	/**Write this to a JsonElement for the config*/
	public JsonElement writeToConfig() {
		JsonObject obj = new JsonObject();
		obj.addProperty("Name", this.name);
		// write triggers
		for (int i=0; i<this.triggers.size(); ++i)
			obj.add("Trigger "+(i+1), this.triggers.get(i).writeToConfig());
		// write conditions
		for (int i=0; i<this.conditions.size(); ++i)
			obj.add("Condition "+(i+1), this.conditions.get(i).writeToConfig());
		// write actions
		for (int i=0; i<this.actions.size(); ++i)
			obj.add("Action "+(i+1), this.actions.get(i).writeToConfig());
		return obj;
	}

	@Nullable
	public static Customization readFromConfig(JsonElement element) {
		Customization customization = null;
		if (element.isJsonObject()) {
			JsonObject obj = element.getAsJsonObject();
			if (obj.has("Name")) {
				String name = obj.get("Name").getAsString();
				customization = new Customization(name);

				// read triggers
				int index = 1;
				while (obj.has("Trigger "+index))
					customization.addTrigger(Trigger.readFromConfig(obj.get("Trigger "+index++)));
				// read conditions
				index = 1;
				while (obj.has("Condition "+index))
					customization.addCondition(Condition.readFromConfig(obj.get("Condition "+index++)));
				// read actions
				index = 1;
				while (obj.has("Action "+index))
					customization.addAction(Action.readFromConfig(obj.get("Action "+index++)));
			}
		}
		return customization;
	}
	
	@Override
	public String toString() {
		return this.name+" {Triggers:"+this.getTriggers()+",Conditions:"+this.getConditions()+",Actions:"+this.getActions()+"}";
	}

}