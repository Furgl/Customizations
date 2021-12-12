package furgl.customizations.client.parts;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import furgl.customizations.client.config.ConfigHelper;
import furgl.customizations.common.Customizations;
import furgl.customizations.common.customizations.Customization;
import furgl.customizations.common.customizations.context.Context;
import furgl.customizations.common.customizations.context.Contexts;
import furgl.customizations.common.customizations.context.holders.ConfigContextHolder;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.impl.builders.DropdownMenuBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

public class DimensionPart extends Part {

	private static enum Dimension {
		OVERWORLD(new ItemStack(Blocks.GRASS_BLOCK), World.OVERWORLD), 
		NETHER(new ItemStack(Blocks.NETHERRACK), World.NETHER), 
		END(new ItemStack(Blocks.END_STONE), World.END);

		public ItemStack stack;
		public RegistryKey<World> key;

		private Dimension(ItemStack stack, RegistryKey<World> key) {
			this.key = key;
			this.stack = stack;
		}

		public static Set<RegistryKey<World>> getKeys() {
			Set<RegistryKey<World>> keys = Sets.newHashSet();
			if (Customizations.server == null)
				for (Dimension dim : Dimension.values())
					keys.add(dim.key);
			else
				keys = Customizations.server.getWorldRegistryKeys();
			return keys;					
		}
		
		@Nullable
		public static RegistryKey<World> getKey(String value) {
			for (RegistryKey<World> key : Dimension.getKeys())
				if (key.getValue().toString().equals(value))
					return key;
			return null;
		}
	}

	public DimensionPart(Customization customization, ConfigContextHolder contextHolder) {
		super("dimension", customization, contextHolder);
	}

	@Override
	public ArrayList<Context> getRelatedContexts() {
		return Lists.newArrayList(Contexts.SELECTED_ENTITY);
	}

	@Override
	protected List<AbstractConfigListEntry> addToConfig(ConfigBuilder builder) {
		DropdownMenuBuilder menu = ConfigHelper.createDropdownMenu(builder, this.getName(), this.getTooltip(), 
				this.contextHolder.getOrAddContext(Contexts.SELECTED_ENTITY).dimension == null ? "" : this.contextHolder.getOrAddContext(Contexts.SELECTED_ENTITY).dimension.getValue().toString(), 
						value -> {
							RegistryKey<World> key = Dimension.getKey(value);
							return key == null ? "" : key.getValue().toString();
						}, value -> new LiteralText((String) value), 
						value -> {
							for (Dimension dim : Dimension.values())
								if (dim.key.getValue().toString().equalsIgnoreCase((String) value))
									return dim.stack;
							return null;
						})
				.setSaveConsumer(value -> this.contextHolder.getOrAddContext(Contexts.SELECTED_ENTITY).dimension = Dimension.getKey(value))
				.setSelections(Dimension.getKeys().stream().map(key -> key.getValue().toString()).collect(Collectors.toSet()));
		// if no server, don't show errors
		this.mainConfigEntry = menu.build();
		return Lists.newArrayList(this.mainConfigEntry);
	}

}