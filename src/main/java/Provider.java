import java.util.Random;
import java.util.UUID;

public class Provider {

    final private String identifier;

    public Provider() {
        identifier = UUID.randomUUID().toString();
    }

    public String get() {
        return identifier;
    }

    public Boolean check() {
        return new Random().nextBoolean();
    }
}
