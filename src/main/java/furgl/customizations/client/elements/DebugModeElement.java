package furgl.customizations.client.elements;

import java.util.List;
import java.util.Optional;

import com.google.common.collect.Lists;

import furgl.customizations.common.Customizations;
import furgl.customizations.common.config.FileConfig;
import furgl.customizations.common.config.FileConfig.DebugMode;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class DebugModeElement extends ConfigElement {

	public DebugModeElement() {
		super("debugMode", null);
	}

	@Override
	protected List<AbstractConfigListEntry> addToConfig(ConfigBuilder builder) {
		this.mainConfigEntry = builder.entryBuilder()
				.startEnumSelector(this.getName(), DebugMode.class, FileConfig.debugMode)
				.setDefaultValue(DebugMode.OFF)
				.setSaveConsumer(debugMode -> FileConfig.debugMode = debugMode)
				.setEnumNameProvider(debugMode -> ((DebugMode) debugMode).getName())
				.setTooltipSupplier(debugMode -> Optional.of(new Text[] {debugMode.getTooltip()}))
				.build();
		return Lists.newArrayList(this.mainConfigEntry);
	}
	
	@Override
	public Text getName() {
		return new TranslatableText("config."+Customizations.MODID+".debugMode.name");
	}

	@Override
	public Text getTooltip() {
		return new TranslatableText("config."+Customizations.MODID+".debugMode.tooltip");
	}

}