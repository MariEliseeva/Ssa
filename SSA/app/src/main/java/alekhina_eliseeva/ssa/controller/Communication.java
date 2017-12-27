package alekhina_eliseeva.ssa.controller;

import android.util.Log;
import android.widget.ArrayAdapter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import alekhina_eliseeva.ssa.PlayResultSong;

public class Communication {
    static void getSuggestList(final ArrayAdapter arrayAdapter, final ArrayList arrayList) {
        FirebaseDatabase.getInstance().getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayList.clear();
                dataSnapshot = dataSnapshot.child("messages")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                for (DataSnapshot c : dataSnapshot.getChildren()) {
                    arrayList.add(c.getValue());
                }
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    static void suggest(final String email) {
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
                Log.e("AAA", dataSnapshot.getValue().toString());
                Log.e("BBB", emailGood);
                suggest2(dataSnapshot.child(emailGood).getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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

    static void ignore(String email) {
        FirebaseDatabase.getInstance().getReference().child("messages")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(email).removeValue();
    }

    static void fixResult(boolean res) {
        String result = res ? "lose" : "win";
        FirebaseDatabase.getInstance().getReference().child("results").child(SongsStorage.otherEmail).
                child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(result);
    }

    static void getResult(final ArrayAdapter arrayAdapter, final ArrayList arrayList) {
        final String emailGood = SongsStorage.otherEmail;
        FirebaseDatabase.getInstance().getReference().child("results").child(emailGood)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        arrayList.add(dataSnapshot.getValue());
                        arrayAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
}
