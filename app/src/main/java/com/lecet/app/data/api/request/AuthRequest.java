package com.lecet.app.data.api.request;

import com.google.gson.annotations.SerializedName;

/*
 * File: AuthRequest Created: 8/16/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */
public class AuthRequest {

    @SerializedName("grant_type")
    private String grantType;

    @SerializedName("client_id")
    private String clientID;

    @SerializedName("client_secret")
    private String clientSecret;

    @SerializedName("username")
    private String userName;

    @SerializedName("password")
    private String password;

    @SerializedName("refresh_token")
    private String refreshToken;

    public AuthRequest(){}

    private AuthRequest(Builder builder) {

        this.grantType = builder.grantType;
        this.clientID = builder.clientID;
        this.clientSecret = builder.clientSecret;
        this.userName = builder.userName;
        this.password = builder.password;
        this.refreshToken = builder.refreshToken;
    }

    public String getGrantType() {
        return grantType;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getClientID() {
        return clientID;
    }

    public String getPassword() {
        return password;
    }

    public String getUserName() {
        return userName;
    }

    public static class Builder {

        private String grantType;
        private String clientID;
        private String clientSecret;
        private String userName;
        private String password;
        private String refreshToken;

        public Builder(String grantType){
            this.grantType = grantType;
        }

        public Builder clientID(String clientID) {

            this.clientID = clientID;
            return this;
        }

        public Builder clientSecret(String clientSecret) {

            this.clientSecret = clientSecret;
            return this;
        }

        public Builder userName(String userName) {

            this.userName = userName;
            return this;
        }

        public Builder password(String password) {

            this.password = password;
            return this;
        }

        public Builder refreshToken(String refreshToken) {

            this.refreshToken = refreshToken;
            return this;
        }

        public AuthRequest build() {

            return new AuthRequest(this);
        }
    }
}
