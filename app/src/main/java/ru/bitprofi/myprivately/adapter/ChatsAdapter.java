package ru.bitprofi.myprivately.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import ru.bitprofi.myprivately.R;
import ru.bitprofi.myprivately.User;

/**
 * Created by Дмитрий on 15.05.2015.
 */
public class ChatsAdapter extends ArrayAdapter<User> {
    private Context _context;

    public ChatsAdapter(Context context, ArrayList<User> users) {
        super(context, 0, users);
        _context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        User user = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_chats, parent, false);
        }

        TextView  name = (TextView) convertView.findViewById(R.id.user_name);
        ImageView image = (ImageView) convertView.findViewById(R.id.user_image);
        TextView  status = (TextView) convertView.findViewById(R.id.user_status);
        TextView  lastTime = (TextView) convertView.findViewById(R.id.user_last_time);
        TextView  lastMessage = (TextView) convertView.findViewById(R.id.user_last_message);
        TextView  unreadMessageCount = (TextView)convertView.findViewById(R.id.user_unread_message_count);

        name.setText(user.getName());
        image.setImageResource(user.getImage());
        status.setText(user.getStatusString());
        if (user.getLastMessage() == null) {
            lastMessage.setText("Последнее сообщение");
        }
        lastTime.setText(user.getLastTime());
/*
        if (user.getLock()){
            lock.setImageResource(R.drawable.lock);
        } else {
            lock.setImageResource(R.drawable.unlock);
        }
*/
        final int count = user.getUnreadMessageCount();
        String str = null;
        if (count < 10) {
            str = "  " + String.valueOf(count);
        } else {
            str = String.valueOf(count);
        }
        unreadMessageCount.setText(str);

        return convertView;
    }
}
