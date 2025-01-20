package com.footpath.store.service;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.footpath.store.business.bean.RoleBean;
import com.footpath.store.dao.RoleDAO;
import com.footpath.store.dao.UserDAO;
import com.footpath.store.dao.UserRepository;
import com.footpath.store.dao.VerificationTokenDAO;
import com.footpath.store.exceptions.CustomUserAuthenticationException;
import com.footpath.store.global.service.MailService;
import com.footpath.store.global.service.UtilsFunction;
import com.footpath.store.model.MailResponse;
import com.footpath.store.model.UserActivityEntity;
import com.footpath.store.model.UserEntity;
import com.footpath.store.model.VerificationTokenEntity;
import com.footpath.store.model.common.Constants;
import com.footpath.store.model.common.ResponseBOModel;
import com.footpath.store.model.enums.ResponseType;
import com.footpath.store.model.enums.RoleType;
import com.footpath.store.model.enums.UserType;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private RoleDAO roleDAO;

	@Autowired
	private VerificationTokenDAO verificationTokenDAO;

	@Autowired
	private MailService mailService;

	@Autowired
	private UtilsFunction utilsFunction;

	@Override
	public ResponseBOModel registerUser(UserEntity reqUser) {

		ResponseBOModel responseModel = new ResponseBOModel();

		UserEntity registeredUser = null;
		String token = utilsFunction.generateRandomToken();

		try {
			System.out.println("001");
			registeredUser = userDAO.findByIdUserName(reqUser.getId().getUserName());
			
			if (registeredUser == null) {
				registeredUser = userDAO.findByEmailId(reqUser.getEmailId());
				System.out.println("001...1");
			}
			System.out.println("002");

			if (registeredUser != null && (registeredUser.getId().getUserName().equals(reqUser.getId().getUserName()) || 
					registeredUser.getEmailId().equals(reqUser.getEmailId()))) {

				System.out.println("---> registered user");

				if (registeredUser.getUserActivity().isDisabled()) {

					System.err.println(registeredUser.getId().getUserName() + " disabled. Contact support team.");
					responseModel.getResponse().put("STATUS_CODE", ResponseType.REGISTERED_DISABLED);

					return responseModel;
				} else {

					if (registeredUser.getUserActivity().isActive()) {
						System.err.println(registeredUser.getId().getUserName() + " Already registered and activated.");
						responseModel.getResponse().put("STATUS_CODE", ResponseType.REGISTERED_ACTIVATED);

						return responseModel;
					} else {
						System.err
								.println(registeredUser.getId().getUserName() + " Already registered. NOT ACTIVATED.");
						responseModel.getResponse().put("STATUS_CODE", ResponseType.REGISTERED_NOT_ACTIVATED);

						return responseModel;
					}
				}
			} else {

				if (reqUser.getId().getUserName().length() > 3) {

					System.out.println("---> new user");

					reqUser.getRoles().add(roleDAO.findByName(RoleType.USER));

					System.out.println("---> role added");

					
					//if(!reqUser.getUserActivity().getUserType().toString().equals(UserType.EMAIL_USER)) {
						
						reqUser.setUserActivity(new UserActivityEntity());
						reqUser.getUserActivity().setActive(true);
						reqUser.getUserActivity().setDisabled(false);
						reqUser.getUserActivity().setRegDate(new Date());
						reqUser.getUserActivity().setUserType(UserType.EMAIL_USER);
						System.out.println("---> activity added");

						reqUser.getAddressEntity().get(0).setUser(reqUser);

						System.out.println("---> address added");

						//reqUser.setVerificationToken(new VerificationTokenEntity());
						//reqUser.getVerificationToken().setToken(token);

						System.out.println("---> token added");

						reqUser.setPassword(UtilsFunction.passwordEncoder().encode(reqUser.getPassword()));
						
				//	}
					UserEntity savedUser = userDAO.save(reqUser);

					System.out.println("---> user added");

					registeredUser = userDAO.findByIdUserName(savedUser.getId().getUserName());

					if (registeredUser != null) {
						System.out.println("004");

						Map<String, Object> mailModel = new HashMap<>();
						mailModel.put("to", registeredUser.getEmailId());
						mailModel.put("userName", registeredUser.getId().getUserName());
						mailModel.put("activationUrl", new Constants().VERIFICATION_LINK_BASE_URL + token);

						MailResponse mailResponse = new MailResponse(registeredUser.getEmailId(), "Verfiy Account",
								registeredUser.getId().getUserName());
						System.out.println("---mail progress---");
						//mailService.sendActivationMail(mailResponse, mailModel); // async mail sent
					} else {
						System.out.println("005");
						throw new CustomUserAuthenticationException("INVALID REGISTRATION !!! SYSTEM FAULT !!!");
					}
				}
			}

		} catch (CustomUserAuthenticationException e) {
			System.err.println(
					"--------- USER REGISTRATION EXCEPTION --> UserServiceImpl --> registerUser() -------------"
							+ e.getMessage());
		}

		responseModel.getResponse().clear();
		responseModel.getResponse().put("userBean", UtilsFunction.getUserEntityToBean(registeredUser));
		responseModel.getResponse().put("userAddressBean",
				UtilsFunction.getAddressEntityToBean(registeredUser.getAddressEntity().get(0)));

		Set<RoleBean> userRoleSet = new HashSet<>();
		registeredUser.getRoles().forEach(role -> {
			userRoleSet.add(UtilsFunction.getRoleEntityToBean(role));
		});

		responseModel.getResponse().put("userRoleBean", userRoleSet);
		responseModel.getResponse().put("STATUS_CODE", ResponseType.REGISTERED_NOT_ACTIVATED);

		return responseModel; // registration success
	}

	@Override
	public boolean sendActivationToken(UserEntity userEntity) {

		boolean status = false;
		String token = utilsFunction.generateRandomToken();

		try {
			if (userRepository.isUserExists(userEntity.getId().getUserName()) != null) {

				VerificationTokenEntity newToken = userEntity.getVerificationToken();
				newToken.setToken(token);
				userRepository.getUserDAO().save(userEntity);

				Map<String, Object> mailModel = new HashMap<>();
				mailModel.put("to", userEntity.getEmailId());
				mailModel.put("userName", userEntity.getId().getUserName());
				mailModel.put("activationUrl", new Constants().VERIFICATION_LINK_BASE_URL + token);// need a url to
																									// append prefix for
																									// token

				MailResponse mailResponse = new MailResponse(userEntity.getEmailId(), "Verfiy Account",
						userEntity.getId().getUserName());
				System.out.println("---mail progress---");
				mailService.sendActivationMail(mailResponse, mailModel); // async mail sent
				status = true;
			} else {
				throw new Exception("mail not sent");
			}
		} catch (Exception e) {
			System.err.println(e.getMessage() + "---- UserServiceImpl --> sendActivationToken-----");
		}
		return status;
	}

	@Override
	public UserEntity isUserExists(String userName) {
		return userRepository.isUserExists(userName);
	}

	@Override
	public UserEntity isUserExistByEmail(String mailId) {
		return userRepository.isUserExistByEmail(mailId);
	}

	@Override
	public ResponseBOModel confirmAndActivateUserId(String token) {

		ResponseBOModel responseModel = new ResponseBOModel();

		UserEntity registeredUser = null;
		try {

			VerificationTokenEntity tokenEntity = userRepository.getVerificationTokenDAO().findByToken(token);

			if (tokenEntity != null)
				registeredUser = userRepository.getUserDAO().findByVerificationToken(tokenEntity);
			if (registeredUser != null) {

				Integer tokenId = registeredUser.getVerificationToken().getId();

				registeredUser.getUserActivity().setActive(true);
				registeredUser.setVerificationToken(null);
				userRepository.getVerificationTokenDAO().deleteById(tokenId);
				userRepository.getUserDAO().save(registeredUser);
			} else {
				responseModel.getResponse().put("STATUS_CODE_1", ResponseType.VERIFICATION_TOKEN_EXPIRED);
				responseModel.getResponse().put("STATUS_CODE_2", ResponseType.VERIFICATION_TOKEN_INVALID);

				return responseModel;
			}
		} catch (Exception e) {

			System.err.println("---- UserServiceImpl ---- confirmAndActivateUserId ---");
		}

		responseModel.getResponse().clear();
		responseModel.getResponse().put("userBean", UtilsFunction.getUserEntityToBean(registeredUser));
		responseModel.getResponse().put("userAddressBean",
				UtilsFunction.getAddressEntityToBean(registeredUser.getAddressEntity().get(0)));

		Set<RoleBean> userRoleSet = new HashSet<>();
		registeredUser.getRoles().forEach(role -> {
			userRoleSet.add(UtilsFunction.getRoleEntityToBean(role));
		});

		responseModel.getResponse().put("userRoleBean", userRoleSet);
		responseModel.getResponse().put("STATUS_CODE", ResponseType.REGISTERED_ACTIVATED);

		return responseModel;
	}

}
