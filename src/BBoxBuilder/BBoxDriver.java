package BBoxBuilder;

import COCOTools.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class BBoxDriver {
	static ArrayList<FireImage> images = new ArrayList<>();
	static HashMap<String, Integer> categories = new HashMap<>();
	static ArrayList<Path> paths = new ArrayList<>();
	static Object lock = new Object();
	static String starter;
	static boolean passedStart = false;

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try {
			COCOTools.loadData("fire_data");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		images = COCOTools.getImages();
		categories = COCOTools.getCategories();
		paths = COCOTools.getPaths();

		try {
			starter = Files.readString(Path.of("fire_starter.txt"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		for (FireImage image : images) { // iterate through all images in the directory
			
			File imageFile = new File(image.getFileName());
			
			System.out.println(image.getFileName());
			if (image.getFileName().compareTo(starter) == 0) { // skip all files before the starter. This allow for multiple runs
				passedStart = true;
			}
			// Open BBoxBuilder to generate bbox file
			synchronized (lock) {
				if (passedStart) {
					try {
						BBoxBuilder builder = new BBoxBuilder();
						builder.createBBoxBuilder(image.getPath(), lock);
						lock.wait();
					} catch (IOException | InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// Write last file name to file for continuation later;
					try {
						Files.writeString(Path.of("fire_starter.txt"), image.getFileName());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
}
