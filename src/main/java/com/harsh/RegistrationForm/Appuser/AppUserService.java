package com.harsh.RegistrationForm.Appuser;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.harsh.RegistrationForm.Registration.token.ConfirmationToken;
import com.harsh.RegistrationForm.Registration.token.ConfirmationTokenService;
//this how we will find users once they have been logged in
@Service
public class AppUserService implements UserDetailsService{

	private final static String USER_NOT_FOUND="user with email %s not found";
	private final AppUserRepository appuserrepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final ConfirmationTokenService confirmationTokenService;
	@Autowired
	public AppUserService(AppUserRepository appuserrepository,
			BCryptPasswordEncoder bCryptPasswordEncoder,ConfirmationTokenService confirmationTokenService) {
		super();
		this.appuserrepository = appuserrepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
		this.confirmationTokenService=confirmationTokenService;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		return appuserrepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException(String.format(USER_NOT_FOUND,email)));
	}
	
	
	public String signUpUser(AppUser user){
		boolean present = appuserrepository.findByEmail(user.getEmail()).isPresent();
		if(present) {
			
			System.out.println(user.getEmail());
			throw new IllegalStateException("email already taken");
		}
		String encodedPass = bCryptPasswordEncoder.encode(user.getPassword());
		user.setPassword(encodedPass);
		appuserrepository.save(user);
		//TODO: send confirmation and token
		String token = UUID.randomUUID().toString();
		ConfirmationToken confirmationtoken = new ConfirmationToken(token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15),user);
		confirmationTokenService.saveConfirmation(confirmationtoken);
		//TODO send email
		
		return token;
	}

	public int enableAppUser(String email) {
		// TODO Auto-generated method stub
		return appuserrepository.enableAppUser(email);
	}
}
