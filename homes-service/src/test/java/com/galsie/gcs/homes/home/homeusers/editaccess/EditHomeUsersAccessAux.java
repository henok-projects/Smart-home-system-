package com.galsie.gcs.homes.home.homeusers.editaccess;

import com.galsie.gcs.homes.data.dto.common.UserHomeAccessInfoDTO;
import com.galsie.gcs.homes.data.dto.homeuseraccess.editaccess.request.EditHomeUsersAccessInfoRequestDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class EditHomeUsersAccessAux {

    Random random = new Random();
    private static String auxGetGetRandomDate() {
        long randomDays = ThreadLocalRandom.current().nextLong(365 * 24);
        LocalDateTime baseDate = LocalDateTime.of(2000, 1, 1, 0, 0, 0);
        LocalDateTime randomDateTime = baseDate.plusDays(randomDays)
                .plusHours(ThreadLocalRandom.current().nextInt(24))
                .plusMinutes(ThreadLocalRandom.current().nextInt(60))
                .plusSeconds(ThreadLocalRandom.current().nextInt(60));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        return randomDateTime.format(formatter);
    }

    private double auxGetRandomNumber() {
        int numIterations = 10;
        int randomNumber = 0;
        for (int i = 0; i < numIterations; i++) {
            randomNumber = random.nextInt(100);
        }
        return randomNumber;
    }


    public EditHomeUsersAccessInfoRequestDTO auxGetValidEditHomeUsersAccessInfoRequestDTO(){
        return new EditHomeUsersAccessInfoRequestDTO((long)auxGetRandomNumber(), Collections.singletonList((long) auxGetRandomNumber()),new UserHomeAccessInfoDTO(auxGetGetRandomDate(),auxGetGetRandomDate(), null, null));
    }

    public EditHomeUsersAccessInfoRequestDTO auxGetInvalidEditHomeUsersAccessInfoRequestDTO(){
        return new EditHomeUsersAccessInfoRequestDTO((long)auxGetRandomNumber(), Collections.singletonList((long) auxGetRandomNumber()),new UserHomeAccessInfoDTO(String.valueOf(auxGetRandomNumber()),String.valueOf(auxGetRandomNumber()), null, null));
    }

}
