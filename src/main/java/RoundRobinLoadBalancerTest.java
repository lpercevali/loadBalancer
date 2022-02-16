import java.util.ArrayList;

public class RoundRobinLoadBalancerTest {

    public static void main(String[] args) {

        final Provider providerOne = new Provider();
        final Provider providerTwo = new Provider();
        final Provider providerThree = new Provider();


        final ArrayList<Provider> providers = new ArrayList<>();
        providers.add(providerOne);
        providers.add(providerTwo);
        providers.add(providerThree);

        final var roundRobinLoadBalancer = new LoadBalancerRoundRobin();

        roundRobinLoadBalancer.register(providers);
        System.out.println("Registered Providers: " + roundRobinLoadBalancer.getProviders().size());


        System.out.println("Provider 1: " + roundRobinLoadBalancer.get());

        System.out.println("Provider 2: " + roundRobinLoadBalancer.get());

        System.out.println("Provider 3: " + roundRobinLoadBalancer.get());

        System.out.println("---------- AGAIN -----------");

        System.out.println("Provider 1: " + roundRobinLoadBalancer.get());


        System.out.println("Excluding Provider: " + providerThree.get());

        System.out.println("-------- RESETTING --------");

        roundRobinLoadBalancer.exclude(providerThree);
        System.out.println("Registered Providers: " + roundRobinLoadBalancer.getProviders().size());

        System.out.println("Provider 1: " + roundRobinLoadBalancer.get());
        System.out.println("Provider 2: " + roundRobinLoadBalancer.get());


        System.out.println("-------- RESETTING --------");

        System.out.println("Including Provider: " + providerThree.get());
        roundRobinLoadBalancer.include(providerThree);
        System.out.println("Registered Providers: " + roundRobinLoadBalancer.getProviders().size());

        System.out.println("---------- AGAIN -----------");

        System.out.println("Provider 3: " + roundRobinLoadBalancer.get());
        System.out.println("Provider 1: " + roundRobinLoadBalancer.get());
        System.out.println("Provider 2: " + roundRobinLoadBalancer.get());


        System.out.println("---------- Excluding all providers -----------");
        System.out.println("Excluding Provider: " + providerOne.get());
        roundRobinLoadBalancer.exclude(providerOne);
        System.out.println("Excluding Provider: " + providerTwo.get());
        roundRobinLoadBalancer.exclude(providerTwo);
        System.out.println("Excluding Provider: " + providerThree.get());
        roundRobinLoadBalancer.exclude(providerThree);
        System.out.println("Provider error: " + roundRobinLoadBalancer.get());

    }
}
