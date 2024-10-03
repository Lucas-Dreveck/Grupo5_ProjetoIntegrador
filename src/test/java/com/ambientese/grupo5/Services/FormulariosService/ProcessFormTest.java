// package com.ambientese.grupo5.Services.FormulariosService;

// import com.ambientese.grupo5.DTO.FormRequest;
// import com.ambientese.grupo5.Model.CompanyModel;
// import com.ambientese.grupo5.Model.Enums.PillarEnum;
// import com.ambientese.grupo5.Model.Enums.CertificateLevelEnum;
// import com.ambientese.grupo5.Model.Enums.AnswersEnum;
// import com.ambientese.grupo5.Model.FormModel;
// import com.ambientese.grupo5.Model.QuestionModel;
// import com.ambientese.grupo5.Model.AnswerModel;
// import com.ambientese.grupo5.Repository.CompanyRepository;
// import com.ambientese.grupo5.Repository.FormRepository;
// import com.ambientese.grupo5.Repository.QuestionRepository;
// import com.ambientese.grupo5.Services.FormService;
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
// class ProcessFormTest {

//     @Mock
//     private FormRepository formRepository;

//     @Mock
//     private CompanyRepository companyRepository;
    
//     @Mock
//     private QuestionRepository questionRepository;

//     @Mock
//     private AnswerRepository answerRepository;

//     @InjectMocks
//     private FormService formService;

//     @Test
//     void testCreateProcessAndGenerateCertificate() {
//         Long companyId = 1L;

//         List<FormRequest> formList = Arrays.asList(
//                 new FormRequest(1L, PillarEnum.Social, AnswersEnum.Conforme),
//                 new FormRequest(2L, PillarEnum.Ambiental, AnswersEnum.Conforme),
//                 new FormRequest(3L, PillarEnum.Governamental, AnswersEnum.NaoConforme)
//         );

//         QuestionModel questionMock = new QuestionModel();
//         questionMock.setId(1L);

//         CompanyModel companyMock = new CompanyModel();
//         companyMock.setId(companyId);
//         when(companyRepository.findById(companyId)).thenReturn(Optional.of(companyMock));

//         FormModel expectedForm = getFormModel();
//         expectedForm.setCompany(companyMock);

//         when(formRepository.save(any())).thenReturn(expectedForm);
//         when(questionRepository.findById(anyLong())).thenReturn(Optional.of(questionMock));

//         FormModel actualForm = formService.createCompleteForm(companyId, formList);

//         assertEquals(expectedForm.getFinalScore(), actualForm.getFinalScore());
//         assertEquals(expectedForm.getSocialScore(), actualForm.getSocialScore());
//         assertEquals(expectedForm.getEnviornmentalScore(), actualForm.getEnviornmentalScore());
//         assertEquals(expectedForm.getGovernmentScore(), actualForm.getGovernmentScore());
//         assertEquals(expectedForm.getCertificate(), actualForm.getCertificate());
//         assertEquals(expectedForm.getCompany(), actualForm.getCompany());

//         for (int i = 0; i < expectedForm.getAnswers().size(); i++) {
//             AnswerModel expectedResposta = expectedForm.getAnswers().get(i);
//             AnswerModel actualResposta = actualForm.getAnswers().get(i);
//             assertEquals(expectedResposta.getAnswer(), actualResposta.getAnswer());
//         }

//         verify(formRepository, times(1)).save(any());
//         verify(answerRepository, times(1)).saveAll(any());
//         verify(companyRepository, times(1)).findById(companyId);
//     }

//     private static FormModel getFormModel() {
//         FormModel expectedFormModel = new FormModel();
//         expectedFormModel.setFinalScore(66);
//         expectedFormModel.setSocialScore(1);
//         expectedFormModel.setEnviornmentalScore(1);
//         expectedFormModel.setGovernmentScore(0);
//         expectedFormModel.setCertificate(CertificateLevelEnum.Bronze);

//         List<AnswerModel> answers = Arrays.asList(
//                 createAnswerModel(AnswersEnum.Conforme),
//                 createAnswerModel(AnswersEnum.Conforme),
//                 createAnswerModel(AnswersEnum.NaoConforme)
//         );

//         expectedFormModel.setAnswers(answers);
//         return expectedFormModel;
//     }

//     private static AnswerModel createAnswerModel(AnswersEnum answerEnum) {
//         AnswerModel answer = new AnswerModel();
//         answer.setAnswer(answerEnum);
//         return answer;
//     }

//     @Test
//     void testCalculateCertificateLevelGold() {
//         CertificateLevelEnum actualLevel = formService.calculateCertificateLevel(100);
//         assertEquals(CertificateLevelEnum.Ouro, actualLevel);
//     }

//     @Test
//     void testCalculateCertificateLevelSilver() {
//         CertificateLevelEnum actualLevel = formService.calculateCertificateLevel(80);
//         assertEquals(CertificateLevelEnum.Prata, actualLevel);
//     }

//     @Test
//     void testCalculateCertificateLevelBronze() {
//         CertificateLevelEnum actualLevel = formService.calculateCertificateLevel(60);
//         assertEquals(CertificateLevelEnum.Bronze, actualLevel);
//     }
// }
