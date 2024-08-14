package com.ambientese.grupo5.Controller.Export;

import com.ambientese.grupo5.Services.Export.ExportPDFService;
import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/pdf")
public class ExportPDFController {

    @Autowired
    private ExportPDFService exportPDFService;

    @PostMapping("/generatePdf")
    public ResponseEntity<byte[]> exportPdf(@RequestBody Map<String, String> payload) throws DocumentException, IOException {
        String htmlContent = payload.get("htmlContent");

        ByteArrayInputStream bis = exportPDFService.generatePdfFromHtml(htmlContent);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=avaliacao.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(bis.readAllBytes());
    }

    @GetMapping("/getPdf/{empresaId}")
    public ResponseEntity<byte[]> testPdf(@PathVariable("empresaId") Long empresaId){
        try {
            ByteArrayInputStream bis = exportPDFService.generatePdf(empresaId);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=avaliacao.pdf");

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(bis.readAllBytes());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
}
