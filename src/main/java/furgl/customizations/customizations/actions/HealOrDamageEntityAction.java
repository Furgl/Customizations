package furgl.customizations.customizations.actions;

import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;

import furgl.customizations.config.elements.ConfigElement;
import furgl.customizations.config.parts.HealOrDamagePart;
import furgl.customizations.config.selectors.SelectableType;
import furgl.customizations.config.subCategories.EntitySubCategory;
import furgl.customizations.customizations.Customization;
import furgl.customizations.customizations.context.Context;
import furgl.customizations.customizations.context.ContextHelper;
import furgl.customizations.customizations.context.ContextHolder;
import furgl.customizations.customizations.context.Contexts;
import furgl.customizations.customizations.context.HealOrDamageContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;

public class HealOrDamageEntityAction extends SelectableType {

	public HealOrDamageEntityAction() {
		super("actions.healOrDamage", PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.STRONG_HEALING));
	}

	@Override
	public void activate(Context[] actionContexts, Context[] triggerContexts) {
		Set<Entity> entities = ContextHelper.getEntity(LivingEntity.class, actionContexts, triggerContexts);
		Contexts.get(Contexts.HEAL_OR_DAMAGE_AMOUNT, actionContexts).ifPresent(context -> {
			for (Entity entity : entities) {
				if (((HealOrDamageContext)context).heal > 0)
					((LivingEntity)entity).heal(((HealOrDamageContext)context).heal);
				if (((HealOrDamageContext)context).damage > 0)
					((LivingEntity)entity).damage(DamageSource.GENERIC, ((HealOrDamageContext)context).damage);
			}
		});
	}

	@Override
	public List<ConfigElement> createRelatedElements(Customization customization, ContextHolder contextHolder) {
		return Lists.newArrayList(new HealOrDamagePart(customization, contextHolder), new EntitySubCategory(customization, contextHolder));
	}

}