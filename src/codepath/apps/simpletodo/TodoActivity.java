package codepath.apps.simpletodo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class TodoActivity extends Activity {
	private final int REQUEST_EDIT_CODE = 1;
	
	ArrayList<String> items;
	ArrayAdapter<String> itemsAdapter;
	ListView lvItems;
	EditText etNewItem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo);
		lvItems = (ListView) findViewById(R.id.lvItems);
		readItems();
		itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
		lvItems.setAdapter(itemsAdapter);
		etNewItem = (EditText) findViewById(R.id.etNewItem);
		setUpListViewListeners();
	}

	public void onAddedItem(View v) {
    	String itemText = etNewItem.getText().toString();
    	itemsAdapter.add(itemText);
    	etNewItem.setText("");
    	writeItems();
    }
	
	private void readItems() {
    	File todoFile = new File(getFilesDir(), "todo.txt");
    	try {
			items = new ArrayList<String>(FileUtils.readLines(todoFile));
		} catch (IOException e) {
			items = new ArrayList<String>();
			e.printStackTrace();
		}
    }
    
    private void writeItems() {
    	File todoFile = new File(getFilesDir(), "todo.txt");
    	try {
			FileUtils.writeLines(todoFile, items);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	
	private void setUpListViewListeners() {
		lvItems.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				items.remove(position);
				itemsAdapter.notifyDataSetChanged();
				writeItems();
				return true;
			}
		});
		lvItems.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent editIntent = new Intent(TodoActivity.this, EditItemActivity.class);
				editIntent.putExtra("pos", position);
				editIntent.putExtra("text", items.get(position));
				startActivityForResult(editIntent, REQUEST_EDIT_CODE);
			}
    	});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (requestCode == REQUEST_EDIT_CODE && resultCode == RESULT_OK) {
    		int pos = data.getIntExtra("pos", -1);
    		items.set(pos, data.getStringExtra("text"));
    		itemsAdapter.notifyDataSetChanged();
    		writeItems();
    	}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.todo, menu);
		return true;
	}

}
