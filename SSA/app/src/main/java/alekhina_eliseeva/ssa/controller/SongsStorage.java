package alekhina_eliseeva.ssa.controller;

import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Random;

import alekhina_eliseeva.ssa.Applications;

class SongsStorage {
    static String addSong(byte[] data) {

        StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        UploadTask uploadTask = storageReference.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            }
        });
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    static String otherEmail = "";
    static String otherUid = "";

    static void getSong(final Applications activity, final ArrayList<Byte> arrayList,
                        final ArrayAdapter<Byte> arrayAdapter, final String email) {
        String emailGood1 = "";
        for (int i = 0; i < email.length(); i++) {
            if (email.charAt(i) == '.') {
                emailGood1 += ',';
            } else {
                emailGood1 += email.charAt(i);
            }
        }
        final String emailGood = emailGood1.toLowerCase();
        otherEmail = emailGood;

        FirebaseDatabase.getInstance().getReference().child("UidByEmail")
                .child(emailGood).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getSong1(activity, arrayList, arrayAdapter, dataSnapshot.getValue().toString());
                otherUid = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private static void getSong1(final Applications activity, final ArrayList<Byte> list,
                                 final ArrayAdapter<Byte> arrayAdapter, String address) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(address);
        final long MANY_MEGABYTE = 13230000;
        storageReference.getBytes(MANY_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                for (byte e : bytes) {
                    list.add(e);
                }
                arrayAdapter.notifyDataSetChanged();
                activity.next();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        });
    }

    static void addNames(String v1, String v2, String v3, String v4) {
        FirebaseDatabase.getInstance().getReference().child("songNames")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("s1").setValue(v1);
        FirebaseDatabase.getInstance().getReference().child("songNames")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("s2").setValue(v2);
        FirebaseDatabase.getInstance().getReference().child("songNames")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("s3").setValue(v3);
        FirebaseDatabase.getInstance().getReference().child("songNames")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("s4").setValue(v4);
    }



    static void getVariants(final ArrayList<String> arrayList, final ArrayAdapter<String> arrayAdapter) {
        final String emailGood = otherEmail;
        FirebaseDatabase.getInstance().getReference().child("UidByEmail")
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getVariants2(dataSnapshot.child(emailGood).getValue().toString(), arrayList, arrayAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private static void getVariants2(final String uid, final ArrayList<String> arrayList,
                                     final ArrayAdapter<String> arrayAdapter) {
        FirebaseDatabase.getInstance().getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayList.clear();
                dataSnapshot = dataSnapshot.child("songNames")
                        .child(uid);
                Controller.setRightAnswer(new Random().nextInt(4));
                for (DataSnapshot c : dataSnapshot.getChildren()) {
                    arrayList.add(c.getValue().toString());
                }
                String tmp = arrayList.get(0);
                arrayList.set(0, arrayList.get(Controller.getRightAnswer()));
                arrayList.set(Controller.getRightAnswer(),tmp);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    static byte[] reverseSong(byte[] bytes) {
        Deque<Byte> deque = new LinkedList<>();
        for (int i = 0; i + 3 < bytes.length; i+=4) {
            deque.addFirst(bytes[i + 3]);
            deque.addFirst(bytes[i + 2]);
            deque.addFirst(bytes[i + 1]);
            deque.addFirst(bytes[i]);
        }
        int i = 0;
        for (Byte b : deque) {
            bytes[i] = b;
            i++;
        }
        return bytes;
    }
}
