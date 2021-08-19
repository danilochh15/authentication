package com.example.authentication.services;

import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.example.authentication.models.User;
import com.example.authentication.repositories.UserRepository;

@Service
public class UserService {
	
	private final UserRepository userRepository;
	
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	//registrar el usuario y hacer Hash a su password
	public User registerUser(User user) {
		String hashed = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
		user.setPassword(hashed);
		return userRepository.save(user);
	}
	
	//Encontrar usuario por su email
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}
	
	//Encontar un usuario por su id
	public User findUserById(Long id) {
		Optional<User> u = userRepository.findById(id);
		if(u.isPresent()) {
			return u.get();
		}else {
			return null;
		}
	}
	
	//Autenticar usuario
	public boolean authenticateUser(String email, String password) {
		//Primero encontrar el usuario por su email
		User user = userRepository.findByEmail(email);
		//Si no lo podemos encontrar por su email, retornar false
		if(user == null) {
			return false;
		}else {
			//Si el password coincide devolvemos true, sino, devolvemos false
			if(BCrypt.checkpw(password,user.getPassword())) {
				return true;
			}else {
				return false;
			}
				
		}
	}

}
