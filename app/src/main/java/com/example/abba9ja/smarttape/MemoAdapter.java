package com.example.abba9ja.smarttape;

/**
 * Created by Abba9ja on 9/21/2017.
 */
import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.abba9ja.smarttape.Data.MemoContract;

public class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.MemoViewHolder> {

    private Cursor mCursor;
    private Context mContext;

    private final MemoAdapterOnClickHandler mClickHandler;

    /**
     * The interface that receives onClick messages.
     */
    public interface MemoAdapterOnClickHandler {
        void onClick(String[] eachData);
    }


    public MemoAdapter(Context mContext, MemoAdapterOnClickHandler clickHandler) {
        this.mContext = mContext;
        mClickHandler = clickHandler;
    }


    @Override
    public MemoAdapter.MemoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.memolist_layout, parent, false);

        return new MemoViewHolder(view);

    }

    @Override
    public void onBindViewHolder(MemoAdapter.MemoViewHolder holder, int position) {
        // Indices for the _id, description, and priority columns
        int idIndex = mCursor.getColumnIndex(MemoContract.MemoEntry._ID);
        int descriptionIndex = mCursor.getColumnIndex(MemoContract.MemoEntry.COLUMN_DESCRIPTION);
        int nameIndex = mCursor.getColumnIndex(MemoContract.MemoEntry.COLUMN_NAME);

        mCursor.moveToPosition(position); // get to the right location in the cursor

        // Determine the values of the wanted data
        final int id = mCursor.getInt(idIndex);
        String description = mCursor.getString(descriptionIndex);
        String name = mCursor.getString(nameIndex);

        //Set values
        holder.itemView.setTag(id);
        //String desc = description.substring(0, 50);
        //desc += " ...";
        holder.taskDescriptionView.setText(description);

        // Programmatically set the text and color for the priority TextView
        //String nameString = name.substring(0, 20); // converts int to String
        //nameString += " ...";
        holder.nameView.setText(name);

    }

    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }

    public Cursor swapCursor(Cursor c) {
        // check if this cursor is the same as the previous cursor (mCursor)
        if (mCursor == c) {
            return null; // bc nothing has changed
        }
        Cursor temp = mCursor;
        this.mCursor = c; // new cursor value assigned

        //check if this is a valid cursor, then update the cursor
        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }

    public class MemoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // Class variables for the task description and priority TextViews
        TextView taskDescriptionView;
        TextView nameView;

        /**
         * Constructor for the TaskViewHolders.
         *
         * @param itemView The view inflated in onCreateViewHolder
         */
        public MemoViewHolder(View itemView) {
            super(itemView);

            taskDescriptionView = (TextView) itemView.findViewById(R.id.tvdocdetails);
            nameView = (TextView) itemView.findViewById(R.id.tvname);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            int idIndex = mCursor.getColumnIndex(MemoContract.MemoEntry._ID);
            int descriptionIndex = mCursor.getColumnIndex(MemoContract.MemoEntry.COLUMN_DESCRIPTION);
            int nameIndex = mCursor.getColumnIndex(MemoContract.MemoEntry.COLUMN_NAME);
            int id = mCursor.getInt(idIndex);
            String description = mCursor.getString(descriptionIndex);
            String name = mCursor.getString(nameIndex);
            String sId = ""+id;
            String eachMemoData[] = {sId, description, name};
            mClickHandler.onClick(eachMemoData);
        }
    }
}
