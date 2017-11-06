package alekhina_eliseeva.ssa.controller;

import android.util.Pair;

import java.util.ArrayList;

/**
 * Created by mari on 06.11.17.
 */

public class Controller {
    public boolean menuSignUp(String login, String password) {
        return UserInfo.signUp(login, password);
    }

    private ArrayList<Integer> arrayListIDs = new ArrayList<>();

    public ArrayList<String> menuGetSongs() {
        ArrayList<Pair<String, Integer>> arrayList = Songs.getSongsList();
        ArrayList<String> arrayListNames = new ArrayList<>();
        for (Pair<String, Integer> element : arrayList) {
            arrayListNames.add(element.first);
            arrayListIDs.add(element.second);
        }
        return arrayListNames;
    }

    private Integer chosenSongID;

    public boolean menuChoseSong(Integer number) {
        chosenSongID = arrayListIDs.get(number);
        arrayListIDs = null;
        return Songs.getSong(chosenSongID);
    }
}
