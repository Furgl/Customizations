package furgl.customizations.client.elements;

import java.util.List;

import com.google.common.collect.Lists;

import furgl.customizations.client.config.ConfigHelper;
import furgl.customizations.common.Customizations;
import furgl.customizations.common.config.FileConfig;
import furgl.customizations.common.impl.IClothConfigScreen;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class ShowTipsElement extends ConfigElement {

	public ShowTipsElement() {
		super("showTips", null);
	}

	@Override
	protected List<AbstractConfigListEntry> addToConfig(ConfigBuilder builder) {
		this.mainConfigEntry = builder.entryBuilder()
				.startBooleanToggle(this.getName(), FileConfig.showTips)
				.setSaveConsumer(bool -> {
					if (bool != FileConfig.showTips) {
						FileConfig.showTips = bool;
						if (ConfigHelper.isCustomizationConfigOpen())
							((IClothConfigScreen)MinecraftClient.getInstance().currentScreen).reload();
					}
				})
				.build();
		return Lists.newArrayList(this.mainConfigEntry);
	}
	
	@Override
	public Text getName() {
		return new TranslatableText("config."+Customizations.MODID+".tips."+this.getId());
	}

	@Override
	public Text getTooltip() {
		return new TranslatableText("config."+Customizations.MODID+".tips."+this.getId());
	}
	
	@Override
	public boolean checkForChanges() {
		return FileConfig.showTips != (boolean) this.getMainConfigEntry().getValue();
	}

}