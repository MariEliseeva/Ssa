package alekhina_eliseeva.ssa.controller;

import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import alekhina_eliseeva.ssa.LogIn;
import alekhina_eliseeva.ssa.SignUp;

class UserInfoDataBase {
    static void getRating(final ArrayAdapter<String> arrayAdapter, final ArrayList<String> arrayList) {
        FirebaseDatabase.getInstance().getReference().child("rating").orderByChild("score")
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot c : dataSnapshot.getChildren()) {
                    int subS = 1;
                    if (c.child("score").getValue().toString().length() == 1) {
                        subS = 0;
                    }
                    arrayList.add(c.child("score").getValue().toString().substring(subS)
                            + " " + c.child("userName").getValue());
                }
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private static int max(int e1, int e2) {
        if (e1 > e2) {
            return e1;
        }
        return e2;
    }

    static void addScore(final int score) {
        FirebaseDatabase.getInstance().getReference().child("rating")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot c) {
                        changeScore(max(0, -Integer.valueOf(c.child("score").getValue().toString()) + score));
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

    static void logIn(final LogIn activity, String email, String password) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        activity.next();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        activity.notNext();
                    }
                });
    }

    static void addUser(final SignUp activity, final String email, String password) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnSuccessListener(
                new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        String emailGood = "";
                        for (int i = 0; i < email.length(); i++) {
                            if (email.charAt(i) == '.') {
                                emailGood += ',';
                            } else {
                                emailGood += email.charAt(i);
                            }
                        }
                        FirebaseDatabase.getInstance().getReference().child("rating").child(user.getUid()).child("userName").push();
                        FirebaseDatabase.getInstance().getReference().child("rating").child(user.getUid()).child("userName").setValue(email);
                        FirebaseDatabase.getInstance().getReference().child("rating").child(user.getUid()).child("score").push();
                        FirebaseDatabase.getInstance().getReference().child("rating").child(user.getUid()).child("score").setValue(0);
                        FirebaseDatabase.getInstance().getReference().child("UidByEmail").child(emailGood).push();
                        FirebaseDatabase.getInstance().getReference().child("UidByEmail").child(emailGood).setValue(user.getUid());

                        FirebaseDatabase.getInstance().getReference().child("messages").child(user.getUid()).child(" ").push();
                        FirebaseDatabase.getInstance().getReference().child("messages").child(user.getUid()).child(" ").setValue("");

                        FirebaseDatabase.getInstance().getReference().child("songNames").child(user.getUid()).push();
                        FirebaseDatabase.getInstance().getReference().child("songNames").child(user.getUid()).
                                child("s1").push();
                        FirebaseDatabase.getInstance().getReference().child("songNames").child(user.getUid()).
                                child("s2").push();
                        FirebaseDatabase.getInstance().getReference().child("songNames").child(user.getUid()).
                                child("s3").push();
                        FirebaseDatabase.getInstance().getReference().child("songNames").child(user.getUid()).
                                child("s4").push();
                        FirebaseDatabase.getInstance().getReference().child("songNames").child(user.getUid()).
                                child("s1").setValue("");
                        FirebaseDatabase.getInstance().getReference().child("songNames").child(user.getUid()).
                                child("s2").setValue("");
                        FirebaseDatabase.getInstance().getReference().child("songNames").child(user.getUid()).
                                child("s3").setValue("");
                        FirebaseDatabase.getInstance().getReference().child("songNames").child(user.getUid()).
                                child("s4").setValue("");
                        activity.next();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                               activity.notNext();
                                            }
                                        }
        );
    }

    static void signOut() {
        FirebaseAuth.getInstance().signOut();
    }
}