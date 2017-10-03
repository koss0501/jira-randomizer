package com.apptimized.tools.randomizer.Utils;

public class ConstStorage {
    public static final String PROJECT_NAME = PropertyHandler.getProperty("PROJECT_NAME");
    public static final String USERS_EXCLUSION_LIST = PropertyHandler.getProperty("USERS_EXCLUSION_LIST");
    public static final String PACKAGERS_USERS_GROUP = PropertyHandler.getProperty("PACKAGERS_USERS_GROUP");

    public static final String QA_ENGINEER_FIELD_ID = "customfield_10214";
    public static final String PACKAGER_FIELD_ID = "customfield_10072";
    public static final String APP_NAME_FIELD_ID = "customfield_10410";
    public static final String APP_VERSION_ID = "customfield_10444";
    public static final String APP_VENDOR_FIELD_ID = "customfield_10327";
    public static final String ASSIGNEE_FIELD_ID = "assignee";
    public static final String APPLICATION_PACKAGE_TYPE = "Application Package";
    public static final String APPLICATION_CHANGE_TYPE = "Application Change";
    public static final String ORDER_PACKAGING_TRANS_ID = "1331";
    public static final String USERNAME_PLACEHOLDER = "#username#";
    public static final String COMMENT_TEXT_PLACEHOLDER = "#comment_text#";
    public static final String CUSTOM_FIELD_ID_PLACEHOLDER = "#field_id#";
    public static final String CUSTOM_FIELD_VALUE_PLACEHOLDER = "#value#";
    public static final String APP_NAME_PLACEHOLDER = "#app_name#";
    public static final String APP_VERSION_PLACEHOLDER = "#app_version#";
    public static final String APP_VENDOR_PLACEHOLDER = "#app_vendor#";
    public static final String SUMMARY_PLACEHOLDER = "#summary#";
    public static final String PARENT_ISSUE_PLACEHOLDER = "#parent_issue#";
    public static final String TRANSITION_ID_PLACEHOLDER = "#transition_id#";

    public static final String DEVELOPER = "Konstantin Babich, k.babich@apptimized.com";

    public static final String SET_ASSIGNEE = "{" +
            "   \"fields\": {" +
            "       \"" + ASSIGNEE_FIELD_ID + "\":{\"name\":\"" + USERNAME_PLACEHOLDER + "\"}" +
            "   }" +
            "}";

    public static final String SET_CUSTOM_FIELD = "{" +
            "    \"fields\" : {" +
            "        \"" + CUSTOM_FIELD_ID_PLACEHOLDER + "\" :" +
            "        {\"name\" : \"" + CUSTOM_FIELD_VALUE_PLACEHOLDER + "\"}" +
            "    }" +
            "}";

    public static final String ADD_COMMENT = "{" +
            "    \"body\": \"" + COMMENT_TEXT_PLACEHOLDER + "\"" +
            "}";

    public static final String CREATE_SUBTASK = "{" +
            "    \"fields\": {" +
            "       \"project\":" +
            "       {" +
            "          \"key\": \"" + PROJECT_NAME + "\"" +
            "       }," +
            "       \"parent\":\n" +
            "        {\n" +
            "            \"key\": \"" + PARENT_ISSUE_PLACEHOLDER + "\"" +
            "        }," +
            "       \"summary\": \"" + SUMMARY_PLACEHOLDER + "\"," +
            "       \"description\": \"\"," +
            "       \"" + APP_NAME_FIELD_ID + "\": \"" + APP_NAME_PLACEHOLDER + "\"," +
            "       \"" + APP_VERSION_ID + "\": \"" + APP_VERSION_PLACEHOLDER + "\"," +
            "       \"" + APP_VENDOR_FIELD_ID + "\": \"" + APP_VENDOR_PLACEHOLDER + "\"," +
            "       \"issuetype\": {" +
            "          \"name\": \"" + APPLICATION_PACKAGE_TYPE + "\"" +
            "       }" +
            "   }" +
            "}";

    public static final String CREATE_TASK = "{" +
            "    \"fields\": {" +
            "       \"project\":" +
            "       {" +
            "          \"key\": \"" + PROJECT_NAME + "\"" +
            "       }," +
            "       \"summary\": \"" + SUMMARY_PLACEHOLDER + "\"," +
            "       \"description\": \"\"," +
            "       \"" + APP_NAME_FIELD_ID + "\": \"" + APP_NAME_PLACEHOLDER + "\"," +
            "       \"" + APP_VERSION_ID + "\": \"" + APP_VERSION_PLACEHOLDER + "\"," +
            "       \"" + APP_VENDOR_FIELD_ID + "\": \"" + APP_VENDOR_PLACEHOLDER + "\"," +
            "       \"issuetype\": {" +
            "          \"name\": \"" + APPLICATION_CHANGE_TYPE + "\"" +
            "       }" +
            "   }" +
            "}";

    public static final String MAKE_TRANSITION = "   {" +
            "    \"transition\": {" +
            "            \"id\": \"" + TRANSITION_ID_PLACEHOLDER + "\"" +
            "        }" +
            "    }";

}
