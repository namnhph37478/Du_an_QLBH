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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class BillActivity extends Activity {

	String tong_cong;
	int code;
	InputStream is = null;
	String result = null;
	String line = null;
	String so_ban_bill;
	private ProgressDialog pDialog;
	List<String> so_ban_da_co = new ArrayList<String>();
	//Hàm định dạng số tiền
	public String tong_cong_format(String tong_cong) {
		String[] tong_mang = new String[13];
		String tong_chuoi_kq = " ";
		int len = tong_cong.length();

		tong_cong = new StringBuilder(tong_cong).reverse().toString();

		for (int i = 0; i < len; i++) {
			String a = tong_cong.substring(i, i + 1);
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
				BillActivity.this);
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
				BillActivity.this);
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bill);

		Intent intent = getIntent();
		Bundle packageFromCaller = intent.getBundleExtra("my_bundle");
		so_ban_da_co = packageFromCaller.getStringArrayList("so_ban_da_co");
		
		final Button btnGet_Bill = (Button) findViewById(R.id.nhanbill);
		final EditText edtSo_Ban_Bill = (EditText) findViewById(R.id.so_ban_bill);
		
		//Disable nút này khi load activity
		btnGet_Bill.setEnabled(false);
		
		//Hàm cho nút nhận bill
		btnGet_Bill.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				so_ban_bill = edtSo_Ban_Bill.getText().toString();
				new GetBill().execute();
			}
		});
		
		//Kiểm tra số bàn nhập vào
		edtSo_Ban_Bill.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				final String[] so_ban_da_co_spl = new String[so_ban_da_co.size()];
				so_ban_da_co.toArray(so_ban_da_co_spl);
				for(String x: so_ban_da_co_spl){
					if(edtSo_Ban_Bill.getText().toString().equals(x)){
						btnGet_Bill.setEnabled(true);
						edtSo_Ban_Bill.setError(null);
						break;
					}
					else {
						btnGet_Bill.setEnabled(false);
						edtSo_Ban_Bill.setError("Bàn số " + edtSo_Ban_Bill.getText().toString() + " chưa được đặt món");
					}
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				btnGet_Bill.setEnabled(false);
			}

			@Override
			public void afterTextChanged(Editable s) {
				final String[] so_ban_da_co_spl = new String[so_ban_da_co.size()];
				so_ban_da_co.toArray(so_ban_da_co_spl);
				for(String x: so_ban_da_co_spl){
					if(edtSo_Ban_Bill.getText().toString().equals(x)){
						btnGet_Bill.setEnabled(true);
						edtSo_Ban_Bill.setError(null);
						break;
					}
					else {
						btnGet_Bill.setEnabled(false);
						edtSo_Ban_Bill.setError("Bàn số " + edtSo_Ban_Bill.getText().toString() + " chưa được đặt món");
						
					}
				}
				if(edtSo_Ban_Bill.getText().toString().equals("")){
					edtSo_Ban_Bill.setError(null);
				}
			}
		});
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_menu, menu);
		return true;
	}

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
			Intent i = new Intent(BillActivity.this, MainActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_CLEAR_TASK);
			BillActivity.this.startActivity(i);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	// Khi ấn vào nút back trên màn hình
	@Override
	public void onBackPressed() {
		doExit();
	};


	class GetBill extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(BillActivity.this);
			pDialog.setMessage("Đang lấy bill...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		protected String doInBackground(String... args) {

			try {
				// Building Parameters
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("so_ban_bill", so_ban_bill));

				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(
						"http://10.0.3.2:8080/qlnh/get_bill.php");
				httppost.setEntity(new UrlEncodedFormEntity(params));
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				is = entity.getContent();
				Log.e("pass1", "success");
			} catch (Exception e) {
				Log.e("fail1", e.toString());
			}

			try {
				BufferedReader reader = new BufferedReader
						(new InputStreamReader(is,"iso-8859-1"),8);
		            	StringBuilder sb = new StringBuilder();
		            	while ((line = reader.readLine()) != null)
				{
		       		    sb.append(line + "\n");
		           	}
		            	is.close();
		            	result = sb.toString();
			        Log.e("pass 2", "connection success ");
			} catch (Exception e) {
				Log.e("fail2", e.toString());
			}
			try {
				JSONObject json_data = new JSONObject(result);
				tong_cong = (json_data.getString("tong_cong"));
				BillActivity.this.runOnUiThread(new Runnable() {
					public void run() {
						AlertDialog.Builder alertDialog = new AlertDialog.Builder(
								BillActivity.this);
						alertDialog.setNegativeButton("OK", null);
						alertDialog
								.setMessage("Tổng số tiền cần thanh toán: \r\n"
										+ tong_cong_format(tong_cong)
										+ " 000đ \r\n"
										+ "Chân thành cảm ơn quý khách! hẹn gặp lại lần sau!");
						alertDialog.setTitle("Quản lý nhà hàng");
						alertDialog.show();
					}
				});

			} catch (Exception e) {
				Log.e("fail3", e.toString());
			}

			return null;
		}

		protected void onPostExecute(String file_url) {
			pDialog.dismiss();
		}
	}
}
