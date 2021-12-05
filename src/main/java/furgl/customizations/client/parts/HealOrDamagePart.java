package furgl.customizations.client.parts;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import furgl.customizations.common.customizations.Customization;
import furgl.customizations.common.customizations.context.Context;
import furgl.customizations.common.customizations.context.ContextHolder;
import furgl.customizations.common.customizations.context.Contexts;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;

public class HealOrDamagePart extends Part {

	public HealOrDamagePart(Customization customization, ContextHolder contextHolder) {
		super("healOrDamage", customization, contextHolder);
	}
	
	@Override
	public ArrayList<Context> getRelatedContexts() {
		return Lists.newArrayList(Contexts.HEAL_OR_DAMAGE_AMOUNT);
	}

	@Override
	protected List<AbstractConfigListEntry> addToConfig(ConfigBuilder builder) {
		List<AbstractConfigListEntry> list = Lists.newArrayList();
		list.add(this.mainConfigEntry = builder.entryBuilder()
				.startFloatField(this.getName("heal"), this.contextHolder.getOrAddContext(Contexts.HEAL_OR_DAMAGE_AMOUNT).heal)
				.setSaveConsumer(value -> this.contextHolder.getOrAddContext(Contexts.HEAL_OR_DAMAGE_AMOUNT).heal = value)
				.build());
		list.add(builder.entryBuilder()
				.startFloatField(this.getName("damage"), this.contextHolder.getOrAddContext(Contexts.HEAL_OR_DAMAGE_AMOUNT).damage)
				.setSaveConsumer(value -> this.contextHolder.getOrAddContext(Contexts.HEAL_OR_DAMAGE_AMOUNT).damage = value)
				.build());
		return list;
	}

}