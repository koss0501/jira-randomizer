package com.apptimized.tools.randomizer.Utils;

import com.apptimized.tools.randomizer.JiraEntities.User;
import com.apptimized.tools.randomizer.RestClient.RestActions;

import java.util.ArrayList;

import static com.apptimized.tools.randomizer.Randomizer.UIInterface.Actions.UIActions.showExceptionAlert;

public class ActionUtils {

    public static ArrayList<User> findQA(ArrayList<User> usersList, int currentUserID){
        for (User user: usersList) {
            if(!user.isQA() && (!usersList.get(currentUserID).getName().equals(user.getName()))){
                usersList.get(currentUserID).setQaEngineer(user);
                user.setQA(true);
                break;
            }
        }
        return usersList;
    }

    static User findUser(ArrayList<User> usersList, String name) {
        try {
            for (User user: usersList) {
                if (user.getName().equals(name)) {
                    return user;
                }
            }
            if (name.equals("")) {
                return null;
            }
            return JsonActions.parseUser(RestActions.getUserInfo(name));
        } catch (Exception e) {
            showExceptionAlert(e);
        }
        return null;
    }

    public static String prepareQuery(String query) {
        query = query.toLowerCase().replace(" ", "+").
                replace("=", "%3D").
                replace("~", "%7E").
                replace("}", "%7D").
                replace("{", "%7B").
                replace("(", "%28").
                replace(")", "%29").
                replace("*", "%2A").
                replace("!", "%21").
                replace(",", "%2C").
                replace("\"", "%22");
        return query;
    }

}
