package com.ambientese.grupo5.Specifications;

import com.ambientese.grupo5.Model.FormularioModel;
import com.ambientese.grupo5.Model.Enums.PorteEnum;

import org.springframework.data.jpa.domain.Specification;

public class FormularioSpecifications {

    public static Specification<FormularioModel> hasNomeFantasia(String nomeFantasia) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("empresa").get("nomeFantasia")), nomeFantasia.toLowerCase() + "%");
    }

    public static Specification<FormularioModel> hasRamo(String ramo) {
        return (root, query, cb) -> cb.equal(root.get("empresa").get("ramo"), ramo);
    }

    public static Specification<FormularioModel> hasPorte(PorteEnum porte) {
        return (root, query, cb) -> cb.equal(root.get("empresa").get("porteEmpresas"), porte);
    }
}
