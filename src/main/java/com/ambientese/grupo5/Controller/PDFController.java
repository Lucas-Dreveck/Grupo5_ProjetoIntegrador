package com.ambientese.grupo5.Controller;

import com.ambientese.grupo5.Services.PDFService;
import com.itextpdf.text.DocumentException;

import io.swagger.v3.oas.annotations.tags.Tag;

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
@RequestMapping("/api/pdf")
@Tag(name = "PDF", description = "Endpoints para gerar PDFs")
public class PDFController {

    private final PDFService pdfService;

    public PDFController(PDFService pdfService) {
        this.pdfService = pdfService;
    }

    @PostMapping
    public ResponseEntity<byte[]> exportPdf(@RequestBody Map<String, String> payload) throws DocumentException, IOException {
        String htmlContent = payload.get("htmlContent");

        ByteArrayInputStream bis = pdfService.generatePdfFromHtml(htmlContent);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=evaluation.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(bis.readAllBytes());
    }

    @GetMapping("/{companyId}")
    public ResponseEntity<byte[]> testPdf(@PathVariable("companyId") Long companyId){
        try {
            ByteArrayInputStream bis = pdfService.generatePdf(companyId);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=evaluation.pdf");

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
