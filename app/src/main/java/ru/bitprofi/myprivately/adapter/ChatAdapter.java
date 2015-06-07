package ru.bitprofi.myprivately.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import ru.bitprofi.myprivately.OneComment;
import ru.bitprofi.myprivately.R;

public class ChatAdapter extends ArrayAdapter<OneComment> {
	private TextView _bubbleItem;
	private LinearLayout _wrapper;
	private List<OneComment> _chatHistory;

	public ChatAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
		_chatHistory = new ArrayList<OneComment>();
	}

	@Override
	public void add(OneComment object) {
		_chatHistory.add(object);
		super.add(object);
	}

	public int getCount() {
		return _chatHistory.size();
	}

	/*
	 * Вернуть элемент по индексу
	 */
	public OneComment getItem(int index) {
		if (index >= 0 && index <= _chatHistory.size()) {
			return _chatHistory.get(index);
		}
		return null;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		if (row == null) {
			LayoutInflater inflater = (LayoutInflater) this.getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.item_chat, parent, false);
		}
		_wrapper = (LinearLayout) row.findViewById(R.id.wrapper);

		OneComment coment = getItem(position);
        if (coment == null) {
            return null;
        }

        if (!coment.getBackground()) {
            _bubbleItem = (TextView) row.findViewById(R.id.comment);
            _bubbleItem.setText(coment.getComment());
            _bubbleItem.setBackgroundResource(coment.getLeft() ? R.drawable.ch_bubble_blue_9 : R.drawable.ch_bubble_white_9);
            _wrapper.setGravity(coment.getLeft() ? Gravity.LEFT : Gravity.RIGHT);
        }

		return row;
	}
}