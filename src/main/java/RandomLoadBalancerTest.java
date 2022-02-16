import java.util.ArrayList;

public class RandomLoadBalancerTest {

    public static void main(String[] args) {

        final Provider providerOne = new Provider();
        final Provider providerTwo = new Provider();
        final Provider providerThree = new Provider();
        final Provider providerFour = new Provider();

        final ArrayList<Provider> providers = new ArrayList<>();
        providers.add(providerOne);
        providers.add(providerTwo);
        providers.add(providerThree);

        final var randomLoadBalancer = new LoadBalancerRandom();

        randomLoadBalancer.register(providers);

        System.out.println("Getting random identifier from provider");
        System.out.println("Provider: " + randomLoadBalancer.get());
        System.out.println("Registered Providers: " + randomLoadBalancer.getProviders().size());

        System.out.println("Registering new provider");
        randomLoadBalancer.include(providerFour);

        System.out.println("Getting random identifier from provider");
        System.out.println("Provider: " + randomLoadBalancer.get());

        System.out.println("Registered Providers: " + randomLoadBalancer.getProviders().size());

        randomLoadBalancer.exclude(providerThree);

        System.out.println("Registered Providers: " + randomLoadBalancer.getProviders().size());

    }
}
