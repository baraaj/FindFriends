package issat.baraa.findfriends.ui.home;

import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import issat.baraa.findfriends.MainActivity;
import issat.baraa.findfriends.R;
import issat.baraa.findfriends.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        binding.btnEnvoie.setOnClickListener(view -> {
            String numero=binding.edphone.getText().toString();
            //Envoie sms ->permission send sms
            if(MainActivity.sentPermissions){
                SmsManager manager= SmsManager.getDefault();
                manager.sendTextMessage(numero,null,"Envoyer moi votre localisation",null,null);

            }else {
               // Toast.makeText(this,"xbxv",Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}