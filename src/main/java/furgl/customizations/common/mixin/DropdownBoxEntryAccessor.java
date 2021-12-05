package furgl.customizations.common.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import me.shedaniel.clothconfig2.gui.entries.DropdownBoxEntry;
import me.shedaniel.clothconfig2.gui.entries.DropdownBoxEntry.SelectionElement;

@Mixin(DropdownBoxEntry.class)
public interface DropdownBoxEntryAccessor {

	@Accessor("selectionElement")
	SelectionElement getSelectionElement();
	
}