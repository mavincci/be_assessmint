package com.assessmint.be.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum EmailTemplate {
    WELCOME("email/welcome"),
    INVITATION("email/invitation"),
    RESULT_ANNOUNCEMENT("email/result_announcement"),
    FORGOT_PASSWORD("email/forgot-password");

    public final String templateName;
}
