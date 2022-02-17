import java.util.concurrent.atomic.AtomicInteger;

public class LoadBalancerRoundRobin extends LoadBalancerAbstract {

    private final AtomicInteger roundRobin;
    private final AtomicInteger requests;

    public LoadBalancerRoundRobin() {
        super();
        roundRobin = new AtomicInteger(0);
        requests = new AtomicInteger(0);
    }

    @Override
    public String get() {
        final var size = getAvailableProviders().size();
        final var canUse = getSemaphore().tryAcquire();
        if (canUse) {
            var providerIndex = roundRobin.getAndIncrement();
            if (providerIndex >= size) {
                roundRobin.set(0);
                providerIndex = 0;
            }
            if (size > 0) {
                requests.getAndDecrement();
                getSemaphore().release();
                return getAvailableProviders().get(providerIndex).get();
            }
        }
        getSemaphore().release();
        return "No providers";
    }
}
