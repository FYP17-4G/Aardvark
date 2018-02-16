package com.example.FYP.aardvark_project;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.SeekBar;
import android.widget.Button;

import com.example.FYP.aardvark_project.kryptoTools.PermuteString;

import java.util.List;

/**
 * Another fragment is "GUI_rpojectView"
 */

public class fragment_project_view_permutation extends Fragment
{
    private App_Framework framework;

    private String cipherText = new String();
    private String originalCipherText = new String();
    private TextView cipherTextView;

    private View view;

    private TextView spaceIndicator;
    private TextView lineIndicator;

    private SeekBar spaceSeekBar;
    private SeekBar lineSeekBar;

    private int space = 0;
    private int line = 0;

    private Button permutateButton;
    private PermuteString permuteString;
    private List<String> permutationResult;

    private final int MAX_SEEKBAR = 20;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_project_view_permutation, container, false);
        cipherTextView = view.findViewById(R.id.perm_cTextView);

        framework = new App_Framework(view.getContext(), true);

        setSeekBars();

        calculatePermutation(cipherText);
        return view;
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

            spaceIndicator.setText("chars per space: " + space);
            lineIndicator.setText("words per line:" + line);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            refresh(cipherText, space, line);
        }
    };

    private void setSeekBars()
    {
        spaceIndicator = view.findViewById(R.id.seekBar_space_indicator);
        lineIndicator = view.findViewById(R.id.seekBar_line_indicator);
        spaceIndicator.setText("chars per space: " + space);
        lineIndicator.setText("words per line:" + line);

        spaceSeekBar = view.findViewById(R.id.seekBar_space);
        lineSeekBar = view.findViewById(R.id.seekBar_line);
        spaceSeekBar.setProgress(space);
        lineSeekBar.setProgress(line);
        spaceSeekBar.setMax(MAX_SEEKBAR);
        lineSeekBar.setMax(MAX_SEEKBAR);
        spaceSeekBar.setOnSeekBarChangeListener(seekBarChangeListener);
        lineSeekBar.setOnSeekBarChangeListener(seekBarChangeListener);
    }

    private void setPermutationButton()
    {

    }

    private void calculatePermutation(String input)
    {
        permutationResult = new PermuteString().permute(input);
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

    /**USE THESE ONLY WHEN INITIALIZING THIS OBJECT AND WHEN THE USER CLICKS ON PERMUTATION VIEW TAB*/
    public void setCipherText(String cipherText)
    {
        this.cipherText = cipherText;
        spaceSeekBar.setProgress(0);
        lineSeekBar.setProgress(0);
    }

    public void setOriginalCipherText(String originalCipherText)
    {
        this.originalCipherText = originalCipherText;
    }

    public void refresh(String val, int space, int line)
    {
        getActivity().runOnUiThread(() -> {
            cipherTextView.setText("");

            String temp = framework.stringNoWhiteSpace(val); //display this variable
            String tempOriginal = framework.stringNoWhiteSpace(originalCipherText);

            if(space > 0) //spacing
            {
                temp = spacing(temp, space);
                tempOriginal = spacing(tempOriginal, space);
            }
            if(line > 0) //lining
            {
                temp = lining(temp, line);
                tempOriginal = lining(tempOriginal, line);
            }

            if(temp.length() == tempOriginal.length())
                for(int i = 0; i < temp.length(); i++)
                {
                    if(temp.charAt(i) == tempOriginal.charAt(i))
                        cipherTextView.append(Character.toString(temp.charAt(i)));
                    else
                        cipherTextView.append(Html.fromHtml("<font color='#EE0000'>"+ temp.charAt(i) +"</font>")); //set the text color to red
                }

        });
    }
}
