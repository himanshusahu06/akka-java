package playing_with_actor.music.messages;

public class MusicPlayerStartCommand {

	private Song song;

	public MusicPlayerStartCommand(Song song) {
		this.song = song;
	}

	public Song getSong() {
		return song;
	}
}
