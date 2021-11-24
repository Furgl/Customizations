package furgl.customizations.config.selectors;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import furgl.customizations.Customizations;
import furgl.customizations.config.ConfigElement;
import furgl.customizations.customizations.Customization;
import furgl.customizations.customizations.actions.ActionTypes;
import furgl.customizations.customizations.conditions.ConditionTypes;
import furgl.customizations.customizations.context.Context;
import furgl.customizations.customizations.context.ContextHolder;
import furgl.customizations.customizations.triggers.TriggerTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class SelectableType implements Selectable {

	public static enum Type {
		TRIGGER("triggers"), CONDITION("conditions"), ACTION("actions");

		public String id;

		private Type(String id) {
			this.id = id;
		}
	}

	private String id;
	private ItemStack stack;
	private Type type;

	public SelectableType(Type type, String id, ItemStack stack) {
		this.type = type;
		this.id = id;
		this.stack = stack;
		if (!id.equals("none"))
			switch (type) {
			case TRIGGER:
				TriggerTypes.ALL_TYPES.add(this);
				break;
			case CONDITION:
				ConditionTypes.ALL_TYPES.add(this);
				break;
			case ACTION:
				ActionTypes.ALL_TYPES.add(this);
				break;
			}
	}

	public String getId() {
		return this.id;
	}

	public Text getName() {
		return new TranslatableText("config."+Customizations.MODID+"."+this.type.id+"."+this.getId()+".name");
	}

	@Override
	public Text getTooltip() {
		return new TranslatableText("config."+Customizations.MODID+"."+this.type.id+"."+this.getId()+".tooltip");
	}

	@Override
	public ItemStack getStack() {
		return this.stack;
	}

	@Override
	public List<ConfigElement> createRelatedElements(Customization customization, ContextHolder contextHolder) {
		return Lists.newArrayList();
	}

	/**Activate this action*/
	public void activate(ArrayList<Context> contexts, Context... eventContext) {}

}