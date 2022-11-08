package com.dmt.dangtus.firebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.dmt.dangtus.firebase.adapter.SinhVienAdapter;
import com.dmt.dangtus.firebase.model.SinhVien;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<SinhVien> sinhVienList;
    private SinhVienAdapter adapter;
    private ListView lvSinhVien;
    private Button btnThem;
    private EditText edtTen, edtLop;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        anhXa();

        database = FirebaseDatabase.getInstance();

        sinhVienList = new ArrayList<>();
        adapter = new SinhVienAdapter(this, R.layout.item_sinh_vien, sinhVienList);
        lvSinhVien.setAdapter(adapter);
        getData();

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = sinhVienList.size();
                String ten = edtTen.getText().toString().trim();
                String lop = edtLop.getText().toString().trim();

                editData(id, ten, lop);

                edtTen.setText("");
                edtLop.setText("");
            }
        });

        lvSinhVien.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showThongTinDialog(i);
            }
        });
    }

    private void getData() {
        DatabaseReference mRef = database.getReference("data");

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    SinhVien sinhVien = data.getValue(SinhVien.class);
                    sinhVienList.add(sinhVien);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Lỗi kết nối intenet", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void editData(int id, String ten, String lop) {
        SinhVien sinhVien = new SinhVien(id, ten, lop);

        DatabaseReference mRef = database.getReference("data/" + id);

        mRef.setValue(sinhVien, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(MainActivity.this, "Thành công", Toast.LENGTH_SHORT).show();

                sinhVienList.removeAll(sinhVienList);
                getData();
            }
        });
    }

    private void deleteData(int id) {
        DatabaseReference mRef = database.getReference("data/" + id);
        mRef.removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(MainActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();

                sinhVienList.removeAll(sinhVienList);
                getData();
            }
        });
    }

    private void showThongTinDialog(int stt) {
        SinhVien sinhVien = sinhVienList.get(stt);

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_detail);
        dialog.setCanceledOnTouchOutside(false);

        //anh xa
        final EditText edtTen = dialog.findViewById(R.id.nameInfoTextView);
        final EditText edtLop = dialog.findViewById(R.id.classInfoTextView);
        final Button btnxoa = dialog.findViewById(R.id.xoaButton);
        final Button btnHuy = dialog.findViewById(R.id.huyButton);
        final Button btnSua = dialog.findViewById(R.id.suaButton);

        //set du lieu
        edtTen.setText(sinhVien.getTen());
        edtLop.setText(sinhVien.getLop());

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = sinhVien.getId();
                String ten = edtTen.getText().toString().trim();
                String lop = edtLop.getText().toString().trim();

                editData(id, ten, lop);

                dialog.dismiss();
            }
        });

        btnxoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteData(sinhVien.getId());
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void anhXa() {
        lvSinhVien = findViewById(R.id.sinhVienListView);
        btnThem = findViewById(R.id.themButton);
        edtTen = findViewById(R.id.tenEditText);
        edtLop = findViewById(R.id.lopEditText);
    }
}