package furgl.customizations.config.selectors;

import java.util.List;

import furgl.customizations.config.ConfigElement;
import furgl.customizations.customizations.Customization;
import furgl.customizations.customizations.context.ContextHolder;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

/**This can be selected from the dropdown menu of a Selector*/
public interface Selectable<T> {

	/**Create other config elements when this selection is picked*/
	List<ConfigElement> createRelatedElements(Customization customization, ContextHolder contextHolder);

	/**Get tooltip to display when this is hovered over in drop down menu*/
	Text getTooltip();

	/**Get item stack to represent this selection in drop down menu*/
	ItemStack getStack();
	
}