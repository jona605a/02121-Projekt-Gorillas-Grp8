package application;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Sound {

    private MediaPlayer mediaPlayer;

    Sound(String path) {
        Media sound = new Media(path);
        mediaPlayer = new MediaPlayer(sound);
    }

    public void play() {
        mediaPlayer.play();
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
