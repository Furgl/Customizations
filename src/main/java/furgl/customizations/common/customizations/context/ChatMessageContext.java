package furgl.customizations.common.customizations.context;

public class ChatMessageContext extends Context {

	public String message = "";
	
	public ChatMessageContext(String message) {
		this();
		this.message = message;
	}
	
	public ChatMessageContext() {
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