package furgl.customizations.common.customizations.context;

public class ConsoleMessageContext extends Context {

	public String message = "";
	
	public ConsoleMessageContext(String message) {
		this();
		this.message = message;
	}
	
	public ConsoleMessageContext() {
		super();
		this.variables.add(new Context.Variable("Message", 
				() -> this.message, 
				message -> this.message = (String) message,
						message -> message, 
						message -> message));
	}
	
	@Override
	public boolean test(Context... eventContexts) {
		return true;
	}	
	
}