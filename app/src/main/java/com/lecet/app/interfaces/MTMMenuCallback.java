package com.lecet.app.interfaces;

import com.lecet.app.data.models.CompanyTrackingList;
import com.lecet.app.data.models.ProjectTrackingList;

/**
 * Created by Josué Rodríguez on 18/10/2016.
 */

public interface MTMMenuCallback {
    void onProjectTrackingListClicked(ProjectTrackingList projectTrackingList);

    void onCompanyTrackingListClicked(CompanyTrackingList companyTrackingList);
}
