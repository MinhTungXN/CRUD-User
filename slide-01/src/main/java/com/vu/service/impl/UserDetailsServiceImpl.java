package com.vu.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.vu.constant.MessageConstant;
import com.vu.entity.User;
import com.vu.repository.RoleRepository;
import com.vu.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private final RoleRepository roleRepository;
	private final UserRepository userRepository;

	public UserDetailsServiceImpl(RoleRepository roleRepository, UserRepository userRepository) {
		this.roleRepository = roleRepository;
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(username);

		if (Optional.ofNullable(user).isEmpty()) {
			String emailNotFoundMessage = username.concat(MessageConstant.NOT_FOUND_MESSAGE);
			throw new UsernameNotFoundException(emailNotFoundMessage);
		}

		// [ROLE_USER, ROLE_ADMIN]
		List<String> roleNames = Optional.ofNullable(roleRepository.findRoleNamesByUserId(user.getId())).orElse(null);

		// ROLE_USER, ROLE_ADMIN
		List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
		if (Optional.ofNullable(roleNames).isPresent()) {
			for (String roleName : roleNames) {
				GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(roleName);
				grantedAuthorities.add(grantedAuthority);
			}
		}

		UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getEmail(),
				user.getPassword(), grantedAuthorities);

		return userDetails;
	}

}
