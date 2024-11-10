package com.ambientese.grupo5.services;

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

import com.ambientese.grupo5.model.CompanyModel;
import com.ambientese.grupo5.model.EvaluationModel;
import com.ambientese.grupo5.model.AnswerModel;
import com.ambientese.grupo5.model.QuestionModel;
import com.ambientese.grupo5.model.AnswerId;
import com.ambientese.grupo5.model.enums.PillarEnum;
import com.ambientese.grupo5.model.enums.AnswersEnum;
import com.ambientese.grupo5.repository.CompanyRepository;
import com.ambientese.grupo5.repository.EvaluationRepository;
import com.itextpdf.text.DocumentException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class PDFServiceTest {

    @Autowired
    private PDFService exportPDFService;

    @MockBean
    private CompanyRepository companyRepository;

    @MockBean
    private EvaluationRepository evaluationRepository;

    @Test
    void testGeneratePdf() throws DocumentException, IOException {
        CompanyModel companyMock = new CompanyModel();
        companyMock.setId(1L);
        companyMock.setTradeName("Empresa Teste");
        Mockito.when(companyRepository.findById(1L)).thenReturn(Optional.of(companyMock));

        EvaluationModel evaluationMock = new EvaluationModel();
        evaluationMock.setId(1L);

        QuestionModel socialQuestion = new QuestionModel();
        socialQuestion.setPillar(PillarEnum.Social);
        socialQuestion.setDescription("Pergunta Social 1");

        AnswerModel socialAnswer = new AnswerModel();
        socialAnswer.setQuestion(socialQuestion);
        socialAnswer.setAnswer(AnswersEnum.Conforme);

        List<AnswerModel> answers = new ArrayList<>();
        answers.add(socialAnswer);

        evaluationMock.setAnswers(answers);
        Mockito.when(evaluationRepository.findLatestEvaluationByCompanyId(1L)).thenReturn(evaluationMock);

        ByteArrayInputStream pdfStream = exportPDFService.generatePdf(1L);

        assertNotNull(pdfStream, "O PDF gerado não pode ser nulo");
    }

    @Test
    void testGeneratePdfWithoutAnswers() throws DocumentException, IOException {
        CompanyModel companyMock = new CompanyModel();
        companyMock.setId(2L);
        companyMock.setTradeName("Empresa Sem Respostas");
        Mockito.when(companyRepository.findById(1L)).thenReturn(Optional.of(companyMock));

        EvaluationModel evaluationMock = new EvaluationModel();
        evaluationMock.setId(2L);
        evaluationMock.setAnswers(new ArrayList<>()); // Simulando sem answers

        Mockito.when(evaluationRepository.findLatestEvaluationByCompanyId(1L)).thenReturn(evaluationMock);

        ByteArrayInputStream pdfStream = exportPDFService.generatePdf(1L);

        assertNotNull(pdfStream, "O PDF gerado não pode ser nulo");
    }

    @Test
    void testGeneratePdfMultipage() throws DocumentException, IOException {
        CompanyModel companyMock = new CompanyModel();
        companyMock.setId(3L);
        companyMock.setTradeName("Empresa Teste");
        Mockito.when(companyRepository.findById(1L)).thenReturn(Optional.of(companyMock));

        EvaluationModel evaluationMock = new EvaluationModel();
        evaluationMock.setId(3L);

        List<AnswerModel> answers = new ArrayList<>();
        for (int i = 0; i < 100; i++) { // Simulando 100 answers
            AnswerModel answer = new AnswerModel();
            answer.setId(new AnswerId() {
                private static final long serialVersionUID = 1L;
                {
                    setEvaluationId(1L);
                    setQuestionId(getQuestionId());
                }
            });
            answer.setAnswer(AnswersEnum.Conforme);
            answer.setQuestion(new QuestionModel() {
                {
                    setDescription("Pergunta");
                    setPillar(PillarEnum.Social);
                }
            });
            answers.add(answer);

        }
        evaluationMock.setAnswers(answers);

        Mockito.when(evaluationRepository.findLatestEvaluationByCompanyId(1L)).thenReturn(evaluationMock);

        ByteArrayInputStream pdfStream = exportPDFService.generatePdf(1L);

        assertNotNull(pdfStream, "O PDF gerado não pode ser nulo");
    }

    @Test
    void testGeneratePdfCompanyNotFound() {
        Mockito.when(companyRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            exportPDFService.generatePdf(999L);
        });
    }
}
