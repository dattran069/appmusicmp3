package com.example.appplaymusic.Fragments.TaiKhoanFragment;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appplaymusic.Commons.Common;
import com.example.appplaymusic.Database.Database;
import com.example.appplaymusic.Fragments.CanhanFragment.CanhanFragment;
import com.example.appplaymusic.Helper.HelpTools;
import com.example.appplaymusic.MainActivity.MainActivity;
import com.example.appplaymusic.Models.CreateUserResponse;
import com.example.appplaymusic.Models.SignInResponse;
import com.example.appplaymusic.Models.SignUpResponse;
import com.example.appplaymusic.Models.User;
import com.example.appplaymusic.R;
import com.example.appplaymusic.Services.APIService;
import com.example.appplaymusic.Services.DataService;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TaiKhoanFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TaiKhoanFragment extends Fragment implements TaiKhoanFragmentView{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TaiKhoanFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TaiKhoanFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TaiKhoanFragment newInstance(String param1, String param2) {
        TaiKhoanFragment fragment = new TaiKhoanFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    View view;
    CircleImageView circleImageViewAvar;
    TextView txtUserName;
    TextView txtHeader;
    TextView txtMessage;
    EditText edtConfirmPwd;
    Button btnDangKi;
    Button btnQuayLaiDangNhap;
    boolean isDangKi=false;
    LinearLayout logoutLinearlayout;
    private Toolbar toolbar;
    TaiKhoanFragmentPresenter taiKhoanFragmentPresenter;
    SharedPreferences sharedpreferences ;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedpreferences= getActivity().getSharedPreferences(Common.SHARED_PREFS, getActivity().MODE_PRIVATE);
        circleImageViewAvar=view.findViewById(R.id.crircleImageView_avar);
        txtUserName=view.findViewById(R.id.txt_userName);
        txtHeader=view.findViewById(R.id.txt_header);
        txtMessage=view.findViewById(R.id.txt_message);
        btnDangKi=view.findViewById(R.id.btnDangKi);
        btnQuayLaiDangNhap=view.findViewById(R.id.btn_backToDangNhap);
        edtConfirmPwd=view.findViewById(R.id.edt_re_pwd);
        logoutLinearlayout=view.findViewById(R.id.linearlayoutLogout);
        logoutLinearlayout.setOnClickListener(view1 -> Toast.makeText(getActivity(), "Logout!", Toast.LENGTH_SHORT).show());
        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Tài khoản cá nhân");
        taiKhoanFragmentPresenter=new TaiKhoanFragmentPresenter(this);
        LinearLayout LinearLayout_login=view.findViewById(R.id.LinearLayout_login);
        LinearLayout LinearLayout_content=view.findViewById(R.id.LinearLayout_content);
        ((MainActivity)getActivity()).setSupportActionBar(toolbar);
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        if(sharedpreferences.getString(Common.USERNAME_KEY, null)!=null) {
            txtUserName.setText(sharedpreferences.getString(Common.USERNAME_KEY, null));
        } else {
            LinearLayout_login.setVisibility(View.VISIBLE);
            LinearLayout_content.setVisibility(View.GONE);
        }

            EditText editTextName=view.findViewById(R.id.edt_name);
            EditText editTextPwd=view.findViewById(R.id.edt_pwd);
            Button btnDangNhap=view.findViewById(R.id.btn_dangnhap);
            btnDangNhap.setOnClickListener(View->{
                String name=editTextName.getText().toString().trim();
                String pwd=editTextPwd.getText().toString().trim();
                Log.d("edtName",name);
                Log.d("edtConfirmPwd",pwd);
                if(TextUtils.isEmpty(name)){
                    Toast.makeText(getActivity(), "Name empty", Toast.LENGTH_SHORT).show();
                    editTextName.requestFocus();
                }
                else if(TextUtils.isEmpty(pwd)){
                    Toast.makeText(getActivity(), "Pwd empty", Toast.LENGTH_SHORT).show();
                    editTextPwd.requestFocus();
                }
                Call<SignInResponse> call= null;
                try {
                    call = APIService.getService().login(name, HelpTools.toHexString(HelpTools.getSHA(pwd)));
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                call.enqueue(new Callback<SignInResponse>() {
                    @Override
                    public void onResponse(Call<SignInResponse> call, Response<SignInResponse> response) {
                        SignInResponse signInResponse=response.body();
                        User user= signInResponse.getUser();

                        if(signInResponse.getMessage().equals("OK")&&user!=null){
                            //đăng nhập thành công
                            Toast.makeText(getActivity(), "Dang nhap thanh cong", Toast.LENGTH_SHORT).show();

                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString(Common.USERNAME_KEY, user.getUserName());
                            Log.d("editor.putString",user.getUserName());
                            Log.d("editor.putString",user.getPassword());
                            editor.putString(Common.PASSWORD_KEY, user.getPassword());
                            editor.putInt(Common.USERID_KEY,user.getId());
                            Log.d("editor.putString", String.valueOf(user.getId()));
                            editor.apply();
                            LinearLayout_login.setVisibility(View.GONE);
                            LinearLayout_content.setVisibility(View.VISIBLE);
                            txtUserName.setText(user.getUserName());
                            txtMessage.setText("");
                            taiKhoanFragmentPresenter.loadPlaylistCanhan(user);
                        }
                        else {
                            if(signInResponse.getMessage().equals("OK"))  txtMessage.setText("Mật khẩu sai !");
                            else txtMessage.setText("Không tồn tại tài khoản "+name);
                        }

                    }

                    @Override
                    public void onFailure(Call<SignInResponse> call, Throwable t) {
                        Toast.makeText(getActivity(), "Dang nhap that bai", Toast.LENGTH_SHORT).show();
                        txtMessage.setText("Đăng nhập thất bại !");
                        Log.d("User login falied: ", t.getMessage());
                    }
                });
            });
            btnDangKi.setOnClickListener(View->{
                if(isDangKi){
                    String name=editTextName.getText().toString().trim();
                    String pwd=editTextPwd.getText().toString().trim();
                    String re_pwd=edtConfirmPwd.getText().toString().trim();
                    if(TextUtils.isEmpty(name)){
                        Toast.makeText(getActivity(), "Name empty", Toast.LENGTH_SHORT).show();
                        txtMessage.setText("chưa nhập tên đăng nhập");
                        editTextName.requestFocus();
                    }
                    else if(TextUtils.isEmpty(pwd)){
                        Toast.makeText(getActivity(), "Pwd empty", Toast.LENGTH_SHORT).show();
                        editTextPwd.requestFocus();
                        txtMessage.setText("chưa nhập mật khẩu");
                    }
                    else if(TextUtils.isEmpty(re_pwd)){
                        Toast.makeText(getActivity(), "re Pwd empty", Toast.LENGTH_SHORT).show();
                        edtConfirmPwd.requestFocus();
                        txtMessage.setText("chưa nhập xác nhận mật khẩu");
                    }
//                    Log.d("edtName",name);
//                    Log.d("edtConfirmPwd",pwd);
//                    Log.d("edtConfirmPwd",re_pwd);
                    if(pwd.equals(re_pwd)&&(!TextUtils.isEmpty(name))&&(!TextUtils.isEmpty(re_pwd))&&(!TextUtils.isEmpty(pwd))){
                        //save user tp database
                        try {
                            String passEncript=HelpTools.toHexString(HelpTools.getSHA(pwd));
                            Log.d("passEncript",passEncript);
                            User user=new User(name,passEncript);
                            JSONObject paramObject = new JSONObject();
                            paramObject.put("userName",name);
                            paramObject.put("password", passEncript);
                            Call<Integer> call=APIService.getService().createUser(name,passEncript);
                            call.enqueue(new Callback<Integer>() {
                                @Override
                                public void onResponse(Call<Integer> call, Response<Integer> response) {
                                    int id=response.body();
                                    //da ton tai userName
                                    if(id==-1) txtMessage.setText("Username đã tồn tại !");
                                    else {
                                        Toast.makeText(getActivity(), "thanh cong", Toast.LENGTH_SHORT).show();
                                        edtConfirmPwd.setVisibility(android.view.View.GONE);
                                        editTextPwd.setText("");
                                        btnDangNhap.setVisibility(android.view.View.VISIBLE);
                                        btnQuayLaiDangNhap.setVisibility(android.view.View.GONE);
                                    }
                                }

                                @Override
                                public void onFailure(Call<Integer> call, Throwable t) {
                                    Toast.makeText(getActivity(), "that bai", Toast.LENGTH_SHORT).show();
                                    Log.d("ly do bai",t.getMessage());
                                }
                            });

                        } catch (NoSuchAlgorithmException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                else{
                    isDangKi=true;
                    txtHeader.setText("Đăng kí");
                    editTextName.setText("");
                    editTextPwd.setText("");
                    edtConfirmPwd.setText("");
                    btnDangNhap.setVisibility(View.GONE);
                    btnQuayLaiDangNhap.setVisibility(android.view.View.VISIBLE);
                    edtConfirmPwd.setVisibility(android.view.View.VISIBLE);
                }

            });
        btnQuayLaiDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtConfirmPwd.setVisibility(View.GONE);
                btnQuayLaiDangNhap.setVisibility(View.GONE);
                isDangKi=false;
                txtHeader.setText("Đăng nhập");
                txtMessage.setText("");
            }
        });
        logoutLinearlayout.setOnClickListener(View->{
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.clear();
            editor.apply();
            LinearLayout_login.setVisibility(View.VISIBLE);
            LinearLayout_content.setVisibility(View.GONE);
        });

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menutaikhoan, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            ((MainActivity) getActivity()).replaceFragment(new CanhanFragment(),0);
        }
        if (id == R.id.trogiup) {
            Toast.makeText(getActivity(), "trogiup", Toast.LENGTH_SHORT).show();
        }
        if (id == R.id.display) {
            Toast.makeText(getActivity(), "display", Toast.LENGTH_SHORT).show();
        }
            return super.onOptionsItemSelected(item);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view= inflater.inflate(R.layout.fragment_tai_khoan, container,
                false);
        return view;
    }
}