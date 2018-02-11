package alekhina_eliseeva.ssa.controller;

import android.widget.ArrayAdapter;

import com.google.firebase.auth.FirebaseAuth;


import java.util.ArrayList;

import alekhina_eliseeva.ssa.Applications;
import alekhina_eliseeva.ssa.LogIn;
import alekhina_eliseeva.ssa.Menu;
import alekhina_eliseeva.ssa.SignUp;


public class Controller {

    public static void signUp(SignUp activity, String login, String password) {
        FirebaseAuth.getInstance().signOut();
        UserInfoDataBase.addUser(activity, login, password);
    }

    public static void signIn(LogIn activity, String login, String password) {
        UserInfoDataBase.logIn(activity, login, password);
    }

    public static void signOut() {
        UserInfoDataBase.signOut();
    }

    public static void getRating(ArrayAdapter<String> arrayAdapter, ArrayList<String> arrayList) {
        UserInfoDataBase.getRating(arrayAdapter, arrayList);
    }

    public static void addSong(byte[] data) {
        data = SongsStorage.reverseSong(data);
        SongsStorage.addSong(data);
    }

    public static void addNames(String v1, String v2, String v3, String v4) {
        SongsStorage.addNames(v1, v2, v3, v4);
    }

    public static void suggest(Menu activity, String email) {
        Communication.suggest(activity, email);
    }

    public static void getSong(Applications activity, ArrayAdapter<Byte> arrayAdapter,
                               ArrayList<Byte> arrayList, String name) {
        SongsStorage.getSong(activity, arrayList, arrayAdapter, name);
    }

    private static int rightAnswer;

    public static int getRightAnswer(){
        return rightAnswer;
    }

    static void setRightAnswer(int newAnswer){
        rightAnswer = newAnswer;
    }

    public static void getVariants(ArrayAdapter<String> arrayAdapter, ArrayList<String> arrayList) {
        SongsStorage.getVariants(arrayList, arrayAdapter);
    }

    public static void getSuggestList(ArrayAdapter<String> arrayAdapter, ArrayList<String> arrayList) {
        Communication.getSuggestList(arrayAdapter, arrayList);
    }

    static void ignore(String email) {
        Communication.ignore(email);
    }

    public static void cancel() {
        Communication.cancel(Communication.friendUid);
    }

    public static void fixResult(boolean res) {
        Communication.fixResult(res);
        Communication.ignore(SongsStorage.otherUid);
        if (res) {
            UserInfoDataBase.addScore(10);
        } else {
            UserInfoDataBase.addScore(-10);
        }
    }

    public static void getResults(ArrayAdapter<String> arrayAdapter, ArrayList<String> arrayList) {
        Communication.getResults(arrayAdapter, arrayList);
    }

    public static byte[] reverse(byte[] bytes) {
        return SongsStorage.reverseSong(bytes);
    }
}
