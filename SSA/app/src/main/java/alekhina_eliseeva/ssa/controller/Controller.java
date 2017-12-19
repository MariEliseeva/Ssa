package alekhina_eliseeva.ssa.controller;

import android.widget.ArrayAdapter;

import com.google.firebase.auth.FirebaseAuth;


import java.util.ArrayList;


public class Controller {

    public static void signUp(String login, String password, String username) {
        FirebaseAuth.getInstance().signOut();
        UserInfoDataBase.addUser(login, password, username);
        //TODO проверка email на уникальность
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
        // TODO: ++, not =
    }

    public static boolean isUser() {
        return (FirebaseAuth.getInstance().getCurrentUser() == null);
    }

    public static void addSong(byte[] data, String v1, String v2, String v3, String v4,  String email) {
        SongsStorage.addSong(data);
        SongsStorage.addNames(v1, v2, v3, v4);
        Communication.suggest(email);
    }

    public static void getSong(ArrayAdapter arrayAdapter, ArrayList arrayList, String name, String part) {
        SongsStorage.getSong(arrayList, arrayAdapter, name, part);
        //TODO: поменять ArrayAdapter на что-то нужное(?)
    }

    public static void getVariants(ArrayAdapter arrayAdapter, ArrayList arrayList, String name) {
        SongsStorage.getVariants(arrayList, arrayAdapter, name);
        //TODO: поменять ArrayAdapter на что-то нужное(?)
    }

    public static void getSuggestList(ArrayAdapter arrayAdapter, ArrayList arrayList) {
        Communication.getSuggestList(arrayAdapter,arrayList);
    }

    public static void ignore(String email) {
        Communication.ignore(email);
    }

    public static void fixResult(boolean res, String email) {
        Communication.fixResult(res, email);
    }

    public static void getResult(ArrayAdapter arrayAdapter, ArrayList arrayList, String email) {
        Communication.getResult(arrayAdapter, arrayList, email);
    }

    public static byte[] reverse(byte[] bytes) {
        return SongsStorage.reverseSong(bytes);
    }
}
