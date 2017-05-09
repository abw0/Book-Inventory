package com.example.karas.katalogbuku_abw;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.example.karas.katalogbuku_abw.activity.Book;
import com.example.karas.katalogbuku_abw.activity.BookForm;
import com.example.karas.katalogbuku_abw.adapter.BooksAdapter;
import com.example.karas.katalogbuku_abw.helper.HelperFunction;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.karas.katalogbuku_abw.R.id.recyclerBook;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(recyclerBook) RecyclerView recyclerbook;
    @BindView(R.id.fab) FloatingActionButton btnAdd;
    private List<Book> bookList = new ArrayList<>();
    private BooksAdapter mAdapter;

    public int TO_FORM = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Books Catalog");

        mAdapter = new BooksAdapter(this, bookList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerbook.setLayoutManager(mLayoutManager);
        recyclerbook.setItemAnimator(new DefaultItemAnimator());
        recyclerbook.addItemDecoration(new DividerDecoration(this));

        recyclerbook.setAdapter(mAdapter);
        recyclerbook.addOnItemTouchListener(new HelperFunction.RecyclerTouchListener(this, recyclerbook, new HelperFunction.ClickListener(){
            @Override
            public void onClick(View view, int position) {
                Intent i = new Intent(MainActivity.this, BookForm.class);
                i.putExtra("bookEdit", bookList.get(position));
                startActivity(i);
            }

            @Override
            public void onLongClick(View view, final int position) {
                final Book book = bookList.get(position);
                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Delete")
                        .setMessage("Are you sure to delete " + book.getBook_title() + " ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //TODO Auto-generate method stub
                                bookList.remove(book);
                                mAdapter.notifyItemRemoved(position);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which){
                                dialog.cancel();
                            }
                        })
                        .create();
                dialog.show();
            }
        }));

        prepareBookData();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent app = new Intent(MainActivity.this, BookForm.class);
                startActivityForResult(app, TO_FORM);


            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == TO_FORM){
            Book bookForm = (Book) data.getExtras().getSerializable("book");
            bookList.add(bookForm);
            Toast.makeText(this, "Book " + bookForm.getBook_title() + " successfully added", Toast.LENGTH_SHORT).show();
            mAdapter.notifyDataSetChanged();
        }
    }

    private void prepareBookData() {
            Book book = new Book("9780439064873", "Harry Potter And The Chamber Of Secrets", "J.K. Rowling", 2000, "Fantasy, Thriller, Mystery", "This is some synopsis", R.drawable.hp_chamber);
            bookList.add(book);

            book = new Book("9780316015844", "Twilight (The Twilight Saga, Book 1)", "Stephanie Meyer", 2006, "Fantasy, Drama, Romance", "This is some synopsis", R.drawable.twilight1);
            bookList.add(book);

            book = new Book("9781484724989", "Journey to Star Wars: The Force Awakens Lost Stars", "Claudia Gray", 2015, "Action, Thriller, Science Fiction", "This is some synopsis",R.drawable.star_wars);
            bookList.add(book);

            book = new Book("9780439136365", "Harry Potter and the Prisoner of Azkaban", "J.K. Rowling", 2001, "Fantasy, Thriller, Mystery", "This is some synopsis", R.drawable.hp_azkaban);
            bookList.add(book);

            mAdapter.notifyDataSetChanged();
        }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mAdapter.getFilter().filter(newText);
        return true;
    }
}
