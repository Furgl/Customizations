package furgl.customizations.common.customizations.context.holders;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Lists;

import furgl.customizations.common.customizations.context.Context;
import furgl.customizations.common.customizations.context.Context.Type;

/**This has context that can be set/read/tested*/
public class ContextHolder {

	private ArrayList<Context> contexts = Lists.newArrayList();
	
	public void addContext(Context... context) {
		this.addContext(Lists.newArrayList(context));
	}
	
	public void addContext(List<Context> context) {
		this.contexts.addAll(context);
	}
	
	public boolean hasContext(Context contextIn) {
		return hasContext(contextIn, null);
	}

	public boolean hasContext(Context contextIn, @Nullable Type type) {
		return getContext(contextIn, type) != null;
	}

	public <T extends Context> T getOrAddContext(T contextIn) {
		return getOrAddContext(contextIn, null);
	}
	
	public <T extends Context> T getOrAddContext(T contextIn, @Nullable Type type) {
		T context = getContext(contextIn, type);
		if (context != null)
			return context;
		else {
			context = (T) contextIn.newInstance();
			context.type = type;
			this.addContext(context);
			return context;
		}
	}

	public ArrayList<Context> getContext() {
		return this.contexts;
	}

	@Nullable
	public <T extends Context> T getContext(T contextIn, Type type) {
		for (Context context : contexts)
			if (context.equals(contextIn) && (type == null || context.type == type))
				return (T) context;
		return null;
	}
	
}