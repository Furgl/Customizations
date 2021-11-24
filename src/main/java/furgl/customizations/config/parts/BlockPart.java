package furgl.customizations.config.parts;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

import furgl.customizations.config.ConfigHelper;
import furgl.customizations.customizations.Customization;
import furgl.customizations.customizations.context.ContextHolder;
import furgl.customizations.customizations.context.Contexts;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class BlockPart extends Part {

	public BlockPart(Customization customization, ContextHolder contextHolder) {
		super("block", customization, contextHolder);
	}

	@Override
	protected List<AbstractConfigListEntry> addToConfig(ConfigBuilder builder) {
		this.mainConfigEntry = ConfigHelper.createFixedDropdownMenu(builder, this.getName(), this.getTooltip(), 
				this.contextHolder.getOrAddContext(Contexts.BLOCK).block, Registry.BLOCK.stream().collect(Collectors.toSet()), 
				str -> {
			          try {
			            return Registry.BLOCK.getOrEmpty(new Identifier(str)).orElse(null);
			          } catch (Exception exception) {
			            return null;
			          } 
			        }, block -> new LiteralText(Registry.BLOCK.getId(block).toString()), block -> this.contextHolder.getOrAddContext(Contexts.BLOCK).block = block)
				.build();
		return Lists.newArrayList(this.mainConfigEntry);
	}

}