package furgl.customizations.config.parts;

import org.jetbrains.annotations.Nullable;

import furgl.customizations.Customizations;
import furgl.customizations.config.ConfigElement;
import furgl.customizations.customizations.Customization;
import furgl.customizations.customizations.context.ContextHolder;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public abstract class Part extends ConfigElement {
	
	protected ContextHolder contextHolder;

	protected Part(String id, @Nullable Customization customization, ContextHolder contextHolder) {
		super(id, customization);
		this.contextHolder = contextHolder;
	}
	
	@Override
	public Text getName() {
		return new TranslatableText("config."+Customizations.MODID+".part."+this.getId()+".name");
	}

	@Override
	public Text getTooltip() {
		return new TranslatableText("config."+Customizations.MODID+".part."+this.getId()+".tooltip");
	}

}
