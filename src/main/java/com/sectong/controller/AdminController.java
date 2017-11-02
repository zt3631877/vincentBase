package com.sectong.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import antlr.StringUtils;

import com.sectong.domain.AuthFunc;
import com.sectong.domain.Authority;
import com.sectong.domain.News;
import com.sectong.domain.UpdatePasswordForm;
import com.sectong.domain.User;
import com.sectong.repository.AuthFuncRepository;
import com.sectong.repository.AuthorityRepository;
import com.sectong.repository.FunctionsRepository;
import com.sectong.repository.NewsRepository;
import com.sectong.repository.ThirdpartyRepository;
import com.sectong.repository.UserRepository;
import com.sectong.service.AppPushService;
import com.sectong.service.SendSMSService;
import com.sectong.service.UserService;
import com.sectong.util.DateStyle;
import com.sectong.util.DateUtil;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.io.ResourceLoader;

@Controller
public class AdminController {
	private UserRepository userRepository;
	private NewsRepository newsRepository;
	private UserService userService;
	private SendSMSService sendSMSService;
	private ThirdpartyRepository thirdpartyRepository;
	private AppPushService appPushService;
	private AuthFuncRepository authFuncRepository;
	private AuthorityRepository authorityRepository;
  
	@Autowired
	protected AuthenticationManager authenticationManager;

	@Autowired
	public AdminController(AuthorityRepository authorityRepository,
			ResourceLoader resourceLoader, NewsRepository newsRepository,
			UserRepository userRepository, UserService userService,
			SendSMSService sendSMSService,
			ThirdpartyRepository thirdpartyRepository,
			AuthFuncRepository authFuncRepository,
			AppPushService appPushService) {
		this.userRepository = userRepository;
		this.newsRepository = newsRepository;
		this.userService = userService;
		this.sendSMSService = sendSMSService;
		this.thirdpartyRepository = thirdpartyRepository;
		this.appPushService = appPushService;
		this.authorityRepository = authorityRepository;
		this.authFuncRepository=authFuncRepository;
	}

	/**
	 * 管理主界面
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/index")
	public String getIndex(Model model) {
		//获取登录用户的权限列表,并对菜单sidebar赋值
		List<AuthFunc> afls= userService.getCurrentUserFunc(model);
		model.addAttribute("dashboard", true);
		model.addAttribute("userscount", userRepository.count());
		model.addAttribute("newscount", newsRepository.count());
		if(afls.isEmpty()){
			return "redirect:/logout";
		}else{
			return "admin/index";
		}
	
	}

	/**
	 * 管理主界面
	 * 
	 * @param model
	 * @return
	 */
//	@GetMapping("/admin/")
//	public String adminIndex(Model model) {
//		userService.getCurrentUserFunc(model);
//		model.addAttribute("dashboard", true);
//		model.addAttribute("userscount", userRepository.count());
//		model.addAttribute("newscount", newsRepository.count());
//		return "admin/index";
//	}

	/**
	 * 新闻管理
	 * 
	 * @param model
	 * @return
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/admin/news")
	public String adminNews(Model model) {
		model.addAttribute("news", true);
		userService.getCurrentUserFunc(model);
		Iterable<News> newslist = newsRepository.findAll();
		model.addAttribute("newslist", newslist);
		return "admin/news";
	}

	/**
	 * 新闻增加表单
	 * 
	 * @param model
	 * @return
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/admin/news/add")
	public String newsAdd(Model model) {
		userService.getCurrentUserFunc(model);
		model.addAttribute("news", true);
		model.addAttribute("newsAdd", new News());
		return "admin/newsAdd";
	}

	/**
	 * 用户增加表单
	 * 
	 * @param model
	 * @return
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/admin/user/add")
	public String userAdd(Model model) {
		model.addAttribute("userAdd", new User());
		userService.getCurrentUserFunc(model);
		model.addAttribute("news", true);
		return "admin/userAdd";
	}

	/**
	 * 新闻修改表单
	 * 
	 * @param model
	 * @return
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/admin/news/edit")
	public String newsEdit(Model model, @RequestParam Long id) {
		userService.getCurrentUserFunc(model);
		model.addAttribute("news", true);
		model.addAttribute("newsEdit", newsRepository.findOne(id));
		return "admin/newsEdit";
	}

	/**
	 * 用户修改表单
	 * 
	 * @param model
	 * @return
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/admin/user/edit")
	public String userEdit(Model model, @RequestParam Long id) {
		model.addAttribute("userEdit", userRepository.findOne(id));
	 
		return "admin/userEdit";
	}

	/**
	 * 新闻修改提交操作
	 * 
	 * @param news
	 * @return
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/admin/news/edit")
	public String newsSubmit(@RequestParam("img") MultipartFile file,
			Model model, HttpServletRequest request) {
		String id = request.getParameter("id");
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		if (!file.isEmpty()) {
			try {
				BufferedOutputStream out = new BufferedOutputStream(
						new FileOutputStream(Paths.get("upload-dir").toString()
								+ File.separator
								+ new File(file.getOriginalFilename())));
				out.write(file.getBytes());
				out.flush();
				out.close();
				News news=null;
				if(org.apache.commons.lang.StringUtils.isEmpty(id)){
					news = new News();
				}else{
					 news=newsRepository.findOne(Long.parseLong(id));
				}

				news.setTitle(title);
				news.setContent(content);
				news.setImg(file.getOriginalFilename());
				news.setDatetime(DateUtil.DateToString(new Date(),
						DateStyle.YYYY_MM_DD_HH_MM_SS));
				User user = userService.getCurrentUser();
				news.setUser(user);
				newsRepository.save(news);
				userService.saveLog(user.getUsername() + "修改新闻");
			} catch (IOException | RuntimeException e) {
				e.printStackTrace();

			}
		} else {

		}

		return "redirect:/admin/news";
	}

	/**
	 * 用户修改提交操作
	 * 
	 * @param news
	 * @return
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/admin/user/edit")
	public String userSubmit(Model model, @ModelAttribute User user) {
		userService.saveLog(userService.getCurrentUser().getUsername() + "修改用户"
				+ user.getUsername());
		String str = userService.create(user);
		if (str.equals("")) {// 正常
			return "redirect:/admin/user";
		} else {
			model.addAttribute("errorinfo", str);
			return "/error";
		}

	}
	/**
	 * 用户管理
	 * 
	 * @param model
	 * @return
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/admin/user")
	public String adminUser(Model model) {
		userService.getCurrentUserFunc(model);
		model.addAttribute("user", true);
		return "admin/user";
	}
	/**
	 * 权限管理
	 * 
	 * @param model
	 * @return
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/admin/role")
	public String adminRole(Model model) {
		List<String> authorityls=new ArrayList<String>();
		authorityls.add("ROLE_ADMIN");
		authorityls.add("ROLE_STATIONMASTER");
		authorityls.add("ROLE_EXPERTS");
		authorityls.add("ROLE_USER");
		List<String> roleAdmin=new ArrayList<String>();
		List<String> roleStationmaster=new ArrayList<String>();
		List<String> roleExperts=new ArrayList<String>();
		List<String> roleUser=new ArrayList<String>();
		List<AuthFunc> afls=authFuncRepository.findAllByAuthority(authorityls);
		//放开菜单属性
		for (AuthFunc af : afls) {
			switch (af.getAuthority()) {  
	        case "ROLE_ADMIN":  
	        	roleAdmin.add(af.getFunctionCode().substring(4));
	            break;  
	        case "ROLE_STATIONMASTER":  
	        	roleStationmaster.add(af.getFunctionCode().substring(4));
	            break;  
	        case "ROLE_EXPERTS":  
	        	roleExperts.add(af.getFunctionCode().substring(4));
	            break;  
	        case "ROLE_USER":  
	        	roleUser.add(af.getFunctionCode().substring(4));
	            break;  
	        }  
		}
		model.addAttribute("roleAdmin", roleAdmin);
		model.addAttribute("roleStationmaster", roleStationmaster);
		model.addAttribute("roleExperts", roleExperts);
		model.addAttribute("roleUser", roleUser);
		userService.getCurrentUserFunc(model);
		model.addAttribute("role", true);
		return "admin/role";
	}
	/**
	 * 提交角色权限信息
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/admin/roleSave")
	public String roleSave(Model model,@RequestParam String roleCode,@RequestParam(value = "roleAdmin", required = false) String roleAdmin ,@RequestParam(value = "roleStationmaster", required = false) String roleStationmaster,@RequestParam(value = "roleExperts", required = false) String roleExperts,@RequestParam(value = "roleUser", required = false) String roleUser) {	 
		 System.out.print(true);
		switch (roleCode) {  
	        case "admin":  
	        	userService.saveRole(roleCode,roleAdmin);
	            break;  
	        case "stationmaster":  
	        	userService.saveRole(roleCode,roleStationmaster);
	            break;  
	        case "experts":  
	        	userService.saveRole(roleCode,roleExperts);
	            break;  
	        case "user":  
	        	userService.saveRole(roleCode,roleUser);
	            break;  
	        }  
		return "redirect:/admin/role?ok#"+roleCode;// 账号正常
//		if (true) { 
//			return "redirect:/admin/role?ok#"+roleCode;// 账号正常
//		} else {
//			return "redirect:/admin/role?error#"+roleCode;// 状态不正常
//		}

	}
	/**
	 * 新闻删除操作
	 * 
	 * @param model
	 * @param id
	 * @return
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/admin/news/del")
	public String delNews(Model model, @RequestParam Long id) {
		newsRepository.delete(id);
		userService.saveLog(userService.getCurrentUser().getUsername() + "删除新闻"
				+ id);
		return "redirect:/admin/news";

	}

	/**
	 * 后台配置管理
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/admin/configuration")
	public String configuration(Model model) {
		return "admin/configuration";
	}

	/**
	 * 第三方配置管理
	 * 
	 * @param model
	 * @return
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/admin/thirdparty")
	public String thirdparty(Model model) {
		userService.getCurrentUserFunc(model);
		model.addAttribute("thirdparty", true);
		String smsUsername = null, smsPassword = null, pushAppID = null, pushAppKey = null, pushMasterSecret = null;
		try {
			smsUsername = thirdpartyRepository.findOne("smsUsername")
					.getValue();
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			smsPassword = thirdpartyRepository.findOne("smsPassword")
					.getValue();
		} catch (Exception e) {
			// TODO: handle exception
		}

		try {
			pushAppID = thirdpartyRepository.findOne("pushAppID").getValue();
		} catch (Exception e) {
			// TODO: handle exception
		}

		try {
			pushAppKey = thirdpartyRepository.findOne("pushAppKey").getValue();
		} catch (Exception e) {
			// TODO: handle exception
		}

		try {
			pushMasterSecret = thirdpartyRepository.findOne("pushMasterSecret")
					.getValue();
		} catch (Exception e) {
			// TODO: handle exception
		}

		if (!sendSMSService.checkSmsAccountStatus(smsUsername, smsPassword)) {
			model.addAttribute("error", true);// 状态不正常
		}
		if (smsUsername == null && smsPassword == null) {
			model.addAttribute("init", true);// 第一次初始化
			model.addAttribute("error", false);
		}

		if (pushAppID == null && pushAppKey == null && pushMasterSecret == null) {
			model.addAttribute("initpush", true);// 第一次初始化
		}
		model.addAttribute("smsUsername", smsUsername);
		model.addAttribute("smsPassword", smsPassword);
		model.addAttribute("pushAppID", pushAppID);
		model.addAttribute("pushAppKey", pushAppKey);
		model.addAttribute("pushMasterSecret", pushMasterSecret);
		return "admin/thirdparty";
	}

	/**
	 * 提交短信平台账号信息
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/admin/thirdpartysms")
	public String thirdpartysms(@RequestParam String username,
			@RequestParam String password) {
		sendSMSService.saveSmsConfig(username, password);
		if (sendSMSService.checkSmsAccountStatus(username, password)) {
			return "redirect:/admin/thirdparty?ok";// 账号正常
		} else {
			return "redirect:/admin/thirdparty?error";// 状态不正常
		}

	}

	/**
	 * 提交推送平台信息
	 * 
	 * @param appID
	 * @param appKey
	 * @param masterSecret
	 * @return
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/admin/thirdpartypush")
	public String thirdPartyPush(@RequestParam String pushAppID,
			@RequestParam String pushAppKey,
			@RequestParam String pushMasterSecret) {
		appPushService.savePushConfig(pushAppID, pushAppKey, pushMasterSecret);

		return "redirect:/admin/thirdparty?pok#push";
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/admin/thirdpartypushtest")
	public String thirdPartyPushTest(@RequestParam String title,
			@RequestParam String text, @RequestParam String openUrl)
			throws IOException {
		if (appPushService.sendPushMsg(title, text, openUrl)) {
			return "redirect:/admin/thirdparty?testok#push";
		} else {
			return "redirect:/admin/thirdparty?testerror#push";
		}

	}

	/**
	 * 用户删除操作
	 * 
	 * @param model
	 * @param id
	 * @return
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/admin/userdel")
	public String delUser(Model model, @RequestParam Long id) {
		User user = userRepository.findOne(id);
		if (user.getEnabled() == 0) {
			user.setEnabled(1);
		} else {
			user.setEnabled(0);
		}
		userService.saveLog(userService.getCurrentUser().getUsername() + "删除用户"
				+ id);
		userRepository.save(user);
		return "redirect:/admin/user";

	}

	/**
	 * 获取二维码接口
	 * 
	 * @return
	 */
	@GetMapping("/qrcode/toQrcodePage")
	public String toQrcodePage(Model model, @RequestParam String key) {
		model.addAttribute("code", key);
		return "qrcode";

	}

	/**
	 * 修改密码
	 * 
	 * @param news
	 * @return
	 */
	@PostMapping("/updatePassword")
	public String updatePassword(@ModelAttribute UpdatePasswordForm form) {
		User user = userService.getUserByUserName(userService
				.getCurrentUserName());
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
				user.getUsername(), form.getOldPassword());
		try {
			Authentication authentication = authenticationManager
					.authenticate(token);

			SecurityContextHolder.getContext()
					.setAuthentication(authentication);
			if (authentication.isAuthenticated() && user.getEnabled() == 1) {
				user.setPassword(new BCryptPasswordEncoder(10).encode(form
						.getPassword()));
				userRepository.save(user);
			}
		} catch (Exception e) {

		}
		userService
				.saveLog(userService.getCurrentUser().getUsername() + "修改密码");
		return "redirect:/logout";
	}

	/**
	 * 修改密码页面跳转
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/updatePassword")
	public String updatePassword(Model model) {
		userService.getCurrentUserFunc(model);
		// model.addAttribute("updatePasswordForm", new UpdatePasswordForm());
		return "admin/profile";
	}

}
