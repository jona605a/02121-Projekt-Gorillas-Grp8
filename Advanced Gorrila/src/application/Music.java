package application;

public class Music {

    private Sound[] musicList;
    private int currentSongIndex = 0;

    public Music(String[] songs) {
        this(songs, 0);
    }

    public Music(String[] songs, String startSongPath) {
       this(songs, Music.getSongIndex(songs, startSongPath));
    }

    public Music(String[] songs, int startSongIndex) {

        musicList = new Sound[songs.length];
        for (int i = 0; i < songs.length; i++) {
            musicList[i] = new Sound(songs[i]);
        }
        this.changeVolume(0.2);
        this.currentSongIndex = startSongIndex;
        this.playSong();
    }

    private void playSong() {
        playSong(this.currentSongIndex);
        // Change to new song when current one is finished
        this.getCurrentSong().getMediaPlayer().setOnEndOfMedia( () -> {
            this.onSongFinished();
        });
    }

    public void playSong(int index) {
        if (index >= this.musicList.length || index < 0) return;
        this.stopCurrentSong();
        this.musicList[index].play();
        this.currentSongIndex = index;
    }

    public void playSong(String path) {
        int songIndex = getSongIndex(this.musicList, path);
        if (songIndex != -1) playSong(songIndex);

    }

    public void continueSong() {
        Sound song = this.getCurrentSong();
        if (!song.isPlaying()) song.play();
    }

    public void pauseCurrentSong() {
        Sound song = this.getCurrentSong();
        if (song.isPlaying()) song.pause();
    }

    public void changeVolume(double newVolume) {

        // Volume is a value between 0 and 1
        if(newVolume < 0 || newVolume > 1) return;

        for (Sound song : this.musicList) {
            song.setVolume(newVolume);
        }
    }

    public void stopCurrentSong() {
        Sound song = this.getCurrentSong();
        if (song.isPlaying()) song.stop();
    }

    public Sound getCurrentSong() {
        return this.musicList[this.currentSongIndex];
    }

    private void onSongFinished() {
        this.currentSongIndex++;
        if (currentSongIndex == this.musicList.length) currentSongIndex = 0;
        this.playSong();
    }

    private static int getSongIndex(Sound[] songs, String path) {
        for (int i = 0; i < songs.length; i++) {
            if (songs[i].getPath() == path) return i;
        }
        return -1;
    }

    private static int getSongIndex(String[] songs, String path) {
        for (int i = 0; i < songs.length; i++) {
            if (songs[i] == path) return i;
        }
        return -1;
    }

}
