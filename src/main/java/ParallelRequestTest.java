import java.util.ArrayList;
import java.util.stream.IntStream;

public class ParallelRequestTest {

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

        IntStream.range(0, 1000)
                .parallel()
                .forEach( i -> System.out.println(String.format(
                        "Client %d - Getting provider [%s]",
                        i, roundRobinLoadBalancer.get())));



    }
}
