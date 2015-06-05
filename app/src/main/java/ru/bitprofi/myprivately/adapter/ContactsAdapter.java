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

public class ContactsAdapter extends ArrayAdapter<User> {
    private Context _context;

    public ContactsAdapter(Context context, ArrayList<User> users) {
        super(context, 0, users);
        _context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        User user = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_contacts, parent, false);
        }

        TextView  name = (TextView) convertView.findViewById(R.id.contact_name);
        ImageView image = (ImageView) convertView.findViewById(R.id.contact_image);
        TextView  status = (TextView) convertView.findViewById(R.id.contact_status);

        name.setText(user.getName());
       // image.setImageResource(user.getImage());
        status.setText("Пользовательский статус");

        return convertView;
    }
}
