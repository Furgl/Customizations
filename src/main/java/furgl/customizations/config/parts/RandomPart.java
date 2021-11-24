package furgl.customizations.config.parts;

import java.util.List;

import com.google.common.collect.Lists;

import furgl.customizations.customizations.Customization;
import furgl.customizations.customizations.context.ContextHolder;
import furgl.customizations.customizations.context.Contexts;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;

public class RandomPart extends Part {

	public RandomPart(Customization customization, ContextHolder contextHolder) {
		super("random", customization, contextHolder);
	}

	@Override
	protected List<AbstractConfigListEntry> addToConfig(ConfigBuilder builder) {
		this.mainConfigEntry = builder.entryBuilder()
				.startFloatField(this.getName(), this.contextHolder.getOrAddContext(Contexts.RANDOM).chance)
				.setSaveConsumer(chance -> this.contextHolder.getOrAddContext(Contexts.RANDOM).chance = chance)
				.setTooltip(this.getTooltip())
				.build();
		return Lists.newArrayList(this.mainConfigEntry);
	}

}