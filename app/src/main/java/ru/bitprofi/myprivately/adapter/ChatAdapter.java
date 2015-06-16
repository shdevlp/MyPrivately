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
	private TextView m_bubble_item;
	private LinearLayout m_wrapper;
	private List<OneComment> m_chat_history;

	public ChatAdapter(Context context) {
		super(context, 0);
		m_chat_history = new ArrayList<OneComment>();
	}

	@Override
	public void add(OneComment object) {
		m_chat_history.add(object);
		super.add(object);
	}

	public int getCount() {
		return m_chat_history.size();
	}

	/*
	 * Вернуть элемент по индексу
	 */
	public OneComment getItem(int index) {
		return m_chat_history.get(index);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		if (row == null) {
			LayoutInflater inflater = (LayoutInflater) this.getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.item_chat, parent, false);
		}
		m_wrapper = (LinearLayout) row.findViewById(R.id.wrapper);

		OneComment coment = getItem(position);
        if (coment == null) {
            return null;
        }

		m_bubble_item = (TextView) row.findViewById(R.id.comment);
        m_bubble_item.setText(coment.getComment());
		m_bubble_item.setBackgroundResource(coment.getLeft() ? R.drawable.bubble_green : R.drawable.bubble_yellow);
        m_wrapper.setGravity(coment.getLeft() ? Gravity.LEFT : Gravity.RIGHT);

		return row;
	}
}