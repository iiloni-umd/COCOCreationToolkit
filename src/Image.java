
public class Image {
	private Annotation annotation;
	private String fileName;
	private int id, catId;
	private final int side = 600;
	private static int currId = 0;

	public Image(String fileName, int catId) {
		this.fileName = fileName;
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
	
	public Annotation getAnnotation() {
		return this.annotation;
	}
}
