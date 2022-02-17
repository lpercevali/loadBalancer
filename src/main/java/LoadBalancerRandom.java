import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class LoadBalancerRandom extends LoadBalancerAbstract {

    private Random random;
    private final AtomicInteger requests;

    public LoadBalancerRandom() {
        super();
        random = new Random();
        requests = new AtomicInteger();
    }

    @Override
    public String get() {
        final var requestNumber = requests.getAndIncrement();
        final var maxParallelRequests = getAvailableProviders().stream().findFirst().get().getMaxParallelRequests();
        final var size = getAvailableProviders().size();
        if (requestNumber < maxParallelRequests * size) {
            requests.getAndDecrement();
            if (size > 0) {
                final var randomProviderIndex = random.nextInt(size);
                return getAvailableProviders().get(randomProviderIndex).get();
            }
        }
        return "No Providers";
    }
}
