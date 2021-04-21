import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import javax.imageio.ImageIO;
import javax.swing.*;

public class BBoxBuilder implements MouseListener{
	Boolean first = false;
	JFrame window = new JFrame();
	Path path;
	private int x1 = 0, y1 = 0, x2 = 0, y2 = 0;
	
	public void createBBoxBuilder(Path path) throws IOException {		
		this.path = path;
		
		ImageIcon icon = new ImageIcon(path.toString());
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
		
		window.dispose();
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
