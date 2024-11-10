package com.ambientese.grupo5.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.ambientese.grupo5.model.AddressModel;

public interface AddressRepository extends JpaRepository<AddressModel, Long> {
}
