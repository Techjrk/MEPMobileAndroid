package com.lecet.app.data.api.deserializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import com.lecet.app.data.models.ActivityUpdate;
import com.lecet.app.data.models.Bid;
import com.lecet.app.data.models.Contact;
import com.lecet.app.data.models.ProjectStage;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * File: ProjectUpdateDeserializer Created: 11/22/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class ActivityUpdateDeserializer implements JsonDeserializer<ActivityUpdate> {

    @Override
    public ActivityUpdate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        final ActivityUpdate projectUpdate = new ActivityUpdate();

        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");


        final Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .create();

        final JsonObject jsonObject = json.getAsJsonObject();

        final JsonElement jsonChangeIndicator = jsonObject.get("changeIndicator");
        if (jsonChangeIndicator != null && !jsonChangeIndicator.isJsonNull()) {

            final String changeIndicator = jsonChangeIndicator.getAsString();
            projectUpdate.setChangeIndicator(changeIndicator);
        }

        final JsonElement jsonAttributeName = jsonObject.get("attributeName");
        if (jsonAttributeName != null && !jsonAttributeName.isJsonNull()) {

            final String attributeName = jsonAttributeName.getAsString();
            projectUpdate.setAttributeName(attributeName);
        }

        final JsonElement jsonAttributeValue = jsonObject.get("attributeValue");
        if (jsonAttributeValue != null && !jsonAttributeValue.isJsonNull()) {

            final String attributeValue = jsonAttributeValue.getAsString();
            projectUpdate.setAttributeValue(attributeValue);
        }

        final JsonElement jsonModelType = jsonObject.get("modelType");
        if (jsonModelType != null && !jsonModelType.isJsonNull()) {

            final String modelType = jsonModelType.getAsString();
            projectUpdate.setModelType(modelType);

            final JsonElement jsonModelObject = jsonObject.get("modelObject");
            if (jsonModelObject != null && !jsonModelObject.isJsonNull()) {

                switch (modelType) {
                    case "Bid":
                        projectUpdate.setBidUpdate(gson.fromJson(jsonModelObject, Bid.class));
                        break;
                    case "ProjectStage":
                        projectUpdate.setStageUpdate(gson.fromJson(jsonModelObject, ProjectStage.class));
                        break;
                    case "ProjectContact":
                        projectUpdate.setContactUpdate(gson.fromJson(jsonModelObject, Contact.class));
                        break;
                    case "WorkType":
                    case "ProjectType":
                        break;
                    default:
                        break;
                }

            }

        }

        final JsonElement jsonModelId = jsonObject.get("modelId");
        if (jsonModelId != null && !jsonModelId.isJsonNull()) {

            final long modelId = jsonModelId.getAsLong();
            projectUpdate.setModelId(modelId);
        }

        final JsonElement jsonModelTitle = jsonObject.get("modelTitle");
        if (jsonModelTitle != null && !jsonModelTitle.isJsonNull()) {

            final String modelTitle = jsonModelTitle.getAsString();
            projectUpdate.setModelTitle(modelTitle);
        }

        final JsonElement jsonProjectTitle = jsonObject.get("projectTitle");
        if (jsonProjectTitle != null && !jsonProjectTitle.isJsonNull()) {

            final String projectTitle = jsonProjectTitle.getAsString();
            projectUpdate.setProjectTitle(projectTitle);
        }

        final JsonElement jsonSummary = jsonObject.get("summary");
        if (jsonSummary != null && !jsonSummary.isJsonNull()) {

            final String summary = jsonSummary.getAsString();
            projectUpdate.setSummary(summary);
        }

        final JsonElement jsonId = jsonObject.get("id");
        if (jsonId != null && !jsonId.isJsonNull()) {

            final long id = jsonId.getAsLong();
            projectUpdate.setId(id);
        }

        final JsonElement jsonCompanyId = jsonObject.get("companyId");
        if (jsonCompanyId != null && !jsonCompanyId.isJsonNull()) {

            final long companyId = jsonCompanyId.getAsLong();
            projectUpdate.setCompanyId(companyId);
        }

        final JsonElement jsonContactId = jsonObject.get("contactId");
        if (jsonContactId != null && !jsonContactId.isJsonNull()) {

            final long contactId = jsonContactId.getAsLong();
            projectUpdate.setContactId(contactId);
        }

        final JsonElement jsonProjectId = jsonObject.get("projectId");
        if (jsonProjectId != null && !jsonProjectId.isJsonNull()) {

            final long projectId = jsonProjectId.getAsLong();
            projectUpdate.setProjectId(projectId);

        }

        final JsonElement jsonCreatedAt = jsonObject.get("createdAt");
        if (jsonCreatedAt != null && !jsonCreatedAt.isJsonNull()) {

            try {

                final Date createdAt = dateFormat.parse(jsonCreatedAt.getAsString());
                projectUpdate.setCreatedAt(createdAt);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        final JsonElement jsonUpdatedAt = jsonObject.get("updatedAt");
        if (jsonUpdatedAt != null && !jsonUpdatedAt.isJsonNull()) {

            try {

                final Date updatedAt = dateFormat.parse(jsonUpdatedAt.getAsString());
                projectUpdate.setUpdatedAt(updatedAt);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return projectUpdate;
    }


}
