package dp.qlnh;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {

	List<String> so_ban_da_co = new ArrayList<String>();
	InputStream is = null;
	String result = null;
	String line = null;

	/* Hàm xác nhận khi muốn thoát */
	protected void doExit() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(
				MainActivity.this);
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
				MainActivity.this);
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
		setContentView(R.layout.activity_main);

		new LoadSoBan().execute();
	}

	// Tạo menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		return super.onCreateOptionsMenu(menu);
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
		}
		return super.onOptionsItemSelected(item);
	}

	// Khi ấn vào nút back trên màn hình
	@Override
	public void onBackPressed() {
		doExit();
	}

	public void Ham_Dat_Mon(View v) {
		Intent i = new Intent(MainActivity.this, MenuActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_CLEAR_TASK);
		MainActivity.this.startActivity(i);
	}

	public void Ham_Tinh_Tien(View v) {
		Intent i = new Intent(MainActivity.this, BillActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_CLEAR_TASK);
		
		Bundle mybundle = new Bundle();
		mybundle.putStringArrayList("so_ban_da_co", (ArrayList<String>) so_ban_da_co);
		i.putExtra("my_bundle", mybundle);
		MainActivity.this.startActivity(i);
	}
	public void Ham_Sua_Mon(View v) {
		final EditText edt = new EditText(this);
		edt.setInputType(InputType.TYPE_CLASS_NUMBER);
		final AlertDialog dialog = new AlertDialog.Builder(this)
				.setTitle("Quản lý nhà hàng")
				.setMessage("Vui lòng nhập số bàn để sửa bill")
				.setView(edt)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						String so_ban = edt.getText().toString();
						Intent i = new Intent(MainActivity.this,
								ChangeMenuActivity.class);
						i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
								| Intent.FLAG_ACTIVITY_CLEAR_TASK);
						Bundle mybundle = new Bundle();
						mybundle.putString("so_ban", so_ban);
						i.putExtra("my_bundle", mybundle);
						MainActivity.this.startActivity(i);
					}
				}).setNegativeButton("Hủy", null)
				.setIcon(R.drawable.ic_launcher).show();
		dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
		edt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				final String[] so_ban_da_co_spl = new String[so_ban_da_co.size()];
				so_ban_da_co.toArray(so_ban_da_co_spl);
				for(String x: so_ban_da_co_spl){
					if(edt.getText().toString().equals(x)){
						dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
						edt.setError(null);
						break;
					}
					else {
						dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
						edt.setError("Bàn số " + edt.getText().toString() + " chưa được đặt món");
					}
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
			}

			@Override
			public void afterTextChanged(Editable s) {
				final String[] so_ban_da_co_spl = new String[so_ban_da_co.size()];
				so_ban_da_co.toArray(so_ban_da_co_spl);
				for(String x: so_ban_da_co_spl){
					if(edt.getText().toString().equals(x)){
						dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
						edt.setError(null);
						break;
					}
					else {
						dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
						edt.setError("Bàn số " + edt.getText().toString() + " chưa được đặt món");
						
					}
				}
				if(edt.getText().toString().equals("")){
					edt.setError(null);
				}
			}
		});
	}
	class LoadSoBan extends AsyncTask<String, String, String> {
		@Override
		protected String doInBackground(String... arg0) {
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(
						"http://10.0.3.2:8080/qlnh/load_so_ban.php");
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
				Log.e("pass", "success");
			} catch (Exception e) {
				Log.e("fail", e.toString());
			}
			try {
				JSONArray jArray = new JSONArray(result);
				for (int i = 0; i < jArray.length(); i++) {
					try {
						JSONObject oneObject = jArray.getJSONObject(i);
						so_ban_da_co.add(oneObject.getString("so_ban"));
					} catch (Exception e) {
						// Oops
					}
				}
				Log.e("pass2", "success2");
			} catch (Exception e) {
				Log.e("fail2", e.toString());
			}
			return null;
		}

		protected void onPostExecute(String file_url) {

		}

	}

}
