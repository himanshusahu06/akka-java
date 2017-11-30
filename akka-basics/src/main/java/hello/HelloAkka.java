package hello;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

/**
 * Actor Msssage Class
 */
class WhoToGreet {

	private String name;

	WhoToGreet(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}

/**
 * Actor class
 */
class Greeter extends AbstractActor {

	@Override
	public Receive createReceive() {
		return receiveBuilder().match(WhoToGreet.class, this::greet).build();
	}

	private void greet(WhoToGreet toGreet) {
		System.out.println("Hello " + toGreet.getName() + " from " + getSender());
	}
}

/**
 * Akka driver
 */
public class HelloAkka {

	public static void main(String[] args) {

		ActorSystem actorSystem = ActorSystem.create("GreetActorSystem");

		ActorRef greeterActorRef = actorSystem.actorOf(Props.create(Greeter.class), "greeter");

		greeterActorRef.tell(new WhoToGreet("Himanshu Sahu"), ActorRef.noSender());
		
		actorSystem.terminate();
	}
}
