package furgl.customizations.common.customizations.context;

public class CommandContext extends Context {

	public String command = "";

	public CommandContext(String command) {
		this();
		this.command = command;
	}

	public CommandContext() {
		super();
		this.variables.add(new Context.Variable("Command", 
				() -> this.command, 
				command -> this.command = (String) command,
				command -> command, 
				command -> command));
	}

	@Override
	public boolean test(Context[] configContexts, Context... eventContexts) {
		return true;
	}	

}