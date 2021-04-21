
public class Annotation {
	private int x1, y1, x2, y2; // x1 and y1 are top left, x2 and y2 are bottom right
	private int id, catId, imageId;
	private static int currId = 0;

	public Annotation(int catId, int imageId) {
		this.id = currId++;
		this.catId = catId;
		this.imageId = imageId;
		setCoords(0, 0, 0, 0);
	}

	public String buildCOCO() {
		String COCO = "{\n";
		COCO += "    \"iscrowd\": " + 0 + ",\n";
		COCO += "    \"image_id\": " + imageId + ",\n";
		COCO += "    \"id\": " + id + ",\n";
		COCO += "    \"category_id\": " + catId + ",\n";

		// bbox annotation
		int height = y2 - y1;
		int width = x2 - x1;
		COCO += "    \"bbox\": [\n";
		COCO += "        " + x1 + ",\n";
		COCO += "        " + y1 + ",\n";
		COCO += "        " + width + ",\n";
		COCO += "        " + height + "\n";
		COCO += "    ]\n";

		COCO += "}";

		return COCO;
	}

	public void setCoords(int x1, int y1, int x2, int y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}
}
