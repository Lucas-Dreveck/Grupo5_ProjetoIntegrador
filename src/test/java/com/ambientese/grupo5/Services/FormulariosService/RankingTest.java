// package com.ambientese.grupo5.Services.EvaluationulariosService;

// import com.ambientese.grupo5.Model.EvaluationModel;
// import com.ambientese.grupo5.Repository.EvaluationRepository;
// import com.ambientese.grupo5.Services.RankingService;

// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;

// import java.util.ArrayList;
// import java.util.List;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.mockito.Mockito.when;

// @ExtendWith(MockitoExtension.class)
// class RankingTest {

//     @Mock
//     private EvaluationRepository evaluationRepository;

//     @InjectMocks
//     private RankingService rankingService;

//     @Test
//     void sortByScoreTest() {
//         List<EvaluationModel> evaluationMock = new ArrayList<>();
//         evaluationMock.add(new EvaluationModel());
//         evaluationMock.add(new EvaluationModel());

//         when(evaluationRepository.findAllByOrderByFinalScoreAsc()).thenReturn(evaluationMock);

//         // Chamando o método a ser testado
//         List<EvaluationModel> evaluations = rankingService.sortByScore();

//         // Verificando se o método retorna a lista correta de formulários ordenada por pontuação final
//         assertEquals(evaluationMock, evaluations);
//     }

//     // Adicione mais testes para os outros métodos de classificação, seguindo o mesmo padrão
// }
