package com.yanir.ex121;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class GradeListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> gradeNames;
    private final ArrayList<String> gradeInfos;
    private final ArrayList<String> grade;

    public GradeListAdapter(Activity context, ArrayList<String> gradeNames, ArrayList<String> gradeInfos, ArrayList<String> grade) {
        super(context, R.layout.grades_list_item, gradeNames);

        this.context=context;
        this.gradeNames=gradeNames;
        this.gradeInfos=gradeInfos;
        this.grade=grade;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.grades_list_item, null,true);

        TextView gradeName = (TextView) rowView.findViewById(R.id.gradeName);
        TextView gradeInfo = (TextView) rowView.findViewById(R.id.gradeInfo);
        TextView grade = (TextView) rowView.findViewById(R.id.grade);

        gradeName.setText(gradeNames.get(position));
        gradeInfo.setText(gradeInfos.get(position));
        grade.setText(""+this.grade.get(position));

        return rowView;

    };
}
