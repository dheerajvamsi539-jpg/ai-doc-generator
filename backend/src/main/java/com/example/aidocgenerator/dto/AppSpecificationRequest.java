package com.example.aidocgenerator.dto;

import jakarta.validation.constraints.NotBlank;

public record AppSpecificationRequest(
    @NotBlank(message = "Project Name is required") String projectName,
    @NotBlank(message = "Description is required") String description,
    String targetAudience,
    @NotBlank(message = "Key Features are required") String keyFeatures,
    String techStack
) {}
