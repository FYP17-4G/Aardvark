package com.example.FYP.aardvark_project;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.content.DialogInterface;
import android.content.ClipboardManager;
import android.content.ClipData;

import com.example.FYP.aardvark_project.encDecTools.*;
import com.example.FYP.aardvark_project.kryptoTools.*;

public class GUI_Enc_Dec extends AppCompatActivity {

    App_Framework framework = new App_Framework(this);;

    /**ENCRYPTION DECRYPTION OBJECT VARIABLES*/
    VigenereAdditive vigenereAdditive;
    VigenereInverse vigenereInverse;
    VigenereSubtractive vigenereSubtractive;
    /**-------------------------------------*/

    //cipher text view
    private String inputText = new String();
    private String key = new String();
    private TextView inputTextView;

    //copy and paste buttons
    private Button copyButton; //copy to clipboard
    private Button pasteButton; //paste from clipboard

    //shift cipher variables
    SeekBar caesarSeekBar;
    Button caesarShiftLeft;
    Button caesarShiftRight;

    TextView caesarShiftIndicator;

    private int shiftCipherBy = 0;

    //vigenere cipher variables
    Button vig_A;
    Button vig_I;
    Button vig_S;

    //transpo cipher variables
    Button transpo_R;

    //substitution cipher variables
    Button sub_R;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gui__enc__dec);

        inputTextView = (TextView) findViewById(R.id.ed_textInputView);

        copyButton = (Button) findViewById(R.id.button_copy_to_clip);
        pasteButton = (Button) findViewById(R.id.button_paste_from_clip);

        copyButton.setOnClickListener(copyPasteButtonListener);
        pasteButton.setOnClickListener(copyPasteButtonListener);

        setCaesarTool();
        setSubstitutionTool();
        setTranpoTool();
        setVigenereTool();
    }

    /**--------- Listeners*/

    View.OnClickListener copyPasteButtonListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            if(view.getId() == R.id.button_copy_to_clip)
            {
                if(!inputText.isEmpty())
                    copyToClip();

                else
                    framework.system_message_small("Error: Text is still empty");
            }
            else
                pasteFromClip();
        }
    };

    View.OnClickListener caesarShiftListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            if((caesarSeekBar.getProgress() != 0) && (!inputText.isEmpty()))
            {
                if(view.getId() == R.id.ed_caesar_shiftLeft)
                    doCaesarShiftL();

                else
                    doCaesarShiftR();
            }

        }
    };

    View.OnClickListener vigenereListener = new View.OnClickListener()
    {
        String title = "Vigenere Subtractive";

        @Override
        public void onClick(View view)
        {
            if(!inputText.isEmpty())
            {
                if(view.getId() == R.id.ed_vigenere_A)
                    title = "Vigenere Additive";
                else if(view.getId() == R.id.ed_vigenere_I)
                    title = "Vigenere Inverse";

                framework.popup_show(title, "Enter Key", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (framework.popup_getInput().isEmpty())
                            framework.system_message_small("Error: Input cannot be empty");

                        else
                        {
                            key = framework.popup_getInput();
                            if (view.getId() == R.id.ed_vigenere_A)
                                doVigenereAdditive(true);
                            else if (view.getId() == R.id.ed_vigenere_I)
                                doVigenereInverse(true);
                            else
                                doVigenereSubstractive(true);
                        }
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (framework.popup_getInput().isEmpty())
                            framework.system_message_small("Error: Input cannot be empty");

                        else
                        {
                            key = framework.popup_getInput();
                            if (view.getId() == R.id.ed_vigenere_A)
                                doVigenereAdditive(false);
                            else if (view.getId() == R.id.ed_vigenere_I)
                                doVigenereInverse(false);
                            else
                                doVigenereSubstractive(false);
                        }
                    }
                }, "Encrypt", "Decrypt");
            }

            else
                framework.system_message_small("Error: input text is still empty");

        }
    };

    View.OnClickListener transpoListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            if(!inputText.isEmpty())
            {
                if(view.getId() == R.id.ed_transpo_rect)
                {
                    framework.popup_getNumber_show("Tranposition", "Enter key", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (framework.popup_getInput().isEmpty())
                                framework.system_message_small("Error: Input cannot be empty");

                            else
                            {
                                key = framework.popup_getInput();
                                doTranspo(true);
                            }
                        }
                    }, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (framework.popup_getInput().isEmpty())
                                framework.system_message_small("Error: Input cannot be empty");

                            else
                            {
                                key = framework.popup_getInput();
                                doTranspo(false);
                            }
                        }
                    }, "Encrypt", "Decrypt");
                }
            }

            else
                framework.system_message_small("Error: input text is still empty");
        }
    };

    View.OnClickListener subListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            if(!inputText.isEmpty())
            {
                if(view.getId() == R.id.ed_substitution_rect)
                {
                    framework.popup_show("Substitution", "Enter key", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (framework.popup_getInput().isEmpty())
                                framework.system_message_small("Error: Input cannot be empty");

                            else {
                                key = framework.popup_getInput();
                                doSub(true);
                            }
                        }
                    });
                }
            }
            else
                framework.system_message_small("Error: input text is still empty");
        }
    };

    /**---------Tool Setters*/
    private void setCaesarTool()
    {
        caesarSeekBar = (SeekBar) findViewById(R.id.ed_caesarSeekBar);
        caesarShiftLeft = (Button) findViewById(R.id.ed_caesar_shiftLeft);
        caesarShiftRight = (Button) findViewById(R.id.ed_caesar_shiftRight);

        caesarSeekBar.setProgress(0);
        caesarSeekBar.setMax(25);
        caesarSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                caesarShiftIndicator.setText("Shift by: " + caesarSeekBar.getProgress());
                shiftCipherBy = caesarSeekBar.getProgress();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        caesarShiftLeft.setOnClickListener(caesarShiftListener);
        caesarShiftRight.setOnClickListener(caesarShiftListener);

        caesarShiftIndicator = (TextView) findViewById(R.id.ed_caesar_shift_indicator);
        caesarShiftIndicator.setText("Shift by: 0");
    }
    private void setVigenereTool()
    {
        vig_A = (Button) findViewById(R.id.ed_vigenere_A);
        vig_I = (Button) findViewById(R.id.ed_vigenere_I);
        vig_S = (Button) findViewById(R.id.ed_vigenere_S);

        vig_A.setOnClickListener(vigenereListener);
        vig_I.setOnClickListener(vigenereListener);
        vig_S.setOnClickListener(vigenereListener);
    }
    private void setTranpoTool()
    {
        transpo_R = (Button) findViewById(R.id.ed_transpo_rect);
        transpo_R.setOnClickListener(transpoListener);
    }
    private void setSubstitutionTool()
    {
        sub_R = (Button) findViewById(R.id.ed_substitution_rect);
        sub_R.setOnClickListener(subListener);
    }

    private void pasteFromClip()
    {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(this.CLIPBOARD_SERVICE);

        if(clipboard.hasPrimaryClip())
        {
            inputText = clipboard.getPrimaryClip().getItemAt(0).getText().toString(); //copy whats inside primary clipboard to input text
            inputText = inputText.toLowerCase();
            inputTextView.setText(inputText.toLowerCase());
        }

        else
            framework.system_message_small("Error: Clipboard is empty");
    }
    private void copyToClip()
    {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(this.CLIPBOARD_SERVICE);

        ClipData clip = ClipData.newPlainText("label", inputText);
        clipboard.setPrimaryClip(clip);

        framework.system_message_small("Copied to clipboard");
    }

    /**------Cipher Algorithm*/

    /**FOR ALL SHIFT ALGORITHM text variable: "input text: String", key variable: "shiftCipherBy: int"*/
    private void doCaesarShiftR() //shift ENCRYPT
    {
        try
        {
            inputText = new Shift().encrypt(inputText, Integer.toString(shiftCipherBy));
        }catch(InvalidKeyException e)
        {}

        refresh();
    }
    private void doCaesarShiftL() //shift DECRYPT
    {
        try
        {
            inputText = new Shift().decrypt(inputText, Integer.toString(shiftCipherBy));
        }catch(InvalidKeyException e)
        {}

        refresh();
    }

    /** FOR RECT TRANSPO, RECT SUBSTITUTION,AND ALL VIGENERE text variable: "input text: String", key variable: "key: String"*/
    private void doVigenereAdditive(boolean encrypt)
    {
        try
        {
            if(encrypt)
                inputText = vigenereAdditive.encrypt(framework.clean(inputText), key);
            else
                inputText = vigenereAdditive.decrypt(framework.clean(inputText), key);
        }catch(InvalidKeyException e)
        {}

        refresh();
    }
    private void doVigenereInverse(boolean encrypt)
    {
        try
        {
            if(encrypt)
                inputText = vigenereInverse.encrypt(framework.clean(inputText), key);
            else
                inputText = vigenereInverse.decrypt(framework.clean(inputText), key);
        }catch(InvalidKeyException e)
        {}

        refresh();
    }
    private void doVigenereSubstractive(boolean encrypt)
    {
        try
        {
            if(encrypt)
                inputText = vigenereSubtractive.encrypt(framework.clean(inputText), key);
            else
                inputText = vigenereSubtractive.decrypt(framework.clean(inputText), key);
        }catch(InvalidKeyException e)
        {}

        refresh();
    }

    private void doTranspo(boolean encrypt)
    {
        try
        {
            if(encrypt)
                inputText = new Transposition().encrypt(inputText, Integer.toString((int)key.charAt(0)));
            else
                inputText = new Transposition().decrypt(inputText, Integer.toString((int)key.charAt(0)));

        }catch(InvalidKeyException e)
        {}

        refresh();
    }

    private void doSub(boolean encrypt)
    {
        try
        {
            if(encrypt)
                inputText = new Substitution().encrypt(framework.clean(inputText), key);
            //else, do decrypt
            else
                inputText = new Substitution().decrypt(framework.clean(inputText), key);
        }catch(InvalidKeyException e)
        {}

        refresh();
    }

    private void refresh() //this refreshes the input text view, !! DO NOTE that it also removes any occurences of whitespace
    {
        inputTextView.setText(framework.clean(inputText));
    }

}
