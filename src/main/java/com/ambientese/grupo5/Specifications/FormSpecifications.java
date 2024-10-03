package com.ambientese.grupo5.Specifications;

import com.ambientese.grupo5.Model.FormModel;
import com.ambientese.grupo5.Model.Enums.SizeEnum;

import org.springframework.data.jpa.domain.Specification;

public class FormSpecifications {

    public static Specification<FormModel> hasTradeName(String tradeName) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("company").get("tradeName")), tradeName.toLowerCase() + "%");
    }

    public static Specification<FormModel> hasSegment(String segment) {
        return (root, query, cb) -> cb.equal(root.get("company").get("segment"), segment);
    }

    public static Specification<FormModel> hasSize(SizeEnum size) {
        return (root, query, cb) -> cb.equal(root.get("company").get("companySize"), size);
    }
}
