package alekhina_eliseeva.ssa.controller;

import android.util.Log;
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

class UserInfoDataBase {
    static void getRating(final ArrayAdapter arrayAdapter, final ArrayList arrayList) {
        FirebaseDatabase.getInstance().getReference().orderByChild("score").addValueEventListener(new ValueEventListener() {
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

    static void changeScore(int score) {
        FirebaseDatabase.getInstance().getReference()
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("score").setValue(-score);
    }

    static void logIn(String email, String password) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnFailureListener(new OnFailureListener() {
                                          @Override
                                          public void onFailure(Exception e) {
                                              Log.e("AAAAAA", e.getMessage());
                                          }
                                      }
                );
    }

    static void addUser(final String email, String password,
                        final String username) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnSuccessListener(
                new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        Log.e("XXXX", String.valueOf(user == null));
                        user.sendEmailVerification();
                        Log.e("BBBB", email);
                        FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("userName").push();
                        FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("userName").setValue(username);
                        FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("score").push();
                        FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("score").setValue(0);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(Exception e) {
                                                Log.e("AAAAAA", e.getMessage());
                                            }
                                        }
        );
    }

    public static void signOut() {
        FirebaseAuth.getInstance().signOut();
    }
}