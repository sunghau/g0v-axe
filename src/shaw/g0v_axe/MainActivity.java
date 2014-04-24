package shaw.g0v_axe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		Button lv1, lv2, lv3, lv4;
		TextView answer;
		static final String levels[] = { "http://axe-level-1.herokuapp.com/", "http://axe-level-1.herokuapp.com/lv2",
				"http://axe-level-1.herokuapp.com/lv3", "http://axe-level-4.herokuapp.com/lv4" };

		View.OnClickListener lv1ClickListener = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				answer.setText("lv1");
				new Thread(new Runnable() {

					@Override
					public void run() {
						Document doc;
						try {
							doc = Jsoup.connect(levels[0]).get();
							Elements trs = doc.select("table tr");
							Elements fieldTr = trs.remove(0).children();
							String[] field = { fieldTr.get(1).text(), fieldTr.get(2).text(), fieldTr.get(3).text(), fieldTr.get(4).text(),
									fieldTr.get(5).text() };
							final JSONArray resultArray = new JSONArray();
							for (Element tr : trs) {
								Elements tds = tr.children();
								JSONObject jsonObject = new JSONObject();
								jsonObject.put("name", tds.get(0).text());
								tds.remove(0);
								JSONObject gradeObject = new JSONObject();
								for (int i = 0; i < tds.size(); i++) {
									gradeObject.put(field[i], Integer.valueOf(tds.get(i).text()));
								}
								jsonObject.put("grades", gradeObject);
								resultArray.put(jsonObject);
							}

							File myFile = new File("/sdcard/answer1.txt");
				            myFile.createNewFile();
				            FileOutputStream fOut = new FileOutputStream(myFile);
							OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fOut);
							outputStreamWriter.write(resultArray.toString());
							outputStreamWriter.close();
							fOut.close();

							getActivity().runOnUiThread(new Runnable() {

								@Override
								public void run() {
									answer.setText(resultArray.toString());
								}
							});

						} catch (IOException | JSONException e) {
							e.printStackTrace();
						}
					}
				}).start();

			}
		};

		View.OnClickListener lv2ClickListener = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				answer.setText("lv2");
				new Thread(new Runnable() {

					@Override
					public void run() {
						Document doc, subdoc;

						try {
							doc = Jsoup.connect(levels[1]).get();
							final JSONArray resultArray = new JSONArray();
							Elements as = doc.select("a");

							for (Element a : as) {
								String linkHref = a.attr("href");
								subdoc = Jsoup.connect(levels[1] + "/" + linkHref).get();
								Elements trs = subdoc.select("table tr").not(":first-child");
								for (Element tr : trs) {
									Elements tds = tr.children();
									JSONObject jsonObject = new JSONObject();
									jsonObject.put("town", tds.get(0).text());
									jsonObject.put("village", tds.get(1).text());
									jsonObject.put("name", tds.get(2).text());
									resultArray.put(jsonObject);
								}
							}
							
							File myFile = new File("/sdcard/answer2.txt");
				            myFile.createNewFile();
				            FileOutputStream fOut = new FileOutputStream(myFile);
							OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fOut);
							outputStreamWriter.write(resultArray.toString());
							outputStreamWriter.close();
							fOut.close();
							
							getActivity().runOnUiThread(new Runnable() {

								@Override
								public void run() {
									answer.setText(resultArray.toString());
								}
							});

						} catch (IOException | JSONException e) {
							e.printStackTrace();
						}
					}
				}).start();
			}
		};

		View.OnClickListener lv3ClickListener = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				answer.setText("lv3");
				new Thread(new Runnable() {

					@Override
					public void run() {

						try {
							Document doc = null;
							Connection connection = Jsoup.connect(levels[2]);
							Elements trs = new Elements();

							do {
								Connection.Response response = connection.execute();
								connection.cookies(response.cookies());
								doc = response.parse();
								trs.addAll(doc.select("table tr").not(":first-child"));
								connection.url(levels[2] + "?page=next");
							} while (doc != null && doc.select("a[href=?page=next]").size() != 0);

							final JSONArray resultArray = new JSONArray();
							for (Element tr : trs) {
								Elements tds = tr.children();
								JSONObject jsonObject = new JSONObject();
								jsonObject.put("town", tds.get(0).text());
								jsonObject.put("village", tds.get(1).text());
								jsonObject.put("name", tds.get(2).text());
								resultArray.put(jsonObject);
							}

							File myFile = new File("/sdcard/answer3.txt");
				            myFile.createNewFile();
				            FileOutputStream fOut = new FileOutputStream(myFile);
							OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fOut);
							outputStreamWriter.write(resultArray.toString());
							outputStreamWriter.close();
							fOut.close();

							getActivity().runOnUiThread(new Runnable() {

								@Override
								public void run() {
									answer.setText(resultArray.toString());
								}
							});

						} catch (IOException | JSONException e) {
							e.printStackTrace();
						}
					}
				}).start();
			}
		};

		View.OnClickListener lv4ClickListener = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				answer.setText("lv4");
				new Thread(new Runnable() {

					@Override
					public void run() {
						Document doc, subdoc;

						try {
							doc = Jsoup.connect(levels[3])
									.userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
									.referrer(levels[3]).get();
							final JSONArray resultArray = new JSONArray();
							Elements as = doc.select("a");
							for (Element a : as) {
								String linkHref = a.attr("href");
								subdoc = Jsoup
										.connect(levels[3] + "/" + linkHref)
										.userAgent(
												"Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
										.referrer(levels[3]).get();
								Elements trs = subdoc.select("table tr").not(":first-child");
								for (Element tr : trs) {
									Elements tds = tr.children();
									JSONObject jsonObject = new JSONObject();
									jsonObject.put("town", tds.get(0).text());
									jsonObject.put("village", tds.get(1).text());
									jsonObject.put("name", tds.get(2).text());
									resultArray.put(jsonObject);
								}
							}

							File myFile = new File("/sdcard/answer4.txt");
				            myFile.createNewFile();
				            FileOutputStream fOut = new FileOutputStream(myFile);
							OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fOut);
							outputStreamWriter.write(resultArray.toString());
							outputStreamWriter.close();
							fOut.close();
							
							getActivity().runOnUiThread(new Runnable() {

								@Override
								public void run() {
									answer.setText(resultArray.toString());
								}
							});

						} catch (IOException | JSONException e) {
							e.printStackTrace();
						}
					}
				}).start();
			}
		};

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container, false);
			lv1 = (Button) rootView.findViewById(R.id.button1);
			lv1.setOnClickListener(lv1ClickListener);
			lv2 = (Button) rootView.findViewById(R.id.button2);
			lv2.setOnClickListener(lv2ClickListener);
			lv3 = (Button) rootView.findViewById(R.id.button3);
			lv3.setOnClickListener(lv3ClickListener);
			lv4 = (Button) rootView.findViewById(R.id.button4);
			lv4.setOnClickListener(lv4ClickListener);
			answer = (TextView) rootView.findViewById(R.id.answer);
			return rootView;
		}
	}

}
