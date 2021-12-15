package furgl.customizations.common.impl;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
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
	
	@Override
	public String toString() {
		return "{"+state+", l='"+world+"', x="+pos.getX()+", y="+pos.getY()+", z="+pos.getZ()+"}";
	}
	
}