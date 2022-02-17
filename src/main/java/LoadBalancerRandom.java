import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class LoadBalancerRandom extends LoadBalancerAbstract {

    private Random random;

    public LoadBalancerRandom() {
        super();
        random = new Random();
    }

    @Override
    public String get() {
        final var size = getAvailableProviders().size();
        final var canUse = getSemaphore().tryAcquire();
        if (canUse) {
            if (size > 0) {
                final var randomProviderIndex = random.nextInt(size);
                getSemaphore().release();
                return getAvailableProviders().get(randomProviderIndex).get();
            }
        }
        getSemaphore().release();
        return "No Providers";
    }
}
