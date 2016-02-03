package eventTicker.backend;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.joda.time.DateTime;

public class AccountingSessionKeys implements Serializable{
	private AppSynchronizer sync = AppSynchronizer.getSynchronizerInstance();

	private class Tuple<X, Y> {
		public final X x;
		public final Y y;

		public Tuple(X x, Y y) {
			this.x = x;
			this.y = y;
		}
	}

	private Map<Long, Tuple<String, DateTime>> userToken = new HashMap<Long, Tuple<String, DateTime>>();
	private Map<String, DateTime> key2Date = new HashMap<String, DateTime>();

	private static AccountingSessionKeys instance;
	char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();

	public static AccountingSessionKeys getInstance() {
		if (instance == null)
			instance = new AccountingSessionKeys();
		return instance;
	}

	public AccountingSessionKeys() {
	}

	private String generateSessionKey() {

		StringBuilder sb = new StringBuilder();
		Random random = new Random();

		for (int i = 0; i < 20; i++) {
			char c = chars[random.nextInt(chars.length)];
			sb.append(c);
		}
		return sb.toString();
	}

	public boolean endSession(String name,String key) {
		Long uid = ((ConcreteUser)sync.findByUserName(name)).getId();
		key2Date.remove(key);
		userToken.remove(uid);

		return true;
	}

	public String startSession(Long uid) {
		String key = generateSessionKey();
		DateTime now = new DateTime().now();
		Tuple<String, DateTime> t = new Tuple<String, DateTime>(key, now);
		userToken.put(uid, t);
		key2Date.put(key, now);
		return key;
	}

	boolean isValid(Tuple<String, DateTime> sessionKey) {
		DateTime d = sessionKey.y.plusMinutes(20);
		if (d.isAfterNow())
			return true;
		System.out.println("timestamp invalid");
		return false;
	}

	public boolean authenticate(String key) {
		// Tuple auth = new Tuple<String, DateTime>();
		System.out.println(key2Date.get(key));
		if (isValid(new Tuple<String, DateTime>(key, key2Date.get(key))))
			return true;
		return false;
	}

	public boolean isLoggedIn(Long uid) {
		if (userToken.containsKey(uid))
			if (isValid(userToken.get(uid)))
				return true;
			else
				userToken.remove(uid);
		return false;
	}
}
