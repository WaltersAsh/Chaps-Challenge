package nz.ac.vuw.ecs.swen225.gp20.rendering;

import javax.sound.sampled.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Sound class to process and play the sound effects
 *
 * @author Matt Jay 300443033
 */
public class SoundEffect {
    Clip sound;
    public SoundEffect(String filename){
        File audio = new File(filename);
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audio);

            AudioFormat format = audioStream.getFormat();

            DataLine.Info info = new DataLine.Info(Clip.class, format);

            sound = (Clip) AudioSystem.getLine(info);

            sound.open(audioStream);
        }
        catch (UnsupportedAudioFileException ex) {
            System.out.println("The specified audio file is not supported.");
            ex.printStackTrace();
        }
        catch (LineUnavailableException ex) {
            System.out.println("Audio line for playing back is unavailable.");
            ex.printStackTrace();
        }
        catch (IOException ex) {
            System.out.println("Error playing the audio file.");
            ex.printStackTrace();
        }
    }

    public void play(){
        sound.start();
    }

    public void reset(){
        sound.setFramePosition(0);
    }

}
