package furgl.customizations.customizations.conditions;

import java.util.Set;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Sets;

import furgl.customizations.config.selectors.SelectableType;
import net.minecraft.item.ItemStack;

public class ConditionTypes {
	
	public static final Set<SelectableType> ALL_TYPES = Sets.newHashSet();

	public static final SelectableType NONE = new SelectableType(SelectableType.Type.CONDITION, "none", ItemStack.EMPTY);
	public static final SelectableType RANDOM = new RandomCondition();
		
	@Nullable
	public static SelectableType getTypeByID(String id) {
		for (SelectableType type : ALL_TYPES)
			if (type.getId().equals(id))
				return type;
		return null;
	}
	
	@Nullable
	public static SelectableType getTypeByName(String name) {
		for (SelectableType type : ALL_TYPES)
			if (type.getName().getString().equals(name))
				return type;
		return null;
	}
	
}