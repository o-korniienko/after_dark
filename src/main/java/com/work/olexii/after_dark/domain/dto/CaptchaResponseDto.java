package com.work.olexii.after_dark.domain.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CaptchaResponseDto {

    private boolean success;
    @JsonAlias("error-codes")
    private Set<String> errorCodes;


    public boolean isSuccess() {
        return success;
    }

    public void setSucces(boolean success) {
        this.success = success;
    }

    public Set<String> getErrorCodes() {
        return errorCodes;
    }

    public void setErrorCodes(Set<String> errorCodes) {
        this.errorCodes = errorCodes;
    }

    @Override
    public String toString() {
        return "CaptchaResponseDto{" +
                "succes=" + success +
                ", errorCodes=" + errorCodes +
                '}';
    }
}
