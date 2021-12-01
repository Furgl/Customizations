package furgl.customizations.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import me.shedaniel.clothconfig2.gui.AbstractConfigScreen;
import net.minecraft.client.gui.screen.Screen;

@Mixin(AbstractConfigScreen.class)
public interface AbstractConfigScreenAccessor {

	@Accessor("parent")
	Screen getParent();
	
}