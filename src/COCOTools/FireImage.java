package COCOTools;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;

public class FireImage {
	private Annotation annotation;
	private String fileName;
	private Path path;
	private int id, catId;
	private final int side = 600;
	private static int currId = 0;

	public FireImage(String fileName, Path path, int catId) {
		this.fileName = fileName;
		this.path = path;
		this.id = currId++;
		this.catId = catId;
		this.annotation = new Annotation(catId, this.id);
	}

	public String buildCOCO() {
		String COCO = "{\n";
		COCO += "\"file_name\": " + "\"" + fileName + "\",\n";
		COCO += "\"id\": " + id + "\n";
		COCO += "}";

		return COCO;
	}
	
	public void buildAnnotation(Path path) {
		try {
			annotation.readCoords(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Annotation getAnnotation() {
		return this.annotation;
	}
	
	public String getFileName() {
		return this.fileName;
	}
	
	public Path getPath() {
		return this.path;
	}
}
