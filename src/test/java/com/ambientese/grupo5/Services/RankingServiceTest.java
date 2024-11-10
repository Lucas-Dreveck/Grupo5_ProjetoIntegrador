package com.ambientese.grupo5.services;

import com.ambientese.grupo5.model.EvaluationModel;
import com.ambientese.grupo5.repository.EvaluationRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RankingServiceTest {

    @Mock
    private EvaluationRepository evaluationRepository;

    @InjectMocks
    private RankingService rankingService;

    @Test
    void sortByScoreTest() {
        List<EvaluationModel> evaluationMock = new ArrayList<>();
        evaluationMock.add(new EvaluationModel());
        evaluationMock.add(new EvaluationModel());

        when(evaluationRepository.findLatestByCompanyOrderByFinalScoreDesc()).thenReturn(evaluationMock);

        List<EvaluationModel> evaluations = rankingService.sortByScore();

        assertEquals(evaluationMock, evaluations);
    }
}
