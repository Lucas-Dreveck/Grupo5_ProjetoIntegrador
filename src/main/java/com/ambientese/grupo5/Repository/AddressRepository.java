package com.ambientese.grupo5.Repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.ambientese.grupo5.Model.AddressModel;

public interface AddressRepository extends JpaRepository<AddressModel, Long> {
}
