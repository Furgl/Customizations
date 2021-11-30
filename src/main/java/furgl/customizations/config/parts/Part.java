package furgl.customizations.config.parts;

import java.util.ArrayList;

import org.jetbrains.annotations.Nullable;

import furgl.customizations.Customizations;
import furgl.customizations.config.elements.ConfigElement;
import furgl.customizations.customizations.Customization;
import furgl.customizations.customizations.context.Context;
import furgl.customizations.customizations.context.ContextHolder;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public abstract class Part extends ConfigElement {
	
	protected ContextHolder contextHolder;

	protected Part(String id, @Nullable Customization customization, ContextHolder contextHolder) {
		super(id, customization);
		this.contextHolder = contextHolder;
	}
	
	/**Get contexts that are used in this element*/
	@Override
	public abstract ArrayList<Context> getRelatedContexts();
	
	@Override
	public Text getName() {
		return getName(null);
	}
	
	public Text getName(@Nullable String str) {
		return new TranslatableText("config."+Customizations.MODID+".part."+this.getId()+(str == null ? "" : "."+str)+".name");
	}
	
	@Override
	public Text getTooltip() {
		return getTooltip(null);
	}
	
	public Text getTooltip(@Nullable String str) {
		return new TranslatableText("config."+Customizations.MODID+".part."+this.getId()+(str == null ? "" : "."+str)+".tooltip");
	}

}
