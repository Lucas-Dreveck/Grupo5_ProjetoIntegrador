package com.ambientese.grupo5.Specifications;

import com.ambientese.grupo5.Model.EvaluationModel;
import com.ambientese.grupo5.Model.Enums.SizeEnum;

import org.springframework.data.jpa.domain.Specification;

public class EvaluationSpecifications {

    public static Specification<EvaluationModel> hasTradeName(String tradeName) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("company").get("tradeName")), tradeName.toLowerCase() + "%");
    }

    public static Specification<EvaluationModel> hasSegment(String segment) {
        return (root, query, cb) -> cb.equal(root.get("company").get("segment"), segment);
    }

    public static Specification<EvaluationModel> hasSize(SizeEnum size) {
        return (root, query, cb) -> cb.equal(root.get("company").get("companySize"), size);
    }
}
