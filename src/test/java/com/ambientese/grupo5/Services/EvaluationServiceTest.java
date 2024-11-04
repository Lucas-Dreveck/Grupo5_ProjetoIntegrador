package com.ambientese.grupo5.Services;

import com.ambientese.grupo5.DTO.EvaluationRequest;
import com.ambientese.grupo5.DTO.EvaluationResponse;
import com.ambientese.grupo5.Model.CompanyModel;
import com.ambientese.grupo5.Model.Enums.PillarEnum;
import com.ambientese.grupo5.Model.Enums.CertificateLevelEnum;
import com.ambientese.grupo5.Model.Enums.AnswersEnum;
import com.ambientese.grupo5.Model.EvaluationModel;
import com.ambientese.grupo5.Model.QuestionModel;
import com.ambientese.grupo5.Model.AnswerId;
import com.ambientese.grupo5.Model.AnswerModel;
import com.ambientese.grupo5.Repository.CompanyRepository;
import com.ambientese.grupo5.Repository.EvaluationRepository;
import com.ambientese.grupo5.Repository.QuestionRepository;
import com.ambientese.grupo5.Repository.AnswerRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EvaluationServiceTest {

    @Mock
    private EvaluationRepository evaluationRepository;

    @Mock
    private CompanyRepository companyRepository;
    
    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private AnswerRepository answerRepository;

    @Spy
    @InjectMocks
    private EvaluationService evaluationService;

    List<QuestionModel> questionsMock = Arrays.asList(
        new QuestionModel("Pergunta 1", PillarEnum.Social),
        new QuestionModel("Pergunta 2", PillarEnum.Social),
        new QuestionModel("Pergunta 3", PillarEnum.Social),
        new QuestionModel("Pergunta 4", PillarEnum.Social),
        new QuestionModel("Pergunta 5", PillarEnum.Social),
        new QuestionModel("Pergunta 6", PillarEnum.Social),
        new QuestionModel("Pergunta 7", PillarEnum.Social),
        new QuestionModel("Pergunta 8", PillarEnum.Social),
        new QuestionModel("Pergunta 9", PillarEnum.Social),
        new QuestionModel("Pergunta 10", PillarEnum.Social),
        new QuestionModel("Pergunta 1", PillarEnum.Ambiental),
        new QuestionModel("Pergunta 2", PillarEnum.Ambiental),
        new QuestionModel("Pergunta 3", PillarEnum.Ambiental),
        new QuestionModel("Pergunta 4", PillarEnum.Ambiental),
        new QuestionModel("Pergunta 5", PillarEnum.Ambiental),
        new QuestionModel("Pergunta 6", PillarEnum.Ambiental),
        new QuestionModel("Pergunta 7", PillarEnum.Ambiental),
        new QuestionModel("Pergunta 8", PillarEnum.Ambiental),
        new QuestionModel("Pergunta 9", PillarEnum.Ambiental),
        new QuestionModel("Pergunta 10", PillarEnum.Ambiental),
        new QuestionModel("Pergunta 1", PillarEnum.Governamental),
        new QuestionModel("Pergunta 2", PillarEnum.Governamental),
        new QuestionModel("Pergunta 3", PillarEnum.Governamental),
        new QuestionModel("Pergunta 4", PillarEnum.Governamental),
        new QuestionModel("Pergunta 5", PillarEnum.Governamental),
        new QuestionModel("Pergunta 6", PillarEnum.Governamental),
        new QuestionModel("Pergunta 7", PillarEnum.Governamental),
        new QuestionModel("Pergunta 8", PillarEnum.Governamental),
        new QuestionModel("Pergunta 9", PillarEnum.Governamental),
        new QuestionModel("Pergunta 10", PillarEnum.Governamental)
    );

    List<EvaluationRequest> evaluationList = Arrays.asList(
        new EvaluationRequest(1L, PillarEnum.Social, AnswersEnum.Conforme),
        new EvaluationRequest(2L, PillarEnum.Social, AnswersEnum.Conforme),
        new EvaluationRequest(3L, PillarEnum.Social, AnswersEnum.NaoConforme),
        new EvaluationRequest(4L, PillarEnum.Social, AnswersEnum.Conforme),
        new EvaluationRequest(5L, PillarEnum.Social, AnswersEnum.Conforme),
        new EvaluationRequest(6L, PillarEnum.Social, AnswersEnum.Conforme),
        new EvaluationRequest(7L, PillarEnum.Social, AnswersEnum.Conforme),
        new EvaluationRequest(8L, PillarEnum.Social, AnswersEnum.Conforme),
        new EvaluationRequest(9L, PillarEnum.Social, AnswersEnum.Conforme),
        new EvaluationRequest(10L, PillarEnum.Social, AnswersEnum.Conforme),
        new EvaluationRequest(11L, PillarEnum.Ambiental, AnswersEnum.Conforme),
        new EvaluationRequest(12L, PillarEnum.Ambiental, AnswersEnum.Conforme),
        new EvaluationRequest(13L, PillarEnum.Ambiental, AnswersEnum.Conforme),
        new EvaluationRequest(14L, PillarEnum.Ambiental, AnswersEnum.Conforme),
        new EvaluationRequest(15L, PillarEnum.Ambiental, AnswersEnum.Conforme),
        new EvaluationRequest(16L, PillarEnum.Ambiental, AnswersEnum.Conforme),
        new EvaluationRequest(17L, PillarEnum.Ambiental, AnswersEnum.Conforme),
        new EvaluationRequest(18L, PillarEnum.Ambiental, AnswersEnum.Conforme),
        new EvaluationRequest(19L, PillarEnum.Ambiental, AnswersEnum.Conforme),
        new EvaluationRequest(20L, PillarEnum.Ambiental, AnswersEnum.Conforme),
        new EvaluationRequest(21L, PillarEnum.Governamental, AnswersEnum.Conforme),
        new EvaluationRequest(22L, PillarEnum.Governamental, AnswersEnum.Conforme),
        new EvaluationRequest(23L, PillarEnum.Governamental, AnswersEnum.Conforme),
        new EvaluationRequest(24L, PillarEnum.Governamental, AnswersEnum.Conforme),
        new EvaluationRequest(25L, PillarEnum.Governamental, AnswersEnum.Conforme),
        new EvaluationRequest(26L, PillarEnum.Governamental, AnswersEnum.Conforme),
        new EvaluationRequest(27L, PillarEnum.Governamental, AnswersEnum.Conforme),
        new EvaluationRequest(28L, PillarEnum.Governamental, AnswersEnum.Conforme),
        new EvaluationRequest(29L, PillarEnum.Governamental, AnswersEnum.Conforme),
        new EvaluationRequest(30L, PillarEnum.Governamental, AnswersEnum.Conforme)
    );

    @Test
    void WhenSearchQuestionsInDbForNewEvaluationThenReturnNewQuestions() {
        // Arrange
        when(evaluationRepository.findIncompleteByCompanyId(1L)).thenReturn(Optional.empty());
        when(questionRepository.findByPillar(PillarEnum.Social)).thenReturn(questionsMock);
        when(questionRepository.findByPillar(PillarEnum.Ambiental)).thenReturn(questionsMock);
        when(questionRepository.findByPillar(PillarEnum.Governamental)).thenReturn(questionsMock);

        // Act
        EvaluationResponse questions = evaluationService.searchQuestionsInDb(true, 1L);

        // Assert
        assertNotNull(questions.getQuestions());
        assertEquals(30, questions.getQuestions().size());
    }

    @Test
    void WhenSearchQuestionsInDbForExistingEvaluationThenReturnCurrentQuestions() {
        // Arrange
        EvaluationModel existingEvaluation = new EvaluationModel();
        existingEvaluation.setId(1L);
        List<AnswerModel> answers = new ArrayList<>();
        answers.add(new AnswerModel(new AnswerId(existingEvaluation.getId(), 1L), existingEvaluation, new QuestionModel("Question 1", PillarEnum.Social), AnswersEnum.Conforme));
        answers.add(new AnswerModel(new AnswerId(existingEvaluation.getId(), 2L), existingEvaluation, new QuestionModel("Question 2", PillarEnum.Ambiental), AnswersEnum.NaoConforme));
        existingEvaluation.setAnswers(answers);
        when(evaluationRepository.findIncompleteByCompanyId(1L)).thenReturn(Optional.of(existingEvaluation));

        // Act
        EvaluationResponse questions = evaluationService.searchQuestionsInDb(false, 1L);

        // Assert
        assertNotNull(questions.getEvaluationRequests());
        assertEquals(2, questions.getEvaluationRequests().size());
        assertEquals(0L, questions.getEvaluationRequests().get(0).getQuestionId());
        assertEquals(AnswersEnum.Conforme, questions.getEvaluationRequests().get(0).getUserAnswer());
        assertEquals(0L, questions.getEvaluationRequests().get(1).getQuestionId());
        assertEquals(AnswersEnum.NaoConforme, questions.getEvaluationRequests().get(1).getUserAnswer());
    }

    @Test
    void WhenSearchQuestionsInDbAndNotEnoughQuestionsFoundThenThrowException() {
        // Arrange
        when(evaluationRepository.findIncompleteByCompanyId(1L)).thenReturn(Optional.empty());
        when(questionRepository.findByPillar(PillarEnum.Social)).thenReturn(questionsMock.subList(0, 3));
        when(questionRepository.findByPillar(PillarEnum.Ambiental)).thenReturn(questionsMock.subList(0, 3));
        when(questionRepository.findByPillar(PillarEnum.Governamental)).thenReturn(questionsMock.subList(0, 3));

        // Act and Assert
        assertThrows(RuntimeException.class, () -> evaluationService.searchQuestionsInDb(true, 1L));
    }

    @Test
    void whenCreateCompleteEvaluationThenCreateCertificate() {
        // Arrange
        Long companyId = 1L;
        List<EvaluationRequest> evaluationRequestList = evaluationList;

        CompanyModel companyMock = new CompanyModel();
        companyMock.setId(companyId);

        // Mock the QuestionRepository to return the expected QuestionModel instances
        when(questionRepository.findById(anyLong()))
            .thenReturn(Optional.of(new QuestionModel()));

        when(evaluationRepository.findIncompleteByCompanyId(companyId)).thenReturn(Optional.empty());
        when(companyRepository.findById(companyId)).thenReturn(Optional.of(companyMock));
        when(evaluationRepository.save(any(EvaluationModel.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(evaluationRepository.saveAndFlush(any(EvaluationModel.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(answerRepository.saveAll(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        EvaluationModel result = evaluationService.createEvaluation(companyId, evaluationRequestList, true);

        // Assert
        verify(evaluationService, times(1)).createCompleteEvaluation(companyId, evaluationRequestList);
        verify(evaluationService, never()).replaceIncompleteEvaluationWithComplete(any(), any());
        verify(evaluationService, never()).createIncompleteEvaluation(any(), any());
        verify(evaluationService, never()).replaceIncompleteEvaluationWithIncomplete(any(), any());
        assertNotNull(result);
        // Add more assertions to verify the result
    }

    @Test
    void whenReplaceIncompleteEvaluationWithComplete() {
        // Arrange
        Long companyId = 1L;
        List<EvaluationRequest> evaluationRequestList = evaluationList;
        EvaluationModel incompleteEvaluation = new EvaluationModel();
        incompleteEvaluation.setId(1L);
        CompanyModel companyModel = new CompanyModel();
        companyModel.setId(companyId);
        incompleteEvaluation.setCompany(companyModel);

        // Mock the QuestionRepository to return the expected QuestionModel instances
        when(questionRepository.findById(anyLong()))
            .thenReturn(Optional.of(new QuestionModel()));

        when(evaluationRepository.findIncompleteByCompanyId(companyId)).thenReturn(Optional.of(incompleteEvaluation));
        when(answerRepository.saveAll(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        EvaluationModel result = evaluationService.createEvaluation(companyId, evaluationRequestList, true);

        // Assert
        verify(evaluationService, never()).createCompleteEvaluation(any(), any());
        verify(evaluationService, times(1)).replaceIncompleteEvaluationWithComplete(incompleteEvaluation, evaluationRequestList);
        verify(evaluationService, never()).createIncompleteEvaluation(any(), any());
        verify(evaluationService, never()).replaceIncompleteEvaluationWithIncomplete(any(), any());
        assertNotNull(result);
        // Add more assertions to verify the result
    }

    @Test
    void whenCreateIncompleteEvaluation() {
        // Arrange
        Long companyId = 1L;
        List<EvaluationRequest> evaluationRequestList = evaluationList;

        CompanyModel companyMock = new CompanyModel();
        companyMock.setId(companyId);

        // Mock the QuestionRepository to return the expected QuestionModel instances
        when(questionRepository.findById(anyLong()))
            .thenReturn(Optional.of(new QuestionModel()));

        when(evaluationRepository.findIncompleteByCompanyId(companyId)).thenReturn(Optional.empty());
        when(companyRepository.findById(companyId)).thenReturn(Optional.of(companyMock));
        when(evaluationRepository.saveAndFlush(any(EvaluationModel.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(evaluationRepository.save(any(EvaluationModel.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(answerRepository.saveAll(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        EvaluationModel result = evaluationService.createEvaluation(companyId, evaluationRequestList, false);

        // Assert
        verify(evaluationService, never()).createCompleteEvaluation(any(), any());
        verify(evaluationService, never()).replaceIncompleteEvaluationWithComplete(any(), any());
        verify(evaluationService, times(1)).createIncompleteEvaluation(companyId, evaluationRequestList);
        verify(evaluationService, never()).replaceIncompleteEvaluationWithIncomplete(any(), any());
        assertNotNull(result);
        // Add more assertions to verify the result
    }

    @Test
    void whenReplaceIncompleteEvaluationWithIncomplete() {
        // Arrange
        Long companyId = 1L;
        List<EvaluationRequest> evaluationRequestList = evaluationList;
        EvaluationModel incompleteEvaluation = new EvaluationModel();
        incompleteEvaluation.setId(1L);
        CompanyModel companyModel = new CompanyModel();
        companyModel.setId(companyId);
        incompleteEvaluation.setCompany(companyModel);

        // Mock the QuestionRepository to return the expected QuestionModel instances
        when(questionRepository.findById(anyLong()))
            .thenReturn(Optional.of(new QuestionModel()));

        when(evaluationRepository.findIncompleteByCompanyId(companyId)).thenReturn(Optional.of(incompleteEvaluation));
        when(evaluationRepository.saveAndFlush(any(EvaluationModel.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(answerRepository.saveAll(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        EvaluationModel result = evaluationService.createEvaluation(companyId, evaluationRequestList, false);

        // Assert
        verify(evaluationService, never()).createCompleteEvaluation(any(), any());
        verify(evaluationService, never()).replaceIncompleteEvaluationWithComplete(any(), any());
        verify(evaluationService, never()).createIncompleteEvaluation(any(), any());
        verify(evaluationService, times(1)).replaceIncompleteEvaluationWithIncomplete(incompleteEvaluation, evaluationRequestList);
        assertNotNull(result);
        // Add more assertions to verify the result
    }

    @Test
    void whenHaveActiveEvaluation() {
        // Arrange
        Long companyId = 1L;
        EvaluationModel activeEvaluation = new EvaluationModel();
        activeEvaluation.setId(1L);
        CompanyModel companyModel = new CompanyModel();
        companyModel.setId(companyId);
        activeEvaluation.setCompany(companyModel);

        when(evaluationRepository.findLatestEvaluationByCompanyId(companyId)).thenReturn(activeEvaluation);

        // Act
        boolean hasActiveEvaluation = evaluationService.haveActiveEvaluation(companyId);

        // Assert
        assertTrue(hasActiveEvaluation);
    }

    @Test
    void whenNoActiveEvaluation() {
        // Arrange
        Long companyId = 1L;

        when(evaluationRepository.findLatestEvaluationByCompanyId(companyId)).thenReturn(null);

        // Act
        boolean hasActiveEvaluation = evaluationService.haveActiveEvaluation(companyId);

        // Assert
        assertFalse(hasActiveEvaluation);
    }

    @Test
    void whenSearchById() {
        // Arrange
        Long evaluationId = 1L;
        EvaluationModel expectedEvaluation = new EvaluationModel();
        expectedEvaluation.setId(evaluationId);

        when(evaluationRepository.findById(evaluationId)).thenReturn(Optional.of(expectedEvaluation));

        // Act
        EvaluationModel result = evaluationService.searchById(evaluationId);

        // Assert
        assertNotNull(result);
        assertSame(expectedEvaluation, result);
    }

    @Test
    void testCalculateCertificateLevelGold() {
        CertificateLevelEnum actualLevel = evaluationService.calculateCertificateLevel(100);
        assertEquals(CertificateLevelEnum.Ouro, actualLevel);
    }

    @Test
    void testCalculateCertificateLevelSilver() {
        CertificateLevelEnum actualLevel = evaluationService.calculateCertificateLevel(80);
        assertEquals(CertificateLevelEnum.Prata, actualLevel);
    }

    @Test
    void testCalculateCertificateLevelBronze() {
        CertificateLevelEnum actualLevel = evaluationService.calculateCertificateLevel(60);
        assertEquals(CertificateLevelEnum.Bronze, actualLevel);
    }
}
