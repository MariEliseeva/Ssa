package alekhina_eliseeva.ssa.controller;

import android.widget.ArrayAdapter;

import com.google.firebase.auth.FirebaseAuth;


import java.util.ArrayList;

import alekhina_eliseeva.ssa.Applications;


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

    public static void addSong(byte[] data) {
        data = SongsStorage.reverseSong(data);
        SongsStorage.addSong(data);
    }

    public static void addNames(String v1, String v2, String v3, String v4) {
        SongsStorage.addNames(v1, v2, v3, v4);
    }

    public static void suggest(String email) {
        Communication.suggest(email);
    }

    public static void getSong(Applications activity, ArrayAdapter arrayAdapter, ArrayList arrayList, String name, String part) {
        SongsStorage.getSong(activity, arrayList, arrayAdapter, name, part);
        //TODO: поменять ArrayAdapter на что-то нужное(?)
    }

    private static int rightAnswer;

    public static int getRightAnswer(){
        return rightAnswer;
    }

    static void setRightAnswer(int newAnswer){
        rightAnswer = newAnswer;
    }

    public static void getVariants(ArrayAdapter arrayAdapter, ArrayList arrayList) {
        SongsStorage.getVariants(arrayList, arrayAdapter);
        //TODO: поменять ArrayAdapter на что-то нужное(?)
    }

    public static void getSuggestList(ArrayAdapter arrayAdapter, ArrayList arrayList) {
        Communication.getSuggestList(arrayAdapter,arrayList);
    }

    public static void ignore(String email) {
        Communication.ignore(email);
    }

    public static void fixResult(boolean res) {
        Communication.fixResult(res);
    }

    public static void getResult(ArrayAdapter arrayAdapter, ArrayList arrayList) {
        Communication.getResult(arrayAdapter, arrayList);
    }

    public static String getEmail(){
        String ans = "";
        if (SongsStorage.otherEmail == null) {
            return "";
        }
        for (int i = 0; i <  SongsStorage.otherEmail.length(); i++) {
            if ( SongsStorage.otherEmail.charAt(i) == ',') {
                ans += '.';
            } else {
                ans +=  SongsStorage.otherEmail.charAt(i);
            }
        }
        return ans;
    }

    public static byte[] reverse(byte[] bytes) {
        return SongsStorage.reverseSong(bytes);
    }
}
