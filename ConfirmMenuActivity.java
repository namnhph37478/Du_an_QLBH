package dp.qlnh;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ConfirmMenuActivity extends Activity {

	// Khai báo biến
	int code;
	InputStream is = null;
	String result = null;
	String line = null;
	int so_mon, so_ban;
	int tong_cong = 0;
	ArrayList<String> mon_duoc_chon = new ArrayList<String>();
	ArrayList<Integer> gia_duoc_chon = new ArrayList<Integer>();
	ArrayList<Integer> so_phan_chon = new ArrayList<Integer>();

	String ten_mon1, ten_mon2, ten_mon3, ten_mon4, ten_mon5, ten_mon6,
			ten_mon7, ten_mon8;
	String sp_mon1, sp_mon2, sp_mon3, sp_mon4, sp_mon5, sp_mon6, sp_mon7,
			sp_mon8;

	// Progress Dialog
	private ProgressDialog pDialog;

	// Hàm định dạng số tiền
	public String tong_cong_format(int tong_cong) {
		String tong_chuoi = String.valueOf(tong_cong);
		String[] tong_mang = new String[13];
		String tong_chuoi_kq = " ";
		int len = tong_chuoi.length();

		tong_chuoi = new StringBuilder(tong_chuoi).reverse().toString();

		for (int i = 0; i < len; i++) {
			String a = tong_chuoi.substring(i, i + 1);
			tong_mang[i] = a;
		}

		for (int j = 0; j < len; j += 2) {
			if (j != 0) {
				tong_mang[j] = tong_mang[j] + " ";
				j += 1;
			}
		}

		for (String x : tong_mang) {
			tong_chuoi_kq += x;
		}

		tong_chuoi_kq = new StringBuilder(tong_chuoi_kq).reverse().toString();
		tong_chuoi_kq = tong_chuoi_kq.replace("llun", "");
		tong_chuoi_kq = tong_chuoi_kq.trim();

		return tong_chuoi_kq;
	}

	/* Hàm xác nhận khi muốn thoát */
	protected void doExit() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(
				ConfirmMenuActivity.this);
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
				ConfirmMenuActivity.this);
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
			Intent i = new Intent(ConfirmMenuActivity.this, MainActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_CLEAR_TASK);
			ConfirmMenuActivity.this.startActivity(i);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	// Khi ấn vào nút back trên màn hình
	@Override
	public void onBackPressed() {
		doExit();
	};

	LinearLayout menu_confirm;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Lấy intent
		Intent intent = getIntent();
		// Lấy dữ liệu ở activity menu
		Bundle packageFromCaller = intent.getBundleExtra("my_bundle");
		so_ban = packageFromCaller.getInt("so_ban");
		so_mon = packageFromCaller.getInt("so_mon");
		mon_duoc_chon = packageFromCaller.getStringArrayList("mon");
		gia_duoc_chon = packageFromCaller.getIntegerArrayList("gia");
		so_phan_chon = packageFromCaller.getIntegerArrayList("so_phan");

		// Đổi liststring sang string
		final String[] mon_duoc_chon_spl = new String[mon_duoc_chon.size()];
		mon_duoc_chon.toArray(mon_duoc_chon_spl);

		int[] gia_duoc_chon_spl = new int[gia_duoc_chon.size()];
		for (int i = 0; i < gia_duoc_chon_spl.length; i++) {
			gia_duoc_chon_spl[i] = gia_duoc_chon.get(i).intValue();
		}

		int[] so_phan_chon_spl = new int[so_phan_chon.size()];
		for (int i = 0; i < so_phan_chon_spl.length; i++) {
			so_phan_chon_spl[i] = so_phan_chon.get(i).intValue();
		}

		// Tạo layout xuất ra món ăn đã chọn
		menu_confirm = new LinearLayout(this);
		menu_confirm.setLayoutParams(new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		menu_confirm.setOrientation(LinearLayout.VERTICAL);

		// Textview
		TextView thong_bao_tv = new TextView(this);
		thong_bao_tv.setText("Quý khách đã chọn những món sau:");
		thong_bao_tv.setTextSize(14);
		thong_bao_tv.setPadding(10, 20, 10, 20);

		// Tạo params
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(20, 0, 20, 0);

		LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		tvParams.setMargins(2, 10, 0, 0);

		LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		btnParams.setMargins(30, 50, 30, 0);

		// Tạo linearlayout horizontal
		LinearLayout title_layout = new LinearLayout(this);
		title_layout.setLayoutParams(layoutParams);
		title_layout.setOrientation(LinearLayout.HORIZONTAL);

		// Tạo thanh tiêu đề bảng
		TextView stt_tv = new TextView(this);
		stt_tv.setLayoutParams(tvParams);
		stt_tv.setText("STT");
		stt_tv.setTextSize(14);
		stt_tv.setGravity(Gravity.CENTER);
		stt_tv.setPadding(10, 10, 10, 10);
		stt_tv.setTextColor(Color.parseColor("#FFFFFF"));
		stt_tv.setBackgroundColor(Color.parseColor("#FFAC82"));
		stt_tv.setWidth(80);

		TextView mon_tv = new TextView(this);
		mon_tv.setLayoutParams(tvParams);
		mon_tv.setText("Món");
		mon_tv.setTextSize(14);
		mon_tv.setGravity(Gravity.CENTER);
		mon_tv.setPadding(10, 10, 10, 10);
		mon_tv.setTextColor(Color.parseColor("#FFFFFF"));
		mon_tv.setBackgroundColor(Color.parseColor("#FFAC82"));
		mon_tv.setWidth(320);

		TextView gia_tv = new TextView(this);
		gia_tv.setLayoutParams(tvParams);
		gia_tv.setText("Giá");
		gia_tv.setTextSize(14);
		gia_tv.setGravity(Gravity.CENTER);
		gia_tv.setPadding(10, 10, 10, 10);
		gia_tv.setTextColor(Color.parseColor("#FFFFFF"));
		gia_tv.setBackgroundColor(Color.parseColor("#FFAC82"));
		gia_tv.setWidth(140);

		TextView so_phan_tv = new TextView(this);
		so_phan_tv.setLayoutParams(tvParams);
		so_phan_tv.setText("S.Phần");
		so_phan_tv.setTextSize(14);
		so_phan_tv.setGravity(Gravity.CENTER);
		so_phan_tv.setPadding(10, 10, 10, 10);
		so_phan_tv.setTextColor(Color.parseColor("#FFFFFF"));
		so_phan_tv.setBackgroundColor(Color.parseColor("#FFAC82"));
		so_phan_tv.setWidth(110);
		// END thanh tiêu đề

		// Add view
		menu_confirm.addView(thong_bao_tv);
		menu_confirm.addView(title_layout);
		title_layout.addView(stt_tv);
		title_layout.addView(mon_tv);
		title_layout.addView(gia_tv);
		title_layout.addView(so_phan_tv);

		// /////////////////////////////////////////////////////////////////////////////////

		for (int i = 1; i <= so_mon; i++) {

			// Tạo layout chứa món ăn đã đặt
			LinearLayout mon_layout = new LinearLayout(this);
			mon_layout.setLayoutParams(layoutParams);
			mon_layout.setOrientation(LinearLayout.HORIZONTAL);

			// Tạo textview cho từng món ăn
			TextView stt_mon_tv = new TextView(this);
			stt_mon_tv.setLayoutParams(tvParams);
			stt_mon_tv.setText(i + "");
			stt_mon_tv.setTextSize(14);
			stt_mon_tv.setGravity(Gravity.CENTER);
			stt_mon_tv.setPadding(10, 10, 10, 10);
			stt_mon_tv.setWidth(80);

			TextView mon_mon_tv = new TextView(this);
			mon_mon_tv.setLayoutParams(tvParams);
			mon_mon_tv.setText(mon_duoc_chon_spl[i - 1]);
			mon_mon_tv.setTextSize(14);
			mon_mon_tv.setPadding(10, 10, 10, 10);
			mon_mon_tv.setWidth(320);

			TextView gia_mon_tv = new TextView(this);
			gia_mon_tv.setLayoutParams(tvParams);
			gia_mon_tv.setText(gia_duoc_chon_spl[i - 1] + " 000đ");
			gia_mon_tv.setTextSize(14);
			gia_mon_tv.setGravity(Gravity.CENTER);
			gia_mon_tv.setPadding(10, 10, 10, 10);
			gia_mon_tv.setWidth(140);

			TextView so_phan_mon_tv = new TextView(this);
			so_phan_mon_tv.setLayoutParams(tvParams);
			so_phan_mon_tv.setText(so_phan_chon_spl[i - 1] + "");
			so_phan_mon_tv.setTextSize(14);
			so_phan_mon_tv.setGravity(Gravity.CENTER);
			so_phan_mon_tv.setPadding(10, 10, 10, 10);
			so_phan_mon_tv.setWidth(110);

			// Add view
			menu_confirm.addView(mon_layout);
			mon_layout.addView(stt_mon_tv);
			mon_layout.addView(mon_mon_tv);
			mon_layout.addView(gia_mon_tv);
			mon_layout.addView(so_phan_mon_tv);

			tong_cong += (gia_duoc_chon_spl[i - 1] * so_phan_chon_spl[i - 1]);

		}
		// /////////////////////////////////////////////////////////////////////////////////////////////

		// ////////////////////////////////////////////////////////////////////////////////////////////

		// Tạo linearlayout horizontal
		LinearLayout end_layout = new LinearLayout(this);
		end_layout.setLayoutParams(layoutParams);
		end_layout.setOrientation(LinearLayout.HORIZONTAL);

		// Textview bàn số
		TextView ban_so_tv = new TextView(this);
		ban_so_tv.setLayoutParams(tvParams);
		ban_so_tv.setText("Bàn số:");
		ban_so_tv.setTextSize(16);
		ban_so_tv.setTypeface(Typeface.DEFAULT_BOLD);
		ban_so_tv.setPadding(10, 10, 10, 10);
		ban_so_tv.setWidth(410);

		TextView ban_so_value_tv = new TextView(this);
		ban_so_value_tv.setLayoutParams(tvParams);
		ban_so_value_tv.setText(String.valueOf(so_ban));
		ban_so_value_tv.setTextSize(16);
		ban_so_value_tv.setGravity(Gravity.CENTER);
		ban_so_value_tv.setTextColor(Color.parseColor("#FA345C"));
		ban_so_value_tv.setPadding(10, 10, 10, 10);
		ban_so_value_tv.setWidth(220);

		// Tạo linearlayout horizontal
		LinearLayout end2_layout = new LinearLayout(this);
		end2_layout.setLayoutParams(layoutParams);
		end2_layout.setOrientation(LinearLayout.HORIZONTAL);

		// Textview tổng cộng
		TextView tong_cong_tv = new TextView(this);
		tong_cong_tv.setLayoutParams(tvParams);
		tong_cong_tv.setText("Tổng cộng:");
		tong_cong_tv.setTypeface(Typeface.DEFAULT_BOLD);
		tong_cong_tv.setTextSize(16);
		tong_cong_tv.setPadding(10, 10, 10, 10);
		tong_cong_tv.setWidth(410);

		TextView tong_cong_value_tv = new TextView(this);
		tong_cong_value_tv.setLayoutParams(tvParams);
		tong_cong_value_tv.setText(tong_cong_format(tong_cong) + " 000đ");
		tong_cong_value_tv.setTextSize(16);
		tong_cong_value_tv.setGravity(Gravity.CENTER);
		tong_cong_value_tv.setTextColor(Color.parseColor("#FA345C"));
		tong_cong_value_tv.setPadding(10, 10, 10, 10);
		tong_cong_value_tv.setWidth(220);

		Button btnXac_nhan = new Button(this);
		btnXac_nhan.setText("Xác nhận");
		btnXac_nhan.setTextColor(Color.parseColor("#FFFFFF"));
		btnXac_nhan.setTextSize(20);
		btnXac_nhan.setBackground(getResources().getDrawable(
				R.drawable.selector));
		btnXac_nhan.setId(1);
		btnXac_nhan.setLayoutParams(btnParams);

		// Add view
		menu_confirm.addView(end_layout);
		end_layout.addView(ban_so_tv);
		end_layout.addView(ban_so_value_tv);
		menu_confirm.addView(end2_layout);
		end2_layout.addView(tong_cong_tv);
		end2_layout.addView(tong_cong_value_tv);
		menu_confirm.addView(btnXac_nhan);

		// /////////////////////////////////////////////////////////////////////////////////////
		setContentView(menu_confirm);

		// Hàm khi ấn vào button xác nhận
		Button button = (Button) findViewById(1);
		button.setOnClickListener(new View.OnClickListener() {

			protected void GetVarValue() {
				switch (so_mon) {
				case 1:
					ten_mon1 = mon_duoc_chon.get(0);
					sp_mon1 = String.valueOf(so_phan_chon.get(0));
					ten_mon2 = null;
					sp_mon2 = null;
					ten_mon3 = null;
					sp_mon3 = null;
					ten_mon4 = null;
					sp_mon4 = null;
					ten_mon5 = null;
					sp_mon5 = null;
					ten_mon6 = null;
					sp_mon6 = null;
					ten_mon7 = null;
					sp_mon7 = null;
					ten_mon8 = null;
					sp_mon8 = null;
					break;
				case 2:
					ten_mon1 = mon_duoc_chon.get(0);
					sp_mon1 = String.valueOf(so_phan_chon.get(0));
					ten_mon2 = mon_duoc_chon.get(1);
					sp_mon2 = String.valueOf(so_phan_chon.get(1));
					ten_mon3 = null;
					sp_mon3 = null;
					ten_mon4 = null;
					sp_mon4 = null;
					ten_mon5 = null;
					sp_mon5 = null;
					ten_mon6 = null;
					sp_mon6 = null;
					ten_mon7 = null;
					sp_mon7 = null;
					ten_mon8 = null;
					sp_mon8 = null;
					break;
				case 3:
					ten_mon1 = mon_duoc_chon.get(0);
					sp_mon1 = String.valueOf(so_phan_chon.get(0));
					ten_mon2 = mon_duoc_chon.get(1);
					sp_mon2 = String.valueOf(so_phan_chon.get(1));
					ten_mon3 = mon_duoc_chon.get(2);
					sp_mon3 = String.valueOf(so_phan_chon.get(2));
					ten_mon4 = null;
					sp_mon4 = null;
					ten_mon5 = null;
					sp_mon5 = null;
					ten_mon6 = null;
					sp_mon6 = null;
					ten_mon7 = null;
					sp_mon7 = null;
					ten_mon8 = null;
					sp_mon8 = null;
					break;
				case 4:
					ten_mon1 = mon_duoc_chon.get(0);
					sp_mon1 = String.valueOf(so_phan_chon.get(0));
					ten_mon2 = mon_duoc_chon.get(1);
					sp_mon2 = String.valueOf(so_phan_chon.get(1));
					ten_mon3 = mon_duoc_chon.get(2);
					sp_mon3 = String.valueOf(so_phan_chon.get(2));
					ten_mon4 = mon_duoc_chon.get(3);
					sp_mon4 = String.valueOf(so_phan_chon.get(3));
					ten_mon5 = null;
					sp_mon5 = null;
					ten_mon6 = null;
					sp_mon6 = null;
					ten_mon7 = null;
					sp_mon7 = null;
					ten_mon8 = null;
					sp_mon8 = null;
					break;
				case 5:
					ten_mon1 = mon_duoc_chon.get(0);
					sp_mon1 = String.valueOf(so_phan_chon.get(0));
					ten_mon2 = mon_duoc_chon.get(1);
					sp_mon2 = String.valueOf(so_phan_chon.get(1));
					ten_mon3 = mon_duoc_chon.get(2);
					sp_mon3 = String.valueOf(so_phan_chon.get(2));
					ten_mon4 = mon_duoc_chon.get(3);
					sp_mon4 = String.valueOf(so_phan_chon.get(3));
					ten_mon5 = mon_duoc_chon.get(4);
					sp_mon5 = String.valueOf(so_phan_chon.get(4));
					ten_mon6 = null;
					sp_mon6 = null;
					ten_mon7 = null;
					sp_mon7 = null;
					ten_mon8 = null;
					sp_mon8 = null;
					break;
				case 6:
					ten_mon1 = mon_duoc_chon.get(0);
					sp_mon1 = String.valueOf(so_phan_chon.get(0));
					ten_mon2 = mon_duoc_chon.get(1);
					sp_mon2 = String.valueOf(so_phan_chon.get(1));
					ten_mon3 = mon_duoc_chon.get(2);
					sp_mon3 = String.valueOf(so_phan_chon.get(2));
					ten_mon4 = mon_duoc_chon.get(3);
					sp_mon4 = String.valueOf(so_phan_chon.get(3));
					ten_mon5 = mon_duoc_chon.get(4);
					sp_mon5 = String.valueOf(so_phan_chon.get(4));
					ten_mon6 = mon_duoc_chon.get(5);
					sp_mon6 = String.valueOf(so_phan_chon.get(5));
					ten_mon7 = null;
					sp_mon7 = null;
					ten_mon8 = null;
					sp_mon8 = null;
					break;
				case 7:
					ten_mon1 = mon_duoc_chon.get(0);
					sp_mon1 = String.valueOf(so_phan_chon.get(0));
					ten_mon2 = mon_duoc_chon.get(1);
					sp_mon2 = String.valueOf(so_phan_chon.get(1));
					ten_mon3 = mon_duoc_chon.get(2);
					sp_mon3 = String.valueOf(so_phan_chon.get(2));
					ten_mon4 = mon_duoc_chon.get(3);
					sp_mon4 = String.valueOf(so_phan_chon.get(3));
					ten_mon5 = mon_duoc_chon.get(4);
					sp_mon5 = String.valueOf(so_phan_chon.get(4));
					ten_mon6 = mon_duoc_chon.get(5);
					sp_mon6 = String.valueOf(so_phan_chon.get(5));
					ten_mon7 = mon_duoc_chon.get(6);
					sp_mon7 = String.valueOf(so_phan_chon.get(6));
					ten_mon8 = null;
					sp_mon8 = null;
					break;
				case 8:
					ten_mon1 = mon_duoc_chon.get(0);
					sp_mon1 = String.valueOf(so_phan_chon.get(0));
					ten_mon2 = mon_duoc_chon.get(1);
					sp_mon2 = String.valueOf(so_phan_chon.get(1));
					ten_mon3 = mon_duoc_chon.get(2);
					sp_mon3 = String.valueOf(so_phan_chon.get(2));
					ten_mon4 = mon_duoc_chon.get(3);
					sp_mon4 = String.valueOf(so_phan_chon.get(3));
					ten_mon5 = mon_duoc_chon.get(4);
					sp_mon5 = String.valueOf(so_phan_chon.get(4));
					ten_mon6 = mon_duoc_chon.get(5);
					sp_mon6 = String.valueOf(so_phan_chon.get(5));
					ten_mon7 = mon_duoc_chon.get(6);
					sp_mon7 = String.valueOf(so_phan_chon.get(6));
					ten_mon8 = mon_duoc_chon.get(7);
					sp_mon8 = String.valueOf(so_phan_chon.get(7));
					break;
				}
			}

			@Override
			public void onClick(View v) {
				GetVarValue();
				new CreateOrders().execute();

			}
		});
	}

	class CreateOrders extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(ConfirmMenuActivity.this);
			pDialog.setMessage("Đang đặt món...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		protected String doInBackground(String... args) {
			try {
				String data = URLEncoder.encode("so_ban", "UTF-8") + "="
						+ URLEncoder.encode(String.valueOf(so_ban), "UTF-8");
				switch (so_mon) {
				case 1:
					data += "&" + URLEncoder.encode("ten_mon1", "UTF-8") + "="
							+ URLEncoder.encode(ten_mon1, "UTF-8");
					data += "&" + URLEncoder.encode("sp_mon1", "UTF-8") + "="
							+ URLEncoder.encode(sp_mon1, "UTF-8");
					
					data += "&" + URLEncoder.encode("tong_cong", "UTF-8") + "="
							+ URLEncoder.encode(String.valueOf(tong_cong), "UTF-8");
					break;
				case 2:
					data += "&" + URLEncoder.encode("ten_mon1", "UTF-8") + "="
							+ URLEncoder.encode(ten_mon1, "UTF-8");
					data += "&" + URLEncoder.encode("sp_mon1", "UTF-8") + "="
							+ URLEncoder.encode(sp_mon1, "UTF-8");

					data += "&" + URLEncoder.encode("ten_mon2", "UTF-8") + "="
							+ URLEncoder.encode(ten_mon2, "UTF-8");
					data += "&" + URLEncoder.encode("sp_mon2", "UTF-8") + "="
							+ URLEncoder.encode(sp_mon2, "UTF-8");
					
					data += "&" + URLEncoder.encode("tong_cong", "UTF-8") + "="
							+ URLEncoder.encode(String.valueOf(tong_cong), "UTF-8");
					break;
				case 3:
					data += "&" + URLEncoder.encode("ten_mon1", "UTF-8") + "="
							+ URLEncoder.encode(ten_mon1, "UTF-8");
					data += "&" + URLEncoder.encode("sp_mon1", "UTF-8") + "="
							+ URLEncoder.encode(sp_mon1, "UTF-8");

					data += "&" + URLEncoder.encode("ten_mon2", "UTF-8") + "="
							+ URLEncoder.encode(ten_mon2, "UTF-8");
					data += "&" + URLEncoder.encode("sp_mon2", "UTF-8") + "="
							+ URLEncoder.encode(sp_mon2, "UTF-8");

					data += "&" + URLEncoder.encode("ten_mon3", "UTF-8") + "="
							+ URLEncoder.encode(ten_mon3, "UTF-8");
					data += "&" + URLEncoder.encode("sp_mon3", "UTF-8") + "="
							+ URLEncoder.encode(sp_mon3, "UTF-8");
					
					data += "&" + URLEncoder.encode("tong_cong", "UTF-8") + "="
							+ URLEncoder.encode(String.valueOf(tong_cong), "UTF-8");
					break;
				case 4:
					data += "&" + URLEncoder.encode("ten_mon1", "UTF-8") + "="
							+ URLEncoder.encode(ten_mon1, "UTF-8");
					data += "&" + URLEncoder.encode("sp_mon1", "UTF-8") + "="
							+ URLEncoder.encode(sp_mon1, "UTF-8");

					data += "&" + URLEncoder.encode("ten_mon2", "UTF-8") + "="
							+ URLEncoder.encode(ten_mon2, "UTF-8");
					data += "&" + URLEncoder.encode("sp_mon2", "UTF-8") + "="
							+ URLEncoder.encode(sp_mon2, "UTF-8");

					data += "&" + URLEncoder.encode("ten_mon3", "UTF-8") + "="
							+ URLEncoder.encode(ten_mon3, "UTF-8");
					data += "&" + URLEncoder.encode("sp_mon3", "UTF-8") + "="
							+ URLEncoder.encode(sp_mon3, "UTF-8");

					data += "&" + URLEncoder.encode("ten_mon4", "UTF-8") + "="
							+ URLEncoder.encode(ten_mon4, "UTF-8");
					data += "&" + URLEncoder.encode("sp_mon4", "UTF-8") + "="
							+ URLEncoder.encode(sp_mon4, "UTF-8");
					
					data += "&" + URLEncoder.encode("tong_cong", "UTF-8") + "="
							+ URLEncoder.encode(String.valueOf(tong_cong), "UTF-8");
					break;
				case 5:
					data += "&" + URLEncoder.encode("ten_mon1", "UTF-8") + "="
							+ URLEncoder.encode(ten_mon1, "UTF-8");
					data += "&" + URLEncoder.encode("sp_mon1", "UTF-8") + "="
							+ URLEncoder.encode(sp_mon1, "UTF-8");

					data += "&" + URLEncoder.encode("ten_mon2", "UTF-8") + "="
							+ URLEncoder.encode(ten_mon2, "UTF-8");
					data += "&" + URLEncoder.encode("sp_mon2", "UTF-8") + "="
							+ URLEncoder.encode(sp_mon2, "UTF-8");

					data += "&" + URLEncoder.encode("ten_mon3", "UTF-8") + "="
							+ URLEncoder.encode(ten_mon3, "UTF-8");
					data += "&" + URLEncoder.encode("sp_mon3", "UTF-8") + "="
							+ URLEncoder.encode(sp_mon3, "UTF-8");

					data += "&" + URLEncoder.encode("ten_mon4", "UTF-8") + "="
							+ URLEncoder.encode(ten_mon4, "UTF-8");
					data += "&" + URLEncoder.encode("sp_mon4", "UTF-8") + "="
							+ URLEncoder.encode(sp_mon4, "UTF-8");

					data += "&" + URLEncoder.encode("ten_mon5", "UTF-8") + "="
							+ URLEncoder.encode(ten_mon5, "UTF-8");
					data += "&" + URLEncoder.encode("sp_mon5", "UTF-8") + "="
							+ URLEncoder.encode(sp_mon5, "UTF-8");
					
					data += "&" + URLEncoder.encode("tong_cong", "UTF-8") + "="
							+ URLEncoder.encode(String.valueOf(tong_cong), "UTF-8");
					break;
				case 6:
					data += "&" + URLEncoder.encode("ten_mon1", "UTF-8") + "="
							+ URLEncoder.encode(ten_mon1, "UTF-8");
					data += "&" + URLEncoder.encode("sp_mon1", "UTF-8") + "="
							+ URLEncoder.encode(sp_mon1, "UTF-8");

					data += "&" + URLEncoder.encode("ten_mon2", "UTF-8") + "="
							+ URLEncoder.encode(ten_mon2, "UTF-8");
					data += "&" + URLEncoder.encode("sp_mon2", "UTF-8") + "="
							+ URLEncoder.encode(sp_mon2, "UTF-8");

					data += "&" + URLEncoder.encode("ten_mon3", "UTF-8") + "="
							+ URLEncoder.encode(ten_mon3, "UTF-8");
					data += "&" + URLEncoder.encode("sp_mon3", "UTF-8") + "="
							+ URLEncoder.encode(sp_mon3, "UTF-8");

					data += "&" + URLEncoder.encode("ten_mon4", "UTF-8") + "="
							+ URLEncoder.encode(ten_mon4, "UTF-8");
					data += "&" + URLEncoder.encode("sp_mon4", "UTF-8") + "="
							+ URLEncoder.encode(sp_mon4, "UTF-8");

					data += "&" + URLEncoder.encode("ten_mon5", "UTF-8") + "="
							+ URLEncoder.encode(ten_mon5, "UTF-8");
					data += "&" + URLEncoder.encode("sp_mon5", "UTF-8") + "="
							+ URLEncoder.encode(sp_mon5, "UTF-8");

					data += "&" + URLEncoder.encode("ten_mon6", "UTF-8") + "="
							+ URLEncoder.encode(ten_mon6, "UTF-8");
					data += "&" + URLEncoder.encode("sp_mon6", "UTF-8") + "="
							+ URLEncoder.encode(sp_mon6, "UTF-8");
					
					data += "&" + URLEncoder.encode("tong_cong", "UTF-8") + "="
							+ URLEncoder.encode(String.valueOf(tong_cong), "UTF-8");
					break;
				case 7:
					data += "&" + URLEncoder.encode("ten_mon1", "UTF-8") + "="
							+ URLEncoder.encode(ten_mon1, "UTF-8");
					data += "&" + URLEncoder.encode("sp_mon1", "UTF-8") + "="
							+ URLEncoder.encode(sp_mon1, "UTF-8");

					data += "&" + URLEncoder.encode("ten_mon2", "UTF-8") + "="
							+ URLEncoder.encode(ten_mon2, "UTF-8");
					data += "&" + URLEncoder.encode("sp_mon2", "UTF-8") + "="
							+ URLEncoder.encode(sp_mon2, "UTF-8");

					data += "&" + URLEncoder.encode("ten_mon3", "UTF-8") + "="
							+ URLEncoder.encode(ten_mon3, "UTF-8");
					data += "&" + URLEncoder.encode("sp_mon3", "UTF-8") + "="
							+ URLEncoder.encode(sp_mon3, "UTF-8");

					data += "&" + URLEncoder.encode("ten_mon4", "UTF-8") + "="
							+ URLEncoder.encode(ten_mon4, "UTF-8");
					data += "&" + URLEncoder.encode("sp_mon4", "UTF-8") + "="
							+ URLEncoder.encode(sp_mon4, "UTF-8");

					data += "&" + URLEncoder.encode("ten_mon5", "UTF-8") + "="
							+ URLEncoder.encode(ten_mon5, "UTF-8");
					data += "&" + URLEncoder.encode("sp_mon5", "UTF-8") + "="
							+ URLEncoder.encode(sp_mon5, "UTF-8");

					data += "&" + URLEncoder.encode("ten_mon6", "UTF-8") + "="
							+ URLEncoder.encode(ten_mon6, "UTF-8");
					data += "&" + URLEncoder.encode("sp_mon6", "UTF-8") + "="
							+ URLEncoder.encode(sp_mon6, "UTF-8");

					data += "&" + URLEncoder.encode("ten_mon7", "UTF-8") + "="
							+ URLEncoder.encode(ten_mon7, "UTF-8");
					data += "&" + URLEncoder.encode("sp_mon7", "UTF-8") + "="
							+ URLEncoder.encode(sp_mon7, "UTF-8");
					
					data += "&" + URLEncoder.encode("tong_cong", "UTF-8") + "="
							+ URLEncoder.encode(String.valueOf(tong_cong), "UTF-8");
					break;
				case 8:
					data += "&" + URLEncoder.encode("ten_mon1", "UTF-8") + "="
							+ URLEncoder.encode(ten_mon1, "UTF-8");
					data += "&" + URLEncoder.encode("sp_mon1", "UTF-8") + "="
							+ URLEncoder.encode(sp_mon1, "UTF-8");

					data += "&" + URLEncoder.encode("ten_mon2", "UTF-8") + "="
							+ URLEncoder.encode(ten_mon2, "UTF-8");
					data += "&" + URLEncoder.encode("sp_mon2", "UTF-8") + "="
							+ URLEncoder.encode(sp_mon2, "UTF-8");

					data += "&" + URLEncoder.encode("ten_mon3", "UTF-8") + "="
							+ URLEncoder.encode(ten_mon3, "UTF-8");
					data += "&" + URLEncoder.encode("sp_mon3", "UTF-8") + "="
							+ URLEncoder.encode(sp_mon3, "UTF-8");

					data += "&" + URLEncoder.encode("ten_mon4", "UTF-8") + "="
							+ URLEncoder.encode(ten_mon4, "UTF-8");
					data += "&" + URLEncoder.encode("sp_mon4", "UTF-8") + "="
							+ URLEncoder.encode(sp_mon4, "UTF-8");

					data += "&" + URLEncoder.encode("ten_mon5", "UTF-8") + "="
							+ URLEncoder.encode(ten_mon5, "UTF-8");
					data += "&" + URLEncoder.encode("sp_mon5", "UTF-8") + "="
							+ URLEncoder.encode(sp_mon5, "UTF-8");

					data += "&" + URLEncoder.encode("ten_mon6", "UTF-8") + "="
							+ URLEncoder.encode(ten_mon6, "UTF-8");
					data += "&" + URLEncoder.encode("sp_mon6", "UTF-8") + "="
							+ URLEncoder.encode(sp_mon6, "UTF-8");

					data += "&" + URLEncoder.encode("ten_mon7", "UTF-8") + "="
							+ URLEncoder.encode(ten_mon7, "UTF-8");
					data += "&" + URLEncoder.encode("sp_mon7", "UTF-8") + "="
							+ URLEncoder.encode(sp_mon7, "UTF-8");

					data += "&" + URLEncoder.encode("ten_mon8", "UTF-8") + "="
							+ URLEncoder.encode(ten_mon8, "UTF-8");
					data += "&" + URLEncoder.encode("sp_mon8", "UTF-8") + "="
							+ URLEncoder.encode(sp_mon8, "UTF-8");
					
					data += "&" + URLEncoder.encode("tong_cong", "UTF-8") + "="
							+ URLEncoder.encode(String.valueOf(tong_cong), "UTF-8");
					break;
				}
				
				URL url = new URL("http://10.0.3.2:8080/qlnh/create_order.php");
				URLConnection conn = url.openConnection();
				conn.setDoOutput(true);

				OutputStreamWriter wr = new OutputStreamWriter(
						conn.getOutputStream());
				wr.write(data);
				wr.flush();

				// ////////////////////////////
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(conn.getInputStream()));
				StringBuilder sb = new StringBuilder();
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				is.close();
				result = sb.toString();
				Log.e("pass2", "success 2");
			} catch (Exception e) {
				Log.e("fail2", e.toString());
			}

			return null;
		}

		protected void onPostExecute(String file_url) {
			pDialog.dismiss();
			ConfirmMenuActivity.this.runOnUiThread(new Runnable() {
				public void run() {
					AlertDialog.Builder alertDialog = new AlertDialog.Builder(
							ConfirmMenuActivity.this);
					alertDialog.setPositiveButton("OK", new OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							Intent i = new Intent(ConfirmMenuActivity.this, MainActivity.class);
							i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
									| Intent.FLAG_ACTIVITY_CLEAR_TASK);
							ConfirmMenuActivity.this.startActivity(i);
						}
					});
					alertDialog.setMessage("Đặt món thành công.");
					alertDialog.setTitle("Quản lý nhà hàng");
					alertDialog.show();
				}
			});
		}
	}
}
