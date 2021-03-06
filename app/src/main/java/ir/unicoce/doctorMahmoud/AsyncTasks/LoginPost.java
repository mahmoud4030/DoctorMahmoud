package ir.unicoce.doctorMahmoud.AsyncTasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cn.pedant.SweetAlert.SweetAlertDialog;
import ir.unicoce.doctorMahmoud.Classes.URLS;
import ir.unicoce.doctorMahmoud.Classes.Variables;
import ir.unicoce.doctorMahmoud.Interface.IWebserviceByTag;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Droid on 8/31/2016.
 */
public class LoginPost extends AsyncTask<Void,Void,String> {

    public Context context;
    private IWebserviceByTag delegate = null;
    public String username,password;
    SweetAlertDialog pDialog ;
    public String Url;
    public String Tag;

    public SharedPreferences prefs;
    public SharedPreferences.Editor editor;

    public LoginPost(Context context,IWebserviceByTag delegate,String username,String password,String Tag){
        this.context=context;
        this.delegate=delegate;
        this.username=username;
        this.password=password;
        this.Tag=Tag;

        this.Url= URLS.Login;
        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
    }

    @Override
    protected void onPreExecute() {
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("لطفا صبر کنید");
        pDialog.setCancelable(true);
        pDialog.show();
    }

    @Override
    protected String doInBackground(Void... voids) {
        Response response = null;
        String strResponse = "nothing_got";

        for(int i=0;i<=9;i++){
            try {
                OkHttpClient client = new OkHttpClient();
                RequestBody body = new FormBody.Builder()
                        .add("Token", Variables.TOKEN)
                        .add("Username",username)
                        .add("Password",password)
                        .build();
                Request request = new Request.Builder()
                        .url(this.Url)
                        .post(body)
                        .build();

                response = client.newCall(request).execute();
                strResponse= response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(response!=null) break;
        }
        return strResponse;
    }

    @Override
    protected void onPostExecute(String result) {
        pDialog.dismiss();

        if (result.equals("nothing_got")) {
            try {
                delegate.getError("NoData",this.Tag);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if(!result.startsWith("{")){
            // moshkel dare kollan
            try {
                delegate.getError("problem",this.Tag);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        else {

            try {
                JSONObject jsonObject=new JSONObject(result);
                int Type=jsonObject.getInt("Status");
                if(Type==1){
                    // save somthing into share preferences
                    prefs = context.getSharedPreferences("Login", MODE_PRIVATE);
                    editor = prefs.edit();

                    editor.putString("name",jsonObject.optString("FirstName"));
                    editor.putString("username",username);
                    editor.putString("phone",jsonObject.optString("PhoneNumber"));
                    editor.putString("email",jsonObject.optString("Email"));
                    editor.putString("password",password);
                    editor.putString("userid",jsonObject.optString("Id"));
                    editor.putBoolean("has_logined",true);
                    editor.apply();

                    delegate.getResult("",this.Tag);
                }
                else {
                    delegate.getError("user pass incorrect",this.Tag);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
