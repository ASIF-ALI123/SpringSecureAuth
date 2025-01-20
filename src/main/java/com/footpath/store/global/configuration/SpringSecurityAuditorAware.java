package com.footpath.store.global.configuration;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;

public class SpringSecurityAuditorAware implements AuditorAware<String> {

	@Override
	public Optional<String> getCurrentAuditor() {
		
		// Just return a string representing the username
		return Optional.ofNullable("Kindson").filter(s -> !s.isEmpty());
	}
	
}
