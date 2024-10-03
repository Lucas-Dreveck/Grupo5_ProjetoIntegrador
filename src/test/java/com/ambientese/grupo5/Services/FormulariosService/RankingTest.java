// package com.ambientese.grupo5.Services.FormulariosService;

// import com.ambientese.grupo5.Model.FormModel;
// import com.ambientese.grupo5.Repository.FormRepository;
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
//     private FormRepository formRepository;

//     @InjectMocks
//     private RankingService rankingService;

//     @Test
//     void sortByScoreTest() {
//         List<FormModel> formMock = new ArrayList<>();
//         formMock.add(new FormModel());
//         formMock.add(new FormModel());

//         when(formRepository.findAllByOrderByFinalScoreAsc()).thenReturn(formMock);

//         // Chamando o método a ser testado
//         List<FormModel> forms = rankingService.sortByScore();

//         // Verificando se o método retorna a lista correta de formulários ordenada por pontuação final
//         assertEquals(formMock, forms);
//     }

//     // Adicione mais testes para os outros métodos de classificação, seguindo o mesmo padrão
// }
