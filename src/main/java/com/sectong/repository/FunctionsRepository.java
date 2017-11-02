package com.sectong.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import com.sectong.domain.Functions;


/**
 * 新闻表Repository定义
 * 
 * @author vincent
 *
 */
public interface FunctionsRepository extends PagingAndSortingRepository<Functions, Long> {

	Page<Functions> findByIdGreaterThan(Long startid, Pageable p);

}
