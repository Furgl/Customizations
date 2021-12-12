package furgl.customizations.common.customizations.context.holders;

import furgl.customizations.common.customizations.context.Context.Type;

public class Other<O> extends EventContextHolder {

	public Other(O... other) {
		super(Type.OTHER, other);
	}

}