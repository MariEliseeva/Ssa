package alekhina_eliseeva.ssa.controller;

import android.widget.ArrayAdapter;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import alekhina_eliseeva.ssa.Applications;
import alekhina_eliseeva.ssa.Menu;


public class Controller {

    public static Task<AuthResult> signUp(String login, String password) {
        FirebaseAuth.getInstance().signOut();
        return UserInfoDataBase.addUser(login, password);
    }

    public static Task<AuthResult> signIn(String login, String password) {
        return UserInfoDataBase.logIn(login, password);
    }

    public static void signOut() {
        UserInfoDataBase.signOut();
    }

    public static void getRating(ArrayAdapter<String> arrayAdapter, List<String> list) {
        UserInfoDataBase.getRating(arrayAdapter, list);
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

    public static void getSong(Applications activity, String name) {
        SongsStorage.getSong(activity, name);
    }

    private static int rightAnswer;

    public static int getRightAnswer(){
        return rightAnswer;
    }

    static void setRightAnswer(int newAnswer){
        rightAnswer = newAnswer;
    }

    public static void getVariants(ArrayAdapter<String> arrayAdapter, List<String> list) {
        SongsStorage.getVariants(list, arrayAdapter);
    }

    public static void getSuggestList(ArrayAdapter<String> arrayAdapter, List<String> list) {
        Communication.getSuggestList(arrayAdapter, list);
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

    public static void getResults(ArrayAdapter<String> arrayAdapter, List<String> list) {
        Communication.getResults(arrayAdapter, list);
    }

    public static byte[] reverse(byte[] bytes) {
        return SongsStorage.reverseSong(bytes);
    }
}
