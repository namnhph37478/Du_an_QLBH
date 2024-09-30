package dp.qlnh;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

public class MenuActivity extends Activity {
	// Khai báo biến lưu trữ
	int so_mon = 0;
	Boolean isOK = false;
	String so_ban; // Biến chứa số món được chọn, số bàn
	List<String> mon_duoc_chon = new ArrayList<String>(); // Mảng chứa id các
															// checkbox được
															// chọn
	List<Integer> gia_duoc_chon = new ArrayList<Integer>();
	List<Integer> so_phan_chon = new ArrayList<Integer>();

	public void kich_hoat_edt(CheckBox chk , final EditText edt){
		chk.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked){
					edt.setEnabled(true);
				}
				else {
					edt.setEnabled(false);
				}
			}
		});
	}
	
	// Hàm thêm số phần ăn
	public void them_so_phan(EditText edt) {
		String tam = edt.getText().toString();
		if (tam.equals("")) {
			isOK = false;
			edt.setError("Vui lòng nhập số phần ăn");
		} else {
			int tam2 = Integer.parseInt(tam);
			so_phan_chon.add(tam2);
			isOK = true;
		}
	}

	/* Hàm xác nhận khi muốn thoát */
	protected void doExit() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(
				MenuActivity.this);
		alertDialog.setPositiveButton("Có", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				finish();
				System.exit(0);
			}
		});

		alertDialog.setNegativeButton("Không", null);
		alertDialog.setMessage("Bạn có muốn thoát?");
		alertDialog.setTitle("Quản lý nhà hàng");
		alertDialog.show();
	}

	/* Hàm tạo Dialog hiện lên khi nhấn menu Thông tin */
	protected void doDialog() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(
				MenuActivity.this);
		alertDialog.setNegativeButton("OK", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		alertDialog.setMessage("Đây là ứng dụng mã nguồn mở."
				+ "\r\nVersion: v1.0");
		alertDialog.setTitle("Quản lý nhà hàng");
		alertDialog.show();
	}

	// Hàm kiểm tra checkbox
	protected void kiem_tra_checkbox() {
		// Khởi tạo checkbox
		CheckBox chkMon1 = (CheckBox) findViewById(R.id.mon1);
		CheckBox chkMon2 = (CheckBox) findViewById(R.id.mon2);
		CheckBox chkMon3 = (CheckBox) findViewById(R.id.mon3);
		CheckBox chkMon4 = (CheckBox) findViewById(R.id.mon4);
		CheckBox chkMon5 = (CheckBox) findViewById(R.id.mon5);
		CheckBox chkMon6 = (CheckBox) findViewById(R.id.mon6);
		CheckBox chkMon7 = (CheckBox) findViewById(R.id.mon7);
		CheckBox chkMon8 = (CheckBox) findViewById(R.id.mon8);

		EditText edt1 = (EditText) findViewById(R.id.mon1_so_phan);
		EditText edt2 = (EditText) findViewById(R.id.mon2_so_phan);
		EditText edt3 = (EditText) findViewById(R.id.mon3_so_phan);
		EditText edt4 = (EditText) findViewById(R.id.mon4_so_phan);
		EditText edt5 = (EditText) findViewById(R.id.mon5_so_phan);
		EditText edt6 = (EditText) findViewById(R.id.mon6_so_phan);
		EditText edt7 = (EditText) findViewById(R.id.mon7_so_phan);
		EditText edt8 = (EditText) findViewById(R.id.mon8_so_phan);
		
		
		// Kiểm tra checkbox có được chọn hay không
		if (chkMon1.isChecked() && (!so_ban.equals(""))) {
			so_mon += 1;
			mon_duoc_chon.add("Thịt kho hột vịt");
			gia_duoc_chon.add(20);
			them_so_phan(edt1);

		}
		if (chkMon2.isChecked()&& (!so_ban.equals(""))) {
			so_mon += 1;
			mon_duoc_chon.add("Xương heo hầm măng");
			gia_duoc_chon.add(55);
			them_so_phan(edt2);
		}
		if (chkMon3.isChecked() && (!so_ban.equals(""))) {
			so_mon += 1;
			mon_duoc_chon.add("Cá chép kho tương");
			gia_duoc_chon.add(60);
			them_so_phan(edt3);
		}
		if (chkMon4.isChecked() && (!so_ban.equals(""))) {
			so_mon += 1;
			mon_duoc_chon.add("Trứng ốp la");
			gia_duoc_chon.add(15);
			them_so_phan(edt4);
		}
		if (chkMon5.isChecked() && (!so_ban.equals(""))) {
			so_mon += 1;
			mon_duoc_chon.add("Đậu hủ chiên");
			gia_duoc_chon.add(8);
			them_so_phan(edt5);
		}
		if (chkMon6.isChecked() && (!so_ban.equals(""))) {
			so_mon += 1;
			mon_duoc_chon.add("Canh hủ qua nhồi thịt");
			gia_duoc_chon.add(30);
			them_so_phan(edt6);
		}
		if (chkMon7.isChecked() && (!so_ban.equals(""))) {
			so_mon += 1;
			mon_duoc_chon.add("Canh chua cá chuối");
			gia_duoc_chon.add(35);
			them_so_phan(edt7);
		}
		if (chkMon8.isChecked() && (!so_ban.equals(""))) {
			so_mon += 1;
			mon_duoc_chon.add("Xào thập cẩm");
			gia_duoc_chon.add(23);
			them_so_phan(edt8);
		}

	};

	// Tạo menu
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		getMenuInflater().inflate(R.menu.menu_menu, menu);
		return super.onCreateOptionsMenu((android.view.Menu) menu);
	}

	// Khi menu được chọn
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.exit:
			doExit(); // Gọi lại hàm xác nhận thoát
			break;
		case R.id.about:
			doDialog();
			break;
		case R.id.backhome:
			Intent i = new Intent(MenuActivity.this, MainActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_CLEAR_TASK);
			MenuActivity.this.startActivity(i);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	// Khi ấn vào nút back trên màn hình
	@Override
	public void onBackPressed() {
		doExit();
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		
		//Khởi tạo edit text
		final EditText edt1 = (EditText) findViewById(R.id.mon1_so_phan);
		final EditText edt2 = (EditText) findViewById(R.id.mon2_so_phan);
		final EditText edt3 = (EditText) findViewById(R.id.mon3_so_phan);
		final EditText edt4 = (EditText) findViewById(R.id.mon4_so_phan);
		final EditText edt5 = (EditText) findViewById(R.id.mon5_so_phan);
		final EditText edt6 = (EditText) findViewById(R.id.mon6_so_phan);
		final EditText edt7 = (EditText) findViewById(R.id.mon7_so_phan);
		final EditText edt8 = (EditText) findViewById(R.id.mon8_so_phan);
		
		//Disable edit text (khi chưa chọn món ăn)
		edt1.setEnabled(false);
		edt2.setEnabled(false);
		edt3.setEnabled(false);
		edt4.setEnabled(false);
		edt5.setEnabled(false);
		edt6.setEnabled(false);
		edt7.setEnabled(false);
		edt8.setEnabled(false);
		
		//Khởi tạo check box
		CheckBox chkMon1 = (CheckBox) findViewById(R.id.mon1);
		CheckBox chkMon2 = (CheckBox) findViewById(R.id.mon2);
		CheckBox chkMon3 = (CheckBox) findViewById(R.id.mon3);
		CheckBox chkMon4 = (CheckBox) findViewById(R.id.mon4);
		CheckBox chkMon5 = (CheckBox) findViewById(R.id.mon5);
		CheckBox chkMon6 = (CheckBox) findViewById(R.id.mon6);
		CheckBox chkMon7 = (CheckBox) findViewById(R.id.mon7);
		CheckBox chkMon8 = (CheckBox) findViewById(R.id.mon8);
		
		//Kích hoạt lại edit text khi món ăn được chọn
		kich_hoat_edt(chkMon1 , edt1);
		kich_hoat_edt(chkMon2 , edt2);
		kich_hoat_edt(chkMon3 , edt3);
		kich_hoat_edt(chkMon4 , edt4);
		kich_hoat_edt(chkMon5 , edt5);
		kich_hoat_edt(chkMon6 , edt6);
		kich_hoat_edt(chkMon7 , edt7);
		kich_hoat_edt(chkMon8 , edt8);
	}

	// Hàm xác nhận đặt món
	public void Ham_xac_nhan_dat_mon(View v) {
		
		
		// Lấy số bàn
		EditText nhap_so_ban = (EditText) findViewById(R.id.so_ban_nhap);
		so_ban = nhap_so_ban.getText().toString();

		kiem_tra_checkbox(); // Gọi lại hàm kiểm tra checkbox

		// Kiểm tra số bàn được nhập chưa
		if (TextUtils.isEmpty(so_ban)) {
			nhap_so_ban.setError("Vui lòng nhập số bàn.");
			return;
		} 
		// Kiểm tra số bàn có chính xác không
		int so_ban_num = Integer.parseInt(so_ban);
		if ((so_ban_num <= 0) | (so_ban_num > 10)) {
			nhap_so_ban.setError("Số bàn chỉ từ 1 đến 10. Vui lòng nhập lại.");
			return;
		}
		if (so_mon == 0) {
			Toast toast = Toast.makeText(this,
					"Vui lòng chọn ít nhất một món.", Toast.LENGTH_LONG);
			toast.show();
		} 
		else if(isOK) {
				// Chuyển sang trang xác nhận
				Intent i = new Intent(MenuActivity.this,
						ConfirmMenuActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_CLEAR_TASK);

				// Đóng gói dữ liệu để chuyển sang activity confirm_menu
				Bundle mybundle = new Bundle();
				mybundle.putInt("so_ban", so_ban_num);
				mybundle.putInt("so_mon", so_mon);
				mybundle.putStringArrayList("mon",
						(ArrayList<String>) mon_duoc_chon);
				mybundle.putIntegerArrayList("gia",
						(ArrayList<Integer>) gia_duoc_chon);
				mybundle.putIntegerArrayList("so_phan",
						(ArrayList<Integer>) so_phan_chon);

				// Đặt bundle vào intent
				i.putExtra("my_bundle", mybundle);

				// Mở activity confirm menu
				MenuActivity.this.startActivity(i);
			}
		else {
			Toast toast = Toast.makeText(this, "Vui lòng nhập số phần ăn cho mỗi món.", Toast.LENGTH_LONG);
			toast.show();
		}
	}
}

