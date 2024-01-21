package com.galsie.gcs.homes.service.home.invites;


import com.galsie.gcs.homes.infrastructure.invites.HomeInviteCodeHelper;
import com.galsie.gcs.homes.repository.home.user.HomeUserInviteEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class HomeInviteCodeService {

    @Autowired
    HomeUserInviteEntityRepository homeUserInviteEntityRepository;

    @Autowired
    HomeInviteCodeHelper homeInviteCodeHelper;

    public boolean gcsInternalIsInviteCodeValid(String inviteUniqueCode) {
        return this.homeInviteCodeHelper.isValidInviteCode(inviteUniqueCode);
    }

    public Optional<String> gcsInternalGenerateUniqueInviteCode(boolean isQRCodeInvite) {
        int retryCount = homeInviteCodeHelper.getMaxGenerationRetries();

        while (retryCount > 0) {
            String inviteCode = homeInviteCodeHelper.generateInviteCode(isQRCodeInvite);
            if (!gcsIngernalDoesInviteCodeAlreadyExist(inviteCode)) {
                return Optional.ofNullable(inviteCode);
            }
            retryCount--;
        }

        return Optional.empty();
    }

    private boolean gcsIngernalDoesInviteCodeAlreadyExist(String inviteUniqueCode) {
        return homeUserInviteEntityRepository.findByInviteUniqueCode(inviteUniqueCode).isPresent() ;
    }
}
