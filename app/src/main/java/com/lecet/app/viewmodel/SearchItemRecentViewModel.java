package com.lecet.app.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import com.lecet.app.content.CompanyDetailActivity;
import com.lecet.app.content.ContactDetailActivity;
import com.lecet.app.content.ProjectDetailActivity;
import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.models.Company;
import com.lecet.app.data.models.Contact;
import com.lecet.app.data.models.Project;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.domain.ProjectDomain;
import com.lecet.app.domain.SearchDomain;
import com.lecet.app.utility.Log;

import java.io.IOException;
import java.io.Serializable;

import io.realm.Realm;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    private boolean isClientLocation2;
    SearchDomain searchDomain;
    SearchViewModel viewModel;
    private boolean hasStarCard;
    ProjectDomain projectDomain;

    /**
     * This is for viewing all section for project, company and contacts with SearchViewModel
     *
     * @param project
     * @param mapsApiKey
     * @param viewModel
     */
    public SearchItemRecentViewModel(Project project, String mapsApiKey, SearchViewModel viewModel) {
        this.project = project;
        this.mapsApiKey = mapsApiKey;
        searchDomain = new SearchDomain(LecetClient.getInstance(), Realm.getDefaultInstance());
        this.viewModel = viewModel;
        projectDomain = new ProjectDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(viewModel.getActivity().getApplication()), Realm.getDefaultInstance());
    }

    public SearchItemRecentViewModel(Company company, String mapsApiKey, SearchViewModel viewModel) {
        this.company = company;
        this.mapsApiKey = mapsApiKey;
        searchDomain = new SearchDomain(LecetClient.getInstance(), Realm.getDefaultInstance());
        this.viewModel = viewModel;
    }

    public SearchItemRecentViewModel(Contact contact, SearchViewModel viewModel) {
        this.contact = contact;
        this.viewModel = viewModel;
    }

    ////////////////////////////////////
    // PROJECT

    @Bindable
    public boolean getHasStarCard() {
        if (project != null) {
            if (project.getUserNotes() != null && project.getUserNotes().size() > 0) {
                hasStarCard = true;
            } else if (project.getImages() != null && project.getImages().size() > 0) {
                hasStarCard = true;
            }
        }
        return hasStarCard;
    }

    public String getTitle() {
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
        return title + ((!title.equals("") && company.equals("")) ? "," : " ");
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
                return String.format("https://maps.googleapis.com/maps/api/staticmap?center=%.6f,%.6f&zoom=20&size=400x400&" +
                                "markers=icon:"+getMarkerIcon(project)+"|%.6f,%.6f&key=%s", project.getGeocode().getLat(), project.getGeocode().getLng(),
                        project.getGeocode().getLat(), project.getGeocode().getLng(), mapsApiKey);
        }

        // company map
        if (company != null) {
            if (company.getCity() == null || company.getState() == null) {
                return null;
            }

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
    private String getMarkerIcon(Project project) {
        StringBuilder urlBuilder = new StringBuilder(BidItemViewModel.url);
        boolean hasUpdates = projectHasUpdates(project);

        // for standard projects, i.e. with Dodge numbers
        if(project.getDodgeNumber() != null) {

            if (project.getProjectStage() == null) {
                urlBuilder.append(hasUpdates ? BidItemViewModel.STANDARD_PRE_BID_MARKER_UPDATE : BidItemViewModel.STANDARD_PRE_BID_MARKER);
            }

            // style marker for pre-bid or post-bid color
            else {
                if (project.getProjectStage().getParentId() == 102) {
                    urlBuilder.append(hasUpdates ? BidItemViewModel.STANDARD_PRE_BID_MARKER_UPDATE : BidItemViewModel.STANDARD_PRE_BID_MARKER);
                }
                else {
                    urlBuilder.append(hasUpdates ? BidItemViewModel.STANDARD_POST_BID_MARKER_UPDATE : BidItemViewModel.STANDARD_POST_BID_MARKER);
                }
            }
        }

        // for custom (user-created) projects, which have no Dodge number
        else {
            // pre-bid user-created projects
            if(project.getProjectStage() == null) {
                urlBuilder.append(hasUpdates ? BidItemViewModel.CUSTOM_PRE_BID_MARKER_UPDATE : BidItemViewModel.CUSTOM_PRE_BID_MARKER);

            }

            // post-bid user-created projects
            else {
                if(project.getProjectStage().getParentId() == 102) {
                    urlBuilder.append(hasUpdates ? BidItemViewModel.CUSTOM_PRE_BID_MARKER_UPDATE : BidItemViewModel.CUSTOM_PRE_BID_MARKER);
                }
                else {
                    urlBuilder.append(hasUpdates ? BidItemViewModel.CUSTOM_POST_BID_MARKER_UPDATE : BidItemViewModel.CUSTOM_POST_BID_MARKER);
                }
            }
        }
        return urlBuilder.toString();
    }
    private boolean projectHasUpdates(Project project) {
        if(project == null) return false;
        if(project.getUserNotes() == null || project.getImages() == null) {
            return false;
        }
        return (project.getImages().size() > 0 || project.getUserNotes().size() > 0);
    }

    ////////////////////////////////////
    // CLICK HANDLERS

    public void onProjectSavedClick(View view) {
        if (viewModel != null) viewModel.setDetailVisible(true);
        if (project == null) {
            onCompanyClick(view);
            return;
        }
        Intent intent = new Intent(view.getContext(), ProjectDetailActivity.class);
        intent.putExtra(ProjectDetailActivity.PROJECT_ID_EXTRA, project.getId());
        SearchViewModel.usingInstantSearch=false;
        view.getContext().startActivity(intent);
    }

    //event for clicking the Saved Search Project Detail item
    public void onProjectClick(View view) {
        if (viewModel != null) viewModel.setDetailVisible(true);
        if (project == null) return;
        SearchViewModel.usingInstantSearch=false;
        Intent intent = new Intent(view.getContext(), ProjectDetailActivity.class);
        intent.putExtra(ProjectDetailActivity.PROJECT_ID_EXTRA, project.getId());
        viewModel.setDetailVisible(true);
        view.getContext().startActivity(intent);
        saveRecentlyProject(project.getId());
    }

    //event for clicking the Saved Search Company Detail item
    public void onCompanyClick(View view) {
        if (viewModel != null) viewModel.setDetailVisible(true);
        if (company == null) return;
        Intent intent = new Intent(view.getContext(), CompanyDetailActivity.class);
        intent.putExtra(CompanyDetailActivity.COMPANY_ID_EXTRA, company.getId());
        view.getContext().startActivity(intent);
        saveRecentlyCompany(company.getId());
    }

    public void onContactClick(View view) {
        if (viewModel != null) viewModel.setDetailVisible(true);
        if (contact == null) return;
        Intent intent = new Intent(view.getContext(), ContactDetailActivity.class);
        intent.putExtra(ContactDetailActivity.CONTACT_ID_EXTRA, contact.getId());
        intent.putExtra("contact", (Serializable) contact);
        view.getContext().startActivity(intent);
    }

    public void saveRecentlyProject(final long projectId) {
        searchDomain.saveRecentProject(projectId, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        Log.d("saverecentproject", "saverecentproject" + response.body().string() + ":" + projectId);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {

                    try {
                        Log.e("unsuccessful", "unsuccessul response : " + response.errorBody().string() + ":" + projectId);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("unsuccessful", "unsuccessul response ioexception");
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                Log.e("unsuccessful", "unsuccessul response failure");
            }
        });
    }

    public void saveRecentlyCompany(final long companyId) {
        //Using the searchDomain to call the method to start retrofitting...

        searchDomain.saveRecentCompany(companyId, new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {

                    try {
                        Log.d("saverecentcompany", "saverecentcompany" + response.body().string() + ":" + companyId);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    try {
                        Log.e("unsuccessful", "unsuccessul response : " + response.errorBody().string() + ":" + companyId);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("unsuccessful", "unsuccessul response ioexception");
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //errorDisplayMsg(t.getLocalizedMessage());
                Log.e("unsuccessful", "unsuccessul response failure");
            }
        });
    }
}

