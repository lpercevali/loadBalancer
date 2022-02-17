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
        final var maxParallelRequests = getAvailableProviders().stream().findFirst().get().getMaxParallelRequests();
        final var size = getAvailableProviders().size();
        final var requestNumber = requests.incrementAndGet();

        if (requestNumber < maxParallelRequests * size) {
            var providerIndex = roundRobin.getAndIncrement();
            if (providerIndex >= size) {
                roundRobin.set(0);
                providerIndex = 0;
            }
            if (size > 0) {
                requests.getAndDecrement();
                return getAvailableProviders().get(providerIndex).get();
            }
        }
        final var decrement = requests.getAndDecrement();
        return "No providers" + " | Requests: " + decrement;
    }
}
