package com.vu.service;

import java.util.List;

import com.vu.dto.UserDto;

public interface UserService {

	List<UserDto> getUsers();

	UserDto getUserById(Long id);

	List<UserDto> getUsersByFullName(String fullname);

	void createUser(UserDto userDto);

	void updateUser(UserDto userDto, Long id);

	void deleteUser(Long userId);
}
