package com.sectong.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.sectong.service.UserService;

@Component
public class ScheduledStart {
	private UserService userService;
	@Autowired
	public ScheduledStart(UserService userService){
		this.userService=userService;
	}
   
	    //@Scheduled(cron="0 */1 * * * ?") 		
		@Scheduled(cron="0 0 01 * * ?")  
	    public void executeFileDownLoadTask() {
	    // userService.createStatement();
  
	    }
}
