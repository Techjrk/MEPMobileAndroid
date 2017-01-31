package com.lecet.app.data.api.request;

/**
 * File: ChangePasswordRequest Created: 1/27/17 Author: domandtom
 *
 * This code is copyright (c) 2017 Dom & Tom Inc.
 */

public class ChangePasswordRequest {

    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
    public ChangePasswordRequest(String oldPassword, String newPassword, String confirmPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }
    @Override
    public String toString() {
        return "ChangePasswordRequest{" +
                "oldPassword='" + oldPassword + '\'' +
                ", newPassword='" + newPassword + '\'' +
                ", confirmPassword='" + confirmPassword +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChangePasswordRequest)) return false;

        ChangePasswordRequest that = (ChangePasswordRequest) o;

        if (oldPassword != null ? !oldPassword.equals(that.oldPassword) : that.oldPassword != null)
            return false;
        if (newPassword != null ? !newPassword.equals(that.newPassword) : that.newPassword != null)
            return false;
        return confirmPassword != null ? confirmPassword.equals(that.confirmPassword) : that.confirmPassword == null;

    }

    @Override
    public int hashCode() {
        int result = oldPassword != null ? oldPassword.hashCode() : 0;
        result = 31 * result + (newPassword != null ? newPassword.hashCode() : 0);
        result = 31 * result + (confirmPassword != null ? confirmPassword.hashCode() : 0);
        return result;
    }
}
