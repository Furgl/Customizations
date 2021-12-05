package furgl.customizations.client.parts;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Lists;

import furgl.customizations.client.config.ConfigHelper;
import furgl.customizations.client.selectors.Selectable;
import furgl.customizations.common.customizations.Customization;
import furgl.customizations.common.customizations.context.Context;
import furgl.customizations.common.customizations.context.ContextHolder;
import furgl.customizations.common.customizations.context.Contexts;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;

public class UUIDPart extends Part {

	@Nullable
	private Selectable target;

	public UUIDPart(Customization c, ContextHolder ctx) {
		this(c, ctx, null);
	}
	
	public UUIDPart(Customization customization, ContextHolder contextHolder, @Nullable Selectable target) {
		super("uuid", customization, contextHolder);
		this.target = target;
	}

	@Override
	public ArrayList<Context> getRelatedContexts() {
		return Lists.newArrayList(Contexts.SELECTED_ENTITY);
	}

	@Override
	protected List<AbstractConfigListEntry> addToConfig(ConfigBuilder builder) {
		if (target != null)
			this.contextHolder.getOrAddContext(Contexts.SELECTED_ENTITY).type = target;
		this.mainConfigEntry = ConfigHelper.createStrField(builder, this.getName(), 
				this.contextHolder.getOrAddContext(Contexts.SELECTED_ENTITY).uuid == null ? "" : 
					this.contextHolder.getOrAddContext(Contexts.SELECTED_ENTITY).uuid.toString(),
				value -> {
					try {
						this.contextHolder.getOrAddContext(Contexts.SELECTED_ENTITY).uuid = UUID.fromString(value);
					}
					catch (Exception e) {};
				}, this.getTooltip());
		return Lists.newArrayList(this.mainConfigEntry);
	}

}