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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Random;
import java.util.Scanner;

public class GameEngine extends Fragment {
    // Intent request codes
    private BluetoothAdapter mBluetoothAdapter = null;
    private String status;
    private TextView lblMode;
    private TextView otherPlayerName;
    private TextView yourAnswer;
    private TextView theirAnswer;
    private EditText answerInput;
    private Button btnSend;
    private Button btnQuit;
    private Button btnDecide;
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
        yourAnswer.setText(""); theirAnswer.setText("");
        btnSend = (Button) v.findViewById(R.id.btnSend);
        btnQuit = (Button) v.findViewById(R.id.btnQuit);
        btnDecide = (Button) v.findViewById(R.id.btnDecide);
        answerInput = (EditText) v.findViewById(R.id.editNumberInput);
        btnSend.setEnabled(false);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Toast.makeText(getActivity(), "Bluetooth is not available", Toast.LENGTH_LONG).show();
            getActivity().finish();
        }
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(answerInput.getText().length() < 1){
                    Toast.makeText(getActivity(), "Enter a number!", Toast.LENGTH_SHORT).show();
                    return;
                }
                yourAnswer.setText(answerInput.getText().toString().trim());
                String msg = "Turn ";
                msg += yourAnswer.getText().toString().trim();
                btService.write(msg.getBytes());
            }
        });
        btnQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btService.stop();
                getActivity().finish();
            }
        });
        btnDecide.setEnabled(false);
        btnDecide.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                pickWinner(true, null);
            }
        });
        return v;
    }

    public void updateOtherPlayerName(String name){
        otherPlayerName.setText(name);
    }

    public void handleMessage(String message){
        Scanner sc = new Scanner(message);
        String mode = sc.next();
        switch(mode){
            case "Turn":
                updateOtherPlayerAnswer(sc.next());
                break;
            case "Decide":
                pickWinner(false, sc.next());
                break;
        }
    }

    public void pickWinner(boolean host, String msg){
        int yours, theirs, win, answer;
        if(host) {
            if (yourAnswer.getText().length() < 1 || theirAnswer.getText().length() < 1) {
                Toast.makeText(getActivity(), "Error: someone has not picked a number", Toast.LENGTH_SHORT).show();
                return;
            }
            Random r = new Random();
            answer = r.nextInt(Integer.MAX_VALUE);
            String send = "Decide "+answer;
            btService.write(send.getBytes());
        } else {
            answer = Integer.parseInt(msg);
        }
        yours = Math.abs(answer - Integer.parseInt(yourAnswer.getText().toString()));
        theirs = Math.abs(answer - Integer.parseInt(theirAnswer.getText().toString()));
        win = Math.min(yours, theirs);
        if(win == yours){
            Toast.makeText(getActivity(),"You Won! Answer: "+answer, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "You Lost! Answer: "+answer, Toast.LENGTH_SHORT).show();
        }
    }

    public void updateOtherPlayerAnswer(String ans){
        theirAnswer.setText(ans);
    }

    public void changeSendBtnState(boolean state){
        btnSend.setEnabled(state);
        btnDecide.setEnabled(state);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    public void setBtService(BluetoothService btService){
        this.btService = btService;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
