package alekhina_eliseeva.ssa.controller;

import java.sql.SQLException;
import java.util.ArrayList;

public class Controller {
    public boolean signUp(String login, String password) {
        boolean result;
        try {
            result = UserInfoDataBase.addUser(login, password);
        } catch (SQLException e) {
            return false;
        }
        userName = login;
        return result;
    }

    private String userName; //login
    private int userType; //first or second player

    public boolean logIn(String login, String password) {
        boolean result = false;
        try {
            result = UserInfoDataBase.checkPassword(login, password);
        } catch (SQLException e) {
            return false;
        }
        if (result) userName = login;
        return result;
    }

    public ArrayList<RatingLine> getRating() {
        try {
            return UserInfoDataBase.getRating();
        } catch (SQLException e) {
            return null;
        }
    }

    public boolean changeScore(int score) {
        try {
            UserInfoDataBase.changeScore(userName, score);
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    /*private ArrayList<Integer> arrayListIDs = new ArrayList<>();

    public ArrayList<String> getSongs() {
        ArrayList<Pair<String, Integer>> arrayList = Songs.getSongsList();
        ArrayList<String> arrayListNames = new ArrayList<>();
        for (Pair<String, Integer> element : arrayList) {
            arrayListNames.add(element.first);
            arrayListIDs.add(element.second);
        }
        return arrayListNames;
    }

    private Integer chosenSongID;
    public boolean choseSong(Integer number) {
        chosenSongID = arrayListIDs.get(number);
        return Songs.getSong(chosenSongID);
    }

    public BroadcastReceiver getConnection(WifiP2pManager manager,
                                           WifiP2pManager.Channel channel, Activity activity) {
        return new WiFiBroadcastReceiver(manager, channel, activity);
    }*/


}
