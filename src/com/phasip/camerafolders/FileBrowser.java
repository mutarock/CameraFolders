package com.phasip.camerafolders;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import android.content.Context;
import android.util.AttributeSet;
//import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * A small file browser view
 * @author Pasi Saarinen
 *
 */
public class FileBrowser extends ListView implements ViewHandler<File>,
		OnItemClickListener {

	private ArrayList<File> files = new ArrayList<File>();
	private File currFile = new File("/");
	private SpecArrayAdapter<File> arrayAdapter;

	/*
	 * TODO private boolean showHiddenFiles = false; public boolean showHidden()
	 * { return showHiddenFiles; } public boolean showHidden(boolean sw) {
	 * showHiddenFiles = sw; return sw; }
	 */
	public void updateFileList() {
		updateFileList(currFile);
	}

	private void shortToast(String msg) {
		Context context = this.getContext();
		Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
		toast.show();
	}

	public File getFile() {
		return currFile;
	}

	public void updateFileList(File f) {
		if (f == null)
			f = new File("/");
		if (!f.isDirectory()) {
			shortToast("Trying to enter non-directory");
			return;
		}
	//	Log.i("qweqwe", "File list updated!");
		currFile = f;
		files.clear();
		File arr[] = f.listFiles();
		if (arr != null)
			files.addAll(Arrays.asList(arr));
		arrayAdapter.notifyDataSetChanged();
	}

	private void initMe(Context c) {
		arrayAdapter = new SpecArrayAdapter<File>(c, R.layout.listitem, files,
				this);
		this.setAdapter(arrayAdapter);
		this.setTextFilterEnabled(true);
		this.setOnItemClickListener(this);
		// updateFileList();

	}
	public File[] getFiles()
	{
		return getFile().listFiles();
	}
	public boolean hasFile(String name) {
		File arr[] = getFile().listFiles();
		for (int a = 0; a < arr.length; a++) {
			File f = arr[a];
			//Log.i("dfweqwe","Checking f: " + arr[a] + " - name: " + name);
			//Yes case sensitive filesystem, but windows users use ntfs...
			if (f.getName().equalsIgnoreCase(name)) 
				return true;
		}
		return false;
	}

	public FileBrowser(Context context) {
		super(context);
		initMe(context);
	}

	public FileBrowser(Context context, AttributeSet attrs) {
		super(context, attrs);
		initMe(context);
			}

	public FileBrowser(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initMe(context);
			}

	@Override
	public File handle(View v, File object) {
		TextView main = (TextView) v.findViewById(R.id.maintext);
		TextView desc = (TextView) v.findViewById(R.id.desctext);
		ImageView img = (ImageView) v.findViewById(R.id.icon);

		main.setText(object.getName());
		if (object.isFile()) {
			img.setImageResource(android.R.drawable.ic_menu_save);
			desc.setText("File");
		}
		if (object.isDirectory()) {
			img.setImageResource(android.R.drawable.ic_menu_slideshow);
			desc.setText("Directory");
		}
		return null;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		final File f = (File) parent.getItemAtPosition(position);
		updateFileList(f);

	}

}