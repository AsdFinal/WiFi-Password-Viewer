package asd.dogw.wifi_pw_viewer;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.io.*;
import java.util.*;
import java.util.regex.*;

import java.lang.Process;

public class MainActivity extends Activity 
{
	ListView LV1;
	TextView tx1;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		LV1 = (ListView)findViewById(R.id.LV1);
		tx1=(TextView)findViewById(R.id.about);
    }
	public void onwork(View view)
	{
		//Toast.makeText(getApplicationContext(),"work in",1);
		tx1.setVisibility(view.GONE);
		LV1.setVisibility(view.VISIBLE);
		work();
	}
	public void work()
	{
		String line=do_exec("su -c cat /data/misc/wifi/wpa_supplicant.conf");
		String pattern = "network=\\{(.*?)\\}";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(line);
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();


		/*LV1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		 @Override
		 public void onItemClick(AdapterView<?> parent, View view,
		 int position, long id)
		 {
		 Toast.makeText(getApplicationContext(), "" + position, 1);
		 }
		 });
		 */


		while (m.find())
		{
			//msgbox(m.group());
			String line1=m.group();
			String pattern1= "psk=\"(.*?)\"\\s";
			Pattern r1= Pattern.compile(pattern1);
			Matcher m1 = r1.matcher(line1);
			if (m1.find())
			{
				String psk=m1.group(1);
				String pattern2 = "ssid=\"(.*?)\"\\s";
				Pattern r2 = Pattern.compile(pattern2);
				Matcher m2 = r2.matcher(line1);
				m2.find();
				String ssid=m2.group(1);
				//msgbox(ssid + "   " + psk);
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("ssid", "SSID : "+ssid);
				map.put("psk","PSK : "+ psk);
				listItem.add(map);
			}
		}
		SimpleAdapter listItemAdapter = new SimpleAdapter(this,
														  listItem, //数据源 
														  R.layout.list_item, //ListItem的XML实现 
														  new String[] { "ssid", "psk" },

														  //ImageItem的XML文件里面的一个ImageView,两个TextView ID 
														  new int[] { R.id.ssid, R.id.psk });

		LV1.setAdapter(listItemAdapter);
	}
	void msgbox(String msg)
	{
		Toast.makeText(getApplicationContext(), msg, 1).show();
	}
	String do_exec(String cmd)
	{
    	String s = "";
    	try
		{
			Process p = Runtime.getRuntime().exec(cmd);
			BufferedReader in = new BufferedReader(
				new InputStreamReader(p.getInputStream()));
			String line = null;
			while ((line = in.readLine()) != null)
			{
				s += line;				
			}
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			s = "error";
			e.printStackTrace();
		}
    	//tx1.setText(s);
		return s;    	
    }
}
