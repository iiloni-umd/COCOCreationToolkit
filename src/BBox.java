import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class BBox {
	private int x1, y1, x2, y2;
	
	public BBox(int x1, int y1, int x2, int y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}
	
	public int getX1() {
		return x1;
	}
	
	public int getY1() {
		return y1;
	}
	
	public int calcWidth() {
		return x2 - x1;
	}
	
	public int calcHeight() {
		return y2 - y1;
	}
	
	public void writeToFile(String name) throws IOException {
		String BBox = "";
		BBox += x1 + "\n";
		BBox += y1 + "\n";
		BBox += calcWidth() + "\n";
		BBox += calcHeight();
		
		String pathString = "fire_bbox\\" + name;
		Files.writeString(Path.of(pathString), BBox);
	}
}
