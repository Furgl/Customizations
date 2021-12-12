package furgl.customizations.common.customizations.context.holders;

import furgl.customizations.common.customizations.context.Context.Type;

public class Cause<C> extends EventContextHolder {

	public Cause(C cause) {
		super(Type.CAUSE, cause);
	}

}