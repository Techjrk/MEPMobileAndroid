package com.lecet.app.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import com.lecet.app.R;
import com.lecet.app.content.CompanyDetailActivity;
import com.lecet.app.content.ContactDetailActivity;
import com.lecet.app.content.ProjectDetailActivity;
import com.lecet.app.data.models.Company;
import com.lecet.app.data.models.Contact;
import com.lecet.app.data.models.Project;

/**
 * File: SearchItemRecentViewModel Created: 10/17/16 Author: domandtom
 * This code is copyright (c) 2016 Dom & Tom Inc.
 * This View Model is used
 */

public class SearchItemRecentViewModel extends BaseObservable {

    private Project project;
    private Company company;
    private Contact contact;
    private String mapsApiKey;
//    private SearchViewModel viewModel;
    private boolean isClientLocation2;

    /**
     * old code - for reference
     * public SearchItemRecentViewModel(Project project, String mapsApiKey) {
     * this.project = project;
     * this.mapsApiKey = mapsApiKey;
     * }
     */
    public SearchItemRecentViewModel(Project project, String mapsApiKey) {
        this.project = project;
        this.mapsApiKey = mapsApiKey;
    //    this.viewModel = viewModel;
        // this.company=null;
    }

    public SearchItemRecentViewModel(Company company, String mapsApiKey) {
        this.company = company;
        this.mapsApiKey = mapsApiKey;
    //    this.viewModel = viewModel;
        //   this.project =null;
    }

    public SearchItemRecentViewModel(Contact contact) {
        this.contact = contact;
     //   this.viewModel = viewModel;
    }


    ////////////////////////////////////
    // PROJECT


    public String getTitle() {
        //    if (project == null) return "Unknown";
        return project != null ? project.getTitle() : company != null ? company.getName() : "Unknown";
    }

    public String getProjectName() {
        return project.getTitle();
    }

    public String getClientLocation() {
        return project != null ? project.getCity() + " , " + project.getState() : company != null ? company.getAddress1() : "";
    }

    public String getClientLocation2() {
        if (project != null && project.getAddress2() != null && !project.getAddress2().trim().equals(""))
            setIsClientLocation2(true);
        else setIsClientLocation2(false);
        return project != null ? project.getAddress2() : "";
    }

    @Bindable
    public boolean getIsClientLocation2() {
        return isClientLocation2;
    }

    @Bindable
    public void setIsClientLocation2(boolean clientLocation2) {
        isClientLocation2 = clientLocation2;
//    notifyPropertyChanged(isClientLocation2);

    }
////////////////////////////////////
    // COMPANY

    public String getCompanyTitle() {
        if (company == null) return "--";
        return company.getName();
    }

    /**
     * Return 1st line street address e.g. 7215 NW 7th St
     */
    public String getCompanyAddress1() {
        if (company == null) return "--";
        return company.getAddress1();
    }

    /**
     * Return 2nd line city, state & zip+4 address e.g. Freemont, CA 10054-1234
     */
    public String getCompanyAddress2() {
        if (company == null) return "--";

        StringBuilder sb = new StringBuilder();

        // city
        if (company.getCity() != null) {
            sb.append(company.getCity());
        }

        // state
        if (company.getState() != null) {
            sb.append(", ");
            sb.append(company.getState());
        }

        // zip + 4
        if (company.getZipPlus4() != null) {
            sb.append(" ");
            String zip = company.getZipPlus4();
            if (zip.length() > 5) {
                sb.append(zip.substring(0, 5));
                sb.append("-");
                sb.append(zip.substring(5, zip.length() - 1)); //to remove the out-of-bounds exception, the value of 9 is changed to zip.length() - 1;
            } else sb.append(company.getZipPlus4());
        }

        return sb.toString();
    }


    ////////////////////////////////////
    // CONTACT

    public String getContactName() {
        if (contact == null) return "Unknown";
        if (contact.getName() == null) return "Unknown";
        if (contact.getName().equals("null")) return "";
        return contact.getName();
    }

    public String getContactDetail() {
        String title = "", company = "";
        if (contact == null) return "Unknown";
        if (contact.getTitle() != null) title = contact.getTitle().trim();
        if (contact.getCompany() != null) company = contact.getCompany().getName().trim();
        return title + ((title != "" && company != "") ? "," : " ");
    }

    public String getContactDetail2() {
        String company = "";
        if (contact == null) return "";
        if (contact.getCompany() != null) company = contact.getCompany().getName().trim();
        return company;
    }

    ////////////////////////////////////
    // MAP IMAGE URL - UNIVERSAL

    public String getMapUrl() {

        // project map
        if (project != null) {
            if (project.getGeocode() == null) {
                return null;
            } else
                return String.format("https://maps.googleapis.com/maps/api/staticmap?center=%.6f,%.6f&zoom=16&size=400x400&" +
                                "markers=color:blue|%.6f,%.6f&key=%s", project.getGeocode().getLat(), project.getGeocode().getLng(),
                        project.getGeocode().getLat(), project.getGeocode().getLng(), mapsApiKey);
        }

        // company map
        if (company != null) {
            if (company.getCity() == null || company.getState() == null) {
                return null;
            }

            //TODO - external
            String mapStr = null;
            StringBuilder sb = new StringBuilder();
            sb.append("https://maps.googleapis.com/maps/api/staticmap");
            sb.append("?zoom=16");
            sb.append("&size=200x200");
            sb.append("&markers=color:blue|");
            if (company.getAddress1() != null) sb.append(company.getAddress1() + ",");
            if (company.getAddress2() != null) sb.append(company.getAddress2() + ",");
            if (company.getCity() != null) sb.append(company.getCity() + ",");
            if (company.getState() != null) sb.append(company.getState());
            sb.append("&key=" + mapsApiKey);
            //mapStr = String.format((sb.toString().replace(" ", "+")), null);
            mapStr = sb.toString().replace(" ", "+");
            return mapStr;
        }

        return null;
    }
/*    public String getMapUrl(Company company) {
        CompanyDetailActivity activity = activityWeakReference.get();
        String mapStr;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("https://maps.googleapis.com/maps/api/staticmap");
        sb2.append("?center=");
        sb2.append(company.getAddress1() + ",");
        sb2.append(company.getAddress2() + ",");
        sb2.append(company.getCity() + ",");
        sb2.append(company.getState());
        sb2.append("&zoom=16");
        sb2.append("&size=200x200");
        sb2.append("&markers=color:blue|");
        sb2.append("&key=" + activity.getString(R.string.google_maps_key));
        mapStr = String.format((sb2.toString().replace(' ', '+')), null);
        return mapStr;
    }*/

    ////////////////////////////////////
    // CLICK HANDLERS

    public void onProjectSavedClick(View view) {
//           Toast.makeText(viewModel.getActivity(), "onClick: \nProject saved detail section", Toast.LENGTH_SHORT).show();
        if (project == null) {
            onCompanyClick(view); return;
        }
        Intent intent = new Intent(view.getContext(), ProjectDetailActivity.class);
        intent.putExtra(ProjectDetailActivity.PROJECT_ID_EXTRA, project.getId());
//        viewModel.getActivity().startActivity(intent);
        view.getContext().startActivity(intent);
    }

    public void onProjectClick(View view) {
        //  Toast.makeText(viewModel.getActivity(), "onClick: \nProject detail section", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(view.getContext(), ProjectDetailActivity.class);
        intent.putExtra(ProjectDetailActivity.PROJECT_ID_EXTRA, project.getId());
        //       viewModel.getActivity().startActivity(intent);
        view.getContext().startActivity(intent);

    }

    public void onCompanyClick(View view) {

        // Toast.makeText(viewModel.getActivity(), "onClick: \nCompany detail section", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(view.getContext(), CompanyDetailActivity.class);
        intent.putExtra(CompanyDetailActivity.COMPANY_ID_EXTRA, company.getId());
//        viewModel.getActivity().startActivity(intent);
        view.getContext().startActivity(intent);
    }

    public void onContactClick(View view) {
        Intent intent = new Intent(view.getContext(), ContactDetailActivity.class);
        intent.putExtra(ContactDetailActivity.CONTACT_ID_EXTRA, contact.getId());
//        viewModel.getActivity().startActivity(intent);
        view.getContext().startActivity(intent);
        //   Log.d("Contact id","Contact id"+contact.getId());
        //   Log.d("cid","cid"+contact.getCompanyId());
        //    Toast.makeText(viewModel.getActivity(), "onClick: Contact detail section"+contact.getId(), Toast.LENGTH_SHORT).show();
    }

}

