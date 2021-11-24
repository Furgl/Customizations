package furgl.customizations.config;

import java.util.function.Consumer;
import java.util.function.Function;

import org.jetbrains.annotations.Nullable;

import furgl.customizations.Customizations;
import furgl.customizations.config.selectors.Selectable;
import furgl.customizations.mixin.DropdownBoxEntryAccessor;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.Tooltip;
import me.shedaniel.clothconfig2.gui.ClothConfigScreen;
import me.shedaniel.clothconfig2.gui.entries.DropdownBoxEntry;
import me.shedaniel.clothconfig2.gui.entries.TextListEntry;
import me.shedaniel.clothconfig2.impl.builders.DropdownMenuBuilder;
import me.shedaniel.math.Point;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

public class ConfigHelper {

	@Nullable
	public static TextListEntry addTip(ConfigBuilder builder, String tip) {
		return addTip(builder, tip, Formatting.GOLD);
	}

	@SuppressWarnings("deprecation")
	@Nullable
	public static TextListEntry addTip(ConfigBuilder builder, String tip, Formatting formatting) {
		if (FileConfig.showTips) {
			final Text text = new TranslatableText("config."+Customizations.MODID+".tips."+tip).formatted(Formatting.ITALIC, formatting);
			return new TextListEntry(new TranslatableText("text.cloth-config.reset_value"), text, -1, null) {
				@Override  
				public int getItemHeight() {
					return super.getItemHeight() - 7;
				}
			};
		}
		else
			return null;
	}

	public static <T> DropdownMenuBuilder<T> createFixedDropdownMenu(ConfigBuilder builder, Text name, Text tooltip, T selection, Iterable<T> selections, Function<String, T> stringToSelection, Function<T, Text> selectionToString, Consumer<T> saveConsumer) {
		return builder.entryBuilder()
				.startDropdownMenu(name, new DropdownBoxEntry.DefaultSelectionTopCellElement<T>(selection, stringToSelection, selectionToString) {
					@Override
					public void render(MatrixStack matrices, int mouseX, int mouseY, int x, int y, int width, int height, float delta) {
						this.textFieldWidget.x = x + 4;
						this.textFieldWidget.y = y + 6;
						this.textFieldWidget.setWidth(width - 4 - 16);
						this.textFieldWidget.setEditable(getParent().isEditable());
						this.textFieldWidget.setEditableColor(getPreferredTextColor());
						this.textFieldWidget.render(matrices, mouseX, mouseY, delta);
						// render item
						if (getStack(this.getValue()) != null) {
							ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
							ItemStack stack = hasConfigError() ? new ItemStack(Items.BARRIER) : getStack(this.getValue());
							itemRenderer.renderGuiItemIcon(stack, x + width - 18, y + 2);
						}
						// draw tooltip
						boolean b = (mouseX >= x && mouseX <= x + width && mouseY > y && mouseY <= y + height);
						if (MinecraftClient.getInstance().currentScreen instanceof ClothConfigScreen) {
							if (b && getTooltip(this.getValue()) != null && !getTooltip(this.getValue()).getString().isEmpty() && !hasConfigError()) 
								((ClothConfigScreen)MinecraftClient.getInstance().currentScreen).addTooltip(Tooltip.of(new Point(mouseX, mouseY), getTooltip(this.getValue())));
							else if (!b && mouseY > y && mouseY <= y + height)
								((ClothConfigScreen)MinecraftClient.getInstance().currentScreen).addTooltip(Tooltip.of(new Point(mouseX, mouseY), tooltip));
						}
					}
					@Override
					public boolean mouseClicked(double mouseX, double mouseY, int button) {
						boolean ret = super.mouseClicked(mouseX, mouseY, button);
						// clear text with right-click
						if (mouseX >= this.textFieldWidget.x && mouseX <= (this.textFieldWidget.x + this.textFieldWidget.getWidth()) && mouseY >= this.textFieldWidget.y-5 && mouseY <= (this.textFieldWidget.y + this.textFieldWidget.getHeight())) { 
							if (button == 1) { // right-click
								this.textFieldWidget.setText(""); // clear text
								this.isSelected = true;
								this.getParent().setFocused(((DropdownBoxEntryAccessor)this.getParent()).getSelectionElement()); // expand dropdown
							}
						}
						// unfocus when clicking elsewhere (except in dropdown)
						else if (mouseX < this.textFieldWidget.x || mouseX > (this.textFieldWidget.x + this.textFieldWidget.getWidth()) || mouseY < this.textFieldWidget.y) 
							this.getParent().setFocused(null);
						return ret;
					}
				},
						new DropdownBoxEntry.DefaultSelectionCellCreator<T>(selectionToString) {
					@Override
					public DropdownBoxEntry.SelectionCellElement<T> create(T selection) {
						final T value = selection;
						return new DropdownBoxEntry.DefaultSelectionCellElement<T>(selection, this.toTextFunction) {
							@Override
							public void render(MatrixStack matrices, int mouseX, int mouseY, int x, int y, int width, int height, float delta) {
								this.rendering = true;
								this.x = x;
								this.y = y;
								this.width = width;
								this.height = height;
								boolean b = (mouseX >= x && mouseX <= x + width && mouseY > y && mouseY <= y + height);
								// highlight
								if (b)
									fill(matrices, x + 1, y + 1, x + width - 1, y + height - 1, -15132391); 
								// draw text
								(MinecraftClient.getInstance()).textRenderer.drawWithShadow(matrices, ((Text)this.toTextFunction.apply(this.r)).asOrderedText(), (x + 4 + 19), (y + 6), b ? 16777215 : 8947848);
								// draw item
								if (getStack(value) != null) {
									ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
									itemRenderer.zOffset = 250;
									itemRenderer.renderGuiItemIcon(getStack(value), x + 4, y + 2);
								}
								// draw tooltip
								if (b && MinecraftClient.getInstance().currentScreen instanceof ClothConfigScreen && getTooltip(value) != null)
									((ClothConfigScreen)MinecraftClient.getInstance().currentScreen).addTooltip(Tooltip.of(new Point(mouseX, mouseY), getTooltip(value)));
							}
						};
					}
					@Override
					public int getCellHeight() {
						return 20;
					}
					@Override
					public int getCellWidth() {
						return 162;
					}
					@Override
					public int getDropBoxMaxHeight() {
						return getCellHeight() * 7;
					}
				})
				.setSaveConsumer(saveConsumer)
				.setSelections(selections);
	}

	@Nullable
	private static ItemStack getStack(Object value) {
		if (value instanceof Selectable)
			return ((Selectable)value).getStack();
		else if (value instanceof Block)
			return new ItemStack((Block)value);
		else if (value instanceof Item)
			return new ItemStack((Item)value);
		return null;
	}

	@Nullable
	private static Text getTooltip(Object value) {
		if (value instanceof Selectable)
			return ((Selectable)value).getTooltip();
		return null;
	}

}