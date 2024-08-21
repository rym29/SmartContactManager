package org.office.manager.config;

import org.office.manager.entity.User;
import org.office.manager.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepo ur;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = ur.getUserByUserName(username);

		if (user == null) {
			throw new UsernameNotFoundException("User not Found !!");
		}
		CustomUserDetails ud = new CustomUserDetails(user);

		return ud;
	}

}
