package com.ambientese.grupo5.Services.Export;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.context.SpringBootTest;

import com.ambientese.grupo5.Model.EmpresaModel;
import com.ambientese.grupo5.Model.FormularioModel;
import com.ambientese.grupo5.Model.RespostaModel;
import com.ambientese.grupo5.Model.PerguntasModel;
import com.ambientese.grupo5.Model.RespostaId;
import com.ambientese.grupo5.Model.Enums.EixoEnum;
import com.ambientese.grupo5.Model.Enums.RespostasEnum;
import com.ambientese.grupo5.Repository.EmpresaRepository;
import com.ambientese.grupo5.Repository.FormularioRepository;
import com.itextpdf.text.DocumentException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class ExportPDFServiceTest {

    @Autowired
    private ExportPDFService exportPDFService;

    @MockBean
    private EmpresaRepository empresaRepository;

    @MockBean
    private FormularioRepository formularioRepository;

    @Test
    public void testGeneratePdf() throws DocumentException, IOException {
        EmpresaModel mockEmpresa = new EmpresaModel();
        mockEmpresa.setId(1L);
        mockEmpresa.setNomeFantasia("Empresa Teste");
        Mockito.when(empresaRepository.findById(1L)).thenReturn(Optional.of(mockEmpresa));

        FormularioModel mockFormulario = new FormularioModel();
        mockFormulario.setId(1L);

        PerguntasModel perguntaSocial = new PerguntasModel();
        perguntaSocial.setEixo(EixoEnum.Social);
        perguntaSocial.setDescricao("Pergunta Social 1");

        RespostaModel respostaSocial = new RespostaModel();
        respostaSocial.setPergunta(perguntaSocial);
        respostaSocial.setResposta(RespostasEnum.Conforme);

        List<RespostaModel> respostas = new ArrayList<>();
        respostas.add(respostaSocial);

        mockFormulario.setRespostas(respostas);
        Mockito.when(formularioRepository.findLatestFormByEmpresaId(1L)).thenReturn(mockFormulario);

        ByteArrayInputStream pdfStream = exportPDFService.generatePdf(1L);

        assertNotNull(pdfStream, "O PDF gerado não pode ser nulo");
    }

    @Test
    public void testGeneratePdfSemRespostas() throws DocumentException, IOException {
        EmpresaModel mockEmpresa = new EmpresaModel();
        mockEmpresa.setId(2L);
        mockEmpresa.setNomeFantasia("Empresa Sem Respostas");
        Mockito.when(empresaRepository.findById(1L)).thenReturn(Optional.of(mockEmpresa));

        FormularioModel mockFormulario = new FormularioModel();
        mockFormulario.setId(2L);
        mockFormulario.setRespostas(new ArrayList<>()); // Simulando sem respostas

        Mockito.when(formularioRepository.findLatestFormByEmpresaId(1L)).thenReturn(mockFormulario);

        ByteArrayInputStream pdfStream = exportPDFService.generatePdf(1L);

        assertNotNull(pdfStream, "O PDF gerado não pode ser nulo");
    }

    @Test
    public void testGeneratePdfMultipage() throws DocumentException, IOException {
        EmpresaModel mockEmpresa = new EmpresaModel();
        mockEmpresa.setId(3L);
        mockEmpresa.setNomeFantasia("Empresa Teste");
        Mockito.when(empresaRepository.findById(1L)).thenReturn(Optional.of(mockEmpresa));

        FormularioModel mockFormulario = new FormularioModel();
        mockFormulario.setId(3L);

        List<RespostaModel> respostas = new ArrayList<>();
        for (int i = 0; i < 100; i++) { // Simulando 100 respostas
            RespostaModel resposta = new RespostaModel();
            resposta.setId(new RespostaId() {
                private static final long serialVersionUID = 1L;
                {
                    setFormularioId(1L);
                    setPerguntaId(getPerguntaId());
                }
            });
            resposta.setResposta(RespostasEnum.Conforme);
            resposta.setPergunta(new PerguntasModel() {
                {
                    setDescricao("Pergunta");
                    setEixo(EixoEnum.Social);
                }
            });
            respostas.add(resposta);

        }
        mockFormulario.setRespostas(respostas);

        Mockito.when(formularioRepository.findLatestFormByEmpresaId(1L)).thenReturn(mockFormulario);

        ByteArrayInputStream pdfStream = exportPDFService.generatePdf(1L);

        assertNotNull(pdfStream, "O PDF gerado não pode ser nulo");
    }

    @Test
    public void testGeneratePdfEmpresaNotFound() {
        Mockito.when(empresaRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            exportPDFService.generatePdf(999L);
        });
    }
}
