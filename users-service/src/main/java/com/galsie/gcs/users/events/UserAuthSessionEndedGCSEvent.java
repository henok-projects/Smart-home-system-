package com.galsie.gcs.users.events;

import com.galsie.gcs.microservicecommon.lib.galsecurity.init.authenticator.user.CodableUserAuthSessionToken;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.events.GCSEventCommonImpl;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserAuthSessionEndedGCSEvent extends GCSEventCommonImpl {
    private CodableUserAuthSessionToken galsieAuthLoginSessionToken;
    @Override
    public boolean isCancellable() {
        return false;
    }
}
