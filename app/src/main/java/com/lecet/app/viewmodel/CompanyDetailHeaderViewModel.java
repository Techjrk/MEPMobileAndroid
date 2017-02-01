package com.lecet.app.viewmodel;

import com.lecet.app.data.models.Company;

/**
 * File: CompanyDetailHeaderViewModel Created: 1/24/17 Author: domandtom
 *
 * This code is copyright (c) 2017 Dom & Tom Inc.
 */

public class CompanyDetailHeaderViewModel {

    private final Company company;

    public CompanyDetailHeaderViewModel(Company company) {

        this.company = company;
    }

    public String getTitle() {

        return company.getName();
    }
}
