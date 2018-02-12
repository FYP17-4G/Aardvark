package com.example.FYP.aardvark_project;

import android.animation.AnimatorInflater;
import android.animation.StateListAnimator;
import android.app.AlertDialog;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.content.Intent;

import com.example.FYP.aardvark_project.Database.DatabaseFramework;


/**
* NOTE:
* adapter = the whole thing
* viewholder = an element in adapter
* */

public class FrontPageAdapter extends RecyclerView.Adapter<FrontPageAdapter.viewHolder>
{
    private int INITIAL_CARD_ANIM_DURATION = 450;
    private final int CARD_ANIM_DURATION_INCREMENT = 100;

    private ArrayList<FrontPageIdentifier> projectTitle;

    private Context context;

    private App_Framework framework;
    private DatabaseFramework database;

    private Animation cardAnim;

    private View view;

    public FrontPageAdapter(ArrayList<FrontPageIdentifier> n)
        {
            this.projectTitle = n;
        }

     @Override
     public viewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
        {
            context = viewGroup.getContext();
            framework = new App_Framework(context, true);
            database = new DatabaseFramework(context);

            LayoutInflater inflater = LayoutInflater.from(context);

            view = inflater.inflate(R.layout.front_page_card, viewGroup, false);
            viewHolder viewHolder = new viewHolder(view);

            return viewHolder;
        }

    @Override
    public void onBindViewHolder(viewHolder holder, int position)
        {
            holder.bind(position);
        }

    @Override
    public int getItemCount()
        {
            return projectTitle.size();
        }

    /**Accepts filtered list container(from a function that does this in the Main View), and assign it this class's FrontPageIdentifier list*/
    public void filterList(ArrayList<FrontPageIdentifier> filteredList)
    {
        projectTitle = filteredList;
        notifyDataSetChanged();
    }

    /**THIS IS INDIVIDUAL ITEM INSIDE THE ADAPTER*/
    class viewHolder extends RecyclerView.ViewHolder
        {
            private TextView itemPreview;
            private TextView itemTitle; //this displays the project title, TextView is an xml element
            private FrameLayout itemContentFrame; //the CARD
            private ConstraintLayout projectEditButton;
            private Button projectEditButtonInner;

            private String id;
            private String title;
            private String previewCText;

            /**this will get the context from the caller of this class (in this case is the main activity)
             * so can start a new activity which is connected to the caller via the Manifest*/
            Context context;

            public viewHolder(View itemView)
                {
                    super(itemView);
                    setCardView(itemView);

                    adjustTheme();

                    setEnterAnimation();
                }

            private final View.OnClickListener editButtonListener = v -> showEditProject();
            private final View.OnClickListener cardClickListener = view -> launchProjectView();

            private final View.OnLongClickListener cardLongClickListener = view -> {

                    AlertDialog alertDialog = framework.popup_cipher_preview(previewCText);
                    alertDialog.show();
                    return true;
            };

            /**this function will be called by member of interface onBindViewHolder from recycler extension*/
            void bind(int idx)
            {
                id = projectTitle.get(idx).getID(); //gets ID from array list container in the parent container
                title = projectTitle.get(idx).getTitle(); //gets title from array list container in the parent container
                idx = idx;

                /**
                 * previewCText variable is used when the user long pressed a card view, and the cipher text preview of that project will be shown
                 * */
                previewCText = new DatabaseFramework(context).getCipherText(id, title); //gets the cipher text directly from the database, similar to the formatting in change history
                String[] split = previewCText.split("\\|"); //split and add to change history

                ArrayList<String> temp = new ArrayList<>(Arrays.asList(split));
                previewCText = temp.get(temp.size() - 1);

                itemPreview.setText(processStringForPreview(previewCText));
                itemTitle.setText("  " + projectTitle.get(idx).getTitle());
            }

            private void setCardView(View itemView)
            {
                context = itemView.getContext();

                itemPreview = itemView.findViewById(R.id.card_preview);
                itemTitle = itemView.findViewById(R.id.card_title);
                itemContentFrame = itemView.findViewById(R.id.card);
                projectEditButton = itemView.findViewById(R.id.card_button);
                projectEditButtonInner = itemView.findViewById(R.id.card_button_inner);

                setOnTouchAnimation();

                itemContentFrame.setOnClickListener(cardClickListener);
                itemContentFrame.setOnLongClickListener(cardLongClickListener);
                projectEditButton.setOnClickListener(editButtonListener);
                projectEditButtonInner.setOnClickListener(editButtonListener);

                itemContentFrame.setClickable(true);
            }

            /**
             * Set up the card animation on object creation.
             * Each individual card have different duration, this creates crisp animation appearance
             * */
            private void setEnterAnimation()
            {
                cardAnim = AnimationUtils.loadAnimation(context, R.anim.anim_card_view);
                cardAnim.setDuration(INITIAL_CARD_ANIM_DURATION);
                INITIAL_CARD_ANIM_DURATION += CARD_ANIM_DURATION_INCREMENT;
                view.startAnimation(cardAnim);
            }

            /**
             * Adjusts the theme to what theme is being selected at the Activity_Settings menu
             * */
            private void adjustTheme()
            {
                //set the card theme
                if(new App_Framework(context, true).setTheme())
                {
                    itemContentFrame.setBackgroundColor(context.getResources().getColor(R.color.cardview_dark_background));
                    itemTitle.setTextColor(context.getResources().getColor(R.color.dark_primaryTextColor));
                    itemPreview.setTextColor(context.getResources().getColor(R.color.dark_secondaryTextColor));
                }
                else
                {
                    itemContentFrame.setBackgroundColor(context.getResources().getColor(R.color.cardview_light_background));
                }
            }

            private String processStringForPreview(String s)
            {
                boolean MORE = true;
                int PREVIEW_LENGTH = 300;

                if(s.length() < PREVIEW_LENGTH)
                {
                    PREVIEW_LENGTH = s.length();
                    MORE = false;
                }

                String temp = new String();

                for(int i = 0; i < PREVIEW_LENGTH; i++)
                    temp += s.charAt(i);

                if(MORE)
                    temp += "......";

                return temp;
            }

            /**THIS SHOWS EDIT PROJECT POPUP MENU*/
            private void showEditProject()
            {
                LayoutInflater layoutInflater = LayoutInflater.from(context);
                View view = layoutInflater.inflate(R.layout.pop_project_options, null);

                final AlertDialog alertDialog = framework.popup_custom(title, view);

                Button editButton = view.findViewById(R.id.button_edit);
                Button deleteButton = view.findViewById(R.id.button_delete);

                editButton.setOnClickListener(view1 -> {

                    /**EDIT function reuses the layout for creating a new project,*/
                    Activity_Main mainActivity = (Activity_Main)context;
                    String cipherText = database.getCipherText(id, title);

                    /**Use pop up menu function from Main Activity*/
                    mainActivity.editProject(id, title, cipherText);

                    alertDialog.cancel();
                    notifyDataSetChanged();
                });

                String delete_message = "Delete '" + title + "'?";
                deleteButton.setOnClickListener(view12 -> framework.system_message_confirmAction("Delete Project", delete_message, (dialogInterface, i) -> {

                    deleteProject();

                    alertDialog.cancel();
                    notifyDataSetChanged();
                }));
                alertDialog.show();
            }

            private void deleteProject()
            {
                database.deleteEntry(id, title);
                projectTitle = database.getAllTitle();

                Activity_Main mainActivity = (Activity_Main)context;
                mainActivity.getListFromDB();

                notifyDataSetChanged();
            }

            private void launchProjectView()//put the passed value as parameter
            {
                /**Start a new activity, and passes some variables: project ID & project Title*/
                Intent intent = new Intent(context, Activity_Project_View.class);
                intent.putExtra("project_view_unique_ID", id);//this will pass on variables to the new activity, access it using the "name" (first param in this function)
                intent.putExtra("project_view_title", title);

                context.startActivity(intent);
            }

            private void setOnTouchAnimation()
            {
                StateListAnimator stateListAnimator = AnimatorInflater.loadStateListAnimator(context, R.anim.lift_on_touch);
                itemContentFrame.setStateListAnimator(stateListAnimator);
            }
        }
}
