package furgl.customizations.client.parts;

import java.util.ArrayList;

import org.jetbrains.annotations.Nullable;

import furgl.customizations.client.elements.ConfigElement;
import furgl.customizations.common.Customizations;
import furgl.customizations.common.customizations.Customization;
import furgl.customizations.common.customizations.context.Context;
import furgl.customizations.common.customizations.context.holders.ConfigContextHolder;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public abstract class Part extends ConfigElement {
	
	protected ConfigContextHolder contextHolder;

	protected Part(String id, @Nullable Customization customization, ConfigContextHolder contextHolder) {
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
	public MutableText getTooltip() {
		return getTooltip(null);
	}
	
	public MutableText getTooltip(@Nullable String str) {
		return new TranslatableText("config."+Customizations.MODID+".part."+this.getId()+(str == null ? "" : "."+str)+".tooltip");
	}

}
