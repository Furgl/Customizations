package furgl.customizations.common.impl;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockAndLocation {

	public World world;
	public BlockPos pos;
	public BlockState state;

	public BlockAndLocation(World world, BlockPos pos, BlockState state) {
		this.world = world;
		this.pos = pos;
		this.state = state;
	}
	
}