package com.galsie.gcs.homes.service.home.invites;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.galsie.gcs.homes.data.discrete.notificationpreferences.HomeUserNotificationPreference;
import com.galsie.gcs.homes.data.entity.home.user.HomeUserEntity;
import com.galsie.gcs.homes.service.home.homeusers.HomeUserHelperService;
import com.galsie.gcs.homes.service.users.UserHomeNotificationPreferenceService;
import com.galsie.gcs.microservicecommon.lib.email.GCSRemoteEmailSender;
import com.galsie.gcs.microservicecommon.lib.email.data.discrete.EmailType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Service
public class HomeUserInvitesNotifierService {

    @Autowired
    GCSRemoteEmailSender gcsRemoteEmailSender;

    @Autowired
    HomeUserHelperService homeUserHelperService;

    @Autowired
    UserHomeNotificationPreferenceService userHomeNotificationPreferenceService;

    public void sendEmailToInvitedUser(String email, String inviteCode, String inviteLink) {
        if (email != null && !email.isEmpty()) {
            var map = new HashMap<String, String>();
            map.put("invitation_code", inviteCode);
            map.put("invite_link", inviteLink);
            gcsRemoteEmailSender.sendEmail(EmailType.EMAIL_BASED_INVITATION_NOTIFICATION, email, map);
        }
    }

    public void sendSMSToInvitedUser(List<String> phoneNumbers) {
        // TODO: integrate with sms
    }

    public void notifyHomeUsersThatNewUserJoined(long homeId, HomeUserEntity joinedUser) throws JsonProcessingException {

        var homeUsers = homeUserHelperService.getHomeUsers(homeId, false, false, false);
        for (HomeUserEntity homeUserEntity : homeUsers) {
            if (Objects.equals(homeUserEntity.getUserId(), joinedUser.getUserId())){
                // If it's the user that joined, ignore
                continue;
            }
            var shouldSendEmail = userHomeNotificationPreferenceService.getPreferenceValue(
                    homeUserEntity,
                    HomeUserNotificationPreference.SHOULD_SEND_USER_JOINED_EMAIL,
                    Boolean.class
            );
            if (!shouldSendEmail) {
                continue;
            }
            sendEmailThatNewUserJoined(homeUserEntity, joinedUser);
        }
    }

    public void sendEmailThatNewUserJoined(HomeUserEntity toUser, HomeUserEntity joinedUser) {
        // TODO: Implement properly
        /*String email = toUser.getEmail();
        var fullName = joinedUser.getFullName();
        var username = joinedUser.getUsername()
        var map = new HashMap<String, String>();
        map.put("username", username);
        map.put("full_name", fullName);
        gcsRemoteEmailSender.sendEmail(EmailType.EMAIL_BASED_USER_JOINED_NOTIFICATION, email, map);
         */
    }
}
