	import javax.swing.*;
	import java.awt.*;
	import java.io.File;

	public class ImageManager {
	    public static ImageIcon sebnemIcon = null;
	    public static ImageIcon sebnemIconSmall = null;
	    public static ImageIcon appIcon = null;

	    public static void loadImages() {
	        try {
	            String[] paths = { "resources/sebnem.png", "sebnem.png", "./resources/sebnem.png", "./sebnem.png" };
	            ImageIcon originalIcon = null;

	            for (String path : paths) {
	                File file = new File(path);
	                if (file.exists()) {
	                    originalIcon = new ImageIcon(path);
	                    break;
	                }
	            }

	            if (originalIcon != null && originalIcon.getIconWidth() > 0) {
	                appIcon = originalIcon;
	                sebnemIcon = scaleImage(originalIcon, 32, 32);
	                sebnemIconSmall = scaleImage(originalIcon, 20, 20);
	            } else {
	                sebnemIcon = fallback(32, 32);
	                sebnemIconSmall = fallback(20, 20);
	                appIcon = fallback(64, 64);
	            }
	        } catch (Exception e) {
	            sebnemIcon = fallback(32, 32);
	            sebnemIconSmall = fallback(20, 20);
	            appIcon = fallback(64, 64);
	        }
	    }

	    private static ImageIcon scaleImage(ImageIcon original, int w, int h) {
	        Image img = original.getImage();
	        Image scaled = img.getScaledInstance(w, h, Image.SCALE_SMOOTH);
	        return new ImageIcon(scaled);
	    }

	    private static ImageIcon fallback(int w, int h) {
	        var img = new java.awt.image.BufferedImage(w, h, java.awt.image.BufferedImage.TYPE_INT_ARGB);
	        var g2d = img.createGraphics();
	        g2d.setColor(new Color(100, 150, 200));
	        g2d.fillRoundRect(2, 2, w - 4, h - 4, 8, 8);
	        g2d.setColor(new Color(70, 110, 160));
	        g2d.drawRoundRect(2, 2, w - 4, h - 4, 8, 8);
	        g2d.dispose();
	        return new ImageIcon(img);
	    }

}
