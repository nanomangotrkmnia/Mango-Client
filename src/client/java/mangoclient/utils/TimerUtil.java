package mangoclient.utils;

public class TimerUtil {
	private long lastMs = System.currentTimeMillis();

	public boolean hasTimePassed(long ms) {
		return System.currentTimeMillis() - lastMs >= ms;
	}

	public boolean hasTimePassed(long ms, boolean reset) {
		if (hasTimePassed(ms)) {
			if (reset) reset();
			return true;
		}
		return false;
	}

	public void reset() {
		lastMs = System.currentTimeMillis();
	}

	public long elapsed() {
		return System.currentTimeMillis() - lastMs;
	}
}
