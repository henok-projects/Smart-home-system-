package com.galsie.gcs.users;

import com.galsie.gcs.microservicecommon.lib.galsecurity.session.codabletoken.jwt.DecodableToken;
import com.galsie.gcs.users.data.dto.login.request.GalsieEmailLoginRequestDTO;
import com.galsie.gcs.users.data.dto.registration.request.register.RegisterNewGalsieAccountRequestDTO;
import com.galsie.gcs.users.repository.UserAccountRepository;
import com.galsie.gcs.users.service.authentication.LocalUserAuthenticatorService;
import com.galsie.gcs.users.service.register.UserRegistrationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;

@SpringBootTest
class UsersApplicationTests {
	/*
	@Autowired
	UserRegistrationService userService;

	@Autowired
	LocalUserAuthenticatorService authenticationService;

	@Autowired
	UserAccountRepository userAccountRepository;
	@Test
	void contextLoads() {
	}

	RegisterNewGalsieAccountRequestDTO getRandomUser(){
		String username = String.valueOf((int)(Math.random()*100000));
		return new RegisterNewGalsieAccountRequestDTO(username, new GalUserEmailVerificationInfoDTO(username + "@galsie.com"), "test123");
	}
	@Test
	void tokenGenerationWorks(){
		var data = new HashMap<String, Object>();
		data.put("test", "some_item");
		data.put("test2", "some_item_2");
		String token = DecodableToken.encodeData(data);
		System.out.println(token);
		var claims = DecodableToken.decodeToken(token);
		for (String key: data.keySet()) {
			var value = data.get(key);
			var claimValue = claims.get(key, String.class);
			assert value.equals(claimValue);
		}
	}

	@Test
	void userAccountRegistrationWorks(){
		var response = userService.registerNewGalUserAccount(getRandomUser());

		assert !response.hasError();

	}

	@Test
	void userAccountRegistrationHasUserInfo(){
		var dto = getRandomUser();
		var response = userService.registerNewGalUserAccount(dto);
		assert !response.hasError();
		var userEntity = userAccountRepository.findByUsername(dto.getUsername());
		assert userEntity.isPresent();
		assert userEntity.get().getUserInfo().getUser().getUsername().equals(dto.getUsername());

	}
	@Test
	void userAccountRegistrationFailsDuplicateUsername(){
		var user = getRandomUser();
		var user2 = getRandomUser();
		user2.setUsername(user.getUsername());
		userService.registerNewGalUserAccount(user);
		var response = userService.registerNewGalUserAccount(user2);
		assert response.hasError();
	}



	@Test
	void userLoginEmailWorks(){
		var user = getRandomUser();
		assert  !userService.registerNewGalUserAccount(user).hasError();
		var response = authenticationService.authenticateUser(new GalsieEmailLoginRequestDTO(user.getHashedPwd(), ((GalUserEmailVerificationInfoDTO)user.getVerificationDTO()).getEmail()));
		assert !response.hasError();
	}

	 */
	/*@Test
	void userLoginPhoneWorks(){
		var user = getRandomUser();
		var response = userService.userLogin(new GalsiePhoneLoginRequestDTO((short) 961, 71842007));
		assert !response.hasError();
	}*/

}
