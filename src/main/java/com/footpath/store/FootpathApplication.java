package com.footpath.store;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.client.RestTemplate;

import com.footpath.store.dao.AddressDAO;
import com.footpath.store.dao.RoleDAO;
import com.footpath.store.dao.UserActivityDAO;
import com.footpath.store.dao.UserDAO;
import com.footpath.store.global.configuration.SpringSecurityAuditorAware;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Footpath API", version = "0.01"))
@EnableJpaAuditing(auditorAwareRef="auditorAware")
public class FootpathApplication implements CommandLineRunner {

	
	@Bean
	public AuditorAware<String> auditorAware() {
		return new SpringSecurityAuditorAware();
	}
	
	
	public static void main(String[] args) {
		SpringApplication.run(FootpathApplication.class, args);
	}

	@Autowired
	private UserDAO userService;

	@Autowired
	private AddressDAO addressService;

	@Autowired
	private RoleDAO roleService;

	@Autowired
	private UserActivityDAO userActivityService;

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	


	@Override
	public void run(String... args) throws Exception {
		System.out.println("***************  -- start -- **********************");

		/*
		 * roleService.save(new RoleEntity(RoleType.USER)); roleService.save(new
		 * RoleEntity(RoleType.ADMIN)); roleService.save(new
		 * RoleEntity(RoleType.SELLER)); roleService.save(new RoleEntity(RoleType.MOD));
		 * roleService.save(new RoleEntity(RoleType.EXTERNAL));
		 * 
		 * System.out.
		 * println("***************  -- role initialization success -- **********************"
		 * );
		 * 
		 * UserEntity user1 = new UserEntity();
		 * 
		 * user1.setId(new UserId("debashish345"));
		 * user1.setEmailId("dmondal5302@gmail.com"); user1.setFName("devashish");
		 * user1.getRoles().add(roleService.findByName(RoleType.USER_ROLE.toString()));
		 * 
		 * AddressEntity add1 = new AddressEntity();
		 * 
		 * add1.setAddressLine("India, Kolkata, Birbhum, Labpur, Abadanga");
		 * add1.setUser(user1);
		 * 
		 * UserActivityEntity act = new UserActivityEntity();
		 * 
		 * act.setUser(user1); act.setActive(true); act.setDisabled(false);
		 * act.setRegDate(new Date()); act.setUserType(UserType.EMAIL_USER);
		 * 
		 * user1.setUserActivity(act); user1.getAddressEntity().add(add1);
		 * 
		 * userService.save(user1);
		 */
	}

}
