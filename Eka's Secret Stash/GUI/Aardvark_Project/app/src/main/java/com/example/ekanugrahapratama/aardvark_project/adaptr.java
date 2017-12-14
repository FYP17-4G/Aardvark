package com.example.ekanugrahapratama.aardvark_project;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.graphics.Color;
import android.content.Intent;
import android.widget.ImageButton;

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

public class adaptr extends RecyclerView.Adapter<adaptr.viewHolder>
{
    ArrayList<frontPageIdentifier> projectTitle;

    public adaptr(ArrayList<frontPageIdentifier> n)
        {
            this.projectTitle = n;
        }

     @Override
     public viewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
        {
            Context context = viewGroup.getContext();
            int layoutIdForListItem = R.layout.front_page_item_content; //This will return the 'ID' of xml file layout, R.layout.<xml name> are built in function designed for this
            LayoutInflater inflater = LayoutInflater.from(context);
            boolean shouldAttachToParentImmediately = false;

            //TODO(97) Figure out how the hell inflater works
            View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
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

    //this is the item inside the recycler view
    class viewHolder extends RecyclerView.ViewHolder
        {
            TextView itemContent; //this displays the project title, TextView is an xml element
            FrameLayout itemContentFrame;
            ImageButton deleteButton;

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
                    itemContent = (TextView)itemView.findViewById(R.id.listContent); //the ID of the element can be found in front_page_item_content
                    itemContentFrame = (FrameLayout)itemView.findViewById(R.id.TitleFrame);
                    deleteButton = (ImageButton)itemView.findViewById(R.id.itemDelete);

                    context = itemView.getContext();

                    itemContentFrame.setOnTouchListener(adapterTouchListener);
                    deleteButton.setOnClickListener(deleteButtonListener);
                }

            //this function will be called by member of interface onBindViewHolder from recycler extension
            void bind(int idx)
                {
                    itemContent.setText(projectTitle.get(idx).getTitle());

                    //these 2 lines will assign unique ID and title taken from list.txt to individual view holder
                    this.id = projectTitle.get(idx).getID();
                    this.title = projectTitle.get(idx).getTitle();
                    this.idx = idx;
                }

            private final View.OnClickListener deleteButtonListener = new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                    {
                    //delete the associated item from list.txt
                        String projectDirectoryFileName = "projectDirectory.txt";

                        try
                            {
                                FileOutputStream fos = context.openFileOutput(projectDirectoryFileName, Context.MODE_PRIVATE);
                                BufferedWriter output = new BufferedWriter(new OutputStreamWriter(fos));

                                for(int i = 0; i < projectTitle.size(); i++)
                                    {
                                        String tempID = projectTitle.get(i).getID();
                                        String tempTitle = projectTitle.get(i).getTitle();

                                        String tmp = tempID + "||" + tempTitle;
                                        if(tmp.equals(id + "||" + title))
                                            {
                                            projectTitle.remove(i);

                                            //TODO(101) ONCE THE DATABASE IS UP, DELETE THE ASSOCIATED DATA OF THIS ITEM
                                            //<...>
                                            }
                                        else
                                            {
                                                output.write(tempID + "||" + tempTitle + "\n");
                                            }
                                    }
                                notifyDataSetChanged(); //refreshes recycler contents;

                                output.close();
                            }catch(IOException e)
                                {}

                    }
            };

            private final View.OnTouchListener adapterTouchListener = new View.OnTouchListener()
            {
                @Override
                public boolean onTouch(View view, MotionEvent event)
                {
                    if(event.getAction() == MotionEvent.ACTION_DOWN)//meaning area is pressed
                        {
                            // Do Stuff
                            //use componentID and component Title as composite later to identify relevant data

                            //TODO(98) Change background color on click, revert color back on release (see the conditional statement after this one)
                            view.setBackgroundColor(Color.GRAY);
                        }
                    else if(event.getAction() == MotionEvent.ACTION_UP)
                        {
                            view.setBackgroundColor(Color.WHITE);

                            //TODO(100) Start new activity on button click, pass on id and title to the new activity (or maybe the value of H(id||title))
                            launchProjectView(id+title);

                        }
                    else
                        {
                            view.setBackgroundColor(Color.WHITE);
                        }

                return true;
                }
            };

            private void launchProjectView(String newActivityParams)//put the passed value as parameter
                {
                    //start a new activity, and passes some variables >>> H(project ID | project Title)
                    Intent intent = new Intent(context, projectView.class);
                    intent.putExtra("project_view_params", newActivityParams);//this will pass on variables to the new activity, access it using the "name" (first param in this function)
                    context.startActivity(intent);
                }
        }
}
