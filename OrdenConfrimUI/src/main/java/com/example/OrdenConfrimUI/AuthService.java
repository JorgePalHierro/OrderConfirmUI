package com.example.OrdenConfrimUI;


import java.util.List;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;





@Service
public class AuthService {

	 private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	 @Autowired
	 private UserRepository userRepository;

	    // Método para cifrar la contraseña antes de guardarla
	    public String encryptPassword(String password) {
	        return passwordEncoder.encode(password);
	    }

	    // Método para validar la contraseña
	    public boolean validate(String username, String rawPassword) { 	
	    	if (username.length() < 8) {
	            username = String.format("%0" + (8 - username.length()) + "d%s", 0, username);
	        }	    	
	    	
	    
	        String storedPasswordHash = getStoredPasswordHash(username);
	        return storedPasswordHash != null && passwordEncoder.matches(rawPassword, storedPasswordHash);
	    }

	    private String getStoredPasswordHash(String username) {
	    	
	    	// userRepository.findByCNUMUSUA(username);
	    	 
	    	 
	    	if ( userRepository.findById(username).orElse(null) !=null) {
	    		
	            return encryptPassword(userRepository.findById(username).orElse(null).getCPASSWOR());
	        }
	        return null;	        
	       
	    }
	    
	    
	    public void printAllUsers() {
	        List<Posusuarios> users = userRepository.findAll();
	        for (Posusuarios user : users) {
	            System.out.println("RNUMEMP"+user.getRNUMEMP() + " Pass:"+ user.getCPASSWOR()); // Asumiendo que has implementado el método toString en Posusuarios
	        }
	    }
}
