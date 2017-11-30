package playing_with_actor.music;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import playing_with_actor.music.messages.MusicPlayAction;
import playing_with_actor.music.messages.MusicPlayerStartCommand;
import playing_with_actor.music.messages.MusicPlayerStopCommand;
import playing_with_actor.music.messages.MusicStopAction;
import playing_with_actor.music.messages.Song;

class MusicPlayerActor extends AbstractActor {

	@Override
	public Receive createReceive() {
		return receiveBuilder().match(MusicPlayAction.class, this::play).match(MusicStopAction.class, this::stop)
				.build();
	}

	private void play(MusicPlayAction musicPlayAction) {
		System.out.println("[MusicPlayerActor] : Playing music - " + musicPlayAction.getSong());
	}

	private void stop(MusicStopAction musicStopAction) {
		System.out.println("[MusicPlayerActor] : Stooping music");
	}
}

class MusicControllerActor extends AbstractActor {

	private ActorRef musicPlayerActorRef;
	
	private Boolean playingSong = false;
	
	private Song currentPlaying = Song.NONE;

	MusicControllerActor(ActorRef musicPlayerActorRef) {
		this.musicPlayerActorRef = musicPlayerActorRef;
	}

	@Override
	public Receive createReceive() {
		return receiveBuilder().match(MusicPlayerStartCommand.class, this::sendPlayCommand)
				.match(MusicPlayerStopCommand.class, this::sendStopCommand).build();
	}

	private void sendPlayCommand(MusicPlayerStartCommand startCommand) {
		if (playingSong == true) {
			System.out.println("[MusicControllerActor] : Sending switch music ["+ currentPlaying + "->" + startCommand.getSong()+ "] action message to MusicPlayer");
		} else {
			System.out.println("[MusicControllerActor] : Sending start music action message to MusicPlayer - " + startCommand.getSong());
		}
		MusicPlayAction musicPlayActionMessage = new MusicPlayAction(startCommand.getSong());
		musicPlayerActorRef.tell(musicPlayActionMessage, self());
		playingSong = true;
		currentPlaying = startCommand.getSong();
	}

	private void sendStopCommand(MusicPlayerStopCommand stopCommand) {
		playingSong = false;
		currentPlaying = Song.NONE;
		System.out.println("[MusicControllerActor] : Sending stop music action message to MusicPlayer actor");
		MusicStopAction musicStopAction = new MusicStopAction();
		musicPlayerActorRef.tell(musicStopAction, self());
	}
}

public class ActorCreation {

	public static void main(String[] args) {

		ActorSystem system = ActorSystem.create("ActorCreation");

		ActorRef musicPlayer = system.actorOf(Props.create(MusicPlayerActor.class), "MusicPlayer");

		ActorRef musicController = system.actorOf(Props.create(MusicControllerActor.class, musicPlayer), "MusicController");

		musicController.tell(new MusicPlayerStartCommand(Song.LET_HER_GO), ActorRef.noSender());
		
		musicController.tell(new MusicPlayerStartCommand(Song.SHAPE_OF_YOU), ActorRef.noSender());
		
		musicController.tell(new MusicPlayerStopCommand(), ActorRef.noSender());
		
		musicController.tell(new MusicPlayerStartCommand(Song.RUDE), ActorRef.noSender());
		
		musicController.tell(new MusicPlayerStopCommand(), ActorRef.noSender());

		system.terminate();
	}
}
