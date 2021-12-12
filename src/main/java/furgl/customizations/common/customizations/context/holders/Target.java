package furgl.customizations.common.customizations.context.holders;

import furgl.customizations.common.customizations.context.Context.Type;

public class Target<T> extends EventContextHolder {

	public Target(T target) {
		super(Type.TARGET, target);
	}

}