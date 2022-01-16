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
        this.currentSongIndex = startSongIndex;
        this.playSong();
    }

    private void playSong() {
        playSong(this.currentSongIndex);
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
        if (this.getCurrentSong() == null) this.playSong(this.currentSongIndex);
    }

    public void pauseCurrentSong() {
        Sound song = this.getCurrentSong();
        if (song != null) song.pause();
    }

    public void changeVolume(double newVolume) {

        // Volume is a value between 0 and 1
        if(newVolume < 0 || newVolume > 1) return;

        for (Sound song : this.musicList) {
            song.setVolume(newVolume);
        }
    }

    public Sound getSongByIndex(int index) {
        if (index >= this.musicList.length) return null;
        return this.musicList[index];
    }

    public void stopCurrentSong() {
        Sound song = this.getCurrentSong();
        if (song != null) song.stop();
    }

    private Sound getCurrentSong() {
        for (Sound song : this.musicList) {
            if (song.isPlaying()) return song;
        }

        return null;
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
