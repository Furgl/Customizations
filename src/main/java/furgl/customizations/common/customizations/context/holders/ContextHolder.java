package furgl.customizations.common.customizations.context.holders;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Lists;

import furgl.customizations.common.customizations.context.Context;

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
		for (Context context : contexts)
			if (context.equals(contextIn))
				return true;
		return false;
	}

	public <T extends Context> T getOrAddContext(T contextIn) {
		T context = getContext(contextIn);
		if (context != null)
			return context;
		else {
			context = (T) contextIn.newInstance();
			this.addContext(context);
			return context;
		}
	}

	public ArrayList<Context> getContext() {
		return this.contexts;
	}

	@Nullable
	public <T extends Context> T getContext(T contextIn) {
		for (Context context : contexts)
			if (context.equals(contextIn))
				return (T) context;
		return null;
	}
	
}