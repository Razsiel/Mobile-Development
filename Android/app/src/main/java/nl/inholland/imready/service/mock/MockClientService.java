package nl.inholland.imready.service.mock;

import nl.inholland.imready.service.model.Client;
import nl.inholland.imready.service.model.FutureplanResponse;
import io.reactivex.Single;
import nl.inholland.imready.service.rest.ClientService;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.mock.BehaviorDelegate;

class MockClientService implements ClientService {
    private final BehaviorDelegate<ClientService> delegate;

    public MockClientService(BehaviorDelegate<ClientService> delegate) {
        this.delegate = delegate;
    }

    @Override
    public Single<ResponseBody> getFuturePlan(String clientId) {
        return delegate.returningResponse(null).getFuturePlan(clientId);
    }

    @Override
    public Call<Void> enrollComponent(String clientId, String componentId) {
        return delegate.returningResponse(null).enrollComponent(clientId, componentId);
    }

    @Override
    public Call<Client> getClient(String clientId) {
        return null;
    }
}
