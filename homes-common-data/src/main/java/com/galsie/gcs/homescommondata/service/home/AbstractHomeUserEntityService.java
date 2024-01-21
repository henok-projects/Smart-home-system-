package com.galsie.gcs.homescommondata.service.home;

import com.galsie.gcs.homescommondata.data.entity.home.user.AbstractHomeUserEntity;
import com.galsie.gcs.homescommondata.repository.home.user.AbstractHomeUserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AbstractHomeUserEntityService {

	@Autowired
	AbstractHomeUserEntityRepository abstractHomeUserEntityRepository;

	public Optional<AbstractHomeUserEntity> getAbstractHomeUserEntity(Long appUserId, Long homeId) {
		return abstractHomeUserEntityRepository.findByAppUserAppUserIdAndHomeHomeId(appUserId, homeId);
	}

}
