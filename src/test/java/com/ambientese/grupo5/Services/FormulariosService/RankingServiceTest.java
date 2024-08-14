package com.ambientese.grupo5.Services.FormulariosService;

import com.ambientese.grupo5.Model.FormularioModel;
import com.ambientese.grupo5.Repository.FormularioRepository;
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
    private FormularioRepository formularioRepository;

    @InjectMocks
    private RankingService rankingService;

    @Test
    void classificarPorPontuacao_DeveRetornarListaDeFormulariosOrdenadaPorPontuacaoFinal() {
        // Mockando a resposta do repositório
        List<FormularioModel> formularioMock = new ArrayList<>();
        formularioMock.add(new FormularioModel(/* Adicione aqui os parâmetros para criar um objeto FormularioModel */));
        formularioMock.add(new FormularioModel(/* Adicione aqui os parâmetros para criar um objeto FormularioModel */));
        // Adicione mais formulários conforme necessário

        when(formularioRepository.findAllByOrderByPontuacaoFinalAsc()).thenReturn(formularioMock);

        // Chamando o método a ser testado
        List<FormularioModel> formularios = rankingService.classificarPorPontuacao();

        // Verificando se o método retorna a lista correta de formulários ordenada por pontuação final
        assertEquals(formularioMock, formularios);
    }

    // Adicione mais testes para os outros métodos de classificação, seguindo o mesmo padrão
}
