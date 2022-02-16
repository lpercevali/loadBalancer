import java.util.Random;

public class LoadBalancerRandom extends LoadBalancerAbstract {

    private Random random;

    public LoadBalancerRandom() {
        super();
        random = new Random();
    }

    @Override
    public String get() {
        final var randomProviderIndex = random.nextInt(getProviders().size());
        if (getProviders().size() > 0) {
            return getProviders().get(randomProviderIndex).get();
        }

        return "No Providers";
    }
}
