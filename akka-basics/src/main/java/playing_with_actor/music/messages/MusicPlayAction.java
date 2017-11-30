package playing_with_actor.music.messages;

public class MusicPlayAction {

	private Song song;

	public MusicPlayAction(Song song) {
		this.song = song;
	}

	public Song getSong() {
		return song;
	}
}
