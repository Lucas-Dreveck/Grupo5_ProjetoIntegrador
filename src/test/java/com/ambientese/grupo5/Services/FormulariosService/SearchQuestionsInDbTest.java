// package com.ambientese.grupo5.Services.FormulariosService;

// import com.ambientese.grupo5.Model.Enums.PillarEnum;
// import com.ambientese.grupo5.DTO.FormResponse;
// import com.ambientese.grupo5.Model.QuestionModel;
// import com.ambientese.grupo5.Repository.QuestionRepository;
// import com.ambientese.grupo5.Services.FormService;

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
//     private FormService formService;

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

//         FormResponse questions = formService.searchQuestionsInDb(true, 1L);

//         if (questions.getFormRequests().size() > 0) {
//             assertEquals(15, questions.getFormRequests().size());
//         } else {
//             assertEquals(15, questions.getQuestions().size());
//         }
//     }
// }
