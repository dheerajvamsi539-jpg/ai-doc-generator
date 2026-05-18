package com.example.aidocgenerator.controller;

import com.example.aidocgenerator.dto.AppSpecificationRequest;
import com.example.aidocgenerator.service.DocumentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.Map;

@RestController
@RequestMapping("/api/documents")
@CrossOrigin(origins = "http://localhost:4200")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping("/generate")
    public ResponseEntity<Map<String, String>> generateDocument(@Valid @RequestBody AppSpecificationRequest request) {
        String generatedDoc = documentService.generateAiPromptDocument(request);
        return ResponseEntity.ok(Map.of("document", generatedDoc));
    }

    @PostMapping(value = "/pdf", produces = org.springframework.http.MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> generatePdf(@Valid @RequestBody AppSpecificationRequest request) {
        byte[] pdfBytes = documentService.generatePdfDocument(request);
        
        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_PDF);
        String filename = "AI_App_Spec.pdf";
        if (request.projectName() != null && !request.projectName().isBlank()) {
            filename = request.projectName().replaceAll("\\s+", "_") + ".pdf";
        }
        headers.setContentDispositionFormData("attachment", filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        
        return new ResponseEntity<>(pdfBytes, headers, org.springframework.http.HttpStatus.OK);
    }
}
