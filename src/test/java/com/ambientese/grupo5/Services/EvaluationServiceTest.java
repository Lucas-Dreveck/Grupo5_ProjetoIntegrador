package com.ambientese.grupo5.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ambientese.grupo5.dto.EvaluationRequest;
import com.ambientese.grupo5.dto.EvaluationResponse;
import com.ambientese.grupo5.exception.InsufficientQuestionsException;
import com.ambientese.grupo5.exception.InvalidCompanyIdException;
import com.ambientese.grupo5.model.CompanyModel;
import com.ambientese.grupo5.model.EvaluationModel;
import com.ambientese.grupo5.model.QuestionModel;
import com.ambientese.grupo5.model.enums.AnswersEnum;
import com.ambientese.grupo5.model.enums.CertificateLevelEnum;
import com.ambientese.grupo5.model.enums.PillarEnum;
import com.ambientese.grupo5.repository.AnswerRepository;
import com.ambientese.grupo5.repository.CompanyRepository;
import com.ambientese.grupo5.repository.EvaluationRepository;
import com.ambientese.grupo5.repository.QuestionRepository;

@ExtendWith(MockitoExtension.class)
class EvaluationServiceTest {

    @Mock
    private EvaluationRepository evaluationRepository;
    @Mock
    private QuestionRepository questionRepository;
    @Mock
    private AnswerRepository answerRepository;
    @Mock
    private CompanyRepository companyRepository;

    @InjectMocks
    private EvaluationService evaluationService;

    private CompanyModel company;
    private List<QuestionModel> questionsPerPillar;
    private List<EvaluationRequest> completeEvaluation;

    @BeforeEach
    void setUp() {
        company = new CompanyModel();
        company.setId(1L);

        // Criar 10 questões por pilar
        questionsPerPillar = createQuestionsForPillar(10);
        
        // Criar avaliação completa com 30 questões
        completeEvaluation = createCompleteEvaluation();
    }

    @Test
    void whenStartNewEvaluation_thenSuccess() {
        // Arrange
        when(evaluationRepository.findIncompleteByCompanyId(1L)).thenReturn(Optional.empty());
        when(questionRepository.findByPillar(any(PillarEnum.class))).thenReturn(questionsPerPillar);

        // Act
        EvaluationResponse response = evaluationService.searchQuestionsInDb(true, 1L);

        // Assert
        assertNotNull(response.getQuestions());
        assertEquals(30, response.getQuestions().size());
        verify(questionRepository, times(3)).findByPillar(any(PillarEnum.class));
    }

    @Test
    void whenStartNewEvaluation_withInsufficientQuestions_thenThrowException() {
        // Arrange
        List<QuestionModel> insufficientQuestions = createQuestionsForPillar(5);
        when(evaluationRepository.findIncompleteByCompanyId(1L)).thenReturn(Optional.empty());
        when(questionRepository.findByPillar(any(PillarEnum.class))).thenReturn(insufficientQuestions);

        // Act & Assert
        assertThrows(InsufficientQuestionsException.class, 
            () -> evaluationService.searchQuestionsInDb(true, 1L));
    }

    @Test
    void whenContinueExistingEvaluation_thenReturnSavedAnswers() {
        // Arrange
        EvaluationModel existingEvaluation = createExistingEvaluation();
        when(evaluationRepository.findIncompleteByCompanyId(1L))
            .thenReturn(Optional.of(existingEvaluation));

        // Act
        EvaluationResponse response = evaluationService.searchQuestionsInDb(false, 1L);

        // Assert
        assertNotNull(response.getEvaluationRequests());
        assertTrue(response.getEvaluationRequests().isEmpty());
    }

    @Test
    void whenSubmitCompleteEvaluation_withGoldScore_thenSuccessAndGoldCertificate() {
        // Arrange
        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));
        when(questionRepository.findById(anyLong())).thenReturn(Optional.of(new QuestionModel()));
        when(evaluationRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(evaluationRepository.saveAndFlush(any())).thenAnswer(i -> i.getArgument(0));

        // Criar avaliação com todas as respostas conformes (100%)
        List<EvaluationRequest> perfectEvaluation = createPerfectEvaluation();

        // Act
        EvaluationModel result = evaluationService.createEvaluation(1L, perfectEvaluation, true);

        // Assert
        assertNotNull(result);
        assertEquals(100, result.getFinalScore());
        assertEquals(CertificateLevelEnum.Ouro, result.getCertificate());
        verify(evaluationRepository).save(any(EvaluationModel.class));
    }

    @Test
    void whenSubmitCompleteEvaluation_withSilverScore_thenSuccessAndSilverCertificate() {
        // Arrange
        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));
        when(questionRepository.findById(anyLong())).thenReturn(Optional.of(new QuestionModel()));
        when(evaluationRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(evaluationRepository.saveAndFlush(any())).thenAnswer(i -> i.getArgument(0));

        // Criar avaliação com 80% de conformidade
        List<EvaluationRequest> silverEvaluation = createEvaluationWithScore(80);

        // Act
        EvaluationModel result = evaluationService.createEvaluation(1L, silverEvaluation, true);

        // Assert
        assertNotNull(result);
        assertTrue(result.getFinalScore() >= 75.1);
        assertEquals(CertificateLevelEnum.Prata, result.getCertificate());
    }

    @Test
    void whenSubmitCompleteEvaluation_withBronzeScore_thenSuccessAndBronzeCertificate() {
        // Arrange
        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));
        when(questionRepository.findById(anyLong())).thenReturn(Optional.of(new QuestionModel()));
        when(evaluationRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(evaluationRepository.saveAndFlush(any())).thenAnswer(i -> i.getArgument(0));

        // Criar avaliação com 60% de conformidade
        List<EvaluationRequest> bronzeEvaluation = createEvaluationWithScore(60);

        // Act
        EvaluationModel result = evaluationService.createEvaluation(1L, bronzeEvaluation, true);

        // Assert
        assertNotNull(result);
        assertTrue(result.getFinalScore() <= 75);
        assertEquals(CertificateLevelEnum.Bronze, result.getCertificate());
    }

    @Test
    void whenSubmitIncompleteEvaluation_thenSaveWithoutCertificate() {
        // Arrange
        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));
        when(questionRepository.findById(anyLong())).thenReturn(Optional.of(new QuestionModel()));
        when(evaluationRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(evaluationRepository.saveAndFlush(any())).thenAnswer(i -> i.getArgument(0));

        List<EvaluationRequest> partialEvaluation = completeEvaluation.subList(0, 15);

        // Act
        EvaluationModel result = evaluationService.createEvaluation(1L, partialEvaluation, false);

        // Assert
        assertNotNull(result);
        assertNull(result.getFinalScore());
        assertNull(result.getCertificate());
        verify(evaluationRepository).save(any(EvaluationModel.class));
    }

    @Test
    void whenHaveActiveEvaluation_thenReturnTrue() {
        // Arrange
        EvaluationModel activeEvaluation = new EvaluationModel();
        activeEvaluation.setCompany(company);
        when(evaluationRepository.findLatestEvaluationByCompanyId(1L))
            .thenReturn(activeEvaluation);

        // Act
        boolean result = evaluationService.haveActiveEvaluation(1L);

        // Assert
        assertTrue(result);
    }

    @Test
    void whenNoActiveEvaluation_thenReturnFalse() {
        // Arrange
        when(evaluationRepository.findLatestEvaluationByCompanyId(1L))
            .thenReturn(null);

        // Act
        boolean result = evaluationService.haveActiveEvaluation(1L);

        // Assert
        assertFalse(result);
    }

    @Test
    void whenSearchById_thenReturnEvaluation() {
        // Arrange
        EvaluationModel evaluation = new EvaluationModel();
        evaluation.setId(1L);
        when(evaluationRepository.findById(1L)).thenReturn(Optional.of(evaluation));

        // Act
        EvaluationModel result = evaluationService.searchById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void whenSubmitCompleteEvaluation_withInvalidCompanyId_thenThrowException() {
        // Arrange
        when(companyRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(InvalidCompanyIdException.class,
            () -> evaluationService.createEvaluation(999L, completeEvaluation, true));
    }

    // Métodos auxiliares
    private List<QuestionModel> createQuestionsForPillar(int count) {
        return Arrays.asList(new QuestionModel[count]); // Simplificado para o exemplo
    }

    private List<EvaluationRequest> createCompleteEvaluation() {
        List<EvaluationRequest> requests = new ArrayList<>();
        for (int i = 1; i <= 30; i++) {
            PillarEnum pillar = i <= 10 ? PillarEnum.Social :
                              i <= 20 ? PillarEnum.Ambiental : 
                              PillarEnum.Governamental;
            requests.add(new EvaluationRequest((long)i, pillar, AnswersEnum.Conforme));
        }
        return requests;
    }

    private List<EvaluationRequest> createPerfectEvaluation() {
        return completeEvaluation.stream()
            .map(req -> new EvaluationRequest(
                req.getQuestionId(),
                req.getQuestionPillar(),
                AnswersEnum.Conforme
            ))
            .toList();
    }

    private List<EvaluationRequest> createEvaluationWithScore(int targetScore) {
        List<EvaluationRequest> evaluation = new ArrayList<>();
        int conformeCount = (targetScore * 30) / 100;
        
        for (int i = 0; i < 30; i++) {
            PillarEnum pillar = i < 10 ? PillarEnum.Social :
                              i < 20 ? PillarEnum.Ambiental :
                              PillarEnum.Governamental;
            
            AnswersEnum answer = i < conformeCount ? AnswersEnum.Conforme : AnswersEnum.NaoConforme;
            
            evaluation.add(new EvaluationRequest(
                (long)(i + 1),
                pillar,
                answer
            ));
        }
        return evaluation;
    }

    private EvaluationModel createExistingEvaluation() {
        EvaluationModel evaluation = new EvaluationModel();
        evaluation.setId(1L);
        evaluation.setCompany(company);
        // Adicione alguns answers conforme necessário
        return evaluation;
    }
}