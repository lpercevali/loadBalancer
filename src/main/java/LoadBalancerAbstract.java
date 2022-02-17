import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public abstract class LoadBalancerAbstract implements LoadBalancer {

    final private int maxProviders = 10;
    final private long hearBeatSeconds = 5;
    private ArrayList<Provider> availableProviders;
    private ArrayList<Provider> providers;

    public LoadBalancerAbstract() {
        providers = new ArrayList<>(maxProviders);
        availableProviders = new ArrayList<>(maxProviders);
    }

    @Override
    public void register(final ArrayList<Provider> providers) {
        if (providers.size() >= maxProviders) return;
        this.providers = providers;
        this.availableProviders = providers;
    }

    @Override
    public void exclude(final Provider provider) {
        this.providers.remove(provider);
        this.availableProviders.remove(provider);
    }

    @Override
    public void include(final Provider provider) {
        this.providers.add(provider);
        this.availableProviders.add(provider);

    }

    @Override
    public void heartBeat() {
        Thread thread = new Thread(heartBeatRunnable(), "Heart Beat Thread");
        thread.start();
    }

    public List<Provider> getAvailableProviders() {
        return this.availableProviders;
    }

    private Runnable heartBeatRunnable() {
        return () -> {
            while (true) {

                final var removedProviders = this.providers.stream().filter(provider -> {
                    final var check = provider.check();
                    if (check) {
                        provider.incrementCounter();
                        if (provider.isAvailable()) {
                            System.out.println(String.format("One more change provider [%s]", provider.get()));
                        }
                    } else {
                        System.out.println(String.format("Reseting chances provider [%s]", provider.get()));
                        provider.zeroingCounter();
                    }
                    return !provider.isAvailable();
                }).toList();
                ArrayList<Provider> copy = (ArrayList<Provider>) this.providers.clone();
                copy.removeAll(removedProviders);
                this.availableProviders = copy;
                System.out.println("Available Providers: " + this.availableProviders.size());
                System.out.println("Total Providers: " + this.providers.size());

                try {
                    Thread.sleep(TimeUnit.SECONDS.toMillis(hearBeatSeconds));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
    }
}
