import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public abstract class LoadBalancerAbstract implements LoadBalancer {

    final private int maxProviders = 10;
    final private long hearBeatSeconds = 5;
    private ArrayList<Provider> providers;
    private Map<Provider, Integer> oldProviders;

    public LoadBalancerAbstract() {
        providers = new ArrayList<>(maxProviders);
        oldProviders = new HashMap<>(maxProviders);
    }

    @Override
    public void register(final ArrayList<Provider> providers) {
        if (providers.size() >= maxProviders) return;
        this.providers = providers;
    }

    @Override
    public void exclude(final Provider provider) {
        this.providers.remove(provider);
    }

    @Override
    public void include(final Provider provider) {
        if (providers.size() >= maxProviders) return;
        this.providers.add(provider);
    }

    @Override
    public void heartBeat() {
        Thread thread = new Thread(heartBeatRunnable(), "Heart Beat Thread");
        thread.start();
    }

    public List<Provider> getProviders() {
        return providers;
    }

    private Runnable heartBeatRunnable() {
        return () -> {
            while (true) {
                final var providers = Collections.synchronizedList(this.providers);
                final var oldProviders = Collections.synchronizedMap(this.oldProviders);
                synchronized (oldProviders) {
                    final var iterator = oldProviders.keySet().iterator();
                    while (iterator.hasNext()) {
                        final var oldProvider = iterator.next();
                        final var count = oldProviders.get(oldProvider) + 1;
                        if (count >= 2) {
                            System.out.println(String.format("Provider [%s] back on rotation", oldProvider.get() ));
                            providers.add(oldProvider);
                            oldProviders.remove(oldProvider);
                        } else {
                            System.out.println(String.format("Provider [%s] one more chance", oldProvider.get() ));
                            oldProviders.put(oldProvider, count);
                        }
                    }
                }

                synchronized (providers) {
                    final var iterator = providers.iterator();
                    while (iterator.hasNext()) {
                        final var provider = iterator.next();
                        if (!provider.check()) {
                            System.out.println(String.format("Provider's [%s] heartbeat failed. Out of rotation", provider.get() ));
                            providers.remove(provider);
                            oldProviders.put(provider, 0);
                        } else {
                            System.out.println(String.format("Provider's [%s] heartbeat was ok", provider.get() ));
                        }
                    }
                }


                try {
                    Thread.sleep(TimeUnit.SECONDS.toMillis(hearBeatSeconds));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
    }
}
