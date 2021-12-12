package furgl.customizations.client.parts;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Lists;

import furgl.customizations.client.config.ConfigHelper;
import furgl.customizations.common.customizations.Customization;
import furgl.customizations.common.customizations.context.Context;
import furgl.customizations.common.customizations.context.Contexts;
import furgl.customizations.common.customizations.context.holders.ConfigContextHolder;
import furgl.customizations.common.customizations.selectables.Selectable;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;

public class PlayerNamePart extends Part {

	@Nullable
	private Selectable target;

	public PlayerNamePart(Customization customization, ConfigContextHolder contextHolder) {
		this(customization, contextHolder, null);
	}
	
	public PlayerNamePart(Customization customization, ConfigContextHolder contextHolder, @Nullable Selectable target) {
		super("playerName", customization, contextHolder);
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
				this.contextHolder.getOrAddContext(Contexts.SELECTED_ENTITY).playerName,
				value -> this.contextHolder.getOrAddContext(Contexts.SELECTED_ENTITY).playerName = value,
				this.getTooltip())
				.build();
		return Lists.newArrayList(this.mainConfigEntry);
	}

}