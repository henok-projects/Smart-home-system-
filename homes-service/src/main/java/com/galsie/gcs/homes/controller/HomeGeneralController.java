package com.galsie.gcs.homes.controller;


import com.galsie.gcs.homes.data.dto.archivehome.request.ArchiveHomeSetRequestDTO;
import com.galsie.gcs.homes.data.dto.gethomeInfo.request.GetBasicHomeInfoRequestDTO;
import com.galsie.gcs.homes.data.dto.leavehome.request.LeaveHomeSetRequestDTO;
import com.galsie.gcs.homes.service.home.HomeService;
import com.galsie.gcs.homes.service.users.UserHomeManagementService;
import com.galsie.gcs.microservicecommon.lib.galsecurity.request.AuthenticatedGCSRequest;
import com.galsie.gcs.microservicecommon.lib.galsecurity.session.GalSecurityAuthSessionType;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.data.dto.GCSResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/home")
public class HomeGeneralController {

    @Autowired
    HomeService homeService;

    @Autowired
    UserHomeManagementService userHomeManagementService;

    @PostMapping("/general/getBasicHomeInfo")
    @AuthenticatedGCSRequest(authSessionTypes = GalSecurityAuthSessionType.USER)
    public ResponseEntity<? extends GCSResponseDTO<?>> getBasicHomeInfo(@RequestBody GetBasicHomeInfoRequestDTO getBasicHomeInfoRequestDto) {
        return homeService.getBasicHomeInfo(getBasicHomeInfoRequestDto).toResponseEntity();
    }

    @PostMapping("/general/archive")
    @AuthenticatedGCSRequest(authSessionTypes = GalSecurityAuthSessionType.USER)
    public ResponseEntity<? extends GCSResponseDTO<?>> archiveHome(@RequestBody ArchiveHomeSetRequestDTO archiveHomeRequestDTO) {
        return userHomeManagementService.archiveHome(archiveHomeRequestDTO).toResponseEntity();
    }


    @PostMapping("/general/leave")
    @AuthenticatedGCSRequest(authSessionTypes = GalSecurityAuthSessionType.USER)
    public ResponseEntity<? extends GCSResponseDTO<?>> leaveHome(@RequestBody LeaveHomeSetRequestDTO homeLeaveRequestDto) {
        return userHomeManagementService.leaveHome(homeLeaveRequestDto).toResponseEntity();
    }
}
