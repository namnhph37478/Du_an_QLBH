package dp.qlnh;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

public class ChangeMenuActivity extends Activity {
	/* Hàm xác nhận khi muốn thoát */
	protected void doExit() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(
				ChangeMenuActivity.this);
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
				ChangeMenuActivity.this);
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
			Intent i = new Intent(ChangeMenuActivity.this, MainActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_CLEAR_TASK);
			ChangeMenuActivity.this.startActivity(i);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	// Khi ấn vào nút back trên màn hình
	@Override
	public void onBackPressed() {
		doExit();
	};
	String so_ban = null;
	private ProgressDialog pDialog;
	InputStream is = null;
	String result = null;
	String line = null;
	String mon1 = null, mon2 = null, mon3 = null, mon4 = null, mon5 = null,
			mon6 = null, mon7 = null, mon8 = null, sp_mon1 = null,
			sp_mon2 = null, sp_mon3 = null, sp_mon4 = null, sp_mon5 = null,
			sp_mon6 = null, sp_mon7 = null, sp_mon8 = null;
	int so_mon = 0;
	List<String> mon_duoc_chon = new ArrayList<String>(); // Mảng chứa id các
	// checkbox được
	// chọn
	List<Integer> gia_duoc_chon = new ArrayList<Integer>();
	List<Integer> so_phan_chon = new ArrayList<Integer>();
	Boolean isOK = false;

	public void kich_hoat_edt(CheckBox chk, final EditText edt) {
		chk.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					edt.setEnabled(true);
				} else {
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

	protected void kiem_tra_checkbox() {
		// Khởi tạo checkbox
		CheckBox chkMon1 = (CheckBox) findViewById(R.id.mon1_2);
		CheckBox chkMon2 = (CheckBox) findViewById(R.id.mon2_2);
		CheckBox chkMon3 = (CheckBox) findViewById(R.id.mon3_2);
		CheckBox chkMon4 = (CheckBox) findViewById(R.id.mon4_2);
		CheckBox chkMon5 = (CheckBox) findViewById(R.id.mon5_2);
		CheckBox chkMon6 = (CheckBox) findViewById(R.id.mon6_2);
		CheckBox chkMon7 = (CheckBox) findViewById(R.id.mon7_2);
		CheckBox chkMon8 = (CheckBox) findViewById(R.id.mon8_2);

		EditText edt1 = (EditText) findViewById(R.id.mon1_so_phan_2);
		EditText edt2 = (EditText) findViewById(R.id.mon2_so_phan_2);
		EditText edt3 = (EditText) findViewById(R.id.mon3_so_phan_2);
		EditText edt4 = (EditText) findViewById(R.id.mon4_so_phan_2);
		EditText edt5 = (EditText) findViewById(R.id.mon5_so_phan_2);
		EditText edt6 = (EditText) findViewById(R.id.mon6_so_phan_2);
		EditText edt7 = (EditText) findViewById(R.id.mon7_so_phan_2);
		EditText edt8 = (EditText) findViewById(R.id.mon8_so_phan_2);

		// Kiểm tra checkbox có được chọn hay không
		if (chkMon1.isChecked() && (!so_ban.equals(""))) {
			so_mon += 1;
			mon_duoc_chon.add("Thịt kho hột vịt");
			gia_duoc_chon.add(20);
			them_so_phan(edt1);

		}
		if (chkMon2.isChecked() && (!so_ban.equals(""))) {
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

	protected void Ham_Change_Menu() {
		kiem_tra_checkbox(); // Gọi lại hàm kiểm tra checkbox
		if (so_mon == 0) {
			Toast toast = Toast.makeText(this,
					"Vui lòng chọn ít nhất một món.", Toast.LENGTH_LONG);
			toast.show();
		}

		else if (isOK) { // Chuyển sang trang xác nhận
			Intent i = new Intent(ChangeMenuActivity.this,
					ConfirmChangeMenuActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_CLEAR_TASK);

			// Đóng gói dữ liệu để chuyển sang activity confirm_menu
			Bundle mybundle = new Bundle();
			mybundle.putInt("so_ban", Integer.parseInt(so_ban));
			mybundle.putInt("so_mon", so_mon);
			mybundle.putStringArrayList("mon",
					(ArrayList<String>) mon_duoc_chon);
			mybundle.putIntegerArrayList("gia",
					(ArrayList<Integer>) gia_duoc_chon);
			mybundle.putIntegerArrayList("so_phan",
					(ArrayList<Integer>) so_phan_chon);

			// Đặt bundle vào intent 
			i.putExtra("my_bundle", mybundle);

			// Mở activity confirm change menu
			ChangeMenuActivity.this.startActivity(i);
		}

		else {
			Toast toast = Toast.makeText(this,
					"Vui lòng nhập số phần ăn cho mỗi món.", Toast.LENGTH_LONG);
			toast.show();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_menu);

		// Lấy dữ liệu ở activity main
		Intent callerIntent = getIntent();
		Bundle packageFromCaller = callerIntent.getBundleExtra("my_bundle");
		so_ban = packageFromCaller.getString("so_ban");

		// Lấy thông tin về menu đã có dựa theo số bàn
		new LoadMenu().execute();

		
		Button changeMenu = (Button) findViewById(R.id.btn_change_menu);
		changeMenu.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Ham_Change_Menu();
			}
		});;
	}

	class LoadMenu extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(ChangeMenuActivity.this);
			pDialog.setMessage("Đang load menu cũ của bạn...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... arg0) {
			try {
				// Building Parameters
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("so_ban", so_ban));

				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(
						"http://10.0.3.2:8080/qlnh/load_order.php");
				httppost.setEntity(new UrlEncodedFormEntity(params));
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				is = entity.getContent();
				Log.e("pass1", "success");
			} catch (Exception e) {
				Log.e("fail1", e.toString());
			}

			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is, "UTF-8"), 8);
				StringBuilder sb = new StringBuilder();
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				is.close();
				result = sb.toString();
				Log.e("pass 2", "success 2");
			} catch (Exception e) {
				Log.e("fail2", e.toString());
			}
			try {
				JSONObject json_data = new JSONObject(result);
				mon1 = (json_data.getString("ten_mon1"));
				mon2 = (json_data.getString("ten_mon2"));
				mon3 = (json_data.getString("ten_mon3"));
				mon4 = (json_data.getString("ten_mon4"));
				mon5 = (json_data.getString("ten_mon5"));
				mon6 = (json_data.getString("ten_mon6"));
				mon7 = (json_data.getString("ten_mon7"));
				mon8 = (json_data.getString("ten_mon8"));
				sp_mon1 = (json_data.getString("sp_mon1"));
				sp_mon2 = (json_data.getString("sp_mon2"));
				sp_mon3 = (json_data.getString("sp_mon3"));
				sp_mon4 = (json_data.getString("sp_mon4"));
				sp_mon5 = (json_data.getString("sp_mon5"));
				sp_mon6 = (json_data.getString("sp_mon6"));
				sp_mon7 = (json_data.getString("sp_mon7"));
				sp_mon8 = (json_data.getString("sp_mon8"));

				Log.e("pass3", "success 3");
			} catch (Exception e) {
				Log.e("fail3", e.toString());
			}
			return null;
		}

		protected void onPostExecute(String file_url) {
			pDialog.dismiss();
			// Khởi tạo checkbox
			CheckBox chkMon1 = (CheckBox) findViewById(R.id.mon1_2);
			CheckBox chkMon2 = (CheckBox) findViewById(R.id.mon2_2);
			CheckBox chkMon3 = (CheckBox) findViewById(R.id.mon3_2);
			CheckBox chkMon4 = (CheckBox) findViewById(R.id.mon4_2);
			CheckBox chkMon5 = (CheckBox) findViewById(R.id.mon5_2);
			CheckBox chkMon6 = (CheckBox) findViewById(R.id.mon6_2);
			CheckBox chkMon7 = (CheckBox) findViewById(R.id.mon7_2);
			CheckBox chkMon8 = (CheckBox) findViewById(R.id.mon8_2);

			EditText edt1 = (EditText) findViewById(R.id.mon1_so_phan_2);
			EditText edt2 = (EditText) findViewById(R.id.mon2_so_phan_2);
			EditText edt3 = (EditText) findViewById(R.id.mon3_so_phan_2);
			EditText edt4 = (EditText) findViewById(R.id.mon4_so_phan_2);
			EditText edt5 = (EditText) findViewById(R.id.mon5_so_phan_2);
			EditText edt6 = (EditText) findViewById(R.id.mon6_so_phan_2);
			EditText edt7 = (EditText) findViewById(R.id.mon7_so_phan_2);
			EditText edt8 = (EditText) findViewById(R.id.mon8_so_phan_2);

			// Disable edit text (khi chưa chọn món ăn)
			edt1.setEnabled(false);
			edt2.setEnabled(false);
			edt3.setEnabled(false);
			edt4.setEnabled(false);
			edt5.setEnabled(false);
			edt6.setEnabled(false);
			edt7.setEnabled(false);
			edt8.setEnabled(false);

			// Kích hoạt lại edit text khi món ăn được chọn
			kich_hoat_edt(chkMon1, edt1);
			kich_hoat_edt(chkMon2, edt2);
			kich_hoat_edt(chkMon3, edt3);
			kich_hoat_edt(chkMon4, edt4);
			kich_hoat_edt(chkMon5, edt5);
			kich_hoat_edt(chkMon6, edt6);
			kich_hoat_edt(chkMon7, edt7);
			kich_hoat_edt(chkMon8, edt8);

			// Lấy dữ liệu ở menu cũ
			if (!mon1.equals("")) {
				chkMon1.setChecked(true);
				edt1.setText(sp_mon1);
				edt1.setEnabled(true);
			}
			if (!mon2.equals("")) {
				chkMon2.setChecked(true);
				edt2.setText(sp_mon2);
				edt2.setEnabled(true);
			}
			if (!mon3.equals("")) {
				chkMon3.setChecked(true);
				edt3.setText(sp_mon3);
				edt3.setEnabled(true);
			}
			if (!mon4.equals("")) {
				chkMon4.setChecked(true);
				edt4.setText(sp_mon4);
				edt4.setEnabled(true);
			}
			if (!mon5.equals("")) {
				chkMon5.setChecked(true);
				edt5.setText(sp_mon5);
				edt5.setEnabled(true);
			}
			if (!mon6.equals("")) {
				chkMon6.setChecked(true);
				edt6.setText(sp_mon6);
				edt6.setEnabled(true);
			}
			if (!mon7.equals("")) {
				chkMon7.setChecked(true);
				edt7.setText(sp_mon7);
				edt7.setEnabled(true);
			}
			if (!mon8.equals("")) {
				chkMon8.setChecked(true);
				edt8.setText(sp_mon8);
				edt8.setEnabled(true);
			}

		}

	}

}
