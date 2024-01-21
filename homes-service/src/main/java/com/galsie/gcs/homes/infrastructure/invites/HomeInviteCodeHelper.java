package com.galsie.gcs.homes.infrastructure.invites;

import com.galsie.lib.utils.StringUtils;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Getter
@Component
public class HomeInviteCodeHelper {


    @Value("${galsie.home.invites.code-length")
    private int inviteCodeLength;

    @Value("${galsie.home.invites.code-regex")
    private String inviteCodePattern;

    @Value("${galsie.home.invites.max-regeneration-retries}")
    private int maxGenerationRetries;

    private final SecureRandom secureRandom = new SecureRandom();

    public String generateInviteCode(boolean isQRCodeInvite) {
        StringBuilder code = new StringBuilder();
        // First character is 'U' for direct invites or 'Q' for QR code invites
        code.append(isQRCodeInvite ? 'Q' : 'U');
        code.append(StringUtils.randomAlphanumeric(inviteCodeLength-1, secureRandom::nextInt));
        return code.toString();
    }

    public boolean isValidInviteCode(String inviteUniqueCode) {
        return inviteUniqueCode != null && inviteUniqueCode.matches(this.inviteCodePattern);
    }

}
