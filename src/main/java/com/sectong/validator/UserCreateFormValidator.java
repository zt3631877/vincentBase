package com.sectong.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.sectong.domain.UserCreateForm;
import com.sectong.service.UserService;

/**
 * 验证字段实现
 * 
 * @author jiekechoo
 *
 */
@Component
public class UserCreateFormValidator implements Validator {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserCreateFormValidator.class);
	private final UserService userService;

	@Autowired
	public UserCreateFormValidator(UserService userService) {
		this.userService = userService;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return clazz.equals(UserCreateForm.class);
	}

	@Override
	public void validate(Object target, Errors errors) {
		LOGGER.debug("Validating {}", target);
		UserCreateForm form = (UserCreateForm) target;
		// validatePasswords(errors, form);
		validateUsername(errors, form);
	}

	private void validateUsername(Errors errors, UserCreateForm form) {
		if (userService.getUserByUserName(form.getUserName()) != null) {
			errors.reject("username.exists", "User with this USERNAME already exists");
		}
	}
}
