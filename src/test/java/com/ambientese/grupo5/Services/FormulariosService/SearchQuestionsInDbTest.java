// package com.ambientese.grupo5.Services.EvaluationulariosService;

// import com.ambientese.grupo5.Model.Enums.PillarEnum;
// import com.ambientese.grupo5.DTO.EvaluationResponse;
// import com.ambientese.grupo5.Model.QuestionModel;
// import com.ambientese.grupo5.Repository.QuestionRepository;
// import com.ambientese.grupo5.Services.EvaluationService;

// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;

// import java.util.Arrays;
// import java.util.List;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.mockito.Mockito.when;

// @ExtendWith(MockitoExtension.class)
// class SearchQuestionsInDbTest {

//     @Mock
//     private QuestionRepository questionRepository;

//     @InjectMocks
//     private EvaluationService evaluationService;

//     @Test
//     void testSearchQuestionsInDb() {
//         List<QuestionModel> questionsMock = Arrays.asList(
//                 new QuestionModel("Pergunta 1", PillarEnum.Social),
//                 new QuestionModel("Pergunta 2", PillarEnum.Ambiental),
//                 new QuestionModel("Pergunta 3", PillarEnum.Governamental),
//                 new QuestionModel("Pergunta 4", PillarEnum.Social),
//                 new QuestionModel("Pergunta 5", PillarEnum.Ambiental)
//         );
//         when(questionRepository.findByPillar(PillarEnum.Social)).thenReturn(questionsMock);
//         when(questionRepository.findByPillar(PillarEnum.Ambiental)).thenReturn(questionsMock);
//         when(questionRepository.findByPillar(PillarEnum.Governamental)).thenReturn(questionsMock);

//         EvaluationResponse questions = evaluationService.searchQuestionsInDb(true, 1L);

//         if (questions.getEvaluationRequests().size() > 0) {
//             assertEquals(15, questions.getEvaluationRequests().size());
//         } else {
//             assertEquals(15, questions.getQuestions().size());
//         }
//     }
// }
