package com.vu.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;

import com.vu.constant.MessageConstant;
import com.vu.constant.PropertyConstant;
import com.vu.dto.UserDto;
import com.vu.entity.Role;
import com.vu.entity.User;
import com.vu.repository.RoleRepository;
import com.vu.repository.UserRepository;
import com.vu.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public List<UserDto> getUsers() {
		List<User> users = userRepository.findAll();
		return users.stream().map(user -> modelMapper.map(user, UserDto.class)).collect(Collectors.toList());
	}

	@Override
	public UserDto getUserById(Long id) {
		User user = userRepository.findById(id).orElse(null);
		return modelMapper.map(user, UserDto.class);
	}

	@Override
	public List<UserDto> getUsersByFullName(String fullname) {
		List<User> users = userRepository.findByFullName(fullname);
		return users.stream().map(user -> modelMapper.map(user, UserDto.class)).collect(Collectors.toList());
	}

	@Override
	public void createUser(UserDto userDto) {
		User user = userRepository.findByEmail(userDto.getEmail());
		Role role = roleRepository.getById(Long.parseLong(PropertyConstant.USER_ROLE_ID));
		if (Optional.ofNullable(user).isEmpty()) {
			user = modelMapper.map(userDto, User.class);
			user.setRole(role);
			String hashedPassword = BCrypt.hashpw(userDto.getPassword(), BCrypt.gensalt(10));
			user.setPassword(hashedPassword);
			userRepository.save(user);
		}
	}

	@Override
	public void updateUser(UserDto userDto, Long id) {
		User user = userRepository.findById(id).orElse(null);
		Role role = roleRepository.getById(Long.parseLong(PropertyConstant.USER_ROLE_ID));
		
		if (Optional.ofNullable(user).isPresent()) {
			user = modelMapper.map(userDto, User.class);
			user.setRole(role);
			if (!userDto.getPassword().isEmpty()) {
				String hashedPassword = BCrypt.hashpw(userDto.getPassword(), BCrypt.gensalt(10));
				user.setPassword(hashedPassword);
			}
			userRepository.save(user);
		}
	}

	@Override
	public void deleteUser(Long userId) {
		User user = userRepository.findById(userId).orElse(null);
		if (Optional.ofNullable(user).isPresent()) {
			LOGGER.info(MessageConstant.DELETE_USER_BY_ID, userId);
			userRepository.deleteById(user.getId());
		}
	}

//	@Override
//	public List<UserDto> getUsers() {
//		List<User> users = userRepository.findAll();
//		List<UserDto> userDtos = new ArrayList<UserDto>();
//		for (User user : users) {
//			UserDto userDto = new UserDto();
//			userDto.setId(user.getId());
//			userDto.setFullname(user.getFullname());
//			userDto.setPassword(user.getPassword());
//			userDto.setEmail(user.getEmail());
//			userDto.setActivated(user.getActivated());
//			userDto.setAddress(user.getAddress());
//			userDto.setAvatar(user.getAvatar());
//			userDto.setRememberToken(user.getRememberToken());
//			
//			userDtos.add(userDto);			
//		}
//		return userDtos;
//	}

}
