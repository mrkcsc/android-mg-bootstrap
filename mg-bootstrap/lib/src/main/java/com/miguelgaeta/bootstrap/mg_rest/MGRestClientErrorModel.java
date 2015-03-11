package com.miguelgaeta.bootstrap.mg_rest;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;

/**
 * A standard error model that all REST endpoints
 * should respect. Created by mrkcsc on 2/10/15.
 */
@SuppressWarnings("UnusedDeclaration")
public class MGRestClientErrorModel {

    @Getter @SerializedName("error")
    private HCModelErrorDetails error;

    public static class HCModelErrorDetails {

        @Getter @SerializedName("message")
        private String message;

        @Getter @SerializedName("type")
        private String type;
    }
}
