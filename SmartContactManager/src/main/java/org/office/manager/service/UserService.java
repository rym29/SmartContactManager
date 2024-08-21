package org.office.manager.service;

import org.office.manager.entity.User;
import org.office.manager.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
	
	@Autowired
	private UserRepo ur;

	public void signup(User user) {
 
		ur.save(user);
	}
}
