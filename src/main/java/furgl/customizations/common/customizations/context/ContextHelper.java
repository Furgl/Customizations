package furgl.customizations.common.customizations.context;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import eu.pb4.placeholders.PlaceholderAPI;
import furgl.customizations.common.Customizations;
import furgl.customizations.common.customizations.context.holders.EventContextHolder;
import furgl.customizations.common.customizations.context.holders.Subject;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

public class ContextHelper {

	public static final String COLOR = "\u00a7";
	public static final String PLACEHOLDER_CHAR = "%";

	/**Parse placeholders in this string with this context*/
	public static String parse(String str, Context... eventContexts) {
		return parse(str, null, eventContexts);
	}

	/**Parse placeholders in this string with this context*/
	public static String parse(String str, Subject subject, Context[] eventContexts) {
		if (Customizations.server != null) {
			// replace our cause, target, and other placeholders
			for (Context ctx : eventContexts)
				for (Entry<String, Function<Context[], String>> entry : ctx.getPlaceholders().entrySet())
					str = str.replaceAll(PLACEHOLDER_CHAR+entry.getKey()+PLACEHOLDER_CHAR, entry.getValue().apply(eventContexts));
			// replace our subject placeholders
			for (Context ctx : subject.getContext())
				for (Entry<String, Function<Context[], String>> entry : ctx.getPlaceholders().entrySet())
					str = str.replaceAll(PLACEHOLDER_CHAR+entry.getKey()+PLACEHOLDER_CHAR, entry.getValue().apply(eventContexts));
			// replace papi placeholders
			ServerPlayerEntity player = subject.player != null ? (ServerPlayerEntity)subject.player : Contexts.get(Contexts.CAUSE_PLAYER, eventContexts).map(ctx -> ctx.getPlayer()).orElse(Contexts.get(Contexts.TARGET_PLAYER, eventContexts).map(ctx -> ctx.getPlayer()).orElse(null));
			if (player != null)
				str = PlaceholderAPI.parseText(Text.of(str), player).getString();
			else
				str = PlaceholderAPI.parseText(Text.of(str), Customizations.server).getString();
			// replace color codes
			str = replaceColorCodes(str);			
		}
		return str;
	}

	/**Replace & with §*/
	public static String replaceColorCodes(String str) {
		if (str == null)
			str = "";
		str = str.replaceAll("\\\\u0026", "&") // replace unicode with &
				.replaceAll("&(\\S)", COLOR+"$1"); // replace & with §
		return str;
	}

	public static RegistryKey<World> getWorldKey(Identifier identifier) {
		if (Customizations.server != null && identifier != null)
			for (World world : Customizations.server.getWorlds())
				if (world.getRegistryKey().getValue().equals(identifier))
					return world.getRegistryKey();
		return World.OVERWORLD;
	}

	@Nullable
	public static World getWorld(RegistryKey<World> worldKey) {
		if (Customizations.server != null)
			return Customizations.server.getWorld(worldKey);
		return null;
	}

	public static String getPlaceholderText(List<EventContextHolder> holders) {
		// map holders by type
		Map<String, Set<String>> map = Maps.newLinkedHashMap();
		for (EventContextHolder holder : holders) {
			String name = holder.getName(true);
			Set<String> set = map.containsKey(name) ? map.get(name) : Sets.newLinkedHashSet();
			for (String placeholder : (List<String>)holder.placeholders)
				set.add("\n - " + holder.type.formatting + 
						ContextHelper.PLACEHOLDER_CHAR + placeholder + ContextHelper.PLACEHOLDER_CHAR);
			map.put(name, set);
		}

		String tooltip = "";
		boolean hasPlaceholders = false;
		String placeholderStr = "";
		for (String name : map.keySet()) {
			Set<String> placeholders = map.get(name);
			// placeholders
			if (!placeholders.isEmpty()) {
				placeholderStr += (hasPlaceholders ? "\n\n" : "\n")+name;
				for (String placeholder : placeholders)
					placeholderStr += placeholder;
				hasPlaceholders = true;
			}
		}
		if (hasPlaceholders) {
			tooltip += "\n\n" + Formatting.GOLD + Formatting.UNDERLINE + 
					new TranslatableText("config." + Customizations.MODID + ".placeholders.tooltip").getString();
			tooltip += placeholderStr;
		}
		return tooltip;
	}

}