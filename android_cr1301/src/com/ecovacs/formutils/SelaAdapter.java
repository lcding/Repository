package com.ecovacs.formutils;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

/**
 * @version 2012-8-27 ����10:36:36
 **/
public class SelaAdapter extends BaseExpandableListAdapter {

    List<String> GroupList = new ArrayList<String>();
    List<List<String>> ChildrenList = new ArrayList<List<String>>();
    Context context = null;

    public SelaAdapter(Context c, List<String> gl, List<List<String>> cl) {
        context = c;
        GroupList.addAll(gl);
        ChildrenList.addAll(cl);
    }

    // �����������
    @Override
    public int getGroupCount() {
        return GroupList.size();
    }

    // ������������
    @Override
    public int getChildrenCount(int groupPosition) {
        return ChildrenList.get(groupPosition).size();
    }

    // ���������
    @Override
    public Object getGroup(int groupPosition) {
        return GroupList.get(groupPosition);
    }

    // �����������
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return ChildrenList.get(groupPosition).get(childPosition);
    }

    // ������ID
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    // ��������ID
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    // �Ƿ���й̶�ID
    @Override
    public boolean hasStableIds() {
        return false;
    }

    // ������VIEW
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
            View convertView, ViewGroup parent) {
        TextView tv = new TextView(context);
        tv.setText(GroupList.get(groupPosition));
        tv.setHeight(40);
        return tv;
    }

    // ��������VIEW
    @Override
    public View getChildView(int groupPosition, int childPosition,
            boolean isLastChild, View convertView, ViewGroup parent) {
        TextView tv = new TextView(context);
        tv.setText(ChildrenList.get(groupPosition).get(groupPosition));
        tv.setHeight(40);

        return tv;
    }

    // �����Ƿ���Ա�ѡ��
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}