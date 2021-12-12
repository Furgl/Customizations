package furgl.customizations.client.parts;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import furgl.customizations.client.config.ConfigHelper;
import furgl.customizations.common.customizations.Customization;
import furgl.customizations.common.customizations.context.Context;
import furgl.customizations.common.customizations.context.Contexts;
import furgl.customizations.common.customizations.context.holders.ConfigContextHolder;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;

public class ChatMessagePart extends Part {

	public ChatMessagePart(Customization customization, ConfigContextHolder contextHolder) {
		super("chatMessage", customization, contextHolder);
	}
	
	@Override
	public ArrayList<Context> getRelatedContexts() {
		return Lists.newArrayList(Contexts.CHAT_MESSAGE);
	}

	@Override
	protected List<AbstractConfigListEntry> addToConfig(ConfigBuilder builder) {
		this.mainConfigEntry = ConfigHelper.createStrField(builder, this.getName(), 
				this.contextHolder.getOrAddContext(Contexts.CHAT_MESSAGE).message,
				value -> this.contextHolder.getOrAddContext(Contexts.CHAT_MESSAGE).message = value,
				this.getTooltip(),
				true, this.getCustomization())
				.build();
		return Lists.newArrayList(this.mainConfigEntry);
	}

}