package COCOTools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import javax.imageio.ImageIO;

import BBoxBuilder.BBoxBuilder;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.*;

public class COCOTools {
	static ArrayList<FireImage> images = new ArrayList<>();
	static HashMap<String, Integer> categories = new HashMap<>();
	static ArrayList<Path> paths = new ArrayList<>();
	static ArrayList<Path> txtPaths = new ArrayList<>();

	public static void main(String[] args) {

		// load data from directory
		try {
			loadData("fire_data_scaled");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// normalize all images to 600 x 600
//		try {
//			normalizeImages();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		// load BBox bounds from file
		try {
			loadBBox();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// build COCO annotation file.
		buildAllCOCO();
	}

	public static ArrayList<FireImage> getImages() {
		return images;
	}

	public static HashMap<String, Integer> getCategories() {
		return categories;
	}

	public static ArrayList<Path> getPaths() {
		return paths;
	}

	public static void buildAllCOCO() {
		int count = 0;

		// Build image COCO
		String imageCOCO = "\"images\": [\n";
		for (FireImage image : images) {
			imageCOCO += image.buildCOCO();
			if (count != images.size() - 1) {
				imageCOCO += ",\n";
			} else {
				imageCOCO += "\n";
			}
		}
		imageCOCO += "],\n";

		// Build annotation COCO
		count = 0;
		String annotationCOCO = "\"annotations\": [\n";
		for (int i = 0; i < images.size(); i++) {
			images.get(i).buildAnnotation(txtPaths.get(i + 1));
			Annotation annotation = images.get(i).getAnnotation();
			annotationCOCO += annotation.buildCOCO();
			if (count != images.size() - 1) {
				annotationCOCO += ",\n";
			} else {
				annotationCOCO += "\n";
			}
		}
		annotationCOCO += "],\n";

		// Build category COCO
		count = 0;
		String categoryCOCO = "\"categories\": [\n";
		Set<String> cats = categories.keySet();
		for (String cat : cats) {

			categoryCOCO += "{\n";
			categoryCOCO += "\"supercategory\": \"person\",\n";
			categoryCOCO += "\"id\": " + categories.get(cat) + ",\n";
			categoryCOCO += "\"name\": \"" + cat + "\"\n";
			categoryCOCO += "}";

			if (count != cats.size() - 1) {
				categoryCOCO += ",\n";
			} else {
				categoryCOCO += "\n";
			}
		}
		categoryCOCO += "]\n";

		// combine images, annotations, and categories
		String COCO = "{\n";
		COCO += imageCOCO;
		COCO += annotationCOCO;
		COCO += categoryCOCO;
		COCO += "}";

		try {
			Files.writeString(Path.of("fire_annotations.txt"), COCO);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void loadData(String dataset) throws IOException {
		int currCatID = 0;

		// Iterate through files and directories in fire_data. Add the path to an
		// ArrayList for further processing
		Files.walk(Paths.get(dataset)).forEach(path -> {
			paths.add(path);
		});

		// process each path to create categories with ids, and create images.
		for (Path path : paths) {
			File curr = path.toFile();

			if (curr.isDirectory())
				categories.put(path.getFileName().toString(), currCatID++);

			if (curr.isFile()) {
				int catId = categories.get(path.getParent().getFileName().toString());
				images.add(new FireImage(path.getFileName().toString(), path, catId));
			}
		}
	}

	public static void loadBBox() throws IOException {
		Files.walk(Paths.get("fire_bbox")).forEach(path -> {
			txtPaths.add(path);
		});
	}

	public static void normalizeImages() throws IOException {
		for (FireImage image : images) {
			BufferedImage img = ImageIO.read(image.getPath().toFile());

			// scale the image to 600 x 600
			BufferedImage scaledImg = getScaledImage(img, 600, 600);

			File newFile = new File(paths.get(0).toString() + "_scaled\\"
					+ image.getPath().getParent().getFileName().toString() + "\\" + image.getFileName());
			System.out.println(ImageIO.write(scaledImg, "png", newFile)); // format doesn't even matter because it gets
																			// overwritten with the filename anyways
		}
	}

	private static BufferedImage getScaledImage(Image srcImg, int w, int h) {
		BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = resizedImg.createGraphics();

		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.drawImage(srcImg, 0, 0, w, h, null);
		g2.dispose();

		return resizedImg;
	}

}
