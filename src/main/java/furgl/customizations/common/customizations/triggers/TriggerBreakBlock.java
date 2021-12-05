package furgl.customizations.common.customizations.triggers;

import java.util.List;
import java.util.function.BiFunction;

import com.google.common.collect.Lists;

import furgl.customizations.client.elements.ConfigElement;
import furgl.customizations.common.customizations.Customization;
import furgl.customizations.common.customizations.context.BlockContext;
import furgl.customizations.common.customizations.context.Context;
import furgl.customizations.common.customizations.context.ContextHelper;
import furgl.customizations.common.customizations.context.ContextHolder;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TriggerBreakBlock extends SelectableTrigger {

	public TriggerBreakBlock(String id, ItemStack stack, BiFunction<Customization, ContextHolder, List<ConfigElement>> relatedElements) {
		super(id, stack, relatedElements);
	}

	@Override
	public List<Context> getContextForPlaceholders() {
		return getContext(null, null, null, null);
	}

	public List<Context> getContext(ServerPlayerEntity player, World world, BlockPos pos, Block block) {
		List<Context> ctxs = Lists.newArrayList(); 
		ctxs.addAll(ContextHelper.getCauseContext(player));
		ctxs.addAll(ContextHelper.getTargetContext(world, pos)); 
		ctxs.add(new BlockContext(block));	
		return ctxs;
	}

}