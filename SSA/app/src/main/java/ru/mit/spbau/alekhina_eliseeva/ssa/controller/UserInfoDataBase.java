package ru.mit.spbau.alekhina_eliseeva.ssa.controller;

import android.widget.ArrayAdapter;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

class UserInfoDataBase {
    static void getRating(final ArrayAdapter<String> arrayAdapter, final List<String> list) {
        DatabaseReference firebaseRef = FirebaseDatabase.getInstance().getReference();
        firebaseRef.child("rating").orderByChild("score")
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot c : dataSnapshot.getChildren()) {
                    int subS = 1;
                    if (c.child("score").getValue().toString().length() == 1) {
                        subS = 0;
                    }
                    list.add(c.child("score").getValue().toString().substring(subS)
                            + " " + c.child("userName").getValue());
                }
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private static int max(int e1) {
        if (0 > e1) {
            return 0;
        }
        return e1;
    }

    static void addScore(final int score) {
        FirebaseDatabase.getInstance().getReference().child("rating")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot c) {
                        changeScore(max( -Integer.valueOf(c.child("score").getValue().toString()) + score));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    private static void changeScore(int score) {
        FirebaseDatabase.getInstance().getReference().child("rating")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("score").setValue(-score);
    }

    static Task<AuthResult> logIn(String email, String password) {
        return FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password);
    }

    static Task<AuthResult> addUser(final String email, String password) {
        return FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        StringBuilder emailGood = new StringBuilder();
                        for (int i = 0; i < email.length(); i++) {
                            if (email.charAt(i) == '.') {
                                emailGood.append(',');
                            } else {
                                emailGood.append(email.charAt(i));
                            }
                        }
                        DatabaseReference firebaseRef = FirebaseDatabase.getInstance().getReference();
                        String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        firebaseRef.child("rating").child(userUid).child("userName").push();
                        firebaseRef.child("rating").child(userUid).child("userName").setValue(email);
                        firebaseRef.child("rating").child(userUid).child("score").push();
                        firebaseRef.child("rating").child(userUid).child("score").setValue(0);

                        firebaseRef.child("UidByEmail").child(emailGood.toString()).push();
                        firebaseRef.child("UidByEmail").child(emailGood.toString()).setValue(userUid);

                        firebaseRef.child("messages").child(userUid).child(" ").push();
                        firebaseRef.child("messages").child(userUid).child(" ").setValue("");

                        firebaseRef.child("songNames").child(userUid).push();
                        firebaseRef.child("songNames").child(userUid).child("s1").push();
                        firebaseRef.child("songNames").child(userUid).child("s2").push();
                        firebaseRef.child("songNames").child(userUid).child("s3").push();
                        firebaseRef.child("songNames").child(userUid).child("s4").push();
                        firebaseRef.child("songNames").child(userUid).child("s1").setValue("");
                        firebaseRef.child("songNames").child(userUid).child("s2").setValue("");
                        firebaseRef.child("songNames").child(userUid).child("s3").setValue("");
                        firebaseRef.child("songNames").child(userUid).child("s4").setValue("");
                    }
                });
    }

    static void signOut() {
        FirebaseAuth.getInstance().signOut();
    }
}