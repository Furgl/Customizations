package furgl.customizations.customizations.context;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

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
		super("Block");
	}

	@Override
	public boolean test(Context eventContext) {
		return this.block == Blocks.AIR || (eventContext instanceof BlockContext && ((BlockContext)eventContext).block == this.block);
	}	

	@Override
	public JsonElement writeToConfig() {
		JsonObject obj = new JsonObject();
		obj.addProperty("Block", Registry.BLOCK.getId(this.block).toString());
		return obj;
	}

	@Override
	public BlockContext readFromConfig(JsonElement element) {
		BlockContext context = (BlockContext) this.newInstance();
		if (element.isJsonObject()) {
			JsonObject obj = element.getAsJsonObject();
			if (obj.has("Block")) {
				Block block = Registry.BLOCK.get(new Identifier(obj.get("Block").getAsString()));
				context.block = block;
			}
		}
		return context;
	}
	
	@Override
	public String toString() {
		return this.getId()+" {block:"+Registry.BLOCK.getId(this.block).toString()+"}";
	}
	
}