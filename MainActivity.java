package com.example.testapp;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class MainActivity extends ListActivity {

	String root;
	List<String> path= null;	//Current path
	List<String> dispfiles= null;	//Names of contents of present directory
	TextView disppath;		//Displays the current path here 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		root=Environment.getExternalStorageDirectory().getPath();	//Gets the path of the storage device
		String extst=Environment.getExternalStorageState();		//Gets present state of storage device
		disppath= (TextView)findViewById(R.id.textView1);
		if(!extst.equals(Environment.MEDIA_MOUNTED))
		{
			
		}
		else
		{
			getDirectory(root);	//Calls function getDirectory() when storage device is properly mounted
		}
		
	
	}
	
	private void getDirectory(String dirstr)	//Function to display contents of current directory 
	{
		disppath.setText(dirstr);
		path=new ArrayList<String>();
		dispfiles= new ArrayList<String>();
		File fstore= new File(dirstr);		//Creates new file using dirstr path
		File[] farray= fstore.listFiles();		
		if(!dirstr.equals(root))		//If selected directory is not the root directory
		{
			path.add(root);
			dispfiles.add(root);
			dispfiles.add("../");
			path.add(fstore.getParent());
		}
		
		Arrays.sort(farray, filecomparator);	//Sorts farray alphabetically
		
		for(int i=0;i<farray.length;i++)
		{
			File temp= farray[i];
			path.add(temp.getPath());
			if(temp.isDirectory())
			{
				dispfiles.add(temp.getName()+"/");	//Adds a '/' if directory
			}
			else
			{
				dispfiles.add(temp.getName());
			}
		}
		
		ArrayAdapter<String> fileList= new ArrayAdapter<String>(this,R.layout.rowlayout,dispfiles);	//Creates an ArrayAdapter to display the contents of directories
		setListAdapter(fileList);
	}
	
	Comparator<? super File> filecomparator= new Comparator<File>(){	//Comparator to sort the contents of directory alphabetically
		public int compare(File f1, File f2)
		{
			return String.valueOf(f1.getName()).compareTo(f2.getName());		
		}
	};
	
	@Override
	protected void onListItemClick(ListView l, View v, int position,long id)	//Function called when an item is clicked
	{
		File clickfile= new File(path.get(position));	//Checks if clicked item is a directory and can be read
		if(clickfile.isDirectory())
		{
			if(clickfile.canRead())
			{
				getDirectory(path.get(position));
			}
			else						//If clicked item cannot be read
			{
				new AlertDialog.Builder(this)			
				.setIcon(R.drawable.ic_launcher)
				.setTitle(clickfile.getName()+" Cannot Be Read!")	
				.setPositiveButton("OK", null).show();
			}
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is dispfiles.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
