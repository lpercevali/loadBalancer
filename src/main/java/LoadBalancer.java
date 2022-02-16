import java.util.ArrayList;

public interface LoadBalancer {
    String get();

    void register(ArrayList<Provider> providers);

    void include(Provider provider);

    void exclude(Provider provider);

    void heartBeat();
}
