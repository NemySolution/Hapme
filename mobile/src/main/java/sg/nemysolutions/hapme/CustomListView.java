package sg.nemysolutions.hapme;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by yeekeng on 9/8/2015.
 * the custom list view with a delete button
 *
 */
public class CustomListView extends BaseAdapter implements ListAdapter {
    private ArrayList<String> list = new ArrayList<>();
    private ArrayList<Command> list2 = new ArrayList<>();
    private Context context;



    public CustomListView(ArrayList<String> list,ArrayList<Command> list2, Context context) {
        this.list = list;
        this.list2 = list2;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
        //just return 0 if your list items do not have an Id variable.
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.cust_listview, null);
        }

        //Handle TextView and display string from your list
        TextView listItemText = (TextView)view.findViewById(R.id.list_item_string);
        listItemText.setText(list.get(position));

        //Handle buttons and add onClickListeners
        Button deleteBtn = (Button)view.findViewById(R.id.delete_btn);


        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do something

                AlertDialog.Builder adb = new AlertDialog.Builder(context);

                adb.setTitle("Delete?");
                adb.setMessage("Are you sure you want to delete " + list.get(position));

                adb.setNegativeButton("Cancel", null);
                adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        list.remove(position); //or some other task
                        list2.remove(position);
                        notifyDataSetChanged();
                    }
                });
                adb.show();


            }
        });


        return view;
    }


}
