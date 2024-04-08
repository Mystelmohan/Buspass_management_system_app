package com.example.buspass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.buspass.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONObject;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

public class MainActivity extends AppCompatActivity implements PaymentResultListener {


    private static final int RAZORPAY_REQUEST_CODE = 1001;
    ActivityMainBinding binding;
    private long backPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Checkout.preload(getApplicationContext());
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new home());
        binding.bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.Home) {
                replaceFragment(new home());
            }

            if (item.getItemId() == R.id.Profile) {
                replaceFragment(new profile());
            }

            if (item.getItemId() == R.id.Settings) {
                replaceFragment(new settings());
            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    public void about(View view) {
        replaceFragment(new About_us());
    }

    public void back1(View view) {
        replaceFragment(new settings());
    }

    public void help(View view) {
        replaceFragment(new Help());
    }
    public void appinfo(View view) {
        replaceFragment(new app_info());
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(getApplicationContext(), "Logout successful", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(MainActivity.this, Login.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        int backStackEntryCount = fragmentManager.getBackStackEntryCount();

        if (backStackEntryCount > 0) {
            // If there are fragments in the back stack, pop the back stack
            fragmentManager.popBackStack();
        } else {
            // If the back stack is empty, navigate to the Home fragment if not already there
            Fragment currentFragment = fragmentManager.findFragmentById(R.id.frame_layout);
            if (!(currentFragment instanceof home)) {
                replaceFragment(new home());

                // Update bottom navigation selected item
                binding.bottomNavigationView.setSelectedItemId(R.id.Home);
            } else {
                // If already on the Home fragment, proceed with default back behavior
                if (backPressedTime + 2000 > System.currentTimeMillis()) {
                    super.onBackPressed();
                    finish();
                } else {
                    Toast.makeText(this, "Press back again to close the app", Toast.LENGTH_SHORT).show();
                }
                backPressedTime = System.currentTimeMillis();
            }
        }
    }

    public void pay(View view) {
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_YSh4s9NOlNjAwd");

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Mohanraj");
            options.put("description", "Buspass payment");
            options.put("currency", "INR");

            options.put("amount", "100.00"); // Amount in paise
            options.put("prefill.email", "mohanrj7904@gmail.com"); // User's email (optional)

            checkout.open(MainActivity.this, options);
        } catch (Exception e) {
            Log.e("MainActivity", "Error in starting Razorpay Checkout", e);
        }
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        // Handle successful payment here
        Log.d("Payment Success", "Payment ID: " + razorpayPaymentID);
    }

    @Override
    public void onPaymentError(int code, String response) {
        // Handle payment error here
        Log.e("Payment Error", "Code: " + code + ", Response: " + response);
    }

}