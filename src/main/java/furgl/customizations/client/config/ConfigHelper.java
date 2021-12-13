package furgl.customizations.client.config;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Lists;

import furgl.customizations.common.Customizations;
import furgl.customizations.common.config.FileConfig;
import furgl.customizations.common.customizations.Customization;
import furgl.customizations.common.customizations.selectables.Selectable;
import furgl.customizations.common.impl.IAbstractConfigScreen;
import me.shedaniel.clothconfig2.api.AbstractConfigEntry;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.Tooltip;
import me.shedaniel.clothconfig2.gui.entries.AbstractListListEntry;
import me.shedaniel.clothconfig2.gui.entries.DropdownBoxEntry;
import me.shedaniel.clothconfig2.gui.entries.StringListEntry;
import me.shedaniel.clothconfig2.gui.entries.SubCategoryListEntry;
import me.shedaniel.clothconfig2.gui.entries.TextListEntry;
import me.shedaniel.clothconfig2.impl.builders.DropdownMenuBuilder;
import me.shedaniel.clothconfig2.impl.builders.StringFieldBuilder;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import me.shedaniel.math.Point;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

public class ConfigHelper {

	public static final Formatting LIST_FORMATTING = Formatting.BLUE;
	public static final Formatting SUB_CATEGORY_FORMATTING = Formatting.GREEN;
	public static final Formatting TIP_FORMATTING = Formatting.GOLD;

	private static final Text RESET_BUTTON = new TranslatableText("text.cloth-config.reset_value");
	public static final int FIELD_WIDTH = 200;

	public static boolean isCustomizationConfigOpen() {
		return MinecraftClient.getInstance().currentScreen != null && MinecraftClient.getInstance().currentScreen == Config.currentScreen;
	}

	/**Custom method for changing text color properly*/
	public static Text getDisplayedFieldName(AbstractConfigEntry entry) {
		MutableText text = entry.getFieldName().shallowCopy();
		boolean hasError = entry.getConfigError().isPresent(); 
		boolean isEdited = entry.isEdited();
		if (hasError)
			text = text.formatted(Formatting.RED); 
		if (isEdited)
			text = text.formatted(Formatting.ITALIC); 
		if (!hasError) {
			if (entry instanceof AbstractListListEntry)
				text = text.formatted(LIST_FORMATTING).formatted(Formatting.UNDERLINE);
			else if (entry instanceof SubCategoryListEntry)
				text = text.formatted(SUB_CATEGORY_FORMATTING);
			else if (!isEdited)
				text = text.formatted(Formatting.GRAY); 
		}
		return (Text)text;
	}

	@Nullable
	public static TextListEntry addTip(ConfigBuilder builder, String tip) {
		return addTip(builder, tip, TIP_FORMATTING);
	}

	@SuppressWarnings("deprecation")
	@Nullable
	public static TextListEntry addTip(ConfigBuilder builder, String tip, Formatting formatting) {
		if (FileConfig.showTips) {
			final Text text = new TranslatableText("config."+Customizations.MODID+".tips."+tip).formatted(Formatting.ITALIC, formatting);
			return new TextListEntry(RESET_BUTTON, text, -1, null) {
				@Override  
				public int getItemHeight() {
					return super.getItemHeight() - 7;
				}
			};
		}
		else
			return null;
	}

	public static StringFieldBuilder createStrField(ConfigBuilder builder, Text name, String value, Consumer<String> saveConsumer, @Nullable MutableText tooltip) {
		return createStrField(builder, name, value, saveConsumer, tooltip, false, null);
	}

	@SuppressWarnings("deprecation")
	public static StringFieldBuilder createStrField(ConfigBuilder builder, Text name, String value, Consumer<String> saveConsumer, @Nullable MutableText tooltip, boolean supportsPlaceholders, Customization customization) {
		if (supportsPlaceholders && customization != null)
			tooltip.append("\n").append(new TranslatableText("config."+Customizations.MODID+".placeholders.holdShift", Formatting.DARK_GRAY, Formatting.ITALIC));
		final Supplier<Text[]> tooltipGetter = 
				(supportsPlaceholders && customization != null) ? 
						() -> Screen.hasShiftDown() ? getTooltip(customization.getPlaceholderText()) : getTooltip(tooltip) 
								: () -> getTooltip(tooltip);

								return new StringFieldBuilder(RESET_BUTTON, name, value) {
									@NotNull
									public StringListEntry build() {
										StringListEntry entry = new StringListEntry(getFieldNameKey(), value, getResetButtonKey(), this.defaultValue, saveConsumer, null, isRequireRestart()) {
											@Override
											public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isHovered, float delta) {
												super.render(matrices, index, y, x, entryWidth, entryHeight, mouseX, mouseY, isHovered, delta);
												// draw tooltip
												if (isMouseInside(mouseX, mouseY, x, y, entryWidth, entryHeight) && MinecraftClient.getInstance().currentScreen instanceof IAbstractConfigScreen) {
													// show value if text field hovered
													if (this.textFieldWidget.isMouseOver(mouseX, mouseY) && !this.getValue().isEmpty())
														((IAbstractConfigScreen)MinecraftClient.getInstance().currentScreen).setTooltip(Tooltip.of(new Point(mouseX, mouseY), Text.of(this.getValue())));
													// show tooltip if not hovering over text field
													else if (tooltip != null) 
														((IAbstractConfigScreen)MinecraftClient.getInstance().currentScreen).setTooltip(Tooltip.of(new Point(mouseX, mouseY), tooltipGetter.get()));
												}
											}
										};
										if (this.errorSupplier != null)
											entry.setErrorSupplier(() -> (Optional)this.errorSupplier.apply(entry.getValue())); 
										return entry;
									}
								};
	}

	public static SubCategoryBuilder createSubCategory(ConfigBuilder builder, Text name, List<AbstractConfigListEntry> children, boolean expanded) {
		return createSubCategory(builder, name, children, expanded, list -> Optional.empty());
	}

	// not sure what I needed this for...
	public static SubCategoryBuilder createSubCategory(ConfigBuilder builder, Text name, List<AbstractConfigListEntry> children, boolean expanded, Function<List<AbstractConfigListEntry>, Optional<Text[]>> tooltipSupplier) {
		return new SubCategoryBuilder(RESET_BUTTON, name) {
			@SuppressWarnings("deprecation")
			@NotNull
			public SubCategoryListEntry build() {
				SubCategoryListEntry entry = new SubCategoryListEntry(getFieldNameKey(), children, expanded);
				entry.setTooltipSupplier(() -> (Optional)tooltipSupplier.apply(entry.getValue()));
				return entry;
			}
		};
	}

	public static <T> DropdownMenuBuilder<T> createDropdownMenu(ConfigBuilder builder, Text name, Text tooltip, T selection, Function<String, T> stringToSelection, Function<T, Text> selectionToText) {
		return createDropdownMenu(builder, name, tooltip, selection, stringToSelection, selectionToText, null);
	}

	public static <T> DropdownMenuBuilder<T> createDropdownMenu(ConfigBuilder builder, Text name, Text tooltip, T selection, Function<String, T> stringToSelection, Function<T, Text> selectionToText, @Nullable Function<T, ItemStack> getStack) {
		return createDropdownMenu(builder, name, tooltip, selection, stringToSelection, selectionToText, getStack, false);
	}

	public static <T> DropdownMenuBuilder<T> createDropdownMenu(ConfigBuilder builder, Text name, Text tooltip, T selection, Function<String, T> stringToSelection, Function<T, Text> selectionToText, @Nullable Function<T, ItemStack> getStack, boolean noErrors) {
		return builder.entryBuilder()
				.startDropdownMenu(name, new DropdownBoxEntry.DefaultSelectionTopCellElement<T>(selection, stringToSelection, selectionToText) {
					private boolean unfocus;
					@Override
					public void render(MatrixStack matrices, int mouseX, int mouseY, int x, int y, int width, int height, float delta) {
						if (unfocus) {
							this.getParent().setFocused(null);
							unfocus = false;
						}
						this.textFieldWidget.x = x + 4;
						this.textFieldWidget.y = y + 6;
						this.textFieldWidget.setWidth(width - 4 - 16);
						this.textFieldWidget.setEditable(getParent().isEditable());
						this.textFieldWidget.setEditableColor(getPreferredTextColor());
						this.textFieldWidget.render(matrices, mouseX, mouseY, delta);
						// render item
						ItemStack stack = getStack(this.getValue(), getStack);
						if (stack != null) {
							ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
							stack = hasConfigError() ? new ItemStack(Items.BARRIER) : stack;
							itemRenderer.renderGuiItemIcon(stack, x + width - 18, y + 2);
						}
						// draw tooltip
						boolean b = (mouseX >= x && mouseX <= x + width && mouseY > y && mouseY <= y + height);
						if (MinecraftClient.getInstance().currentScreen instanceof IAbstractConfigScreen) {
							if (b && getTooltip(this.getValue()) != null && !(getTooltip(this.getValue()).length == 1 && getTooltip(this.getValue())[0].getString().isEmpty()) && !hasConfigError()) 
								((IAbstractConfigScreen)MinecraftClient.getInstance().currentScreen).setTooltip(Tooltip.of(new Point(mouseX, mouseY), getTooltip(this.getValue())));
							else if (!b && mouseY > y && mouseY <= y + height && !tooltip.getString().isEmpty())
								((IAbstractConfigScreen)MinecraftClient.getInstance().currentScreen).setTooltip(Tooltip.of(new Point(mouseX, mouseY), tooltip));
						}
					}
					@Override
					public Optional<Text> getError() {
						// if no errors, never show errors
						if (noErrors)
							return Optional.empty();
						else
							return super.getError();
					}
					@Override
					public boolean mouseClicked(double mouseX, double mouseY, int button) {
						boolean ret = super.mouseClicked(mouseX, mouseY, button);
						// clear text with right-click
						if (mouseX >= this.textFieldWidget.x && mouseX <= (this.textFieldWidget.x + this.textFieldWidget.getWidth()) && mouseY >= this.textFieldWidget.y-5 && mouseY <= (this.textFieldWidget.y + this.textFieldWidget.getHeight())) { 
							if (button == 1) { // right-click
								this.textFieldWidget.setText(""); // clear text
								ret = true;
							}
						}
						// unfocus when clicking elsewhere (except in dropdown)
						else if (mouseX < this.textFieldWidget.x || mouseX > (this.textFieldWidget.x + this.textFieldWidget.getWidth()) || mouseY < this.textFieldWidget.y) 
							this.getParent().setFocused(null);
						return ret;
					}
					@Override
					public void setValue(T value) {
						this.textFieldWidget.setText(((Text)this.toTextFunction.apply(value)).getString());
						this.textFieldWidget.setCursor(0);
						// hide dropdown after setting value
						unfocus = true;
					}
				},
						new DropdownBoxEntry.DefaultSelectionCellCreator<T>(selectionToText) {
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
								if (getStack(value, getStack) != null) {
									ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
									itemRenderer.zOffset = 210;
									itemRenderer.renderGuiItemIcon(getStack(value, getStack), x + 4, y + 2);
								}
								// draw tooltip
								if (b && MinecraftClient.getInstance().currentScreen instanceof IAbstractConfigScreen && getTooltip(value) != null)
									((IAbstractConfigScreen)MinecraftClient.getInstance().currentScreen).setTooltip(Tooltip.of(new Point(mouseX, mouseY), getTooltip(value)));
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
				});
	}

	@Nullable
	private static <T> ItemStack getStack(Object value, @Nullable Function<T, ItemStack> getStack) {
		if (value instanceof Selectable)
			return ((Selectable)value).getStack();
		else if (value instanceof Block)
			return new ItemStack((Block)value);
		else if (value instanceof Item)
			return new ItemStack((Item)value);
		else if (value instanceof EntityType)
			return new ItemStack(SpawnEggItem.forEntity((EntityType<?>) value));
		else if (getStack != null)
			return getStack.apply((T) value);
		return null;
	}

	@Nullable
	private static Text[] getTooltip(Object value) {
		String str = null;
		if (value instanceof String)
			str = (String) value;
		else if (value instanceof Text)
			str = ((Text) value).getString();
		else if (value instanceof Selectable) 
			str = ((Selectable)value).getTooltip().getString();

		if (str != null) {
			List<Text> list = Lists.newArrayList();
			for (String line : str.split("\n"))
				list.add(Text.of(line));
			return list.toArray(new Text[0]);
		}
		return null;
	}

}