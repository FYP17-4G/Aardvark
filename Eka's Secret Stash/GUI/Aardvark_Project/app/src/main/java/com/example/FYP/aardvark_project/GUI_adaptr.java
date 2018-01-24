package com.example.FYP.aardvark_project;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.io.BufferedWriter;

import android.content.Context;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.graphics.Color;
import android.content.Intent;
import android.widget.ImageButton;

import com.example.FYP.aardvark_project.Database.DatabaseFramework;


/*
* NOTE:
* adapter = the whole thing
* viewholder = an element in adapter
*
* 3 things to override when extending RecyclerView.Adapter
*   - onCreateViewHolder
*   - onBindViewHolder
*   - getItemCount
* */

public class GUI_adaptr extends RecyclerView.Adapter<GUI_adaptr.viewHolder>
{
    ArrayList<FrontPageIdentifier> projectTitle;
    private String projectDirectoryFileName = "projectDirectory.txt";

    Context context;

    App_Framework framework;
    DatabaseFramework database;

    public GUI_adaptr(ArrayList<FrontPageIdentifier> n)
        {
            this.projectTitle = n;
        }

     @Override
     public viewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
        {
            context = viewGroup.getContext();
            int layoutIdForListItem = R.layout.front_page_card; //This will return the 'ID' of xml file layout, R.layout.<xml name> are built in function designed for this
            LayoutInflater inflater = LayoutInflater.from(context);
            boolean shouldAttachToParentImmediately = false;

            View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
            viewHolder viewHolder = new viewHolder(view);

            framework = new App_Framework(context);
            database = new DatabaseFramework(context);

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

    /**THIS IS INDIVIDUAL ITEM INSIDE THE ADAPTER*/
    class viewHolder extends RecyclerView.ViewHolder
        {
            TextView itemPreview;
            TextView itemTitle; //this displays the project title, TextView is an xml element
            FrameLayout itemContentFrame;
            ConstraintLayout projectEditButton;
            Button projectEditButtonInner;
            CardView cardView;

            private String id;
            private String title;

            private int idx; //entry index in the recycler component

            //this will get the context from the caller of this class (in this case is the main activity)
            //this way we can start a new activity which is connected to the caller via the Manifest
            Context context;

            //@constructor
            public viewHolder(View itemView)
                {
                    super(itemView);
                    itemPreview = (TextView)itemView.findViewById(R.id.card_preview);
                    itemTitle = (TextView)itemView.findViewById(R.id.card_title); //the ID of the element can be found in front_page_card
                    itemContentFrame = (FrameLayout)itemView.findViewById(R.id.card);
                    projectEditButton = (ConstraintLayout) itemView.findViewById(R.id.card_button);
                    projectEditButtonInner = itemView.findViewById(R.id.card_button_inner);

                    context = itemView.getContext();

                    itemContentFrame.setOnTouchListener(adapterTouchListener);
                    projectEditButton.setOnClickListener(editButtonListener);
                    projectEditButtonInner.setOnClickListener(editButtonListener);
                }

            //this function will be called by member of interface onBindViewHolder from recycler extension
            void bind(int idx)
                {
                    //these 2 lines will assign unique ID and title taken from list.txt to individual view holder
                    this.id = projectTitle.get(idx).getID();
                    this.title = projectTitle.get(idx).getTitle();
                    this.idx = idx;

                    itemPreview.setText(processStringForPreview(new DatabaseFramework(context).getCipherText(id, title))); //gets the cipher text directly from the database
                    itemTitle.setText("  " + projectTitle.get(idx).getTitle());
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

            private final View.OnClickListener editButtonListener = new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                    {
                        showEditProject();
                    }
            };

            /**THIS SHOWS EDIT PROJECT POPUP MENU*/
            private void showEditProject()
            {
                LayoutInflater layoutInflater = LayoutInflater.from(context);
                View view = layoutInflater.inflate(R.layout.pop_menu_edit_project, null);

                final AlertDialog alertDialog = framework.popup_custom(title, view);

                Button editButton = view.findViewById(R.id.button_edit);
                Button deleteButton = view.findViewById(R.id.button_delete);

                editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        /**
                         * EDIT function reuses the layout for creating a new project,
                         * */

                        GUI_MainActivity mainActivity = (GUI_MainActivity)context;
                        String cipherText = database.getCipherText(id, title);
                        mainActivity.createNewProject(id, title, cipherText);

                        alertDialog.cancel();
                        notifyDataSetChanged();
                    }
                });

                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        framework.system_message_confirmAction("Delete Project", "Delete '" + title + "'?", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                deleteProject();

                                alertDialog.cancel();
                                notifyDataSetChanged();
                            }
                        });
                    }
                });
                alertDialog.show();
            }

            private void deleteProject()
            {
                database.deleteEntry(id, title);
                projectTitle = database.getAllTitle();

                GUI_MainActivity mainActivity = (GUI_MainActivity)context;
                mainActivity.getListFromDB();

                notifyDataSetChanged(); //refreshes recycler contents;
            }

            protected boolean projectExist(String newProjectTitle)
            {
                for(int i = 0; i < projectTitle.size(); i++)
                    if(projectTitle.get(i).getTitle().equals(newProjectTitle))
                        return true;

                return false;
            }

            //TODO(renameProject()) DELETE ASSOCIATED TEXT FILES AS WELL
            /**A hash value is created upon creating new project, so then it will be id||title||hash
             *
             * When the associated project is renamed, THE VALUE OF HASH DOES NOT CHANGE
             * Since this will help to avoid avalanche effect in the database caused by changing the hash value
             * */
            private void renameProject()
            {
                framework.popup_show("Rename Project", title, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        String newProjectName = framework.popup_getInput();

                        if(projectExist(newProjectName))
                            framework.system_message_small("Project name already exist");

                        else if(newProjectName.isEmpty())
                             framework.system_message_small("The project name cannot be empty");

                        else
                        {
                            //find the old project name, replace with the new one
                            /*for(int x = 0; x < projectTitle.size(); x++)
                                if(projectTitle.get(x).getTitle().equals(title))
                                    {
                                        projectTitle.get(x).setTitle(newProjectName);
                                        itemContent.setText(projectTitle.get(x).getTitle());
                                        break;
                                    }*/

                            //TODO() RENAME RELATED TEXT FILES HERE
                            /*String oldName = id+title+"cipherTextOriginal.txt";
                            String newName = id+newProjectName+"cipherTextOriginal.txt";
                            framework.renameTextFile(oldName, newName);

                            oldName = id+title+"notes.txt";
                            newName = id+newProjectName+"notes.txt";
                            framework.renameTextFile(oldName, newName);

                            title = newProjectName;*/
                            database.updateData(id, title, "PROJECT_TITLE", newProjectName);
                            projectTitle = database.getAllTitle();
                            notifyDataSetChanged();
                        }
                    }
                });
            }

            /**This will store the data to a text file that contains information to display the list of existing projects*/
            /**!!THIS FUNCTION IS DIFFERENT FROM THE ONE IN MAIN ACTIVITY*/
            //FILE STORAGE WILL BE HANDLED INTERNALLY BY ANDROID
            private void writeToList()
            {
                //overwrite list.txt
                BufferedWriter outputFile;

                try
                {
                    FileOutputStream fos = context.openFileOutput(projectDirectoryFileName, context.MODE_PRIVATE);
                    outputFile = new BufferedWriter(new OutputStreamWriter(fos));

                    for(int i = 0; i < projectTitle.size(); i++)
                        outputFile.write(projectTitle.get(i).getID() + "||" + projectTitle.get(i).getTitle()+"\n");

                    outputFile.close();
                }catch(IOException e)
                {}
            }

            private final View.OnTouchListener adapterTouchListener = new View.OnTouchListener()
            {
                @Override
                public boolean onTouch(View view, MotionEvent event)
                {
                    /*if(event.getAction() == MotionEvent.ACTION_DOWN)//meaning area is pressed
                        {
                            // Do Stuff
                            //use componentID and component Title as composite later to identify relevant data

                            //view.setBackgroundColor(Color.GRAY);
                        }
                    else if(event.getAction() == MotionEvent.ACTION_UP)
                        {
                            //view.setBackgroundColor(Color.GRAY);

                            launchProjectView();
                        }
                    else
                        {
                            //view.setBackgroundColor(Color.GRAY);
                        }*/

                if(event.getAction() == MotionEvent.ACTION_UP)
                {
                    //view.setBackgroundColor(Color.GRAY);

                    launchProjectView();
                }

                return true;
                }
            };

            private void launchProjectView()//put the passed value as parameter
                {
                    //start a new activity, and passes some variables >>> H(project ID | project Title)
                    Intent intent = new Intent(context, GUI_project_view.class);
                    intent.putExtra("project_view_unique_ID", id);//this will pass on variables to the new activity, access it using the "name" (first param in this function)
                    intent.putExtra("project_view_title", title);

                    context.startActivity(intent);
                }
        }
}