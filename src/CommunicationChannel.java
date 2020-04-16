import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Class that implements the channel used by headquarters and space explorers to communicate.
 */
public class CommunicationChannel {
	private BlockingQueue<Message> spaceExplorerChannel;
	private BlockingQueue<Message> headquartersChannel;
	private static HashMap<Long, Message> primitiveMessages;

	/**
	 * Creates a {@code CommunicationChannel} object.
	 */
	public CommunicationChannel() {
		spaceExplorerChannel = new LinkedBlockingQueue<Message>();
		headquartersChannel = new LinkedBlockingQueue<Message>();
		primitiveMessages = new HashMap<>();
	}

	/**
	 * Puts a message on the space explorer channel (i.e., where space explorers write to and 
	 * headquarters read from).
	 * 
	 * @param message
	 *            message to be put on the channel
	 */
	public void putMessageSpaceExplorerChannel(Message message) {
		try {
			spaceExplorerChannel.put(message);
		} catch (InterruptedException e) {
			return;
		}
	}

	/**
	 * Gets a message from the space explorer channel (i.e., where space explorers write to and
	 * headquarters read from).
	 * 
	 * @return message from the space explorer channel
	 */
	public Message getMessageSpaceExplorerChannel() {
		Message mes = null;

		try {
			mes = spaceExplorerChannel.take();
		} catch (InterruptedException e) {
			return null;
		}

		return mes;
	}

	/**
	 * Puts a message on the headquarters channel (i.e., where headquarters write to and 
	 * space explorers read from).
	 * 
	 * @param message
	 *            message to be put on the channel
	 */
	public void putMessageHeadQuarterChannel(Message message) {
		try {
			if (!message.getData().equals("END") && !message.getData().equals("EXIT")) {
				long id = Thread.currentThread().getId();

				if (primitiveMessages.containsKey(id)) {
					Message toBeAdded = new Message(primitiveMessages.get(id).getCurrentSolarSystem(),
							message.getCurrentSolarSystem(), message.getData());
					headquartersChannel.put(toBeAdded);
					primitiveMessages.remove(id);
				} else {
					primitiveMessages.put(id, message);
				}
			} else {
				headquartersChannel.put(message);
			}

		} catch (InterruptedException e) {
			return;
		}
	}

	/**
	 * Gets a message from the headquarters channel (i.e., where headquarters write to and
	 * space explorer read from).
	 * 
	 * @return message from the header quarter channel
	 */
	public Message getMessageHeadQuarterChannel() {
		Message mes = null;

		try {
			mes = headquartersChannel.take();
		} catch (InterruptedException e) {
			return null;
		}

		return mes;
	}
}
