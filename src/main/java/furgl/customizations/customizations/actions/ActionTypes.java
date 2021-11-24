package furgl.customizations.customizations.actions;

import java.util.Set;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Sets;

import furgl.customizations.config.selectors.SelectableType;
import net.minecraft.item.ItemStack;

public class ActionTypes {
	
	public static final Set<SelectableType> ALL_TYPES = Sets.newHashSet();

	public static final SelectableType NONE = new SelectableType(SelectableType.Type.ACTION, "none", ItemStack.EMPTY);
	public static final SelectableType CONSOLE_MESSAGE = new ConsoleMessageAction();
	public static final SelectableType SERVER_COMMAND = new ServerCommandAction();
		
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