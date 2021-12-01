package furgl.customizations.config.parts;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

import furgl.customizations.config.ConfigHelper;
import furgl.customizations.config.selectors.Selectables;
import furgl.customizations.customizations.Customization;
import furgl.customizations.customizations.context.Context;
import furgl.customizations.customizations.context.ContextHolder;
import furgl.customizations.customizations.context.Contexts;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class EntityTypePart extends Part {

	public EntityTypePart(Customization customization, ContextHolder contextHolder) {
		super("entityType", customization, contextHolder);
	}

	@Override
	public ArrayList<Context> getRelatedContexts() {
		return Lists.newArrayList(Contexts.SELECTED_ENTITY);
	}

	@Override
	protected List<AbstractConfigListEntry> addToConfig(ConfigBuilder builder) {
		Set<String> selections = Registry.ENTITY_TYPE.stream().map(type -> EntityType.getId(type).toString()).collect(Collectors.toSet());
		selections.add(Selectables.ANY.getName().getString());
		this.mainConfigEntry = ConfigHelper.<String>createDropdownMenu(builder, this.getName(), this.getTooltip(), 
				this.contextHolder.getOrAddContext(Contexts.SELECTED_ENTITY).entityType == null ? Selectables.ANY.getName().getString() : 
					EntityType.getId(this.contextHolder.getOrAddContext(Contexts.SELECTED_ENTITY).entityType).toString(), 
					value -> {
						try {
							EntityType<?> type = Registry.ENTITY_TYPE.getOrEmpty(new Identifier(value.toLowerCase())).orElse(null);
							return type == null ? Selectables.ANY.getName().getString() : EntityType.getId(type).toString();
						} 
						catch (Exception e) {
							return Selectables.ANY.getName().getString();
						} 
					}, value -> new LiteralText(value),
					value -> {
						EntityType<?> type = Registry.ENTITY_TYPE.getOrEmpty(new Identifier(value.toLowerCase())).orElse(null);
						return type == null ? ItemStack.EMPTY : new ItemStack(SpawnEggItem.forEntity(type));
					})
				.setSaveConsumer(value -> this.contextHolder.getOrAddContext(Contexts.SELECTED_ENTITY).entityType = Registry.ENTITY_TYPE.getOrEmpty(new Identifier(value.toLowerCase())).orElse(null))
				.setSelections(selections)
				.build();
		return Lists.newArrayList(this.mainConfigEntry);
	}

}