package furgl.customizations.common.customizations.context;

import java.util.Map;
import java.util.function.Function;

import com.google.common.collect.Maps;

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
	public boolean test(Context... eventContexts) {
		return this.block == Blocks.AIR || Contexts.get(Contexts.BLOCK, eventContexts).map(ctx -> ctx.block == this.block).orElse(false);
	}
	
	@Override
	protected Map<String, Function<Context[], String>> createPlaceholders() {
		Map<String, Function<Context[], String>> map = Maps.newLinkedHashMap();
		map.put("block_id", eventContexts -> Registry.BLOCK.getId(this.block).toString());
		return map;
	}

}