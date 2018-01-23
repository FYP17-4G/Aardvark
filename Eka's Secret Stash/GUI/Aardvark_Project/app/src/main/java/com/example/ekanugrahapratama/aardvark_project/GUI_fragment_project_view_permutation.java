package com.example.ekanugrahapratama.aardvark_project;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.SeekBar;
import android.widget.Button;

import com.example.ekanugrahapratama.aardvark_project.kryptoTools.PermuteString;

/**
 * Another fragment is "GUI_rpojectView"
 */

public class GUI_fragment_project_view_permutation extends Fragment
{
    private static final String TAG = "fragment_project_view_permutation";

    private App_Framework framework;

    private String cipherText = new String();
    private TextView cipherTextView;

    private View view;

    private TextView spaceIndicator;
    private TextView lineIndicator;

    private SeekBar spaceSeekBar;
    private SeekBar lineSeekBar;

    private Button permutateButton;

    private int space = 0;
    private int line = 0;

    private PermuteString permuteString;

    private final int MAX_SEEKBAR = 20;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_project_view_permutation, container, false);

        framework = new App_Framework(view.getContext());

        permuteString = new PermuteString();

        cipherTextView = (TextView) view.findViewById(R.id.permutation_cipherTextView);
        cipherTextView.setTextColor(Color.BLACK);

        /**Build the view here*/
        spaceIndicator = (TextView) view.findViewById(R.id.seekBar_space_indicator);
        lineIndicator = (TextView) view.findViewById(R.id.seekBar_line_indicator);
        spaceIndicator.setText("chars per space: " + space);
        lineIndicator.setText("words per line:" + line);

        spaceSeekBar = (SeekBar) view.findViewById(R.id.seekBar_space);
        lineSeekBar = (SeekBar) view.findViewById(R.id.seekBar_line);
        spaceSeekBar.setProgress(space);
        lineSeekBar.setProgress(line);
        spaceSeekBar.setMax(MAX_SEEKBAR);
        lineSeekBar.setMax(MAX_SEEKBAR);
        spaceSeekBar.setOnSeekBarChangeListener(seekBarChangeListener);
        lineSeekBar.setOnSeekBarChangeListener(seekBarChangeListener);

        permutateButton = (Button) view.findViewById(R.id.button_permutate);
        permutateButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                framework.popup_getNumber_show("Permutate", "Enter permutation block size", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        int key = Integer.parseInt(framework.popup_getInput());

                        /**Runs permutation on different thread, since the function is heavy AF*/
                        getActivity().runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                String temp = permutation(cipherText, key);
                                refresh(temp, space, line);
                            }
                        });
                    }
                }, 3);
            }
        });

        return view;
    };

    /**USE THIS ONLY WHEN INITIALIZING THIS OBJECT*/
    public void setCipherText(String cipherText)
    {
        this.cipherText = cipherText;
        cipherTextView.setText(this.cipherText);
    }

    public void refresh(String val, int space, int line)
    {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String temp = framework.stringNoWhiteSpace(val); /**DISPLAY temp! NOT cipherText*/

                //do spacing here
                if(space != 0)
                    temp = spacing(temp, space);
                //do lining here
                if(line != 0)
                    temp = lining(temp, line);

                spaceIndicator.setText("chars per space: " + space);
                lineIndicator.setText("words per line:" + line);
                cipherTextView.setText(temp);
            }
        });
    }

    private String permutation(String input, int blockSize)
    {
        return permuteString.permute(framework.clean(input), blockSize);
    }

    private String spacing(String input, int space)
    {
        String temp = new String();

        int x = 0;
        for(int i = 0; i < input.length(); i++)
        {
            temp += input.charAt(i);
            if(x >= space)
            {
                temp += ' ';
                x = 0;
            }
            x++;
        }

        return temp;
    }

    private String lining(String input, int line)
    {
        String temp = new String();

        int x = 0;
        for(int i = 0; i < input.length(); i++)
        {
            if(input.charAt(i) == ' ')
                x++;

            if(x >= line)
            {
                temp += '\n';
                x = 0;
            }

            temp += input.charAt(i);
        }

        return temp;
    }

    SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener()
    {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b)
        {
            if(seekBar.getId() == R.id.seekBar_space)
                space = spaceSeekBar.getProgress();
            else
                line = lineSeekBar.getProgress();

            refresh(cipherText, space, line);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };
}
