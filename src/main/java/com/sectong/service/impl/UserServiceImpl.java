package com.sectong.service.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.sectong.domain.AuthFunc;
import com.sectong.domain.Authority;
import com.sectong.domain.LogInfo;
import com.sectong.domain.Qrcode;
import com.sectong.domain.ResetPasswordForm;
import com.sectong.domain.Sms;
import com.sectong.domain.User;
import com.sectong.domain.UserCreateForm;
import com.sectong.repository.AuthFuncRepository;
import com.sectong.repository.AuthorityRepository;
import com.sectong.repository.FunctionsRepository;
import com.sectong.repository.LogInfoRepository;
import com.sectong.repository.ThirdpartyRepository;
import com.sectong.repository.UserRepository;
import com.sectong.service.UserService;
import com.sectong.util.DateStyle;
import com.sectong.util.DateUtil;

/**
 * 用户服务接口实现
 * 
 * @author vincent
 *
 */
@EnableTransactionManagement
@Service
public class UserServiceImpl implements UserService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(UserServiceImpl.class);
	private final UserRepository userRepository;
	private final AuthorityRepository authorityRepository;
	private final AuthFuncRepository authFuncRepository;
	private final ThirdpartyRepository thirdpartyRepository;
	private final LogInfoRepository logInfoRepository;
	@Autowired
	private Environment env;

	/**
	 * 装载userRepository
	 * 
	 * @param userRepository
	 */
	@Autowired
	public UserServiceImpl(LogInfoRepository logInfoRepository,ThirdpartyRepository thirdpartyRepository,
			AuthFuncRepository authFuncRepository,
			UserRepository userRepository,
			AuthorityRepository authorityRepository ) {
		this.userRepository = userRepository;
		this.authorityRepository = authorityRepository;
		this.thirdpartyRepository=thirdpartyRepository;
		this.logInfoRepository=logInfoRepository; 
		this.authFuncRepository=authFuncRepository;
	}

	/**
	 * 查找用户名
	 */
	@Override
	public User getUserByUserName(String userName) {
		LOGGER.debug("Getting user by username={}", userName);
		return userRepository.findByUsername(userName);
	}
	
	 

	/**
	 * 创建新用户
	 */
	@Transactional
	@Override
	public User create(UserCreateForm form) {
		User user = new User();
		user.setUsername(form.getUserName());
		user.setPassword(new BCryptPasswordEncoder(10).encode(form
				.getPassword()));
		user.setEnabled(1);
	//	user.setName(form.getUserName());
		Authority authority = new Authority();
		authority.setUserName(form.getUserName());
		if (form.getVcode() == null) {
			authority.setAuthority("ROLE_ADMIN");
		} else {
			authority.setAuthority("ROLE_USER");
		}
 
		authorityRepository.save(authority);

		// System.out.println(form);
		return userRepository.save(user);
	}

	/**
	 * 创建新用户
	 */
	@Override
	public String create(User user) {

		if (user.getId() != null) {

			User usertmp = userRepository.findOne(user.getId());
 
			usertmp.setName(user.getName());
 
			userRepository.save(usertmp);
			 
		}else{
			user.setPassword(new BCryptPasswordEncoder(10).encode("123456"));// 默认密码
			user.setEnabled(1);
			user.setDatetime(DateUtil.DateToString(new Date(),
					DateStyle.YYYY_MM_DD_HH_MM_SS));
			Authority authority = new Authority();
			authority.setUserName(user.getUsername());

			authority.setAuthority("ROLE_USER");
			authorityRepository.save(authority);
			user = userRepository.save(user);
		}
		
		return "";
	}

	@Override
	public Object uploadImage(MultipartFile file, HttpServletRequest request) {
		User user = getCurrentUser();
		HashMap<String, Object> ret = new HashMap<String, Object>();
		if (file != null) {
			if (!file.isEmpty()) {
				try {
					byte[] bytes = file.getBytes();

					// 当前app根目录
					String rootPath = request.getServletContext().getRealPath(
							"/");

					// 需要上传的相对地址（application.properties中获取）
					String relativePath = env
							.getProperty("image.file.upload.dir");

					// 文件夹是否存在，不存在就创建
					File dir = new File(rootPath + File.separator
							+ relativePath);
					if (!dir.exists())
						dir.mkdirs();
					String fileExtension = getFileExtension(file);

					// 生成UUID样式的文件名
					String filename = java.util.UUID.randomUUID().toString()
							+ "." + fileExtension;

					// 文件全名
					String fullFilename = dir.getAbsolutePath()
							+ File.separator + filename;

					// 用户头像被访问路径
					String relativeFile = relativePath + File.separator
							+ filename;

					// 保存图片
					File serverFile = new File(fullFilename);
					BufferedOutputStream stream = new BufferedOutputStream(
							new FileOutputStream(serverFile));
					stream.write(bytes);
					stream.close();
					LOGGER.info("Server File Location = "
							+ serverFile.getAbsolutePath());

					String serverPath = new URL(request.getScheme(),
							request.getServerName(), request.getServerPort(),
							request.getContextPath()).toString();
					ret.put("url", serverPath + "/" + relativeFile);

					user.setImage(relativeFile);
					userRepository.save(user);

				} catch (Exception e) {
					LOGGER.info("error: {}", e);
					ret.put("url", "none");
				}
			}
		}
		return null;
	}

	/**
	 * 返回文件后缀名，如果有的话
	 */
	public static String getFileExtension(MultipartFile file) {
		if (file == null) {
			return null;
		}

		String name = file.getOriginalFilename();
		int extIndex = name.lastIndexOf(".");

		if (extIndex == -1) {
			return "";
		} else {
			return name.substring(extIndex + 1);
		}
	}
	/**
	 * 获取当前登录用户权限
	 */
	@Override
	public List<AuthFunc> getCurrentUserFunc(Model model) {
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
	 
		// 登录密码，未加密的  
		System.out.println("Credentials:"  
		 + auth.getCredentials());  
		WebAuthenticationDetails details = (WebAuthenticationDetails) auth.getDetails();  
		// 获得访问地址  
		System.out.println("RemoteAddress:" + details.getRemoteAddress());  
		// 获得sessionid  
		System.out.println("SessionId:" + details.getSessionId());  
		// 获得当前用户所拥有的权限  
		List<String> authls=new ArrayList<String>();
		List<GrantedAuthority> authorities = (List<GrantedAuthority>) auth.getAuthorities();  
		for (GrantedAuthority ga:authorities){
			authls.add(ga.getAuthority());
			//System.out.println("Authority:" + ga.getAuthority());  	
		}
		
		if (authls.isEmpty()){
			return null;
		}else{
			List<AuthFunc> afls=authFuncRepository.findAllByAuthority(authls);
			//放开菜单属性
			for (AuthFunc af : afls) {
				model.addAttribute(af.getFunctionCode(), true);	 
			}
			return afls;
		}
		
	}
	/**
	 * 获取当前登录用户名称
	 */
	@Override
	public String getCurrentUserName() {
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		String name = auth.getName();
		return name;
	}

	/**
	 * 获取当前用户实例
	 */
	@Override
	public User getCurrentUser() {
		return userRepository.findByUsername(getCurrentUserName());
	}

	@Override
	public Object listAllUsers(Pageable p) {
		Page<User> users = userRepository.findAll(p);
		return users;
	}

	@Override
	public Object getUserList(int current, int rowCount, String searchPhrase) {
		HashMap<String, Object> ret = new HashMap<String, Object>();
		ArrayList<Object> pList = new ArrayList<Object>();
		Long total = userRepository.count();
		int i = 0;

		if (rowCount == -1) {
			rowCount = new Long(total).intValue();
		}

		Page<User> users = userRepository.findByUsernameContaining(
				searchPhrase, new PageRequest(current - 1, rowCount));
		for (User user : users) {
			HashMap<String, Object> data = new HashMap<String, Object>();
			 
		
			if(user.getCompanyid()!=null){
			 
			}else {
				data.put("companyName", "空");
			}
			if(user.getPointid()!=null){
			
			}else{
				data.put("refuelingPointName", "空");
			}
		
			data.put("id", user.getId());
			data.put("userName", user.getUsername());
			data.put("image", user.getImage());
			data.put("name", user.getName());
			if (user.getEnabled() == 1) {
				data.put("enabled", "<font color='green'>启用</font>");
			} else {
				data.put("enabled", "<font color='red'>禁用</font>");
			}

			List<Authority> authorities = authorityRepository
					.findByUsername(user.getUsername());
			ArrayList<String> arrayList = new ArrayList<String>();
			for (Authority authority : authorities) {
				if (authority.getAuthority().equals("ROLE_ADMIN")) {
					arrayList.add("管理员");
				} else if (authority.getAuthority().equals("ROLE_USER")) {
					arrayList.add("用户");
				}
			}
			data.put("role", arrayList.toString());
			pList.add(data);
			i++;
		}

		ret.put("current", current);
		ret.put("rowCount", i);
		ret.put("rows", pList);
		ret.put("total", total);

		return ret;
	}

	 

	@Override
	public User findById(Long id) {
		// TODO Auto-generated method stub
		return userRepository.findById(id);
	}

	@Override
	public User resetPassword(ResetPasswordForm form) {
		User user = userRepository.findByUsername(form.getMobile());
		user.setPassword(new BCryptPasswordEncoder(10).encode(form
				.getPassword()));
		return userRepository.save(user);

	}
  
	@Override
	public void saveLog(String msg) {
		LogInfo logInfo=new LogInfo();
		logInfo.setId(UUID.randomUUID().toString().replaceAll("-", ""));
		logInfo.setMsg(msg);
		logInfo.setCreateTime(DateUtil.DateToString(new Date(),
				DateStyle.YYYY_MM_DD_HH_MM_SS));
	}
	
	@Transactional
	@Override
	public void saveRole(String roleCode,String roleFunc) {
		if(!StringUtils.isEmpty(roleFunc)){
			authFuncRepository.deleteRoleBySql("ROLE_"+roleCode.toUpperCase());
			String[] rfls=roleFunc.split(",");
			for(String stemp:rfls){
				AuthFunc authFunc=new AuthFunc();
				authFunc.setId(UUID.randomUUID().toString().replaceAll("-", ""));
				authFunc.setAuthority("ROLE_"+roleCode.toUpperCase());
				authFunc.setFunctionCode("func"+stemp);
				authFuncRepository.save(authFunc);
			}
		}
		
		 
	
	}
	 
}
