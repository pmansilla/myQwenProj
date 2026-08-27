package com.czw.smartkit.user;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.czw.smartkit.R;
import com.czw.smartkit.base.TitleActivity;
import com.czw.smartkit.entity.CountryEntity;
import java.io.Serializable;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class CountryCodeactivity extends TitleActivity {
    private CodeAdapter codeAdapter = null;
    private ArrayList<CountryEntity> codes = CountryEntity.getCountry();
    private ListView list;

    /* loaded from: classes.dex */
    static class CodeAdapter extends BaseAdapter {
        private ArrayList<CountryEntity> codes;
        private Context context;

        public CodeAdapter(ArrayList<CountryEntity> arrayList, Context context) {
            this.codes = null;
            this.codes = arrayList;
            this.context = context;
        }

        @Override // android.widget.Adapter
        public int getCount() {
            return this.codes.size();
        }

        @Override // android.widget.Adapter
        public Object getItem(int i) {
            return this.codes.get(i);
        }

        @Override // android.widget.Adapter
        public long getItemId(int i) {
            return i;
        }

        @Override // android.widget.Adapter
        public View getView(int i, View view, ViewGroup viewGroup) {
            Tag tag;
            CountryEntity countryEntity = this.codes.get(i);
            if (view == null) {
                view = LayoutInflater.from(this.context).inflate(R.layout.code_item, (ViewGroup) null);
                tag = new Tag(view);
            } else {
                tag = (Tag) view.getTag();
            }
            tag.name.setText(countryEntity.name);
            tag.code.setText("+" + countryEntity.code);
            return view;
        }
    }

    /* loaded from: classes.dex */
    static class Tag {
        private TextView code;
        private TextView name;

        public Tag(View view) {
            this.name = (TextView) view.findViewById(R.id.name);
            this.code = (TextView) view.findViewById(R.id.code);
            view.setTag(this);
        }
    }

    public void back(View view) {
        finish();
    }

    @Override // com.czw.smartkit.base.BaseActivity
    public void initView() {
        this.titleBar.setTitle(R.string.choice_area);
        this.list = (ListView) findViewById(R.id.list);
        this.codeAdapter = new CodeAdapter(this.codes, this);
        this.list.setAdapter((ListAdapter) this.codeAdapter);
        this.list.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.czw.smartkit.user.CountryCodeactivity.1
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                CountryCodeactivity.this.setResult(-1, new Intent().putExtra("code", (Serializable) CountryCodeactivity.this.codes.get(i)));
                CountryCodeactivity.this.finish();
            }
        });
    }

    @Override // com.czw.smartkit.base.BaseActivity
    public int loadLayout() {
        return R.layout.activity_code_list;
    }
}
