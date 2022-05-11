package Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.inotes.R;
import com.example.inotes.databinding.NoteslistviewBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import DatabaseFiles.NotesEntities;

public class iNotesAdapter extends RecyclerView.Adapter<iNotesAdapter.iNotesViewHolder> implements Filterable {

    private final List<NotesEntities> notesList;
    Context context;
    ClickListener clickListener;
    List<NotesEntities> allNotes;

    public iNotesAdapter(List<NotesEntities> notes, Context context, ClickListener clickListener) {
        this.notesList = notes;
        this.context = context;
        this.clickListener = clickListener;
        this.allNotes = new ArrayList<>(notesList);
    }

    @NonNull
    @Override
    public iNotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        NoteslistviewBinding noteslistDesignBinding = NoteslistviewBinding.inflate(inflater,parent,false);
        return new iNotesViewHolder(noteslistDesignBinding);
    }

    @SuppressLint({"ResourceAsColor", "NotifyDataSetChanged"})
    @Override
    public void onBindViewHolder(@NonNull iNotesViewHolder holder, int position) {
        NotesEntities notes = notesList.get(position);
        holder.binding.setNotesItem(notes);
        holder.binding.TextTitle.setMovementMethod(new ScrollingMovementMethod());
        holder.binding.TextTitle.setHorizontallyScrolling(true);
        int color = changeColor();

        holder.binding.cardView.setCardBackgroundColor(holder.binding.cardView.getResources().getColor(color));

        holder.binding.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.click(notes);
            }
        });

        holder.binding.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                clickListener.OnLongClick(notes);
                return false;
            }
        });

    }

    public NotesEntities getNoteAt(int position){
        return  notesList.get(position);
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    private int changeColor(){
        ArrayList<Integer> colorList = new ArrayList<>();
        int color;

        colorList.add(R.color.color1);
        colorList.add(R.color.color2);
        colorList.add(R.color.color3);
        colorList.add(R.color.color4);
        colorList.add(R.color.color5);

        Random random = new Random();
        color = random.nextInt(colorList.size());

        return colorList.get(color);

    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {

        //        run on a background thread
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<NotesEntities> filteredList = new ArrayList<>();

            if(charSequence.toString().isEmpty()){
                filteredList.addAll(allNotes);
            }else{
                String filteredPattern = charSequence.toString().toLowerCase().trim();
                for(NotesEntities eachNoteList: allNotes){
                    if(eachNoteList.getNotes_Title().toLowerCase().contains(filteredPattern)){
                        filteredList.add(eachNoteList);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        //        run on a UI thread
        @SuppressLint("NotifyDataSetChanged")
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            notesList.clear();
            notesList.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };


    public static class iNotesViewHolder extends RecyclerView.ViewHolder{

        NoteslistviewBinding binding;

        public iNotesViewHolder(@NonNull NoteslistviewBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }
    }
}
