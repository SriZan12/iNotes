package com.example.inotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.inotes.databinding.ActivityMainBinding;

import Adapter.ClickListener;
import Adapter.iNotesAdapter;
import DatabaseFiles.NotesEntities;
import ViewModel.iNotes_ViewModel;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    iNotes_ViewModel viewModel;
    iNotesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupToolbar();

        binding.floatingbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddNotesActivity.class);
                startActivity(intent);
            }
        });

        viewModel = ViewModelProviders.of(this).get(iNotes_ViewModel.class);

        UpdateRecyclerView();

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(binding.recyclerView);

    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
    }

    private final ClickListener clickListener = new ClickListener() {
        @Override
        public void click(NotesEntities notesEntities) {
            Intent intent = new Intent(MainActivity.this, ShowNoteActivity.class);
            intent.putExtra("notesDesc", notesEntities.getDescription());
            intent.putExtra("notesTitle", notesEntities.getNotes_Title());
            startActivity(intent);

        }

        @Override
        public void OnLongClick(NotesEntities notes) {
            Intent intent = new Intent(MainActivity.this, AddNotesActivity.class);
            intent.putExtra("updateNotes", notes);
            startActivity(intent);
        }


    };

    @SuppressLint("NotifyDataSetChanged")
    public void UpdateRecyclerView() {
        viewModel.getAllNotes.observe(this, notesEntities -> {
            binding.recyclerView.setHasFixedSize(true);
            binding.recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
            adapter = new iNotesAdapter(notesEntities, MainActivity.this, clickListener);
            binding.recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        });
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            viewModel.DeleteNotes(adapter.getNoteAt(viewHolder.getAdapterPosition()));
            Toast.makeText(MainActivity.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search Notes....");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }
}
