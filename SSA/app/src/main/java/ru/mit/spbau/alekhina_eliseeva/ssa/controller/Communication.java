package ru.mit.spbau.alekhina_eliseeva.ssa.controller;

import android.widget.ArrayAdapter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import ru.mit.spbau.alekhina_eliseeva.ssa.Menu;

class Communication {
    private static DatabaseReference firebaseRef = FirebaseDatabase.getInstance().getReference();
    private static String currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private static String currentEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
    static void getSuggestList(final ArrayAdapter<String> arrayAdapter, final List<String> list) {
        firebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
                dataSnapshot = dataSnapshot.child("messages").child(currentUid);
                for (DataSnapshot c : dataSnapshot.getChildren()) {
                    if (c.getKey().equals(" ")) {
                        continue;
                    }
                    list.add(c.getValue().toString());
                }
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    static String friendUid = "";

    static void suggest(final Menu activity, final String email) {
        StringBuilder emailGood1 = new StringBuilder();
        for (int i = 0; i < email.length(); i++) {
            if (email.charAt(i) == '.') {
                emailGood1.append(',');
            } else {
                emailGood1.append(email.charAt(i));
            }
        }
        final String emailGood = emailGood1.toString().toLowerCase();
        firebaseRef.child("UidByEmail").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    suggest2(dataSnapshot.child(emailGood).getValue().toString());
                    friendUid = dataSnapshot.child(emailGood).getValue().toString();
                } catch (Exception e) {
                    activity.notNext();
                    return;
                }
                activity.next();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                activity.notNext();
            }
        });
    }
    private static void suggest2(final String Uid) {
        firebaseRef.child("messages").child(Uid).child(currentUid).push();
        firebaseRef.child("messages").child(Uid).child(currentUid).setValue(currentEmail);
    }

    static void ignore(String uid) {
        FirebaseDatabase.getInstance().getReference().child("messages")
                .child(currentUid).child(uid).removeValue();
    }

    static void cancel(String uid) {
        FirebaseDatabase.getInstance().getReference().child("messages")
                .child(uid).child(currentUid).removeValue();
    }

    static void fixResult(boolean res) {
        StringBuilder emailGood1 = new StringBuilder();
        for (int i = 0; i < currentEmail.length(); i++) {
            if (currentEmail.charAt(i) == '.') {
                emailGood1.append(',');
            } else {
                emailGood1.append(currentEmail.charAt(i));
            }
        }
        final String emailGood = emailGood1.toString().toLowerCase();
        String result = res ? "выиграл" : "проиграл";
        Controller.ignore(SongsStorage.otherEmail);
        FirebaseDatabase.getInstance().getReference().child("results").child(SongsStorage.otherEmail).
                child(emailGood).setValue(result);
    }

    static void getResults(final ArrayAdapter<String> arrayAdapter, final List<String> list) {
        StringBuilder emailGood1 = new StringBuilder();
        for (int i = 0; i < currentEmail.length(); i++) {
            if (currentEmail.charAt(i) == '.') {
                emailGood1.append(',');
            } else {
                emailGood1.append(currentEmail.charAt(i));
            }
        }
        final String emailGood = emailGood1.toString().toLowerCase();
        FirebaseDatabase.getInstance().getReference().child("results").child(emailGood)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        list.clear();
                        for (DataSnapshot c : dataSnapshot.getChildren()) {
                            String email = c.getKey();
                            StringBuilder emailGood2 = new StringBuilder();
                            for (int i = 0; i < email.length(); i++) {
                                if (email.charAt(i) == ',') {
                                    emailGood2.append('.');
                                } else {
                                    emailGood2.append(email.charAt(i));
                                }
                            }
                            list.add(emailGood2.toString().toLowerCase() + " " + c.getValue());
                        }
                        arrayAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }
}
