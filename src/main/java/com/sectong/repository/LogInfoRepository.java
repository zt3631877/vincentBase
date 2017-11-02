package com.sectong.repository;

 
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import com.sectong.domain.LogInfo;
 

/**
 *  
 * 
 * @author vincent
 *
 */
@RestResource(exported = false)

public interface LogInfoRepository extends PagingAndSortingRepository<LogInfo, Long> {
  
}
