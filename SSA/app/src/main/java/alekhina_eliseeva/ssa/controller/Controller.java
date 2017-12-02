package alekhina_eliseeva.ssa.controller;

import android.widget.ArrayAdapter;

import com.google.firebase.auth.FirebaseAuth;


import java.util.ArrayList;


public class Controller {

    public static void signUp(String login, String password, String username) {
        FirebaseAuth.getInstance().signOut();
        UserInfoDataBase.addUser(login, password, username);
    }

    public static void signIn(String login, String password) {
        UserInfoDataBase.logIn(login, password);
    }

    public static void signOut() {
        UserInfoDataBase.signOut();
    }

    public static void getRating(ArrayAdapter arrayAdapter, ArrayList arrayList) {
        new UserInfoDataBase().getRating(arrayAdapter, arrayList);
    }

    public static void changeScore(int score) {
        new UserInfoDataBase().changeScore(score);
    }

    public static boolean isUser() {
        return (FirebaseAuth.getInstance().getCurrentUser() == null);
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
