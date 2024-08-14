package com.ambientese.grupo5.Services.Export;

import com.ambientese.grupo5.Model.EmpresaModel;
import com.ambientese.grupo5.Model.FormularioModel;
import com.ambientese.grupo5.Model.RespostaModel;
import com.ambientese.grupo5.Model.Enums.EixoEnum;
import com.ambientese.grupo5.Repository.EmpresaRepository;
import com.ambientese.grupo5.Repository.FormularioRepository;
import com.itextpdf.text.DocumentException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExportPDFService {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private FormularioRepository formularioRepository;

    public ByteArrayInputStream generatePdfFromHtml(String htmlContent) throws DocumentException, IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(htmlContent);
        renderer.layout();
        renderer.createPDF(outputStream);

        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    
    public ByteArrayInputStream generatePdf(Long empresaId) throws DocumentException, IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer();

        EmpresaModel empresa = empresaRepository.findById(empresaId).orElseThrow(() -> new RuntimeException("Empresa não encontrada"));
        FormularioModel formulario = formularioRepository.findLatestFormByEmpresaId(empresaId);

        EixoEnum social = EixoEnum.Social;
        EixoEnum governamental = EixoEnum.Governamental;
        EixoEnum ambiental = EixoEnum.Ambiental;

        List<RespostaModel> respostas = formulario.getRespostas();
        List<RespostaModel> respostasSocial = respostas.stream().filter(resposta -> resposta.getPergunta().getEixo().equals(social)).collect(Collectors.toList());
        List<RespostaModel> respostasGovernamental = respostas.stream().filter(resposta -> resposta.getPergunta().getEixo().equals(governamental)).collect(Collectors.toList());
        List<RespostaModel> respostasAmbiental = respostas.stream().filter(resposta -> resposta.getPergunta().getEixo().equals(ambiental)).collect(Collectors.toList());

        String socialPercentage = formulario.getPontuacaoSocial() + "%";
        String governamentalPercentage = formulario.getPontuacaoGovernamental() + "%";
        String ambientalPercentage = formulario.getPontuacaoAmbiental() + "%";

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
                + "<h1 class=\"title\">Resultados da Empresa " + empresa.getNomeFantasia() + "</h1>"
                + "<div class=\"tables\">"
                + "<h2 class=\"title\">Social " + socialPercentage + "</h2>"
                + "<table class=\"table table-social\">"
                + generateTableHtml(respostasSocial)
                + "</table>"
                + "<div class=\"page-break\"></div>"
                + "<h2 class=\"title\">Governamental " + governamentalPercentage + "</h2>"
                + "<table class=\"table table-governamental\">"
                + generateTableHtml(respostasGovernamental)
                + "</table>"
                + "<div class=\"page-break\"></div>"
                + "<h2 class=\"title\">Ambiental " + ambientalPercentage + "</h2>"
                + "<table class=\"table table-ambiental\">"
                + generateTableHtml(respostasAmbiental)
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

    private String generateTableHtml(List<RespostaModel> respostas) {
        StringBuilder tableHtml = new StringBuilder();
        tableHtml.append("<tr><th>Pergunta</th><th>Resposta</th></tr>");
        for (RespostaModel resposta : respostas) {
            tableHtml.append("<tr>")
                    .append("<td>").append(resposta.getPergunta().getDescricao()).append("</td>")
                    .append("<td>").append(resposta.getResposta()).append("</td>")
                    .append("</tr>");
        }
        return tableHtml.toString();
    }
}

