package com.galsie.gcs.homescommondata.service.user;

import com.galsie.gcs.homescommondata.data.entity.user.AppUserRemoteEntity;
import com.galsie.gcs.homescommondata.repository.appuser.AppUserRemoteEntityRepository;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.exception.GCSResponseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class AppUserRemoteEntityService {

	@Autowired
	private AppUserRemoteEntityRepository appUserRemoteRepo;

	// Whomever is calling this method, must be sure that the appUserId exists in the users-service
	@Transactional
	public AppUserRemoteEntity gcsInternalGetOrCreateAppUserEntity(Long appUserId) throws GCSResponseException  {
		var appUserRemoteEntityOpt = appUserRemoteRepo.findById(appUserId);
		if (appUserRemoteEntityOpt.isPresent()){
			return appUserRemoteEntityOpt.get();
		}
		// create, save and return the new entity
		var newRemoteAppUser = AppUserRemoteEntity.builder().appUserId(appUserId).build();
		GCSResponse.saveEntityThrows(appUserRemoteRepo, newRemoteAppUser);
		return newRemoteAppUser;
	}

}
