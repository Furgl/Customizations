package furgl.customizations.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import furgl.customizations.Customizations;
import furgl.customizations.customizations.Customization;
import furgl.customizations.customizations.CustomizationManager;
import net.minecraft.util.JsonHelper;

public class FileConfig {

	private static final Gson GSON = new GsonBuilder()
			.disableHtmlEscaping()
			.setPrettyPrinting()
			.serializeNulls()
			.create();
	private static final String FILE = "./config/"+Customizations.MODID+".cfg";
	private static File file;
	
	public static boolean showTips;

	public static void init() {
		try {
			// create file if it doesn't already exist
			file = new File(FILE);
			if (!file.exists()) {
				file.createNewFile();
				writeToFile(true);
			}
			readFromFile();
			// write current values / defaults to file
			writeToFile(false);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void readFromFile() {
		try (BufferedReader reader = Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8)) {
			JsonObject parser = (JsonObject) JsonHelper.deserialize(reader);
			
			JsonElement element = parser.get("Show Tips");
			showTips = element == null ? true : element.getAsBoolean();

			CustomizationManager.clearCustomizations();
			for (Entry<String, JsonElement> entry : parser.entrySet()) {
				if (entry.getKey().startsWith("Customization ")) {
					Customization customization = Customization.readFromConfig(entry.getValue());
					if (customization != null)
						CustomizationManager.addCustomization(customization);
				}
			}

		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void writeToFile(boolean writeDefaults) {
		try (BufferedWriter writer = Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8)) {
			JsonObject obj = new JsonObject();

			obj.addProperty("Show Tips", writeDefaults ? true : showTips);
			
			ArrayList<Customization> customizations = CustomizationManager.getAllCustomizations();
			for (int i=0; i<customizations.size(); ++i) 
				obj.add("Customization "+(i+1), customizations.get(i).writeToConfig());

			writer.write(GSON.toJson(obj));
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}