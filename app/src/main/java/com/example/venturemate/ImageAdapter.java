package com.example.venturemate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class ImageAdapter extends BaseAdapter {
	private Context context;
	private final String[] categoryValues;

	public ImageAdapter(Context context, String[] categoryValues) {
		this.context = context;
		this.categoryValues = categoryValues;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View gridView;

		if (convertView == null) {

			gridView = new View(context);

			gridView = inflater.inflate(R.layout.category, null);

			// set value into textview
			TextView textView = (TextView) gridView
					.findViewById(R.id.grid_item_label);
			textView.setText(categoryValues[position]);

			// set image based on selected text
			ImageView imageView = (ImageView) gridView
					.findViewById(R.id.grid_item_image);

			String category = categoryValues[position];

			if (category.equals("Hiking")) {
				imageView.setImageResource(R.drawable.hiking);
			} else if (category.equals("Cycling")) {
				imageView.setImageResource(R.drawable.cycling);
			} else if (category.equals("Camping")) {
				imageView.setImageResource(R.drawable.camping);
			} else if (category.equals("Rafting")) {
				imageView.setImageResource(R.drawable.rafting);
			} else if (category.equals("Rock Climbing")) {
				imageView.setImageResource(R.drawable.rock_climbing);
			} else if (category.equals("Safari")) {
				imageView.setImageResource(R.drawable.safari);
			} else if (category.equals("Other")){
				imageView.setImageResource(R.drawable.other);
			}

		} else {
			gridView = (View) convertView;
		}

		return gridView;
	}

	@Override
	public int getCount() {
		return categoryValues.length;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

}