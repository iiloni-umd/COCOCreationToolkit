import java.util.ArrayList;
import java.util.HashMap;
import java.io.*;
import java.nio.file.*;

public class COCOTools {
	static ArrayList<Image> images = new ArrayList<>();
	static HashMap<String, Integer> categories = new HashMap<>();
	static ArrayList<Path> paths = new ArrayList<>();
	
	public static void main(String[] args) {

		try {
			loadData();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		buildAllCOCO();
		
		try {
			BBoxBuilder builder = new BBoxBuilder();
			builder.createBBoxBuilder(paths.get(2));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	public static void buildAllCOCO() {
		int count = 0;
		
		//Build image COCO
		String imageCOCO = "\"images\": [\n";
		for (Image image: images) {
			imageCOCO += image.buildCOCO();
			if (count != images.size()-1) {
				imageCOCO += ",\n";
			} else {
				imageCOCO += "\n";
			}
		}
		imageCOCO += "],\n";
		
		//Build annotation COCO
		count = 0;
		String annotationCOCO = "\"annotations\": [\n";
		for (Image image: images) {
			Annotation annotation = image.getAnnotation();
			annotationCOCO += annotation.buildCOCO();
			if (count != images.size()-1) {
				annotationCOCO += ",\n";
			} else {
				annotationCOCO += "\n";
			}
		}
		annotationCOCO += "],\n";
		
		//combine images and annotations
		String COCO = "{\n";
		COCO += imageCOCO;
		COCO += annotationCOCO;
		COCO += "}";
		
		try {
			Files.writeString(Path.of("fire_annotations.txt"), COCO);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void loadData() throws IOException{
		int currCatID = 0;
		
		//Iterate through files and directories in fire_data. Add the path to an ArrayList for further processing
		Files.walk(Paths.get("fire_data")).forEach(path -> {
			paths.add(path);
		});
		
		//process each path to create categories with ids, and create images.
		for (Path path: paths) {
			File curr = path.toFile();
			
			if (curr.isDirectory())
				categories.put(path.getFileName().toString(), currCatID++);
			
			if (curr.isFile()) {
				int catId = categories.get(path.getParent().getFileName().toString());
				images.add(new Image(path.getFileName().toString(), catId));
			}
		}
	}
	
}
