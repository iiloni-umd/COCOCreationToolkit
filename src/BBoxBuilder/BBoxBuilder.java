package BBoxBuilder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.imageio.ImageIO;
import javax.swing.*;

public class BBoxBuilder implements MouseListener{
	Boolean first = false;
	JFrame window = new JFrame();
	Path path;
	private int x1 = 0, y1 = 0, x2 = 0, y2 = 0;
	Object lock;
	
	public void createBBoxBuilder(Path path, Object lock) throws IOException {		
		this.path = path;
		this.lock = lock;
		
		Image img = ImageIO.read(path.toFile());
			
		//scale the image to 600 x 600
		img = this.getScaledImage(img, 600, 600);
		
		
		
		ImageIcon icon = new ImageIcon(img);
		System.out.println(path.toString());
		//BufferedImage image = ImageIO.read(path.toFile());
		JLabel imgLabel = new JLabel(icon);
		window.add(imgLabel);
		
		window.setBounds(0, 0, 600, 600);
		window.setVisible(true);
		window.addMouseListener(this);
	}
	
	@Override
	public void mouseReleased(MouseEvent mouse) {	
		synchronized (lock) {
			if (!first) {
				x1 = mouse.getX();
				y1 = mouse.getY();
				System.out.println("Top Left X: " + x1);
				System.out.println("Top Left Y: " + y1);
				first = true;
				return;
			}

			x2 = mouse.getX();
			y2 = mouse.getY();
			System.out.println("Bottom Right X: " + x2);
			System.out.println("Bottom Right Y: " + y2);
			
			
			String fileName = path.getFileName().toString();
			fileName = fileName.substring(0, fileName.length()-4);
			fileName += ".txt";
			
			BBox bbox = new BBox(x1, y1, x2, y2);
			try {
				bbox.writeToFile(fileName);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				Files.writeString(Path.of("fire_starter.txt"), path.getFileName().toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			lock.notify();
		}
		
		
		window.dispose();
	}
	
	private Image getScaledImage(Image srcImg, int w, int h){
	    BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2 = resizedImg.createGraphics();

	    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    g2.drawImage(srcImg, 0, 0, w, h, null);
	    g2.dispose();

	    return resizedImg;
	}

	//unused method stubs
	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}	
}
