package com.example.ekanugrahapratama.aardvark_project;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.graphics.Color;

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

    class viewHolder extends RecyclerView.ViewHolder
        {
            TextView itemContent; //this displays the project title, TextView is an xml element

            private String id;
            private String title;
            private int idx; //entry index in the recycler component

            //@constructor
            public viewHolder(View itemView)
                {
                    super(itemView);
                    itemContent = (TextView)itemView.findViewById(R.id.listContent); //the ID of the element can be found in front_page_item_content

                    itemView.setOnTouchListener(adapterTouchListener);
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

                            //TODO(99) Delete these 2 lines later
                            //displays snackbar popup for confirmation, DELETE THESE 2 lines later!!
                            Snackbar.make(view, "you clicked "+title+"(ID: "+id+")", Snackbar.LENGTH_LONG).show();
                            System.out.println("THIS MEANS BUTTON CLICK IS WORKING >>>> " + title);
                        }
                    else if(event.getAction() == MotionEvent.ACTION_UP)
                        {
                            view.setBackgroundColor(Color.WHITE);
                        }

                return true;
                }
            };
        }
}
