package furgl.customizations.common.customizations.context.event;

import java.util.Map;
import java.util.function.Function;

import com.google.common.collect.Maps;

import furgl.customizations.common.customizations.context.Context;

public class DisconnectReasonContext extends EventContext {

	public String reason = "";

	public DisconnectReasonContext(String reason) {
		this();
		this.reason = reason;
	}

	public DisconnectReasonContext() {
		super();
		this.variables.add(new Context.Variable("Reason", 
				() -> this.reason, 
				reason -> this.reason = (String) reason,
				reason -> reason, 
				reason -> reason));
	}
	
	@Override
	protected Map<String, Function<Context[], String>> createPlaceholders() {
		Map<String, Function<Context[], String>> map = Maps.newLinkedHashMap();
		map.put("disconnect_reason", eventContexts -> this.reason);
		return map;
	}

}