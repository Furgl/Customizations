package furgl.customizations.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import furgl.customizations.impl.IAbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import net.minecraft.text.Text;

@Mixin(AbstractConfigListEntry.class)
public class AbstractConfigListEntryMixin implements IAbstractConfigListEntry {

	@Shadow @Final @Mutable
	private Text fieldName;

	@Override
	public void setFieldName(Text fieldName) {
		this.fieldName = fieldName;
	}
	
}