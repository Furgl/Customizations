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

public class DebugVisualsElement extends ConfigElement {

	public DebugVisualsElement() {
		super("debugVisuals", null);
	}

	@Override
	protected List<AbstractConfigListEntry> addToConfig(ConfigBuilder builder) {
		this.mainConfigEntry = builder.entryBuilder()
				.startBooleanToggle(this.getName(), FileConfig.debugVisuals)
				.setTooltip(this.getTooltip())
				.setSaveConsumer(bool -> {
					FileConfig.debugVisuals = bool;
				})
				.build();
		return Lists.newArrayList(this.mainConfigEntry);
	}
	
}