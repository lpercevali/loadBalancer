import java.util.Random;
import java.util.UUID;

public class Provider {

    final private String identifier;
    private int heartBeatCounter = 2;
    private int maxParallelRequests = 2;

    public Provider() {
        identifier = UUID.randomUUID().toString();
    }

    public String get() {
        return identifier;
    }

    public Boolean check() {
        return new Random().nextInt(10) < 8;
    }

    public void incrementCounter() {
        heartBeatCounter += 1;
        if (heartBeatCounter >= 2) heartBeatCounter = 2;
    }

    public void zeroingCounter() {
        heartBeatCounter = 0;
    }

    public boolean isAvailable() {
        return heartBeatCounter == 2;
    }

    public int getMaxParallelRequests() {
        return maxParallelRequests;
    }
}
