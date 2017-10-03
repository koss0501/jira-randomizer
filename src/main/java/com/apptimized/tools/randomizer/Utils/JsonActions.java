package com.apptimized.tools.randomizer.Utils;

import com.apptimized.tools.randomizer.JiraEntities.Issue;
import com.apptimized.tools.randomizer.JiraEntities.User;
import com.apptimized.tools.randomizer.Utils.ConstStorage;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.apptimized.tools.randomizer.Utils.ActionUtils.findUser;

public class JsonActions {

    public static ArrayList<Issue> parseIssues(String jsonArray) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String name;
        JsonNode rootArray = mapper.readTree(jsonArray);
        ArrayList<Issue> issuesList = new ArrayList<Issue>();
        for(JsonNode root : rootArray.path("issues")){
            Issue issue = new Issue();
            issue.setKey(root.path("key").asText());
            JsonNode sub_node = root.path("fields").path(ConstStorage.PACKAGER_FIELD_ID);
            if (!sub_node.isMissingNode()) {
                name = sub_node.path("name").toString().replace("\"", "");
                issue.setPackager(name.equals("") ? "unassigned" : name);
            }
            sub_node = root.path("fields").path("summary");
            if (!sub_node.isMissingNode()) {
                name = sub_node.toString().replace("\"", "");
                issue.setSummary(name);
            }
            sub_node = root.path("fields").path(ConstStorage.QA_ENGINEER_FIELD_ID);
            if (!sub_node.isMissingNode()) {
                name = sub_node.path("name").toString().replace("\"", "");
                issue.setQa_engineer(name.equals("") ? "unassigned" : name);
            }
            sub_node = root.path("fields").path(ConstStorage.ASSIGNEE_FIELD_ID);
            if (!sub_node.isMissingNode()) {
                name = sub_node.path("name").toString().replace("\"", "");
                issue.setAssignee(name.equals("") ? "unassigned" : name);
            }
            issuesList.add(issue);
        }
        return issuesList;
    }

    public static ArrayList<User> parseUsers(String jsonArray) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootArray = mapper.readTree(jsonArray);
        ArrayList<User> usersList = new ArrayList<User>();
        for(JsonNode root : rootArray.path("users").path("items")){
            if(ConstStorage.USERS_EXCLUSION_LIST.contains(root.path("name").asText())) {
                continue;
            }
            User user = new User();
            user.setName(root.path("name").asText());
            user.setEmailAddress(root.path("emailAddress").asText());
            user.setActive(root.path("active").asText());
            usersList.add(user);
        }
        return usersList;
    }

    public static User parseUser(String jsonArray) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        if (jsonArray.startsWith("[") && jsonArray.endsWith("]"))
        {
            jsonArray = jsonArray.substring(1, jsonArray.length()-1);
        }
        JsonNode root = mapper.readTree(jsonArray);
        return new User(root.path("name").asText(), root.path("emailAddress").asText(), root.path("active").asText());
    }

    public static Map<String, String> parseUserDataToMap(String jsonArray) {
        Map<String, String> userInfo = new HashMap<String, String>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            if (jsonArray.startsWith("[") && jsonArray.endsWith("]"))
            {
                jsonArray = jsonArray.substring(1, jsonArray.length()-1);
            }
            JsonNode root = mapper.readTree(jsonArray);
            userInfo.put("name", root.path("name").asText());
            userInfo.put("emailAddress", root.path("emailAddress").asText());
            userInfo.put("active", root.path("active").asText());

        } catch (IOException e) {
            System.out.println("Exception: " + e);
        }
        return userInfo;
    }

    public static String getNodeFromJson(String jsonArray, String node) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootArray = mapper.readTree(jsonArray);
        if(rootArray.path(node).isMissingNode()){
            return null;
        } else {
            return rootArray.path(node).toString();
        }
    }

}
