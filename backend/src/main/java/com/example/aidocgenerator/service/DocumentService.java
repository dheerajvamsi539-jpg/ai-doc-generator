package com.example.aidocgenerator.service;

import com.example.aidocgenerator.dto.AppSpecificationRequest;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class DocumentService {

    private final ChatClient chatClient;

    public DocumentService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public String generateAiPromptDocument(AppSpecificationRequest request) {
        String prompt = """
            # AI Application Development Specification
            
            You are an expert AI software developer. Your task is to develop an application based on the following specifications.
            
            ## 1. Project Overview
            **Project Name:** %s
            **Description:** %s
            **Target Audience:** %s
            
            ## 2. Key Features
            The application must implement the following core features:
            %s
            
            ## 3. Technology Stack
            Please use the following technologies, frameworks, and languages:
            %s
            
            ## 4. Instructions for AI Agent
            1. Begin by outlining the system architecture.
            2. Provide the folder structure for the application.
            3. Generate the required configuration files (e.g., pom.xml, package.json).
            4. Implement the core business logic based on the 'Key Features' section.
            5. Provide instructions on how to build and run the application.
            
            Please output your response in well-formatted Markdown with valid code blocks.
            """.formatted(
                request.projectName(),
                request.description(),
                request.targetAudience(),
                request.keyFeatures(),
                request.techStack()
            );
            
        // Call AI model to generate the actual specification document
        return chatClient.prompt()
                .user(prompt)
                .call()
                .content();
    }
    
    public byte[] generatePdfDocument(AppSpecificationRequest request) {
        String markdown = generateAiPromptDocument(request);
        try (java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream()) {
            com.lowagie.text.Document document = new com.lowagie.text.Document();
            com.lowagie.text.pdf.PdfWriter.getInstance(document, out);
            document.open();
            
            com.lowagie.text.Font titleFont = com.lowagie.text.FontFactory.getFont(com.lowagie.text.FontFactory.HELVETICA_BOLD, 18);
            com.lowagie.text.Font normalFont = com.lowagie.text.FontFactory.getFont(com.lowagie.text.FontFactory.HELVETICA, 12);
            
            document.add(new com.lowagie.text.Paragraph("AI Application Development Specification", titleFont));
            document.add(new com.lowagie.text.Paragraph("\n"));
            document.add(new com.lowagie.text.Paragraph(markdown, normalFont));
            
            document.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF", e);
        }
    }
}
