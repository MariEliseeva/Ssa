package alekhina_eliseeva.ssa.controller;

import android.content.Context;
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

    public static void reverseAudio(String name) {

        Songs.reverseAudio(name);
    }

    public static String addSong(byte[] data) {
        return SongsStorage.addSong(data);
    }

    public static void getSong(ArrayAdapter arrayAdapter, ArrayList arrayList, String address) {
        SongsStorage.getSong(arrayList, arrayAdapter, address);
    }

}
