package alekhina_eliseeva.ssa.controller;

import android.util.Pair;

import java.util.ArrayList;

/**
 * Created by mari on 06.11.17.
 */

public class Controller {
    /**
     * Add new login and password to data base
     * @param login new login
     * @param password password
     * @return success or not
     */
    public boolean signUp(String login, String password) {
        return UserInfo.signUp(login, password);
    }

    private String userName; //login
    private int userType; //first or second player

    /**
     * Checks the password is correct and remember user's name.
     * @param login login
     * @param password password to check
     * @return logged in or not
     */
    public boolean logIn(String login, String password) {
        boolean result = UserInfo.check(login, password);
        if (result) userName = login;
        return result;
    }

    Object secondPlayer; // connection with another player
    /**
     * Connects to friend using WiFi address.
     * @param address adress to connect
     * @return success or not
     */
    public boolean connectWiFi(String address) {
        userType = 0;
        secondPlayer = LocalNetwork.connect(address);
        return (secondPlayer != null);
    }

    /**
     * Co
     * @return
     */
    public boolean confirmConnection() {
        userType = 1;
        secondPlayer = LocalNetwork.confirmConnection();
        return (secondPlayer != null);
    }

    private ArrayList<Integer> arrayListIDs = new ArrayList<>();

    /**
     * Returns list of songs.
     * @return names of all songs in the base in an arrayList
     */
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
    private Integer secondSongID;
    private Integer thirdSongID;
    private Integer fourthSongID;

    /**
     * First player choose one song, other 3 are randomly generated.
     * @param number number of chosen song in an arrayListNames
     * @return success or not
     */
    public boolean choseSong(Integer number) {
        chosenSongID = arrayListIDs.get(number);
        secondSongID = 0;
        thirdSongID = 0;
        fourthSongID = 0;
        return Songs.getSong(chosenSongID);
    }

    /**
     * Waiting for another player to finish
     * @return
     */
    public boolean updateSecondPlayerInfo(){
        while (!LocalNetwork.endOrNot(secondPlayer));
        return true;
    }

    public void playSong(int part) {
    }

    public void singSong() {
    }

    public void stopSong() {
    }

    public void guessSong() {
    }
}
