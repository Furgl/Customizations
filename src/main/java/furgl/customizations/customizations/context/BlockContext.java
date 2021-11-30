package furgl.customizations.customizations.context;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class BlockContext extends Context {

	public Block block = Blocks.AIR;

	public BlockContext(Block block) {
		this();
		this.block = block;
	}

	public BlockContext() {
		super();
		this.variables.add(new Context.Variable("Block", 
				() -> this.block, 
				block -> this.block = (Block) block,
				block -> Registry.BLOCK.getId(this.block).toString(), 
				str -> Registry.BLOCK.get(new Identifier((String)str))));
	}

	@Override
	public boolean test(Context eventContext) {
		return this.block == Blocks.AIR || (eventContext instanceof BlockContext && ((BlockContext)eventContext).block == this.block);
	}	

}