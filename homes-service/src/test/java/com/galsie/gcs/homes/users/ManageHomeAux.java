package com.galsie.gcs.homes.users;

import com.galsie.gcs.homes.data.dto.leavehome.request.LeaveSingleHomeDTO;
import com.galsie.gcs.homes.data.dto.gethomeInfo.request.GetBasicHomeInfoRequestDTO;
import com.galsie.gcs.homes.data.dto.archivehome.request.ArchiveHomeSetRequestDTO;
import com.galsie.gcs.homes.data.dto.leavehome.request.LeaveHomeSetRequestDTO;
import java.util.*;

public class ManageHomeAux {

    Random random = new Random();

    private Long auxGetLongRandomNumber() {
        return (random.nextLong(100L));
    }

    private Long auxGetRandomNegNumber() {
        return random.nextLong(Long.MAX_VALUE) * -1;
    }


    public ArchiveHomeSetRequestDTO auxGetHomeArchiveRequest() {
        List<Long> homeIds = new ArrayList<>();
        homeIds.add(auxGetLongRandomNumber());
        homeIds.add(auxGetLongRandomNumber());
        return new ArchiveHomeSetRequestDTO(new HashSet<>(homeIds));
    }

    public LeaveHomeSetRequestDTO auxGetHomeLeaveRequest() {

        List<LeaveSingleHomeDTO> leaveHomes = new ArrayList<>();
        LeaveSingleHomeDTO hl = LeaveSingleHomeDTO.builder()
                .homeId(auxGetLongRandomNumber())
                .leaveEvenIfSomeOperatorsWouldntWork(true)
                .newHousemasterHomeUserIds(Collections.singletonList(auxGetLongRandomNumber()))
                .allowDeleteIfLastUser(true)
                .build();
        leaveHomes.add(hl);

        return new LeaveHomeSetRequestDTO(leaveHomes.stream().toList(), true);
    }
    public GetBasicHomeInfoRequestDTO auxGetHomeInfoRequest() {
        var homId = auxGetLongRandomNumber();
        return new GetBasicHomeInfoRequestDTO(homId);
    }

    public GetBasicHomeInfoRequestDTO auxGetInvalidHomeInfoRequest() {
        var homId = auxGetRandomNegNumber();
        return new GetBasicHomeInfoRequestDTO(homId);
    }
}
