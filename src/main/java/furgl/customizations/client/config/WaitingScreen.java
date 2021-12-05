package furgl.customizations.client.config;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class WaitingScreen extends Screen {

	public final Screen parent;
	private Text text;

	public WaitingScreen(Screen parent, Text text) {
		super(text);
		this.parent = parent;
		this.text = text;
	}
	
	public void setText(Text text) {
		this.text = text;
	}

	@Override
	public void onClose() {
		this.client.openScreen(this.parent);
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		super.render(matrices, mouseX, mouseY, delta);
		this.renderBackgroundTexture(0);
		this.textRenderer.drawWithShadow(matrices, this.text, this.width/2-(this.textRenderer.getWidth(this.text.getString()))/2, this.height/2-this.textRenderer.fontHeight/2, -1);
	}

}