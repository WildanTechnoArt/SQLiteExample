package android.cianjur.developer.net.sqliteexample;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

//Class Adapter ini Digunakan Untuk Mengatur Bagaimana Data akan Ditampilkan
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private ArrayList<DataFilter> dataList;
    private Context context;

    //Membuat Konstruktor pada Class RecyclerViewAdapter
    RecyclerViewAdapter(ArrayList<DataFilter> dataList){
        this.dataList = dataList;
    }

    //ViewHolder Digunakan Untuk Menyimpan Referensi Dari View-View
    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView Nama, Jurusan, Nim;
        private ImageButton Overflow;

        ViewHolder(View itemView) {
            super(itemView);

            //Mendapatkan Context dari itemView yang terhubung dengan Activity ViewData
            context = itemView.getContext();

            //Menginisialisasi View-View untuk kita gunakan pada RecyclerView
            Nama = itemView.findViewById(R.id.name);
            Jurusan = itemView.findViewById(R.id.jurusan);
            Overflow = itemView.findViewById(R.id.overflow);
            Nim = itemView.findViewById(R.id.NIM);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Membuat View untuk Menyiapkan dan Memasang Layout yang Akan digunakan pada RecyclerView
        View V = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_design, parent, false);
        return new ViewHolder(V);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        //Memanggil Nilai/Value Pada View-View Yang Telah Dibuat pada Posisi Tertentu
        final String Nama = dataList.get(position).getNama();//Mengambil data (Nama) sesuai dengan posisi yang telah ditentukan
        final String Jurusan = dataList.get(position).getJurusan();//Mengambil data (Jurusan) sesuai dengan posisi yang telah ditentukan
        final String NIM = dataList.get(position).getNIM();//Mengambil data (NIM) sesuai dengan posisi yang telah ditentukan
        holder.Nama.setText(Nama);
        holder.Jurusan.setText(Jurusan);
        holder.Nim.setText(NIM);

        //Mengimplementasikan Menu Popup pada Overflow (ImageButton)
        holder.Overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                //Membuat Instance/Objek dari PopupMenu
                PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
                popupMenu.inflate(R.menu.popup_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.delete:
                                //Menghapus Data Dari Database
                                DBMahasiswa getDatabase = new DBMahasiswa(view.getContext());
                                SQLiteDatabase DeleteData = getDatabase.getWritableDatabase();
                                //Menentukan di mana bagian kueri yang akan dipilih
                                String selection = DBMahasiswa.MyColumns.NIM + " LIKE ?";
                                //Menentukan Nama Dari Data Yang Ingin Dihapus
                                String[] selectionArgs = {NIM};
                                DeleteData.delete(DBMahasiswa.MyColumns.NamaTabel, selection, selectionArgs);

                                //Menghapus Data pada List dari Posisi Tertentu
                                String position2 = String.valueOf(NIM.indexOf(NIM));
                                dataList.remove(position);
                                notifyItemRemoved(position);
                                if (position2 == null) {
                                    notifyItemRangeChanged(Integer.parseInt(position2), dataList.size());
                                }
                                break;

                            case R.id.update:
                                Intent dataForm = new Intent(view.getContext(), UpdateActivity.class);
                                dataForm.putExtra("SendNIM", NIM);
                                context.startActivity(dataForm);
                                ((Activity)context).finish();
                                break;
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        //Menghitung Ukuran/Jumlah Data Yang Akan Ditampilkan Pada RecyclerView
        return dataList.size();
    }

    void setFilter(ArrayList<DataFilter> filterList){
        dataList = new ArrayList<>();
        dataList.addAll(filterList);
        notifyDataSetChanged();
    }

}