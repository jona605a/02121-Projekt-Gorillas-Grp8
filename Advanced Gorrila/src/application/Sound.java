package application;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.AudioClip;

public class Sound {

    private MediaPlayer mediaPlayer;

    public Sound(String path) {
        Media sound = new Media(getClass().getResource(path).toExternalForm());
        mediaPlayer = new MediaPlayer(sound);
    }

    public void play() {
        mediaPlayer.play();
    }

    public static void play(String path) {
        // For quick sound effects
        AudioClip clip = new AudioClip(Sound.class.getResource(path).toExternalForm());
        clip.play();
    }

    public void setVolume(double volume) {
        mediaPlayer.setVolume(volume);
    }

    public void stop() {
        mediaPlayer.stop();
    }

    public void pause() {
        mediaPlayer.pause();
    }

    public boolean isPlaying() {
        return mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING;
    }

    public String getPath() {
        return mediaPlayer.getMedia().getSource();
    }

    public MediaPlayer getMediaPlayer() {
        return this.mediaPlayer;
    }
}
