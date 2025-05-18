package com.example.allaskereso_portal;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirestoreHelper {
    private static final String TAG = "FirestoreOperations";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private static final String LOG = RegisterActivity.class.getName();

    public void saveJobToFirestore(Job job) {
        db.collection("Jobs")
        .add(job)
        .addOnSuccessListener(documentReference -> {
            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
            job.setId(documentReference.getId());
        })
        .addOnFailureListener(e -> {
            Log.w(TAG, "Error adding document", e);
        });
    }

    public void updateOrCreateJob(Job job) {
        String jobId = job.getId();
        db.collection("Jobs").document(jobId)
                .set(job)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully written!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error writing document", e));
    }

    public void getJobs(final FirestoreCallback firestoreCallback) {
Log.e(LOG, "inside getjobs");
        db.collection("Jobs")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Job> jobList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.v(LOG, document.getId() + " -------=> " + document.getData());
                            try {
                                Job job = document.toObject(Job.class);
                                jobList.add(job);
                            } catch (RuntimeException e) {
                                Log.v(TAG, "Error while creating job: " + document.getId(), e);
                            }
                        }
                        firestoreCallback.onCallback(jobList);
                    } else {
                        Log.e(LOG, "Error getting documents.", task.getException());
                        firestoreCallback.onError(task.getException());
                        Log.e(LOG, "Firestore error:", task.getException());
                    }
                });
    }

    public interface FirestoreCallback {
        void onCallback(List<Job> jobList);
        void onError(Exception e);
    }

    public interface CreateUserCallback {
        void onSuccess(FirebaseUser user);
        void onError(Exception e);
    }

    public void createUserWithEmailAndPassword(String email, String password, String name, final CreateUserCallback callback) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "createUserWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            String userId = user.getUid();
                            Map<String, Object> userData = new HashMap<>();
                            userData.put("email", email);
                            userData.put("name", name);

                            db.collection("Users").document(userId)
                                    .set(userData)
                                    .addOnSuccessListener(aVoid -> {
                                        Log.d(TAG, "Firestore user document created for UID: " + userId);
                                        callback.onSuccess(user);
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e(TAG, "Error creating Firestore user document.", e);
                                        callback.onError(e);
                                    });
                        }
                    } else {
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        callback.onError(task.getException());
                    }
                });
    }

    public interface UpdateUserProfileCallback {
        void onSuccess();
        void onError(Exception e);
    }

    public void updateUserEmail(String userId, String newEmail, final UpdateUserProfileCallback callback) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            user.updateEmail(newEmail)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User email updated successfully.");
                            db.collection("Users").document(userId)
                                .update("email", newEmail)
                                .addOnSuccessListener(aVoid -> {
                                    Log.d(TAG, "Firestore user document updated successfully.");
                                    callback.onSuccess();
                                })
                                .addOnFailureListener(e -> {
                                    Log.e(TAG, "Error updating Firestore user document.", e);
                                    callback.onError(e);
                            });
                            callback.onSuccess();
                        } else {
                            Log.e(TAG, "Error updating user email.", task.getException());
                            callback.onError(task.getException());
                        }
                    });
        } else {
            Log.w(TAG, "No current user found to update email.");
            callback.onError(new Exception("No current user found."));
        }
    }

    public void updateUserFirestore(String userId, String newName, final UpdateUserProfileCallback callback) {
        Log.v(LOG, "----- USER ID----- \n"+userId);
        db.collection("Users").document(userId)
        .update("name", newName)
        .addOnSuccessListener(aVoid -> {
            Log.d(TAG, "Firestore user document updated successfully.");
            callback.onSuccess();
        })
        .addOnFailureListener(e -> {
            Log.e(TAG, "Error updating Firestore user document.", e);
            callback.onError(e);
        });
    }

    public interface DeleteUserCallback {
        void onSuccess();
        void onError(Exception e);
    }

    public void deleteCurrentUser(final DeleteUserCallback callback) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            user.delete()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User account deleted successfully.");
                            // Optionally, you might want to delete the user's Firestore data here
                            deleteUserFirestoreData(user.getUid(), new DeleteUserCallback() {
                                @Override
                                public void onSuccess() {
                                    Log.d(TAG, "Firestore user data also deleted.");
                                    callback.onSuccess();
                                }

                                @Override
                                public void onError(Exception e) {
                                    Log.e(TAG, "Error deleting Firestore user data.", e);
                                    callback.onError(e);
                                }
                            });
                        } else {
                            Log.e(TAG, "Error deleting user account.", task.getException());
                            callback.onError(task.getException());
                        }
                    });
        } else {
            Log.w(TAG, "No current user found to delete.");
            callback.onError(new Exception("No current user found."));
        }
    }

    public void deleteUserFirestoreData(String userId, final DeleteUserCallback callback) {
        db.collection("Users").document(userId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Firestore document for user " + userId + " deleted.");
                    callback.onSuccess();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error deleting Firestore document for user " + userId + ".", e);
                    callback.onError(e);
                });
    }
}
