package com.sectong.repository;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import com.sectong.domain.User;

/**
 * 用户User CrudRepository定义
 * 
 * @author vincent
 *
 */
@RestResource(exported = false) // 禁止暴露REST

public interface UserRepository extends CrudRepository<User, Long> {

	Collection<User> findAll();

	User findByUsername(String userName);
	User findById(Long id);
	Page<User> findAll(Pageable p);
	@Query("select count(u) from User u where  u.enabled =1 and companyid is null and pointid is null") 
	Long countNoCompanyNoPoint();
	@Query("select u from User u where  u.enabled =1 and u.companyid is null and u.pointid is null   ") 
	Page<User> findByUsernameNoCompanyNoPoint(Pageable p);
	Page<User> findByUsernameContaining(String searchPhrase, Pageable p);

}
