package com.galsie.gcs.homes.home.invites;


import com.galsie.gcs.homes.data.dto.invites.request.qr.QRCodeInviteRequestDTO;
import com.galsie.gcs.homes.data.dto.invites.request.user.HomeDirectUserInviteByCommon;
import com.galsie.gcs.homes.data.dto.invites.request.user.HomeDirectUserInviteByEmailOrPhoneDTO;
import com.galsie.gcs.homes.data.dto.invites.request.user.HomeDirectUserInviteByUsernameDTO;
import com.galsie.gcs.homes.data.dto.invites.request.user.HomeDirectUserSetInviteRequestDTO;
import org.springframework.stereotype.Component;

import java.util.*;
@Component
public class HomeUserInviteAux {

    Random random = new Random();

    private String auxGetValidRandomName(){
        return (random.nextInt(26) + "abcd");
    }

    private String auxGetValidRandomEmail() {
        String name = auxGetValidRandomName();
        int randomNumber = random.nextInt(10000);
        String domain = "gmail.com";

        return name + randomNumber + "@" + domain;
    }
    private Long auxGetRandomNumber(){
        int numIterations = 10;
        long randomNumber = 0;
        for (int i = 0; i < numIterations; i++) {
            randomNumber = random.nextLong(100);
        }
        return  randomNumber;
    }

    public HomeDirectUserSetInviteRequestDTO auxGetValidInviteUserByEmailOrPhoneRequestDTO() {

        var inviteByEmailOrPhone = new HomeDirectUserInviteByEmailOrPhoneDTO();
        inviteByEmailOrPhone.setEmail(auxGetValidRandomEmail());

        List<HomeDirectUserInviteByCommon> inviteList = new ArrayList<>();
        inviteList.add(inviteByEmailOrPhone);
        var requestDTO = new HomeDirectUserSetInviteRequestDTO();
        requestDTO.setInviteList(inviteList);
        requestDTO.setHomeId(auxGetRandomNumber());
        return requestDTO;
    }

    public HomeDirectUserSetInviteRequestDTO auxGetValidInviteUserByUsernameRequestDTO() {

        var inviteByUsername = new HomeDirectUserInviteByUsernameDTO();
        inviteByUsername.setUsername(auxGetValidRandomName());

        List<HomeDirectUserInviteByCommon> inviteList = new ArrayList<>();
        inviteList.add(inviteByUsername);
        var requestDTO = new HomeDirectUserSetInviteRequestDTO();
        requestDTO.setInviteList(inviteList);
        requestDTO.setHomeId(auxGetRandomNumber());
        return requestDTO;
    }

    public QRCodeInviteRequestDTO auxGetInviteUserByQRCodeRequestDTO() {
        return new QRCodeInviteRequestDTO();
    }


}