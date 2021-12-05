package furgl.customizations.client.parts;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

import furgl.customizations.client.config.ConfigHelper;
import furgl.customizations.client.selectors.Selectables;
import furgl.customizations.common.customizations.Customization;
import furgl.customizations.common.customizations.context.Context;
import furgl.customizations.common.customizations.context.ContextHolder;
import furgl.customizations.common.customizations.context.Contexts;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class BlockPart extends Part {

	public BlockPart(Customization customization, ContextHolder contextHolder) {
		super("block", customization, contextHolder);
	}

	@Override
	public ArrayList<Context> getRelatedContexts() {
		return Lists.newArrayList(Contexts.BLOCK);
	}

	@Override
	protected List<AbstractConfigListEntry> addToConfig(ConfigBuilder builder) {
		this.mainConfigEntry = ConfigHelper.createDropdownMenu(builder, this.getName(), this.getTooltip(), 
				this.contextHolder.getOrAddContext(
						Contexts.BLOCK).block, 
				value -> {
					try {
						return Registry.BLOCK.getOrEmpty(new Identifier(value)).orElse(Blocks.AIR);
					} catch (Exception e) {
						return Blocks.AIR;
					} 
				}, block -> block == Blocks.AIR ? Selectables.ANY.getName() : new LiteralText(Registry.BLOCK.getId(block).toString()))
				.setSaveConsumer(block -> this.contextHolder.getOrAddContext(Contexts.BLOCK).block = block)
				.setSelections(Registry.BLOCK.stream().collect(Collectors.toSet()))
				.build();
		return Lists.newArrayList(this.mainConfigEntry);
	}

}