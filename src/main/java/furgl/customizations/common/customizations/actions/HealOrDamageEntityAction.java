package furgl.customizations.common.customizations.actions;

import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;

import com.google.common.collect.Sets;

import furgl.customizations.client.elements.ConfigElement;
import furgl.customizations.common.customizations.Customization;
import furgl.customizations.common.customizations.context.Context;
import furgl.customizations.common.customizations.context.Contexts;
import furgl.customizations.common.customizations.context.HealOrDamageContext;
import furgl.customizations.common.customizations.context.holders.ConfigContextHolder;
import furgl.customizations.common.customizations.selectables.SelectableAction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;

public class HealOrDamageEntityAction extends SelectableAction {

	public HealOrDamageEntityAction(String id, ItemStack stack, BiFunction<Customization, ConfigContextHolder, List<ConfigElement>> relatedElements) {
		super(Entity.class, id, stack, relatedElements);
	}

	@Override
	public void activate(Context[] configContexts, Context[] eventContexts) {
		Set<Entity> entities = Contexts.get(Contexts.SELECTED_ENTITY, eventContexts).map(ctx -> ctx.selectedEntities).orElse(Sets.newHashSet());
		Contexts.get(Contexts.HEAL_OR_DAMAGE_AMOUNT, eventContexts).ifPresent(context -> {
			for (Entity entity : entities) {
				if (entity instanceof LivingEntity) {
					if (((HealOrDamageContext)context).heal > 0)
						((LivingEntity)entity).heal(((HealOrDamageContext)context).heal);
					if (((HealOrDamageContext)context).damage > 0)
						((LivingEntity)entity).damage(DamageSource.GENERIC, ((HealOrDamageContext)context).damage);
				}
			}
		});
	}

}