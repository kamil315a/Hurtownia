package com.example.hurtownia;
        import androidx.annotation.NonNull;
        import androidx.appcompat.app.AppCompatActivity;

        import android.content.Intent;
        import android.os.Bundle;
        import android.util.Patterns;
        import android.view.View;
        import android.widget.Toast;

        import com.example.hurtownia.databinding.ActivityRegisterUserBinding;
        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.Task;
        import com.google.firebase.auth.AuthResult;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.database.FirebaseDatabase;

        import java.util.regex.Pattern;
public class RegisterUser extends AppCompatActivity {
  
    private FirebaseAuth mAuth;
    private ActivityRegisterUserBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // tworze instancje firebase:


        mAuth = FirebaseAuth.getInstance();


        //bindowanie (view) przyciskow:


        binding = ActivityRegisterUserBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        // zdarzenie na klikniecie RegisterUser przenosi do (dokonczyc):

        
        binding.RegisterUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent intent = new Intent(RegisterUser.this, MainActivity.class);
                // RegisterUser.this.startActivity(intent);

                final String email = binding.EmailAdress.getText().toString().trim();
                String password = binding.Password.getText().toString().trim();
                final String nickName = binding.NickName.getText().toString().trim();

                if (email.isEmpty()) {
                    binding.EmailAdress.setError("Please provide valid email");
                    binding.EmailAdress.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                  binding.EmailAdress.setError("Please provide valid email");
                   binding.EmailAdress.requestFocus();
                   return;
                }

                if (password.isEmpty()) {
                    binding.Password.setError("Password is required");
                    binding.Password.requestFocus();
                    return;
                }
                if (password.length()<=8){
                    binding.Password.setError("Min password length should be 9 characters!");
                    binding.Password.requestFocus();
                    return;
                }


                if (nickName.isEmpty()) {
                    binding.NickName.setError("Full name is required");
                    binding.NickName.requestFocus();
                    return;
                }

                binding.progressBar2.setVisibility(View.VISIBLE);
                mAuth.createUserWithEmailAndPassword(email,password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    User user = new User(nickName, email);

                                    FirebaseDatabase.getInstance().getReference("Users")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(RegisterUser.this, "User has been registered successfull!", Toast.LENGTH_LONG).show();
                                                binding.progressBar2.setVisibility(View.VISIBLE);
                                            }else{
                                                Toast.makeText(RegisterUser.this, "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                                                binding.progressBar2.setVisibility(View.GONE);
                                            }
                                        }
                                    });
                                }else{
                                    Toast.makeText(RegisterUser.this, "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                                    binding.progressBar2.setVisibility(View.GONE);
                                }
                            }
                        });

            }
        });






    }


}