package com.sectong.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import com.sectong.domain.AuthFunc;
import com.sectong.domain.ResetPasswordForm;
import com.sectong.domain.User;
import com.sectong.domain.UserCreateForm;
 

/**
 * 用户服务网接口定义
 * 
 * @author vincent
 *
 */
public interface UserService {

	User create(UserCreateForm form);
	String create(User user) ;
	User getUserByUserName(String userName);
	List<AuthFunc> getCurrentUserFunc(Model model);
	Object uploadImage(MultipartFile file, HttpServletRequest request);

	User getCurrentUser();
	User findById(Long id);
	String getCurrentUserName();
	User resetPassword(ResetPasswordForm form);
	Object listAllUsers(Pageable p);
	Object getUserList(int current, int rowCount, String searchPhrase);
	 void saveLog(String msg);
	 //保存角色权限
	 void saveRole(String roleCode,String roleFunc);
}
