package com.sectong.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.sectong.domain.AuthFunc;



/**
 * 角色权限表Repository定义
 * 
 * @author vincent
 *
 */
public interface AuthFuncRepository extends CrudRepository<AuthFunc, Long> {

 
	@Query("select u from AuthFunc u where   u.authority in (?1)") 
	List<AuthFunc> findAllByAuthority(List<String> authority);
	@Query(value="delete from AuthFunc u where u.authority = :authority")  
    @Modifying  
    public void deleteRoleBySql(String authority);  
}
