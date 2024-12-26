package com.example.OrdenConfrimUI;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<Posusuarios, String> {

	void findByCNOMUSUA(String username);
    // Aquí puedes añadir métodos personalizados si es necesario

//	Optional<Posusuarios> findByCNUMUSUA(String username);



}

