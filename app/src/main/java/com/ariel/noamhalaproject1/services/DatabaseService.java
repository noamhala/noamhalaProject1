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

        // Private method to generate a new ID for a new object in the database
        private String generateNewId(@NotNull final String path) {
            return databaseReference.child(path).push().getKey();
        }

        // New method to generate a unique workout ID
        public String generateWorkoutId() {
            return generateNewId("workouts");
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

        // New public method to fetch workouts for a specific coach
        public void getCoachWorkouts(Coach coachId, @NotNull final DatabaseCallback<List<Workout>> callback) {
            readData("workouts/coach/" + coachId).get().addOnCompleteListener(task -> {
                if (!task.isSuccessful()) {
                    Log.e(TAG, "Error getting data", task.getException());
                    callback.onFailed(task.getException());
                    return;
                }
                List<Workout> workouts = new ArrayList<>();
                task.getResult().getChildren().forEach(dataSnapshot -> {
                    Workout workout = dataSnapshot.getValue(Workout.class);  // Deserialize the workout
                    Log.d(TAG, "Got workout: " + workout);
                    workouts.add(workout);  // Add to the list
                });

                callback.onCompleted(workouts);  // Return the list of workouts
            });
        }

        // New public method to submit a workout request
        public void submitWorkoutRequestForCoach(Workout workout, @Nullable final DatabaseCallback<Void> callback) {

            writeData("coachWorkoutsSchedule/"+workout.getCoachId()+"/"+ workout.getDate().substring(0,4)+"/" +workout.getDate().substring(5,7)+"/"+workout.getDate().substring(8)+"/"+ workout.getId(), workout, callback);

           // writeData("coaches/"+workout.getCoachId()+  "/workouts/" + workout.getId(), workout, callback);
        }



        // New public method to submit a workout request
        public void submitWorkoutRequestForTrainee(Workout workout, @Nullable final DatabaseCallback<Void> callback) {

            writeData("traineeWorkoutsSchedule/"+workout.getCoachId()+"/"+ workout.getDate().substring(0,4)+"/"+workout.getDate().substring(5,7)+"/" +workout.getDate().substring(8)+"/"+ workout.getId(), workout, callback);
          //  writeData("trainees/"+workout.getTraineeId()+  "/workouts/" + workout.getId(), workout, callback);
        }

        // Method to retrieve workouts for a coach on a specific date
      //  public void retrieveWorkoutsForCoach(String coachId, int year, int month, int day,  @NotNull DatabaseCallback<Workout> callback) {
            // Get reference to the coach's workouts on the specified date
          //  reference = database.getReference("coachWorkoutsSchedule/" + coachId + "/" + year + "/" + (month < 10 ? "0" + month : month) + "/" + (day < 10 ? "0" + day : day));

       //    getData(("coachWorkoutsSchedule/" + coachId + "/" + year + "/" + month + "/" + day ), Workout.class, callback);


       // }

        // Public method to get all coaches
        public void retrieveWorkoutsForCoach( String coachId, String year, String month, String day,  @NotNull final DatabaseCallback<List<Workout>> callback) {

            String path="coachWorkoutsSchedule/" + coachId + "/" + year + "/" + "0"+ month + "/" +"0"+ day ;

            if(day.isEmpty()) {

                path="coachWorkoutsSchedule/" + coachId + "/" + year + "/" +  "0"+month ;
            }


            if(month.isEmpty()) {

                path="coachWorkoutsSchedule/" + coachId + "/" + year ;
            }
            Log.d(TAG, "pATH data"+ path);


            readData(path).get().addOnCompleteListener(task -> {
                if (!task.isSuccessful()) {
                    Log.e(TAG, "Error getting data", task.getException());
                    callback.onFailed(task.getException());
                    return;
                }
                List<Workout> workouts = new ArrayList<>();
                task.getResult().getChildren().forEach(dataSnapshot -> {
                    Workout workout = dataSnapshot.getValue(Workout.class);
                    Log.d(TAG, "Got coaches: " + workout);
                    workouts.add(workout);
                });

                callback.onCompleted(workouts);
            });
        }

        public void getWorkoutsForCoach( String coachId,   @NotNull final DatabaseCallback<List<Workout>> callback) {

            String path="coachWorkoutsSchedule/" + coachId;
            DatabaseReference myRef=  readData(path);

            List<Workout> workouts = new ArrayList<>();

            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {



                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        Log.d("postSnapshot", "num  is: " + postSnapshot.getKey());

                        for (DataSnapshot dm : postSnapshot.getChildren()) {

                            Log.d("dm", "num  is: " + dm.getKey());
                            for (DataSnapshot dd : dm.getChildren()) {


                                Log.d("dd", "num  is: " + dd.getKey());
                                for (DataSnapshot value : dd.getChildren()) {
                                    Workout workout = value.getValue(Workout.class);


                                    workouts.add(workout);
                                    Log.d("workout", "Value is: " + workout);
                                }
                            }
                        }
                    }
                    callback.onCompleted(workouts);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }

        public void getWorkoutsForTrainee(String traineeId, @NotNull final DatabaseCallback<List<Workout>> callback) {
            String path = "traineeWorkoutsSchedule/" + traineeId;
            DatabaseReference myRef = readData(path);

            List<Workout> workouts = new ArrayList<>();

            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // Loop through the data snapshot to collect the workouts
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        Log.d("postSnapshot", "num is: " + postSnapshot.getKey());

                        for (DataSnapshot dm : postSnapshot.getChildren()) {
                            Log.d("dm", "num is: " + dm.getKey());

                            for (DataSnapshot dd : dm.getChildren()) {
                                Log.d("dd", "num is: " + dd.getKey());

                                for (DataSnapshot value : dd.getChildren()) {
                                    // Deserialize the workout object from the snapshot
                                    Workout workout = value.getValue(Workout.class);
                                    if (workout != null) {
                                        workouts.add(workout);
                                        Log.d("workout", "Value is: " + workout);
                                    }
                                }
                            }
                        }
                    }

                    // Callback with the list of workouts
                    callback.onCompleted(workouts);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle error during database operation
                    Log.e("DatabaseError", "Failed to fetch workouts", error.toException());
                    callback.onFailed(error.toException());
                }
            });
        }


    }
