package com.vu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.vu.constant.ParameterConstant;
import com.vu.constant.QueryConstant;
import com.vu.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	User findByEmail(String email);

	@Query(nativeQuery = true, value = QueryConstant.FIND_ALL_CUSTOMERS_WITH_FULLNAME)
	List<User> findByFullName(@Param(ParameterConstant.FULLNAME_PARAMETER) String fullname);
	
}