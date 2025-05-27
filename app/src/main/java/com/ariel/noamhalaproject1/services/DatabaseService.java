    package com.ariel.noamhalaproject1.services;

    import android.util.Log;

    import androidx.annotation.NonNull;
    import androidx.annotation.Nullable;

    import com.ariel.noamhalaproject1.models.Coach;
    import com.ariel.noamhalaproject1.models.Trainee;
    import com.ariel.noamhalaproject1.models.User;
    import com.ariel.noamhalaproject1.models.Workout;  // Make sure to import the Workout model
    import com.google.firebase.database.DataSnapshot;
    import com.google.firebase.database.DatabaseError;
    import com.google.firebase.database.DatabaseReference;
    import com.google.firebase.database.FirebaseDatabase;
    import com.google.firebase.database.ValueEventListener;

    import org.jetbrains.annotations.NotNull;

    import java.util.ArrayList;
    import java.util.List;

    public class DatabaseService {

        // Tag for logging
        private static final String TAG = "DatabaseService";

        public void getTrainees(@NotNull final DatabaseCallback<List<Trainee>> callback) {
            readData("trainees/").get().addOnCompleteListener(task -> {
                if (!task.isSuccessful()) {
                    Log.e(TAG, "Error getting data", task.getException());
                    callback.onFailed(task.getException());
                    return;
                }

                List<Trainee> trainees = new ArrayList<>();
                task.getResult().getChildren().forEach(dataSnapshot -> {
                    Trainee trainee = dataSnapshot.getValue(Trainee.class);  // Deserialize the trainee
                    Log.d(TAG, "Got trainee: " + trainee);
                    trainees.add(trainee);  // Add to the list
                });

                callback.onCompleted(trainees);  // Return the list of trainees
            });
        }

        // Callback interface for database operations
        public interface DatabaseCallback<T> {

            void onCompleted(T object);
            void onFailed(Exception e);
        }

        // Singleton instance of this service
        private static DatabaseService instance;

        // Reference to the Firebase Realtime Database
        private final DatabaseReference databaseReference;

        // Private constructor to initialize the database reference
        private DatabaseService() {
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference();
        }

        // Get instance of the DatabaseService (singleton)
        public static DatabaseService getInstance() {
            if (instance == null) {
                instance = new DatabaseService();
            }
            return instance;
        }

        // Private method to write data to Firebase at a specific path
        private void writeData(@NotNull final String path, @NotNull final Object data, final @Nullable DatabaseCallback<Void> callback) {
            databaseReference.child(path).setValue(data).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    if (callback != null) {
                        callback.onCompleted(task.getResult());
                    }
                } else {
                    if (callback != null) {
                        callback.onFailed(task.getException());
                    }
                }
            });
        }

        // Private method to read data from Firebase at a specific path
        private DatabaseReference readData(@NotNull final String path) {
            return databaseReference.child(path);
        }

        // Private method to get data from Firebase at a specific path
        private <T> void getData(@NotNull final String path, @NotNull final Class<T> clazz, @NotNull final DatabaseCallback<T> callback) {
            readData(path).get().addOnCompleteListener(task -> {
                if (!task.isSuccessful()) {
                    Log.e(TAG, "Error getting data", task.getException());
                    callback.onFailed(task.getException());
                    return;
                }
                T data = task.getResult().getValue(clazz);
                callback.onCompleted(data);
            });
        }

        /// get a list of data from the database at a specific path
        /// @param path the path to get the data from
        /// @param clazz the class of the objects to return
        /// @param callback the callback to call when the operation is completed
        private <T> void getDataList(@NotNull final String path, @NotNull final Class<T> clazz, @NotNull final DatabaseCallback<List<T>> callback) {
            readData(path).get().addOnCompleteListener(task -> {
                if (!task.isSuccessful()) {
                    Log.e(TAG, "Error getting data", task.getException());
                    callback.onFailed(task.getException());
                    return;
                }
                List<T> tList = new ArrayList<>();
                task.getResult().getChildren().forEach(dataSnapshot -> {
                    T t = dataSnapshot.getValue(clazz);
                    tList.add(t);
                });

                callback.onCompleted(tList);
            });
        }

        // Private method to generate a new ID for a new object in the database
        private String generateNewId(@NotNull final String path) {
            return databaseReference.child(path).push().getKey();
        }


        // Public method to create a new user in the database
        // Public method to create a new user in the database
        public void createNewUser(@NotNull final User user, @Nullable final DatabaseCallback<Void> callback) {
            if (user instanceof Trainee) {
                // If the user is a trainee, create the new trainee
                createNewTrainee((Trainee) user, callback);
            } else if (user instanceof Coach) {
                // If the user is a coach, create the new coach
                createNewCoach((Coach) user, callback);
            } else {
                // If the user is neither a Trainee nor a Coach, handle the error
                Log.e(TAG, "Unknown user type: " + user.getClass().getSimpleName());
                if (callback != null) {
                    callback.onFailed(new IllegalArgumentException("Unknown user type"));
                }
            }
        }


        // Public method to get a user from the database by ID
        public void getUser(String uid, @NotNull final DatabaseCallback<User> callback) {
            // First, try fetching the user as a coach

            DatabaseService.DatabaseCallback<Trainee> traineeDatabaseCallback = new DatabaseCallback<Trainee>() {
                @Override
                public void onCompleted(Trainee trainee) {
                    callback.onCompleted(trainee);
                }

                @Override
                public void onFailed(Exception e) {

                }
            };
            getData("coaches/" + uid, Coach.class, new DatabaseCallback<Coach>() {
                @Override
                public void onCompleted(Coach coach) {
                    if (coach != null) {
                        callback.onCompleted(coach);  // If the user is found as a coach, return it
                    } else {
                        // If the coach is not found, try fetching the user as a trainee
                        getData("trainees/" + uid, Trainee.class, traineeDatabaseCallback);
                    }
                }

                @Override
                public void onFailed(Exception e) {
                    // If error happens while fetching the coach, try fetching the trainee
                    getData("trainees/" + uid, Trainee.class, traineeDatabaseCallback);
                }
            });
        }

        // New method to generate a unique workout ID
        public String generateWorkoutId() {
            return generateNewId("workouts");
        }

        // Public method to get a coach from the database by ID
        public void getCoach(String uid, @NotNull final DatabaseCallback<Coach> callback) {
            getData("coaches/"+uid, Coach.class, callback);
        }

        // Public method to get a trainee from the database by ID
        public void getTrainee(String uid, @NotNull final DatabaseCallback<Trainee> callback) {
            getData("trainees/" + uid, Trainee.class, callback);
        }

        // Public method to get all coaches
        public void getCoaches(@NotNull final DatabaseCallback<List<Coach>> callback) {
            readData("coaches/").get().addOnCompleteListener(task -> {
                if (!task.isSuccessful()) {
                    Log.e(TAG, "Error getting data", task.getException());
                    callback.onFailed(task.getException());
                    return;
                }
                List<Coach> coaches = new ArrayList<>();
                task.getResult().getChildren().forEach(dataSnapshot -> {
                    Coach coach = dataSnapshot.getValue(Coach.class);
                    Log.d(TAG, "Got coaches: " + coach);
                    coaches.add(coach);
                });

                callback.onCompleted(coaches);
            });
        }

        // Public method to create a new trainee in the database
        public void createNewTrainee(@NotNull final Trainee trainee, @Nullable final DatabaseCallback<Void> callback) {
            writeData("trainees/" + trainee.getId(), trainee, callback);  // Save to "trainees" path, not "users"
        }

        // Public method to create a new coach in the database
        public void createNewCoach(@NotNull final Coach coach, @Nullable final DatabaseCallback<Void> callback) {
            writeData("coaches/" + coach.getId(), coach, callback);
        }

        public void updateUser(User currentUser, DatabaseCallback<Void> databaseCallback) {
            createNewUser(currentUser, databaseCallback);
        }

        public void updateCoach(Coach currentCoach, DatabaseCallback<Void> databaseCallback) {
            // Use the same internal logic as creating a new coach, since it's an overwrite
            createNewCoach(currentCoach, databaseCallback);
        }

        public void updateTrainee(Trainee currentTrainee, DatabaseCallback<Void> callback) {
            createNewTrainee(currentTrainee, callback); // Overwrites the existing one
        }
        public void deleteCoach(@NotNull final String uid, @Nullable final DatabaseCallback<Void> callback) {
            deleteData("coaches/" + uid, callback);
        }
        public void deleteTrainee(@NotNull final String uid, @Nullable final DatabaseCallback<Void> callback) {
            deleteData("trainees/" + uid, callback);
        }

        /// remove data from the database at a specific path
        /// @param path the path to remove the data from
        /// @param callback the callback to call when the operation is completed
        /// @see DatabaseCallback
        private void deleteData(@NotNull final String path, @Nullable final DatabaseCallback<Void> callback) {
            readData(path).removeValue((error, ref) -> {
                if (error != null) {
                    if (callback == null) return;
                    callback.onFailed(error.toException());
                } else {
                    if (callback == null) return;
                    callback.onCompleted(null);
                }
            });
        }



        // New public method to fetch workouts for a specific coach
        public void getCoachWorkouts(String coachId, @NotNull final DatabaseCallback<List<Workout>> callback) {
            getDataList("workouts", Workout.class, new DatabaseCallback<List<Workout>>() {
                @Override
                public void onCompleted(List<Workout> workouts) {
                    workouts.removeIf(workout -> !workout.getCoachId().equals(coachId));
                    callback.onCompleted(workouts);
                }

                @Override
                public void onFailed(Exception e) {
                    callback.onFailed(e);
                }
            });
        }

        public void getTraineeWorkouts(String traineeId, @NotNull final DatabaseCallback<List<Workout>> callback) {
            getDataList("workouts", Workout.class, new DatabaseCallback<List<Workout>>() {
                @Override
                public void onCompleted(List<Workout> workouts) {
                    workouts.removeIf(workout -> !workout.getTraineeId().equals(traineeId));
                    callback.onCompleted(workouts);
                }

                @Override
                public void onFailed(Exception e) {
                    callback.onFailed(e);
                }
            });
        }

        public void updateWorkout(Workout workout, DatabaseCallback<Void> callback) {
            writeData("workouts/"+workout.getId(), workout, callback);
        }

    }
