package furgl.customizations.client.selectors;

import java.util.Set;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import furgl.customizations.client.parts.CommandPart;
import furgl.customizations.client.parts.ConsoleMessagePart;
import furgl.customizations.client.parts.DimensionPart;
import furgl.customizations.client.parts.EntityTypePart;
import furgl.customizations.client.parts.HealOrDamagePart;
import furgl.customizations.client.parts.NumberOfEntitiesPart;
import furgl.customizations.client.parts.PlayerNamePart;
import furgl.customizations.client.parts.PositionPart;
import furgl.customizations.client.parts.RadiusPart;
import furgl.customizations.client.parts.RandomPart;
import furgl.customizations.client.parts.UUIDPart;
import furgl.customizations.client.subCategories.BlockSubCategory;
import furgl.customizations.client.subCategories.EntitySubCategory;
import furgl.customizations.client.subCategories.PositionSubCategory;
import furgl.customizations.common.customizations.actions.ConsoleMessageAction;
import furgl.customizations.common.customizations.actions.HealOrDamageEntityAction;
import furgl.customizations.common.customizations.actions.PlayerCommandAction;
import furgl.customizations.common.customizations.actions.SelectableAction;
import furgl.customizations.common.customizations.actions.ServerCommandAction;
import furgl.customizations.common.customizations.conditions.SelectableCondition;
import furgl.customizations.common.customizations.triggers.SelectableTrigger;
import furgl.customizations.common.customizations.triggers.TriggerBreakBlock;
import furgl.customizations.common.customizations.triggers.TriggerPlayerLogin;
import furgl.customizations.common.customizations.triggers.TriggerPlayerLogout;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;

public class Selectables {

	public static final Set<Selectable> ALL_SELECTABLES = Sets.newLinkedHashSet();
	
	public static final Selectable BLANK = new Selectable("misc.blank", ItemStack.EMPTY);
	public static final Selectable ANY = new Selectable("misc.any", new ItemStack(Blocks.GRASS_BLOCK));
	public static final Selectable NONE = new Selectable("misc.none", new ItemStack(Blocks.BARRIER));
	
	public static final Selectable ENTITY_CAUSE = new Selectable("entity.cause", new ItemStack(Items.FOX_SPAWN_EGG));
	public static final Selectable ENTITY_TARGET = new Selectable("entity.target", new ItemStack(Items.CREEPER_SPAWN_EGG));
	public static final Selectable ENTITY_NEAREST = new Selectable("entity.nearest", new ItemStack(Items.BLAZE_SPAWN_EGG), (c, ctx) -> Lists.newArrayList(new EntityTypePart(c, ctx), new RadiusPart(c, ctx), new NumberOfEntitiesPart(c, ctx), new PositionSubCategory(c, ctx)));
	public static final Selectable ENTITY_SPECIFIC = new Selectable("entity.specific", new ItemStack(Items.AXOLOTL_SPAWN_EGG), (c, ctx) -> Lists.newArrayList(new UUIDPart(c, ctx), new PlayerNamePart(c, ctx)));
	public static final Selectable ENTITY_ALL_PLAYERS = new Selectable("entity.allPlayers", new ItemStack(Items.BEE_SPAWN_EGG));
	
	public static final Selectable POSITION_CAUSE = new Selectable("position.cause", new ItemStack(Blocks.GRASS_BLOCK));
	public static final Selectable POSITION_TARGET = new Selectable("position.target", new ItemStack(Blocks.DIRT));
	public static final Selectable POSITION_FIXED = new Selectable("position.fixed", new ItemStack(Blocks.IRON_BLOCK), (c, ctx) -> Lists.newArrayList(new PositionPart(c, ctx), new DimensionPart(c, ctx)));
	
	public static final Set<SelectableTrigger> ALL_TRIGGERS = Sets.newLinkedHashSet();
	public static final TriggerBreakBlock TRIGGER_BREAK_BLOCK = new TriggerBreakBlock("triggers.breakBlock", new ItemStack(Items.DIAMOND_PICKAXE), (c, ctx) -> new BlockSubCategory(c, ctx).getChildren());
	public static final TriggerPlayerLogin TRIGGER_PLAYER_LOGIN = new TriggerPlayerLogin("triggers.playerLogin", new ItemStack(Items.FILLED_MAP), (c, ctx) -> Lists.newArrayList(new PlayerNamePart(c, ctx, Selectables.ENTITY_CAUSE), new UUIDPart(c, ctx, Selectables.ENTITY_CAUSE)));
	public static final TriggerPlayerLogout TRIGGER_PLAYER_LOGOUT = new TriggerPlayerLogout("triggers.playerLogout", new ItemStack(Items.MAP), (c, ctx) -> Lists.newArrayList(new PlayerNamePart(c, ctx, Selectables.ENTITY_CAUSE), new UUIDPart(c, ctx, Selectables.ENTITY_CAUSE)));
	//public static final TriggerKillEntity TRIGGER_KILL_ENTITY = new TriggerKillEntity("triggers.killEntity", new ItemStack(Items.DIAMOND_SWORD), (c, ctx) -> Lists.newArrayList(new EntityTypePart(c, ctx)));
	
	public static final Set<SelectableCondition> ALL_CONDITIONS = Sets.newLinkedHashSet();
	public static final SelectableCondition CONDITION_RANDOM = new SelectableCondition("conditions.random", new ItemStack(Blocks.DISPENSER), (c, ctx) -> Lists.newArrayList(new RandomPart(c, ctx)));
	
	public static final Set<SelectableAction> ALL_ACTIONS = Sets.newLinkedHashSet();
	public static final SelectableAction ACTION_CONSOLE_MESSAGE = new ConsoleMessageAction("actions.consoleMessage", new ItemStack(Items.CHAIN_COMMAND_BLOCK), (c, ctx) -> Lists.newArrayList(new ConsoleMessagePart(c, ctx)));
	public static final SelectableAction ACTION_PLAYER_COMMAND = new PlayerCommandAction("actions.playerCommand", new ItemStack(Blocks.REPEATING_COMMAND_BLOCK), (c, ctx) -> Lists.newArrayList(new CommandPart(c, ctx), new EntitySubCategory(c, ctx)));
	public static final SelectableAction ACTION_SERVER_COMMAND = new ServerCommandAction("actions.serverCommand", new ItemStack(Items.COMMAND_BLOCK), (c, ctx) -> Lists.newArrayList(new CommandPart(c, ctx)));
	public static final SelectableAction ACTION_HEAL_OR_DAMAGE = new HealOrDamageEntityAction("actions.healOrDamage", PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.STRONG_HEALING), (c, ctx) -> Lists.newArrayList(new HealOrDamagePart(c, ctx), new EntitySubCategory(c, ctx)));

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