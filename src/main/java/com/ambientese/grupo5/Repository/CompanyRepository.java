package com.ambientese.grupo5.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ambientese.grupo5.Model.CompanyModel;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<CompanyModel, Long> {

    @Query("SELECT c FROM CompanyModel c ORDER BY c.tradeName ASC")
    List<CompanyModel> findAllOrderByTradeNameAsc();    

    @Query("SELECT DISTINCT c.segment FROM CompanyModel c")
    List<String> findDistinctSegment();

    List<CompanyModel> findAllByTradeNameStartingWithIgnoreCaseOrderByTradeNameAsc(String tradeName);

    List<CompanyModel> findAllByCompanyNameStartingWithIgnoreCaseOrderByCompanyNameAsc(String companyName);

    List<CompanyModel> findAllBySegmentStartingWithIgnoreCaseOrderByTradeNameAsc(String segment);
    
    Optional<CompanyModel> findByCnpj(String cnpj);
}
