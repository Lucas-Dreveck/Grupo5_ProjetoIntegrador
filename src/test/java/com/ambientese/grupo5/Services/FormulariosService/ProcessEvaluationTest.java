// package com.ambientese.grupo5.Services.EvaluationulariosService;

// import com.ambientese.grupo5.DTO.EvaluationRequest;
// import com.ambientese.grupo5.Model.CompanyModel;
// import com.ambientese.grupo5.Model.Enums.PillarEnum;
// import com.ambientese.grupo5.Model.Enums.CertificateLevelEnum;
// import com.ambientese.grupo5.Model.Enums.AnswersEnum;
// import com.ambientese.grupo5.Model.EvaluationModel;
// import com.ambientese.grupo5.Model.QuestionModel;
// import com.ambientese.grupo5.Model.AnswerModel;
// import com.ambientese.grupo5.Repository.CompanyRepository;
// import com.ambientese.grupo5.Repository.EvaluationRepository;
// import com.ambientese.grupo5.Repository.QuestionRepository;
// import com.ambientese.grupo5.Services.EvaluationService;
// import com.ambientese.grupo5.Repository.AnswerRepository;

// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;

// import java.util.Arrays;
// import java.util.List;
// import java.util.Optional;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.ArgumentMatchers.anyLong;
// import static org.mockito.Mockito.*;

// @ExtendWith(MockitoExtension.class)
// class ProcessEvaluationTest {

//     @Mock
//     private EvaluationRepository evaluationRepository;

//     @Mock
//     private CompanyRepository companyRepository;
    
//     @Mock
//     private QuestionRepository questionRepository;

//     @Mock
//     private AnswerRepository answerRepository;

//     @InjectMocks
//     private EvaluationService evaluationService;

//     @Test
//     void testCreateProcessAndGenerateCertificate() {
//         Long companyId = 1L;

//         List<EvaluationRequest> evaluationList = Arrays.asList(
//                 new EvaluationRequest(1L, PillarEnum.Social, AnswersEnum.Conforme),
//                 new EvaluationRequest(2L, PillarEnum.Ambiental, AnswersEnum.Conforme),
//                 new EvaluationRequest(3L, PillarEnum.Governamental, AnswersEnum.NaoConforme)
//         );

//         QuestionModel questionMock = new QuestionModel();
//         questionMock.setId(1L);

//         CompanyModel companyMock = new CompanyModel();
//         companyMock.setId(companyId);
//         when(companyRepository.findById(companyId)).thenReturn(Optional.of(companyMock));

//         EvaluationModel expectedEvaluation = getEvaluationModel();
//         expectedEvaluation.setCompany(companyMock);

//         when(evaluationRepository.save(any())).thenReturn(expectedEvaluation);
//         when(questionRepository.findById(anyLong())).thenReturn(Optional.of(questionMock));

//         EvaluationModel actualEvaluation = evaluationService.createCompleteEvaluation(companyId, evaluationList);

//         assertEquals(expectedEvaluation.getFinalScore(), actualEvaluation.getFinalScore());
//         assertEquals(expectedEvaluation.getSocialScore(), actualEvaluation.getSocialScore());
//         assertEquals(expectedEvaluation.getEnviornmentalScore(), actualEvaluation.getEnviornmentalScore());
//         assertEquals(expectedEvaluation.getGovernmentScore(), actualEvaluation.getGovernmentScore());
//         assertEquals(expectedEvaluation.getCertificate(), actualEvaluation.getCertificate());
//         assertEquals(expectedEvaluation.getCompany(), actualEvaluation.getCompany());

//         for (int i = 0; i < expectedEvaluation.getAnswers().size(); i++) {
//             AnswerModel expectedResposta = expectedEvaluation.getAnswers().get(i);
//             AnswerModel actualResposta = actualEvaluation.getAnswers().get(i);
//             assertEquals(expectedResposta.getAnswer(), actualResposta.getAnswer());
//         }

//         verify(evaluationRepository, times(1)).save(any());
//         verify(answerRepository, times(1)).saveAll(any());
//         verify(companyRepository, times(1)).findById(companyId);
//     }

//     private static EvaluationModel getEvaluationModel() {
//         EvaluationModel expectedEvaluationModel = new EvaluationModel();
//         expectedEvaluationModel.setFinalScore(66);
//         expectedEvaluationModel.setSocialScore(1);
//         expectedEvaluationModel.setEnviornmentalScore(1);
//         expectedEvaluationModel.setGovernmentScore(0);
//         expectedEvaluationModel.setCertificate(CertificateLevelEnum.Bronze);

//         List<AnswerModel> answers = Arrays.asList(
//                 createAnswerModel(AnswersEnum.Conforme),
//                 createAnswerModel(AnswersEnum.Conforme),
//                 createAnswerModel(AnswersEnum.NaoConforme)
//         );

//         expectedEvaluationModel.setAnswers(answers);
//         return expectedEvaluationModel;
//     }

//     private static AnswerModel createAnswerModel(AnswersEnum answerEnum) {
//         AnswerModel answer = new AnswerModel();
//         answer.setAnswer(answerEnum);
//         return answer;
//     }

//     @Test
//     void testCalculateCertificateLevelGold() {
//         CertificateLevelEnum actualLevel = evaluationService.calculateCertificateLevel(100);
//         assertEquals(CertificateLevelEnum.Ouro, actualLevel);
//     }

//     @Test
//     void testCalculateCertificateLevelSilver() {
//         CertificateLevelEnum actualLevel = evaluationService.calculateCertificateLevel(80);
//         assertEquals(CertificateLevelEnum.Prata, actualLevel);
//     }

//     @Test
//     void testCalculateCertificateLevelBronze() {
//         CertificateLevelEnum actualLevel = evaluationService.calculateCertificateLevel(60);
//         assertEquals(CertificateLevelEnum.Bronze, actualLevel);
//     }
// }
