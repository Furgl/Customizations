package furgl.customizations.customizations.triggers;

import java.util.Set;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Sets;

import furgl.customizations.config.selectors.SelectableType;
import net.minecraft.item.ItemStack;

public class TriggerTypes {
	
	public static final Set<SelectableType> ALL_TYPES = Sets.newHashSet();

	public static final SelectableType NONE = new SelectableType(SelectableType.Type.TRIGGER, "none", ItemStack.EMPTY);
	public static final SelectableType BREAK_BLOCK = new BreakBlockTrigger();
	public static final SelectableType KILL_MOB = new KillMobTrigger();
		
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