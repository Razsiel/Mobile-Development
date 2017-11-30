package nl.inholland.imready.service.rest;

import nl.inholland.imready.service.BaseClient;

public class RestClient extends BaseClient {

    public RestClient() {
        super();
    }

    @Override
    public ServerAuthenticationService getAuthenticationService() {
        return retrofit.create(ServerAuthenticationService.class);
    }

    @Override
    public BlockService getBlockService() {
        return retrofit.create(BlockService.class);
    }

    @Override
    public ClientService getClientService() {
        return retrofit.create(ClientService.class);
    }

    @Override
    public FamilyService getFamilyService() {
        return retrofit.create(FamilyService.class);
    }

    @Override
    public CaregiverService getCaregiverService() {
        return retrofit.create(CaregiverService.class);
    }

    @Override
    public MessageBaseService getMessageService() {
        return retrofit.create(MessageClientService.class);
    }
}
