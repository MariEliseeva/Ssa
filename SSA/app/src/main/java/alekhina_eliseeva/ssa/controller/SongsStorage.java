package alekhina_eliseeva.ssa.controller;

import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import alekhina_eliseeva.ssa.Applications;

class SongsStorage {
    private static DatabaseReference firebaseRef = FirebaseDatabase.getInstance().getReference();
    private static String currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    static void addSong(byte[] data) {
        UploadTask uploadTask = FirebaseStorage.getInstance().getReference()
                .child(currentUid).putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            }
        });
    }

    static String otherEmail = "";
    static String otherUid = "";

    static void getSong(final ArrayList<Task<byte[]>> tasks, final AtomicBoolean isEnd, final String email) {
        StringBuilder emailGood1 = new StringBuilder();
        for (int i = 0; i < email.length(); i++) {
            if (email.charAt(i) == '.') {
                emailGood1.append(',');
            } else {
                emailGood1.append(email.charAt(i));
            }
        }
        final String emailGood = emailGood1.toString().toLowerCase();
        otherEmail = emailGood;
        firebaseRef.child("UidByEmail")
                .child(emailGood).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tasks.add(getSong1(dataSnapshot.getValue().toString()));
                synchronized (isEnd) {
                    isEnd.set(true);
                    isEnd.notify();
                }
                otherUid = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private static Task<byte[]> getSong1(String address) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(address);
        final long MANY_MEGABYTE = 13230000;
        return storageReference.getBytes(MANY_MEGABYTE);
    }

    static void addNames(String v1, String v2, String v3, String v4) {
        firebaseRef.child("songNames").child(currentUid).child("s1").setValue(v1);
        firebaseRef.child("songNames").child(currentUid).child("s2").setValue(v2);
        firebaseRef.child("songNames").child(currentUid).child("s3").setValue(v3);
        firebaseRef.child("songNames").child(currentUid).child("s4").setValue(v4);
    }



    static void getVariants(final List<String> list, final ArrayAdapter<String> arrayAdapter) {
        final String emailGood = otherEmail;
        firebaseRef.child("UidByEmail")
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getVariants2(dataSnapshot.child(emailGood).getValue().toString(), list, arrayAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private static void getVariants2(final String uid, final List<String> list,
                                     final ArrayAdapter<String> arrayAdapter) {
        firebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
                dataSnapshot = dataSnapshot.child("songNames")
                        .child(uid);
                Controller.setRightAnswer(new Random().nextInt(4));
                for (DataSnapshot c : dataSnapshot.getChildren()) {
                    list.add(c.getValue().toString());
                }
                String tmp = list.get(0);
                list.set(0, list.get(Controller.getRightAnswer()));
                list.set(Controller.getRightAnswer(),tmp);
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
