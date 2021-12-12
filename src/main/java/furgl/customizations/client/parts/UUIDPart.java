package furgl.customizations.client.parts;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
import net.minecraft.text.Text;

public class UUIDPart extends Part {

	@Nullable
	private Selectable target;

	public UUIDPart(Customization c, ConfigContextHolder ctx) {
		this(c, ctx, null);
	}

	public UUIDPart(Customization customization, ConfigContextHolder contextHolder, @Nullable Selectable target) {
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
					}, this.getTooltip())
				.setErrorSupplier(str -> {
					try {
						if (!str.isEmpty())
							UUID.fromString(str);
						return Optional.empty();
					}
					catch (IllegalArgumentException e) {
						return Optional.of(Text.of("Invalid UUID"));
					}
				})
				.build();
		return Lists.newArrayList(this.mainConfigEntry);
	}

}