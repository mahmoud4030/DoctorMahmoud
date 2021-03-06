package ir.unicoce.doctorMahmoud.Activity;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cocosw.bottomsheet.BottomSheet;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ir.unicoce.doctorMahmoud.AsyncTasks.LoginPost;
import ir.unicoce.doctorMahmoud.AsyncTasks.SignUpPost;
import ir.unicoce.doctorMahmoud.Classes.Internet;
import ir.unicoce.doctorMahmoud.Classes.ROOTS;
import ir.unicoce.doctorMahmoud.Classes.ResideMenu;
import ir.unicoce.doctorMahmoud.Classes.ResideMenuItem;
import ir.unicoce.doctorMahmoud.Classes.Variables;
import ir.unicoce.doctorMahmoud.Fragment.ImageSliderFragment;
import ir.unicoce.doctorMahmoud.Interface.IWebserviceByTag;
import ir.unicoce.doctorMahmoud.Interface.OnFragmentInteractionListener;
import ir.unicoce.doctorMahmoud.R;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity
        implements
        View.OnClickListener,
        IWebserviceByTag,
        OnFragmentInteractionListener
{
    EditText edtUsername ;
    EditText edtName ;
    EditText edtPhone;
    EditText edtEmail ;
    EditText edtPassword ;

    public Handler handler;
    Animation rotateCardViewAnimation;
    boolean firstCards;
    ResideMenu resideMenu;
    private ResideMenuItem itemEnteghadat;
    private ResideMenuItem itemFavorites;
    private ResideMenuItem itemSupport;
    private ResideMenuItem itemIntroduceToOthers;
    private ResideMenuItem itemDarookhane;
    public SharedPreferences prefs;
    public SharedPreferences.Editor editor;
    private Typeface San;
    View snack_view;
    private String DName, DPhone, DEmail,DPassword, DUsername;
    private static final String SignUpTag="SignUpTag";
    private static final String LoginTag="LoginTag";
    private FragmentManager fragmentManager;
    private FragmentTransaction ft;

    @BindView(R.id.mainactivity_4cardview)
    LinearLayout mainactivity4cardview;
    @BindView(R.id.card_view1_image)
    ImageView cardView1Image;
    @BindView(R.id.card_view1_text)
    TextView cardView1Text;
    @BindView(R.id.card_view2_image)
    ImageView cardView2Image;
    @BindView(R.id.card_view2_text)
    TextView cardView2Text;
    @BindView(R.id.card_view3_image)
    ImageView cardView3Image;
    @BindView(R.id.card_view3_text)
    TextView cardView3Text;
    @BindView(R.id.card_view4_image)
    ImageView cardView4Image;
    @BindView(R.id.card_view4_text)
    TextView cardView4Text;
    @BindView(R.id.activity_main)
    LinearLayout activityMain;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        init();
        setFragment();
        RootMaker();
    }

    private void init() {
        San = Typeface.createFromAsset(getAssets(), "fonts/SansLight.ttf");
        setMainTable();
        rotateCardViewAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_animation);
        firstCards = true;

        resideMenu = new ResideMenu(this);
        resideMenu.setUse3D(true);
        resideMenu.setBackground(R.drawable.menu_background);
        resideMenu.attachToActivity(this);
        //valid scale factor is between 0.0f and 1.0f. leftmenu'width is 150dip.
//        resideMenu.setScaleValue(0.6f);

        // create menu items;

        itemFavorites = new ResideMenuItem(this, R.drawable.ic_favorites,R.string.favorites);
        itemEnteghadat= new ResideMenuItem(this, R.drawable.ic_suggestion, R.string.enteghadat);
        itemIntroduceToOthers = new ResideMenuItem(this, R.drawable.ic_introduce, R.string.introduce_to_others);
        itemDarookhane=new ResideMenuItem(this, R.drawable.ic_pharmacy,R.string.darookhane);
        itemSupport = new ResideMenuItem(this, R.drawable.ic_support, R.string.support);


        itemFavorites.setOnClickListener(this);
        itemEnteghadat.setOnClickListener(this);
        itemIntroduceToOthers.setOnClickListener(this);
        itemDarookhane.setOnClickListener(this);
        itemSupport.setOnClickListener(this);

        resideMenu.addMenuItem(itemFavorites, ResideMenu.DIRECTION_RIGHT);
        resideMenu.addMenuItem(itemEnteghadat, ResideMenu.DIRECTION_RIGHT);
        resideMenu.addMenuItem(itemIntroduceToOthers, ResideMenu.DIRECTION_RIGHT);
        resideMenu.addMenuItem(itemDarookhane, ResideMenu.DIRECTION_RIGHT);
        resideMenu.addMenuItem(itemSupport, ResideMenu.DIRECTION_RIGHT);

        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_LEFT);

    }
    /*set ImageSlider fragment*/
    protected void setFragment() {

        fragmentManager = getSupportFragmentManager();
        ImageSliderFragment myFragment = new ImageSliderFragment();

        ft = fragmentManager.beginTransaction();
        ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        ft.add(R.id.frame, myFragment);
        ft.commit();

    }// end setFragment()

    private void setMainTable() {

    }

    @OnClick({R.id.card_view1, R.id.card_view2, R.id.card_view3, R.id.card_view4, R.id.mainactivity_4cardview
            ,R.id.drawer_icon})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.mainactivity_4cardview:
                mainactivity4cardview.startAnimation(rotateCardViewAnimation);
                switchCards();
                break;
            case R.id.card_view1:
                if (firstCards) {
                    showBottomsheetFirstCard();
                } else {
                    startActivity(new Intent(MainActivity.this,CommonQuestionsActivity.class));
                }
                break;
            case R.id.card_view2:
                if (firstCards) {
                    showBottomsheetSecondCard();
                } else {
                    startActivity(new Intent(MainActivity.this,NewsActivity.class));
                }
                break;
            case R.id.card_view3:
                if (firstCards) {
//                    showBottomsheetThirdCard();
                    checkForLogin();
                } else {
                    startActivity(new Intent(MainActivity.this,VideosActivity.class));
                }
                break;
            case R.id.card_view4:
                if (firstCards) {
                    showBottomsheetFourthCard();
                } else {
                    startActivity(new Intent(MainActivity.this,ImagesActivity.class));
                }
                break;
            case R.id.drawer_icon:
                resideMenu.openMenu(ResideMenu.DIRECTION_RIGHT);
                break;
        }
//////////////////////////// for menu Items //////////////////////////////////////////////////////////
        if (view == itemFavorites) {
            startActivity(new Intent(MainActivity.this,FavoriteActivity.class));
            resideMenu.closeMenu();
        } else if (view == itemEnteghadat) {
            startActivity(new Intent(MainActivity.this,SendIdeasActivity.class));
            resideMenu.closeMenu();
        } else if (view == itemIntroduceToOthers) {
            sendToOthers();
            resideMenu.closeMenu();
        } else if (view == itemDarookhane) {
         // TODO : add drug store
            resideMenu.closeMenu();
        }
        else if (view == itemSupport) {
            startActivity(new Intent(MainActivity.this,SupportActivity.class));
            resideMenu.closeMenu();
        }
    }

    private void sendToOthers() {
        // معرفی به دوستان
        try{
            ArrayList<Uri> uris = new ArrayList<>();
            Intent sendIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
            sendIntent.setType("application/*");
            uris.add(Uri.fromFile(new File(getApplicationInfo().publicSourceDir)));
            sendIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
            startActivity(Intent.createChooser(sendIntent, null));
        }catch(Exception e){e.printStackTrace();}
    }

    private void checkForLogin() {
        prefs = getSharedPreferences("Login", MODE_PRIVATE);
        if ( prefs.getBoolean("has_logined",false)) {
            showBottomsheetThirdCard();
        } else {
            DialogLogin();
        }
    }

    private void DialogLogin(){
        final Dialog d = new Dialog(this);
        d.setCancelable(true);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setContentView(R.layout.dialog_login);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = d.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        final TextView txtTitleLogin = (TextView) d.findViewById(R.id.txtTitle_dialoglogin);
        final TextInputLayout till = (TextInputLayout) d.findViewById(R.id.til1_dialoglogin);
        final TextInputLayout till2 = (TextInputLayout) d.findViewById(R.id.til2_dialoglogin);
        final EditText edtlogin = (EditText) d.findViewById(R.id.edtLogin_dialoglogin);
        final EditText edtPassword = (EditText) d.findViewById(R.id.edtLoginPassword_dialoglogin);
        final Button btnLogin = (Button) d.findViewById(R.id.btnLogin_dialoglogin);
        final Button btnShowReg = (Button) d.findViewById(R.id.btnRegShow_dialoglogin);

        till.setTypeface(San);
        till2.setTypeface(San);
        txtTitleLogin.setTypeface(San);
        edtlogin.setTypeface(San);
        btnLogin.setTypeface(San);


        btnShowReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.dismiss();
                DialogRegister();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String DUsername = edtlogin.getText().toString();
                String DPass = edtPassword.getText().toString();
                if(DUsername.length()<1){
                    Toast.makeText(MainActivity.this, "تعداد کاراکترها کم است.", Toast.LENGTH_SHORT).show();
                }
                else if(DPass.length()<3){
                    Toast.makeText(MainActivity.this, "تعداد کاراکترهای رمز عبور غیر مجاز است.", Toast.LENGTH_SHORT).show();
                }
                else{
                    //AskSever(false);
                    if (Internet.isNetworkAvailable(MainActivity.this)) {
                        LoginPost post = new LoginPost(MainActivity.this,MainActivity.this,DUsername,DPass,LoginTag);
                        post.execute();
                        d.dismiss();
                    } else {
                        Snackbar snackbar = Snackbar
                                .make(findViewById(R.id.activity_main),"دسترسی اینترنت را بررسی نمایید", Snackbar.LENGTH_LONG);
                        snack_view = snackbar.getView();
                        snack_view.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        TextView tv = (TextView) snack_view.findViewById(android.support.design.R.id.snackbar_text);
                        tv.setTextColor(Color.WHITE);
                        snackbar.show();
                    }
                }
            }
        });

        d.show();

    }// end DialogLogin()

    public void DialogRegister(){
        final Dialog d = new Dialog(this);
        d.setCancelable(true);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setContentView(R.layout.dialog_register);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = d.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        Button btnCommit,btnCancel;
        TextInputLayout till = (TextInputLayout) d.findViewById(R.id.til1_dialogreg);
        TextInputLayout til2 = (TextInputLayout) d.findViewById(R.id.til2_dialogreg);
        TextInputLayout til3 = (TextInputLayout) d.findViewById(R.id.til3_dialogreg);
        TextInputLayout til4 = (TextInputLayout) d.findViewById(R.id.til4_dialogreg);
        TextInputLayout til5 = (TextInputLayout) d.findViewById(R.id.til5_dialogreg);
        edtUsername = (EditText) d.findViewById(R.id.edtUsername_dialogreg);
        edtName = (EditText) d.findViewById(R.id.edtName_dialogreg);
        edtPhone = (EditText) d.findViewById(R.id.edtPhone_dialogreg);
        edtEmail = (EditText) d.findViewById(R.id.edtEmail_dialogreg);
        edtPassword = (EditText) d.findViewById(R.id.edtEmail_dialogreg);

        btnCommit = (Button) d.findViewById(R.id.btnCommit_dialogreg);
        btnCancel = (Button) d.findViewById(R.id.btnCancel_dialogreg);

        edtName.setTypeface(San);
        edtPhone.setTypeface(San);
        edtEmail.setTypeface(San);
        edtUsername.setTypeface(San);
        btnCancel.setTypeface(San);
        btnCommit.setTypeface(San);
        til2.setTypeface(San);
        til3.setTypeface(San);
        til4.setTypeface(San);
        til5.setTypeface(San);
        till.setTypeface(San);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.dismiss();
            }
        });

        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DUsername = edtUsername.getText().toString();
                DName = edtName.getText().toString();
                DPhone = edtPhone.getText().toString();
                DEmail = edtEmail.getText().toString();
                DPassword = edtPassword.getText().toString();

                if(        DUsername.equals("")
                        || DName.equals("")
                        || DPhone.equals("")
                        || DEmail.equals("")
                        || !DEmail.contains("@")
                        ){
                    Toast.makeText(MainActivity.this, "لطفا اطلاعات خواسته شده را به طور صحیح وارد کنید", Toast.LENGTH_SHORT).show();

                }else{
                    //AskSever(true);
                    DUsername = edtUsername.getText().toString();
                    DName = edtName.getText().toString();
                    DPhone = edtPhone.getText().toString();
                    DEmail = edtEmail.getText().toString();
                    DPassword = edtPassword.getText().toString();

                    if(Internet.isNetworkAvailable(MainActivity.this)){
                        d.dismiss();
                        SignUpPost post = new SignUpPost(MainActivity.this,MainActivity.this,DName,DUsername,DPhone,DEmail,DPassword,SignUpTag);
                        post.execute();
                    }
                    else {
                        // show net error
                        Snackbar snackbar = Snackbar
                                .make(findViewById(R.id.activity_main),"دسترسی اینترنت را بررسی نمایید", Snackbar.LENGTH_LONG);
                        snack_view = snackbar.getView();
                        snack_view.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        TextView tv = (TextView) snack_view.findViewById(android.support.design.R.id.snackbar_text);
                        tv.setTextColor(Color.WHITE);
                        snackbar.show();
                    }
                }
            }
        });

        d.show();
    }// end DialogRegister()

    private void showBottomsheetFirstCard() {
        new BottomSheet.Builder(this,R.style.BottomSheet_StyleDialog).title(R.string.aboutus).icon(R.drawable.ic_aboutus)
                .sheet(R.menu.menu_aboutus).grid()
                .listener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        switch (which){
                            case R.id.aboutus_about:
                                startActivity(new Intent(MainActivity.this,AboutUsActivity.class));
                                break;
                            case R.id.aboutus_departmants:
                                startActivity(new Intent(MainActivity.this,PartsActivity.class));
                                break;
                            case R.id.aboutus_social_network:
                                show_social_dialog();
                                break;
                            case R.id.aboutus_tamas_bama:
                                startActivity(new Intent(MainActivity.this,ContactUsActivity.class));
                                break;
                        }

                    }
                }).show();
    }

    private void showBottomsheetSecondCard() {
        new BottomSheet.Builder(this,R.style.BottomSheet_StyleDialog).title(R.string.services).icon(R.drawable.ic_services)
                .sheet(R.menu.menu_services).grid()
                .listener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        switch (which){
                            case R.id.services_introduce_services:
                                startActivity(new Intent(MainActivity.this,ServicesActivity.class));
                                break;
                            case R.id.services_insurances:
                                startActivity(new Intent(MainActivity.this,InsuranceActivity.class));
                                break;
                        }
                    }
                }).show();
    }

    private void showBottomsheetThirdCard() {
        new BottomSheet.Builder(this,R.style.BottomSheet_StyleDialog).title(R.string.club).icon(R.drawable.ic_club)
                .sheet(R.menu.menu_club).grid()
                .listener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        switch (which){
                            case R.id.club_reservation:
                                if (Internet.isNetworkAvailable(MainActivity.this)) {
                                    startActivity(new Intent(MainActivity.this,ReservationActivity.class));
                                }
                                else Toast.makeText(MainActivity.this, R.string.error_internet, Toast.LENGTH_SHORT).show();

                                break;
//                            case R.id.club_match:
//                                Toast.makeText(MainActivity.this, "clicked", Toast.LENGTH_SHORT).show();
//                                break;
                            case R.id.club_poll:
                                startActivity(new Intent(MainActivity.this,PollActivity.class));
                                break;
                            case R.id.club_moshavere:
                                startActivity(new Intent(MainActivity.this,ChatsActivity.class));
                                break;
                            case R.id.club_chat:
                                openTelegramChannel(Variables.Telegram_chat_Id);
                                break;
                        }
                    }
                }).show();
    }

    private void showBottomsheetFourthCard() {
        new BottomSheet.Builder(this,R.style.BottomSheet_StyleDialog).title(R.string.danestaniha).icon(R.drawable.ic_danestaniha)
                .sheet(R.menu.menu_danestaniha).grid()
                .listener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        switch (which){
                            case R.id.danestabniha_magazines:
                                startActivity(new Intent(MainActivity.this,MagazineActivity.class));
                                break;
                            case R.id.danestabniha_darooha:
                                startActivity(new Intent(MainActivity.this,DrugsActivity.class));
                                break;
                            case R.id.danestabniha_befor_operation:
                                Intent intent = new Intent(MainActivity.this,CareActivity.class);
                                intent.putExtra("isCareAfter",false);
                                startActivity(intent);
                                break;
                            case R.id.danestabniha_after_operation:
                                Intent intent2 = new Intent(MainActivity.this,CareActivity.class);
                                intent2.putExtra("isCareAfter",true);
                                startActivity(intent2);
                                break;

                        }
                    }
                }).show();
    }

    private void switchCards() {
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // switch images and texts
                if (firstCards) {
                    firstCards = false;
                    cardView1Image.setImageResource(R.drawable.ic_common_questions);
                    cardView1Text.setText(R.string.common_questions);

                    cardView2Image.setImageResource(R.drawable.ic_news);
                    cardView2Text.setText(R.string.news);

                    cardView3Image.setImageResource(R.drawable.ic_video_gallery);
                    cardView3Text.setText(R.string.gallery_video);

                    cardView4Image.setImageResource(R.drawable.ic_image_gallery);
                    cardView4Text.setText(R.string.gallery_image);
                } else {
                    firstCards = true;
                    cardView1Image.setImageResource(R.drawable.ic_about_us);
                    cardView1Text.setText(R.string.aboutus);

                    cardView2Image.setImageResource(R.drawable.ic_services);
                    cardView2Text.setText(R.string.services);

                    cardView3Image.setImageResource(R.drawable.ic_club);
                    cardView3Text.setText(R.string.club);

                    cardView4Image.setImageResource(R.drawable.ic_danestaniha);
                    cardView4Text.setText(R.string.danestaniha);
                }

            }
        }, 200);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return resideMenu.dispatchTouchEvent(ev);
    }

    @Override
    public void onBackPressed() {
        if(resideMenu.isOpened()){
            resideMenu.closeMenu();
        }
        else {
            moveTaskToBack(true);
            finish();
            System.exit(0);
        }
    }

    public  void show_social_dialog(){
        final Dialog d = new Dialog(this);
        d.setCancelable(true);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setContentView(R.layout.linear_choose_dialog_3taee);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = d.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        final LinearLayout linear_1=(LinearLayout)d.findViewById(R.id.dialog_linear_1);
        final LinearLayout linear_12=(LinearLayout)d.findViewById(R.id.dialog_linear_2);
        final LinearLayout linear_13=(LinearLayout)d.findViewById(R.id.dialog_linear_3);

        final TextView txtOne= (TextView) d.findViewById(R.id.dilog_text_1);
        final TextView txtTWo = (TextView) d.findViewById(R.id.dilog_text_2);
        final TextView txtThree = (TextView) d.findViewById(R.id.dilog_text_3);

        txtOne.setText("کانال تلگرام");
        txtTWo.setText("اینستاگرام");
        txtThree.setText("وب سایت");

        linear_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
                openTelegramChannel(Variables.Telegram_Channel_Id);

            }
        });

        linear_12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
                openInstagramPage();

            }
        });

        linear_13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Variables.WebSite_Link));
                startActivity(browserIntent);

            }
        });

        d.show();
    }

    public static boolean isAppAvailable(Context context, String appName) {
        PackageManager pm = context.getPackageManager();
        try
        {
            pm.getPackageInfo(appName, PackageManager.GET_ACTIVITIES);
            return true;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            return false;
        }
    }

    private void openTelegramChannel(String TelegramId) {
        final String appName = "org.telegram.messenger";
        final boolean isAppInstalled = isAppAvailable(getApplicationContext(), appName);
        if (isAppInstalled) {
            Intent telegram = new Intent(android.content.Intent.ACTION_VIEW);
            telegram.setData(Uri.parse("https://telegram.me/"+TelegramId));
            telegram.setPackage("org.telegram.messenger");
            MainActivity.this.startActivity(Intent.createChooser(telegram, ""));
        }
        else {
            Snackbar snackbar = Snackbar
                    .make(findViewById(R.id.activity_main), "اپلیکیشن تلگرام یافت نشد", Snackbar.LENGTH_LONG);

            snack_view = snackbar.getView();
            snack_view.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            TextView tv = (TextView) snack_view.findViewById(android.support.design.R.id.snackbar_text);
            tv.setTextColor(Color.WHITE);
            snackbar.show();
        }
    }

    private void openInstagramPage() {

        Uri uri = Uri.parse("http://instagram.com/_u/xxx");
        Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

        likeIng.setPackage("com.instagram.android");

        try {
            startActivity(likeIng);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://instagram.com/xxx")));

        }
    }

    @Override
    public void getResult(Object result, String Tag) throws Exception {

        prefs = getSharedPreferences("Login", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        switch (Tag){
            case "LoginTag":

                Toast.makeText(this, "با موفقیت وارد شدید", Toast.LENGTH_SHORT).show();
                // baz kardane bottom sheet
                showBottomsheetThirdCard();
                break;
            case "SignUpTag":

                // save sign up things
                editor.putString("name",edtName.getText().toString());
                editor.putString("username",edtUsername.getText().toString());
                editor.putString("phone",edtPhone.getText().toString());
                editor.putString("email",edtEmail.getText().toString());
                editor.putString("password",edtPassword.getText().toString());
                editor.putString("userid", (String) result);
                editor.putBoolean("has_logined",true);
                editor.apply();

                Toast.makeText(this, "ثبت نام با موفقیت انجام شد", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void getError(String ErrorCodeTitle, String Tag) throws Exception {

        switch (Tag){
            case "LoginTag":
                if(ErrorCodeTitle.equals("user pass incorrect"))
                    Toast.makeText(this, "اطلاعات وارد شده صحیح نیست", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "مشکلی پیش آمده است", Toast.LENGTH_SHORT).show();
                break;
            case "SignUpTag":
                if(ErrorCodeTitle.equals("signup faild"))
                    Toast.makeText(this, "ثبت نام انجام نشد", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "مشکلی پیش آمده است", Toast.LENGTH_SHORT).show();
                break;
        }

    }

    @Override
    public void onFragmentInteraction(int tagNumber) {

    }

    private void RootMaker() {

        File root   = new File(ROOTS.ROOT_DIR);
        File video  = new File(ROOTS.VIDEOS);
        File voice  = new File(ROOTS.VOICES);
        File pdf    = new File(ROOTS.PDFS);
        File image  = new File(ROOTS.IMAGES);


        if(!root.exists()){
            root.mkdir();
            video.mkdir();
            voice.mkdir();
            pdf.mkdir();
            image.mkdir();
        }

    }// end RootMaker()

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}// end class
