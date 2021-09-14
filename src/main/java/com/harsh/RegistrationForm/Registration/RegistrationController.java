package com.harsh.RegistrationForm.Registration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping(path="api/v1/registration")
public class RegistrationController {
	
	private RegistrationService registrationservice;
	@Autowired
	public RegistrationController(RegistrationService registrationservice) {
		this.registrationservice = registrationservice;
	}

	@PostMapping
	public String register(@RequestBody RegistrationRequest request){
		return registrationservice.register(request);
	}
			

    @GetMapping(path = "confirm")
    public String confirm(@RequestParam("token") String token) {
        return registrationservice.confirmToken(token);
    }
}
