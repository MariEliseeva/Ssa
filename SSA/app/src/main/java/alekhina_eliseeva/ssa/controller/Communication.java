package alekhina_eliseeva.ssa.controller;

import android.widget.ArrayAdapter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import alekhina_eliseeva.ssa.Menu;

class Communication {
    static void getSuggestList(final ArrayAdapter<String> arrayAdapter,
                               final ArrayList<String> arrayList) {
        FirebaseDatabase.getInstance().getReference()
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayList.clear();
                dataSnapshot = dataSnapshot.child("messages")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                for (DataSnapshot c : dataSnapshot.getChildren()) {
                    if (c.getKey().toString().equals(" ")) {
                        continue;
                    }
                    arrayList.add(c.getValue().toString());
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
        String emailGood1 = "";
        for (int i = 0; i < email.length(); i++) {
            if (email.charAt(i) == '.') {
                emailGood1 += ',';
            } else {
                emailGood1 += email.charAt(i);
            }
        }
        final String emailGood = emailGood1.toLowerCase();
        FirebaseDatabase.getInstance().getReference().child("UidByEmail").addListenerForSingleValueEvent(new ValueEventListener() {
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
        FirebaseDatabase.getInstance().getReference().child("messages").
                child(Uid).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).push();
        FirebaseDatabase.getInstance().getReference().child("messages").
                child(Uid).child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(FirebaseAuth.getInstance().getCurrentUser().getEmail());
    }

    static void ignore(String uid) {
        FirebaseDatabase.getInstance().getReference().child("messages")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(uid).removeValue();
    }

    static void cancel(String uid) {
        FirebaseDatabase.getInstance().getReference().child("messages")
                .child(uid)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
    }

    static void fixResult(boolean res) {
        String emailGood1 = "";
        for (int i = 0; i < FirebaseAuth.getInstance().getCurrentUser().getEmail().length(); i++) {
            if (FirebaseAuth.getInstance().getCurrentUser().getEmail().charAt(i) == '.') {
                emailGood1 += ',';
            } else {
                emailGood1 += FirebaseAuth.getInstance().getCurrentUser().getEmail().charAt(i);
            }
        }
        final String emailGood = emailGood1.toLowerCase();
        String result = res ? "выиграл" : "проиграл";
        Controller.ignore(SongsStorage.otherEmail);
        FirebaseDatabase.getInstance().getReference().child("results").child(SongsStorage.otherEmail).
                child(emailGood).setValue(result);
    }

    static void getResults(final ArrayAdapter<String> arrayAdapter, final ArrayList<String> arrayList) {
        String emailGood1 = "";
        for (int i = 0; i < FirebaseAuth.getInstance().getCurrentUser().getEmail().length(); i++) {
            if (FirebaseAuth.getInstance().getCurrentUser().getEmail().charAt(i) == '.') {
                emailGood1 += ',';
            } else {
                emailGood1 += FirebaseAuth.getInstance().getCurrentUser().getEmail().charAt(i);
            }
        }
        final String emailGood = emailGood1.toLowerCase();
        FirebaseDatabase.getInstance().getReference().child("results").child(emailGood)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        arrayList.clear();
                        for (DataSnapshot c : dataSnapshot.getChildren()) {
                            String email = c.getKey();
                            String emailGood2 = "";
                            for (int i = 0; i < email.length(); i++) {
                                if (email.charAt(i) == ',') {
                                    emailGood2 += '.';
                                } else {
                                    emailGood2 += email.charAt(i);
                                }
                            }
                            arrayList.add(emailGood2.toLowerCase() + " " + c.getValue());
                        }
                        arrayAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }
}
