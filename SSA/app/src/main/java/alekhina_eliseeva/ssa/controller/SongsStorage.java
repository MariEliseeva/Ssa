package alekhina_eliseeva.ssa.controller;

import android.net.Uri;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class SongsStorage {
    static String addSong(byte[] data) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference()
               .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        // Get the data from an ImageView as bytes

        UploadTask uploadTask = storageReference.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                //Uri downloadUrl = taskSnapshot.getDownloadUrl();
                //Log.e("AAAA", FirebaseAuth.getInstance().getCurrentUser().getUid());
            }
        });
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    static void getSong(final ArrayList list, final ArrayAdapter arrayAdapter, String address) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(address);
        final long ONE_MEGABYTE = 1024 * 1024;
        storageReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/island.jpg" is returns, use this as needed
                for (byte e : bytes) {
                    list.add(e);
                }
                arrayAdapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exception) {
                // Handle any errors
            }
        });
    }
}
