package com.galsie.gcs.homes.data.dto.getreceivedinvites;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.homes.data.discrete.receivedinvites.GetReceivedUserInvitesResponseErrorType;
import com.galsie.gcs.homes.data.entity.home.invites.HomeInviteEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.sun.istack.NotNull;
import lombok.*;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import static com.galsie.gcs.homes.data.dto.getreceivedinvites.SingleUserInviteResponseDTO.fromHomeInviteEntity;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class GetReceivedUserInvitesResponseDTO {

    @JsonProperty("received_invites_response_error")
    @Nullable
    GetReceivedUserInvitesResponseErrorType receivedInvitesResponseError;

    @JsonProperty("received_invite_list")
    @NotNull
    List<SingleUserInviteResponseDTO> receivedInviteList;

    public static List<SingleUserInviteResponseDTO> fromListOfHomeInviteEntity(List<HomeInviteEntity> entityList) {

        return entityList.stream()
                .map(entity -> fromHomeInviteEntity(entity))
                .collect(Collectors.toList());
    }


    public Optional<GetReceivedUserInvitesResponseErrorType> validate() {
        if (receivedInvitesResponseError == null) {
            return null;
        }

        if (receivedInvitesResponseError != null && this.getReceivedInviteList() != null) {
            throw new IllegalStateException("Only one of error or data must be present");
        }

        if (receivedInvitesResponseError == null && this.getReceivedInviteList() == null) {
            throw new IllegalStateException("Either error or data must be present");
        }

        return Optional.empty();
    }
}