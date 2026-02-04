package com.bugbytes.moneytalks.business.validation;

public class ItemValidator {

    public void validate(String title, String description) {
        if (title == null || title.trim().isEmpty()) {
            throw new ValidationException("Title is required.");
        }
        if (title.trim().length() > 60) {
            throw new ValidationException("Title must be 60 characters or less.");
        }
        if (description != null && description.length() > 500) {
            throw new ValidationException("Description must be 500 characters or less.");
        }
    }
}
