package com.ambientese.grupo5.services;

import com.ambientese.grupo5.model.AnswerModel;
import com.ambientese.grupo5.model.CompanyModel;
import com.ambientese.grupo5.model.EvaluationModel;
import com.ambientese.grupo5.model.enums.PillarEnum;
import com.ambientese.grupo5.repository.CompanyRepository;
import com.ambientese.grupo5.repository.EvaluationRepository;
import com.itextpdf.text.DocumentException;

import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PDFService {

    private final CompanyRepository companyRepository;
    private final EvaluationRepository evaluationRepository;

    public PDFService(CompanyRepository companyRepository, 
                      EvaluationRepository evaluationRepository) {
        this.companyRepository = companyRepository;
        this.evaluationRepository = evaluationRepository;
    }

    public ByteArrayInputStream generatePdfFromHtml(String htmlContent) throws DocumentException, IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(htmlContent);
        renderer.layout();
        renderer.createPDF(outputStream);

        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    
    public ByteArrayInputStream generatePdf(Long companyId) throws DocumentException, IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer();

        CompanyModel company = companyRepository.findById(companyId).orElseThrow(() -> new RuntimeException("Empresa não encontrada"));
        EvaluationModel evaluation = evaluationRepository.findLatestEvaluationByCompanyId(companyId);

        PillarEnum social = PillarEnum.Social;
        PillarEnum government = PillarEnum.Governamental;
        PillarEnum enviornmental = PillarEnum.Ambiental;

        List<AnswerModel> answers = evaluation.getAnswers();
        List<AnswerModel> answersSocial = answers.stream().filter(answer -> answer.getQuestion().getPillar().equals(social)).collect(Collectors.toList());
        List<AnswerModel> answersGovernment = answers.stream().filter(answer -> answer.getQuestion().getPillar().equals(government)).collect(Collectors.toList());
        List<AnswerModel> answersEnviornmental = answers.stream().filter(answer -> answer.getQuestion().getPillar().equals(enviornmental)).collect(Collectors.toList());

        String socialPercentage = evaluation.getSocialScore() + "%";
        String governmentPercentage = evaluation.getGovernmentScore() + "%";
        String enviornmentalPercentage = evaluation.getEnviornmentalScore() + "%";

         String cssContent = "h1, h2 { text-align: center; }"
                + "table { width: 100%; border-collapse: collapse; }"
                + "th, td { border: 1px solid black; padding: 8px; text-align: left; }"
                + ".tables { margin: 20px 0; }"
                + ".page-break { page-break-before: always; }";

        String htmlContent = "<!DOCTYPE html>"
                + "<html>"
                + "<head>"
                + "<style>" + cssContent + "</style>"
                + "</head>"
                + "<body>"
                + "<div class=\"result-content\" id=\"result-content\">"
                + "<h1 class=\"title\">Resultados da Empresa " + company.getTradeName() + "</h1>"
                + "<div class=\"tables\">"
                + "<h2 class=\"title\">Social " + socialPercentage + "</h2>"
                + "<table class=\"table table-social\">"
                + generateTableHtml(answersSocial)
                + "</table>"
                + "<div class=\"page-break\"></div>"
                + "<h2 class=\"title\">Governamental " + governmentPercentage + "</h2>"
                + "<table class=\"table table-government\">"
                + generateTableHtml(answersGovernment)
                + "</table>"
                + "<div class=\"page-break\"></div>"
                + "<h2 class=\"title\">Ambiental " + enviornmentalPercentage + "</h2>"
                + "<table class=\"table table-enviornmental\">"
                + generateTableHtml(answersEnviornmental)
                + "</table>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";

        renderer.setDocumentFromString(htmlContent);
        renderer.layout();
        renderer.createPDF(outputStream);

        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    private String generateTableHtml(List<AnswerModel> answers) {
        StringBuilder tableHtml = new StringBuilder();
        tableHtml.append("<tr><th>Pergunta</th><th>Resposta</th></tr>");
        for (AnswerModel answer : answers) {
            tableHtml.append("<tr>")
                    .append("<td>").append(answer.getQuestion().getDescription()).append("</td>")
                    .append("<td>").append(answer.getAnswer()).append("</td>")
                    .append("</tr>");
        }
        return tableHtml.toString();
    }
}

