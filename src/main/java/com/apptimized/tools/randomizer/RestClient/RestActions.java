package com.apptimized.tools.randomizer.RestClient;

import com.apptimized.tools.randomizer.Utils.ConstStorage;
import com.apptimized.tools.randomizer.Utils.PropertyHandler;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static com.apptimized.tools.randomizer.Randomizer.UIInterface.Actions.UIActions.showExceptionAlert;
import static com.apptimized.tools.randomizer.Utils.ActionUtils.prepareQuery;
import static com.apptimized.tools.randomizer.Utils.ConstStorage.*;

public class RestActions {

    private static String JIRA_URL = PropertyHandler.getProperty("JIRA_URL") + "/rest/api/2";
    private static String JIRA_USER = PropertyHandler.getProperty("JIRA_USER");
    private static String JIRA_PASS = PropertyHandler.getProperty("JIRA_PASS");
    private static String OK_STATUS = "204;201;200";
    private static String authHeader = "Basic " + new String(
            Base64.encodeBase64((JIRA_USER + ":" + JIRA_PASS).getBytes())
    );

    private static String putJsonRequest(String json, String uri) {
        try{
            HttpPut request = new HttpPut(uri);
            StringEntity params = new StringEntity(json,"UTF-8");
            params.setContentType("application/json");
            request.addHeader(HttpHeaders.AUTHORIZATION, authHeader);
            request.addHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            request.setEntity(params);
            HttpClient client = HttpClientBuilder.create().build();
            return client.execute(request).toString();
        } catch (IOException e) {
            showExceptionAlert(e);
        }
        return null;
    }

    private static HttpResponse postJsonRequest(String json, String uri) {
        try{
            HttpPost request = new HttpPost(uri);
            StringEntity params = new StringEntity(json,"UTF-8");
            params.setContentType("application/json");
            request.addHeader(HttpHeaders.AUTHORIZATION, authHeader);
            request.addHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            request.setEntity(params);
            HttpClient client = HttpClientBuilder.create().build();
            return client.execute(request);
        } catch (Exception e) {
            showExceptionAlert(e);
        }
        return null;
    }

    private static HttpResponse getJsonRequest(String uri) {
        try {
            HttpGet request = new HttpGet(uri);
            request.addHeader(HttpHeaders.AUTHORIZATION, authHeader);
            request.addHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            HttpClient client = HttpClientBuilder.create().build();
            return client.execute(request);
        } catch (Exception e) {
            showExceptionAlert(e);
        }
        return null;
    }

    public static String getQueryResults(String query) {
        return getContentFromResponse(getJsonRequest(JIRA_URL + "/search?jql=" + prepareQuery(query) +
                "&fields=summary,"+ ASSIGNEE_FIELD_ID +"," + QA_ENGINEER_FIELD_ID + "," + PACKAGER_FIELD_ID));
    }

    public static String setAssignee(String username, String issueID) {
        return putJsonRequest(ConstStorage.SET_ASSIGNEE.replace(ConstStorage.USERNAME_PLACEHOLDER, username),
                JIRA_URL + "/issue/" + issueID);
    }

    public static String setQAEngineer(String username, String issueID) {
        return putJsonRequest(ConstStorage.SET_CUSTOM_FIELD.
                        replace(ConstStorage.CUSTOM_FIELD_ID_PLACEHOLDER, QA_ENGINEER_FIELD_ID).
                        replace(ConstStorage.CUSTOM_FIELD_VALUE_PLACEHOLDER, username),
                JIRA_URL + "/issue/" + issueID);
    }

    public static String setPackager(String username, String issueID) {
        return putJsonRequest(ConstStorage.SET_CUSTOM_FIELD.
                        replace(ConstStorage.CUSTOM_FIELD_ID_PLACEHOLDER, ConstStorage.PACKAGER_FIELD_ID).
                        replace(ConstStorage.CUSTOM_FIELD_VALUE_PLACEHOLDER, username),
                JIRA_URL + "/issue/" + issueID);
    }

    public static String getUsersFromGroup(String groupName) {
        return getContentFromResponse(getJsonRequest(JIRA_URL + "/group?groupname=" + groupName.replace(" ", "+") + "&expand=users"));
    }

    public static String addComment(String commentText, String issueID) {
        return getContentFromResponse(postJsonRequest(ConstStorage.ADD_COMMENT.replace(ConstStorage.COMMENT_TEXT_PLACEHOLDER, commentText),
                JIRA_URL + "/issue/" + issueID + "/comment"));
    }

    public static String getUserInfo(String userName) {
        return getContentFromResponse(getJsonRequest(JIRA_URL + "/user/search?username=" + userName));
    }

    public static String createSubtask(String parentIssueID, String summary, String app_name, String app_version, String app_vendor) throws Exception {
        return getContentFromResponse(postJsonRequest(ConstStorage.CREATE_SUBTASK.replace(ConstStorage.SUMMARY_PLACEHOLDER, summary).
                        replace(ConstStorage.PARENT_ISSUE_PLACEHOLDER, parentIssueID).
                        replace(ConstStorage.APP_NAME_PLACEHOLDER, app_name).
                        replace(ConstStorage.APP_VERSION_PLACEHOLDER, app_version).
                        replace(ConstStorage.APP_VENDOR_PLACEHOLDER, app_vendor),
                JIRA_URL + "/issue/"));
    }

    public static String createIssue(String summary, String app_name, String app_version, String app_vendor) throws Exception {
        summary = summary.replace("Package of", "")
                .replace("Packaging of","")
                .trim();
        return getContentFromResponse(postJsonRequest(ConstStorage.CREATE_TASK.replace(ConstStorage.SUMMARY_PLACEHOLDER, summary).
                        replace(ConstStorage.APP_NAME_PLACEHOLDER, app_name).
                        replace(ConstStorage.APP_VERSION_PLACEHOLDER, app_version).
                        replace(ConstStorage.APP_VENDOR_PLACEHOLDER, app_vendor),
                JIRA_URL + "/issue/"));
    }

    public static String makeTransitionById(String issueID, String transitionID) throws Exception {
        HttpResponse response = postJsonRequest(MAKE_TRANSITION.replace(TRANSITION_ID_PLACEHOLDER, transitionID),
                JIRA_URL + "/issue/" + issueID + "/transitions?expand=transitions.fields");
        if(!OK_STATUS.contains(String.valueOf(response.getStatusLine().getStatusCode()))) {
            throw new Exception("Incorrect status code after transition!");
        }
        return String.valueOf(response.getStatusLine().getStatusCode());
    }

    private static String getContentFromResponse(HttpResponse response) {
        try {
            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            return result.toString();
        } catch (Exception e) {
        showExceptionAlert(e);
        }
        return null;
    }



}