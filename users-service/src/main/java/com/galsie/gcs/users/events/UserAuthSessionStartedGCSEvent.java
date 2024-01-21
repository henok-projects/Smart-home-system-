package com.galsie.gcs.users.events;

import com.galsie.gcs.serviceutilslibrary.utils.gcs.events.GCSEventCommonImpl;
import com.galsie.gcs.microservicecommon.lib.galsecurity.init.authenticator.user.CodableUserAuthSessionToken;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserAuthSessionStartedGCSEvent extends GCSEventCommonImpl {
    private CodableUserAuthSessionToken galsieAuthLoginSessionToken;

    @Override
    public boolean isCancellable() {
        return true;
    }
}
