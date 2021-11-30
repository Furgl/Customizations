package furgl.customizations.config.selectors;

import java.util.Set;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import furgl.customizations.config.parts.DimensionPart;
import furgl.customizations.config.parts.EntityTypePart;
import furgl.customizations.config.parts.NumberOfEntitiesPart;
import furgl.customizations.config.parts.PlayerNamePart;
import furgl.customizations.config.parts.PositionPart;
import furgl.customizations.config.parts.RadiusPart;
import furgl.customizations.config.parts.UUIDPart;
import furgl.customizations.config.subCategories.PositionSubCategory;
import furgl.customizations.customizations.actions.ConsoleMessageAction;
import furgl.customizations.customizations.actions.HealOrDamageEntityAction;
import furgl.customizations.customizations.actions.PlayerCommandAction;
import furgl.customizations.customizations.actions.ServerCommandAction;
import furgl.customizations.customizations.conditions.RandomCondition;
import furgl.customizations.customizations.triggers.BreakBlockTrigger;
import furgl.customizations.customizations.triggers.KillEntityTrigger;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class Selectables {

	public static final Set<Selectable> ALL_SELECTABLES = Sets.newHashSet();
	
	public static final SelectableType BLANK = new SelectableType("misc.blank", ItemStack.EMPTY);
	public static final SelectableType ANY = new SelectableType("misc.any", new ItemStack(Blocks.GRASS_BLOCK));
	public static final SelectableType NONE = new SelectableType("misc.none", new ItemStack(Blocks.BARRIER));
	
	public static final SelectableType ENTITY_CAUSE = new SelectableType("entity.cause", new ItemStack(Items.FOX_SPAWN_EGG));
	public static final SelectableType ENTITY_TARGET = new SelectableType("entity.target", new ItemStack(Items.CREEPER_SPAWN_EGG));
	public static final SelectableType ENTITY_NEAREST = new SelectableType("entity.nearest", new ItemStack(Items.BLAZE_SPAWN_EGG), (c, ctx) -> Lists.newArrayList(new EntityTypePart(c, ctx), new RadiusPart(c, ctx), new NumberOfEntitiesPart(c, ctx), new PositionSubCategory(c, ctx)));
	public static final SelectableType ENTITY_SPECIFIC = new SelectableType("entity.specific", new ItemStack(Items.AXOLOTL_SPAWN_EGG), (c, ctx) -> Lists.newArrayList(new UUIDPart(c, ctx), new PlayerNamePart(c, ctx)));
	
	public static final SelectableType POSITION_CAUSE = new SelectableType("position.cause", new ItemStack(Blocks.GRASS_BLOCK));
	public static final SelectableType POSITION_TARGET = new SelectableType("position.target", new ItemStack(Blocks.DIRT));
	public static final SelectableType POSITION_FIXED = new SelectableType("position.fixed", new ItemStack(Blocks.IRON_BLOCK), (c, ctx) -> Lists.newArrayList(new PositionPart(c, ctx), new DimensionPart(c, ctx)));
	
	public static final SelectableType TRIGGER_BREAK_BLOCK = new BreakBlockTrigger();
	public static final SelectableType TRIGGER_KILL_ENTITY = new KillEntityTrigger();
	
	public static final SelectableType CONDITION_RANDOM = new RandomCondition();
	
	public static final SelectableType ACTION_CONSOLE_MESSAGE = new ConsoleMessageAction();
	public static final SelectableType ACTION_PLAYER_COMMAND = new PlayerCommandAction();
	public static final SelectableType ACTION_SERVER_COMMAND = new ServerCommandAction();
	public static final SelectableType ACTION_HEAL_OR_DAMAGE = new HealOrDamageEntityAction();

	@Nullable
	public static Selectable getTypeByID(String id) {
		for (Selectable type : ALL_SELECTABLES)
			if (type.getId().equals(id))
				return type;
		return null;
	}
	
	@Nullable
	public static Selectable getTypeByName(String name) {
		for (Selectable type : ALL_SELECTABLES)
			if (type.getName().getString().equals(name))
				return type;
		return null;
	}
	
}