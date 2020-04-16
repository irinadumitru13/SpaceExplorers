import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Set;
import java.nio.charset.StandardCharsets;

/**
 * Class for a space explorer.
 */
public class SpaceExplorer extends Thread {
	private final Integer hashCount;
	private static Set<Integer> discovered = new HashSet<Integer>();
	private static CommunicationChannel channel;

	/**
	 * Creates a {@code SpaceExplorer} object.
	 * 
	 * @param hashCount
	 *            number of times that a space explorer repeats the hash operation
	 *            when decoding
	 * @param discovered
	 *            set containing the IDs of the discovered solar systems
	 * @param channel
	 *            communication channel between the space explorers and the
	 *            headquarters
	 */
	public SpaceExplorer(Integer hashCount, Set<Integer> discovered, CommunicationChannel channel) {
		this.hashCount = hashCount;
		this.discovered = discovered;
		this.channel = channel;
	}

	@Override
	public void run() {
		while (true) {
			Message headQuarterMessage = channel.getMessageHeadQuarterChannel();

			String data = headQuarterMessage.getData();

			if (data.equals("EXIT")) {
				System.exit(0);
			} else if (data.equals("END")) {
				continue;
			} else {
				int parent = headQuarterMessage.getParentSolarSystem();
				int current = headQuarterMessage.getCurrentSolarSystem();

				if (!discovered.contains(current)) {
					String frequency = encryptMultipleTimes(data, hashCount);
					channel.putMessageSpaceExplorerChannel(new Message(parent, current, frequency));
					discovered.add(current);
				}
			}
		}
	}

	/**
	 * Applies a hash function to a string for a given number of times (i.e.,
	 * decodes a frequency).
	 * 
	 * @param input
	 *            string to he hashed multiple times
	 * @param count
	 *            number of times that the string is hashed
	 * @return hashed string (i.e., decoded frequency)
	 */
	private String encryptMultipleTimes(String input, Integer count) {
		String hashed = input;
		for (int i = 0; i < count; ++i) {
			hashed = encryptThisString(hashed);
		}

		return hashed;
	}

	/**
	 * Applies a hash function to a string (to be used multiple times when decoding
	 * a frequency).
	 * 
	 * @param input
	 *            string to be hashed
	 * @return hashed string
	 */
	private static String encryptThisString(String input) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] messageDigest = md.digest(input.getBytes(StandardCharsets.UTF_8));

			// convert to string
			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < messageDigest.length; i++) {
				String hex = Integer.toHexString(0xff & messageDigest[i]);
				if (hex.length() == 1)
					hexString.append('0');
				hexString.append(hex);
			}
			return hexString.toString();

		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
}
