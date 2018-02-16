package com.example.FYP.aardvark_project;

import android.graphics.Color;
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





    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_project_view_permutation, container, false);
        cipherTextView = view.findViewById(R.id.perm_cTextView);

        framework = new App_Framework(view.getContext(), true);

        //setSeekBars();

        calculatePermutation(cipherText);

        if(framework.isDarkTheme())
            cipherTextView.setTextColor(Color.WHITE);

        return view;
    }





    private void setPermutationButton()
    {

    }

    private void calculatePermutation(String input)
    {
        //permutationResult = new PermuteString().permute(input);
    }



    /**USE THESE ONLY WHEN INITIALIZING THIS OBJECT AND WHEN THE USER CLICKS ON PERMUTATION VIEW TAB*/
    public void setCipherText(String cipherText)
    {
        /*this.cipherText = cipherText;
        spaceSeekBar.setProgress(0);
        lineSeekBar.setProgress(0);*/
    }

    public void setOriginalCipherText(String originalCipherText)
    {
        this.originalCipherText = originalCipherText;
    }


}
