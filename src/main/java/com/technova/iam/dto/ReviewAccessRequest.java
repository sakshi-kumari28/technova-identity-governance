package com.technova.iam.dto;

import jakarta.validation.constraints.NotBlank;

public class ReviewAccessRequest {

    @NotBlank(message = "Decision is required")
    private String decision;

    private String reviewComment;

    public ReviewAccessRequest() {
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

    public String getReviewComment() {
        return reviewComment;
    }

    public void setReviewComment(String reviewComment) {
        this.reviewComment = reviewComment;
    }
}