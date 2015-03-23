package entena.com.nfcguessgame;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class GameEngine extends Fragment {
    // Intent request codes
    private BluetoothAdapter mBluetoothAdapter = null;
    private String status;
    private TextView lblMode;
    private TextView otherPlayerName;
    private TextView yourAnswer;
    private TextView theirAnswer;
    private Button btnSend;
    private Button btnQuit;
    private BluetoothService btService;

    public GameEngine() {
        // Required empty public constructor
    }

    public void setStatus(String status){
        this.status = status;
        if(lblMode != null){
            lblMode.setText(status);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_game, container, false);
        lblMode = (TextView) v.findViewById(R.id.txtViewMode);
        otherPlayerName = (TextView) v.findViewById(R.id.lblOtherPlayer);
        yourAnswer = (TextView) v.findViewById(R.id.yourAnswer);
        theirAnswer = (TextView) v.findViewById(R.id.theirAnswer);
        btnSend = (Button) v.findViewById(R.id.btnSend);
        btnQuit = (Button) v.findViewById(R.id.btnQuit);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Toast.makeText(getActivity(), "Bluetooth is not available", Toast.LENGTH_LONG).show();
            getActivity().finish();
        }
        return v;
    }

    public void updateOtherPlayerName(String name){
        otherPlayerName.setText(name);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        btService = ((MainActivity) activity).mChatService;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
