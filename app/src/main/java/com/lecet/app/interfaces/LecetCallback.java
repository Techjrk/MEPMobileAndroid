package com.lecet.app.interfaces;

/**
 * File: LecetCallback Created: 10/11/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public interface LecetCallback<T> {

    void onSuccess(T result);
    void onFailure(int code, String message);
}
