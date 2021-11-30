package furgl.customizations.customizations.actions;

import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;

import furgl.customizations.Customizations;
import furgl.customizations.config.elements.ConfigElement;
import furgl.customizations.config.parts.CommandPart;
import furgl.customizations.config.selectors.SelectableType;
import furgl.customizations.config.subCategories.EntitySubCategory;
import furgl.customizations.customizations.Customization;
import furgl.customizations.customizations.context.Context;
import furgl.customizations.customizations.context.ContextHelper;
import furgl.customizations.customizations.context.ContextHolder;
import furgl.customizations.customizations.context.Contexts;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class PlayerCommandAction extends SelectableType {

	public PlayerCommandAction() {
		super("actions.playerCommand", new ItemStack(Blocks.REPEATING_COMMAND_BLOCK));
	}

	@Override
	public void activate(Context... contexts) {
		if (Customizations.server != null) {
			Set<Entity> entities = ContextHelper.getEntity(PlayerEntity.class, contexts);
			for (Entity entity : entities)
				Contexts.get(Contexts.COMMAND, contexts)
				.ifPresent(context -> Customizations.server.getCommandManager().execute(entity.getCommandSource(), context.command));
		}
	}

	@Override
	public List<ConfigElement> createRelatedElements(Customization customization, ContextHolder contextHolder) {
		return Lists.newArrayList(new CommandPart(customization, contextHolder), new EntitySubCategory(customization, contextHolder));
	}

}