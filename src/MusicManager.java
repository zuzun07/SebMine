
	import javax.sound.sampled.*;
	import java.io.File;

	public class MusicManager {
	    private static Clip globalClip;

	    public static void playBackground(String fileName) {
	        if (globalClip != null && globalClip.isRunning()) return;
	        stopBackground();

	        try {
	            String[] paths = {
	                "resources/" + fileName,
	                fileName,
	                "./resources/" + fileName,
	                "./" + fileName
	            };

	            for (String path : paths) {
	                File file = new File(path);
	                if (file.exists()) {
	                    AudioInputStream audio = AudioSystem.getAudioInputStream(file);
	                    globalClip = AudioSystem.getClip();
	                    globalClip.open(audio);
	                    globalClip.loop(Clip.LOOP_CONTINUOUSLY);
	                    break;
	                }
	            }
	        } catch (Exception ignored) {}
	    }

	    public static void stopBackground() {
	        if (globalClip != null) {
	            globalClip.stop();
	            globalClip.close();
	            globalClip = null;
	        }
	    }

	    public static void playSound(String fileName) {
	        try {
	            String[] paths = {
	                "resources/" + fileName,
	                fileName,
	                "./resources/" + fileName,
	                "./" + fileName
	            };

	            for (String path : paths) {
	                File file = new File(path);
	                if (file.exists()) {
	                    AudioInputStream audio = AudioSystem.getAudioInputStream(file);
	                    Clip clip = AudioSystem.getClip();
	                    clip.open(audio);
	                    clip.start();
	                    break;
	                }
	            }
	        } catch (Exception ignored) {}
	    }

}
