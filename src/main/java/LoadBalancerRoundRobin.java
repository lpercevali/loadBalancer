import java.util.concurrent.atomic.AtomicInteger;

public class LoadBalancerRoundRobin extends LoadBalancerAbstract {

    private final AtomicInteger roundRobin;

    public LoadBalancerRoundRobin() {
        super();
        roundRobin = new AtomicInteger();
    }

    @Override
    public String get() {
        var providerIndex = roundRobin.getAndIncrement();
        if (providerIndex >= getProviders().size()) {
            roundRobin.set(0);
            providerIndex = 0;
        }
        if (getProviders().size() > 0) {
            return getProviders().get(providerIndex).get();
        }
        return "No providers";
    }
}
