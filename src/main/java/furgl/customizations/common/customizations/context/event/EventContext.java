package furgl.customizations.common.customizations.context.event;

import furgl.customizations.common.customizations.context.Context;

/**Contexts that are exclusively used for events, never in the config*/
public abstract class EventContext extends Context {

	public EventContext(Type type) {
		super(type);
	}
	
	public EventContext() {
		super();
	}
	
	@Override
	public boolean test(Context... eventContexts) {
		return true;
	}
	
}