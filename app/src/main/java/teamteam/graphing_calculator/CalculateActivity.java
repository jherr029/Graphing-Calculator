package teamteam.graphing_calculator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class CalculateActivity extends AppCompatActivity {

    String str = "";
    double result = 0, num1 = 0, num2 = 0;
    int sign = 0, flag = 0;
    View vi;
    EditText et_show;
    Button but_num[] = new Button[10];
    Button but_mark[] = new Button[14];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_show = (EditText) findViewById(R.id.et_show);
        et_show.setKeyListener(null);
        int i;
        int j;
        int[] btn_id = new int[] { R.id.btn_add, R.id.btn_sub, R.id.btn_mlt,
                R.id.btn_div, R.id.btn_equal, R.id.btn_spot, R.id.btn_sin,
                R.id.btn_cos, R.id.btn_tan, R.id.btn_cot, R.id.btn_square,
                R.id.btn_sqrt, R.id.clears, R.id.clearAll };
        for (i = 0; i < but_num.length; i++) {
            String btnid = "btn" + "_" + i;
            int resID = getResources().getIdentifier(btnid, "id",
                    "com.example.android_calculator");
            but_num[i] = (Button) findViewById(resID);
            but_num[i].setOnClickListener(new onClicklistener());
        }
        for (j = 0; j < but_mark.length; j++) {
            but_mark[j] = (Button) findViewById(btn_id[j]);
            but_mark[j].setOnClickListener(new onClicklistener());
        }
    }

    public double calculator() {
        switch (sign) {
            case 0:
                result = num2;
                break;
            case 1:
                result = num1 + num2;
                break;
            case 2:
                result = num1 - num2;
                break;
            case 3:
                result = num1 * num2;
                break;
            case 4:
                result = num1 / num2;
                break;
        }
        num1 = result;
        sign = 0;
        return result;
    }

    public void click_num(int num) {
        int o = 0;
        if (num == 0) {
            if (flag == 1) {
                str = "";
                str += 0;
                et_show.setText(str);
                flag = 0;
            } else {
                char ch1[];
                ch1 = str.toCharArray();
                if (!(ch1.length == 1 && ch1[0] == '0')) {
                    str += 0;
                    et_show.setText(str);
                }
            }
        } else {
            for (o = 1; o < 10; o++) {
                if (o == num) {
                    if (flag == 1) {
                        str = "";
                        str += o;
                        et_show.setText(str);
                        flag=0;
                    }else{
                        str+=o;
                        et_show.setText(str);
                    }
                }
            }
        }
    }
    public void click_mark(int num, View vv){
        if(str!=""){
            if(vi==but_mark[num]){
                sign=num+1;
            }else{
                num2=Double.parseDouble(str);
                calculator();
                str=""+result;
                et_show.setText(str);
                sign=num+1;
                flag=1;
                vi=vv;
            }
        }
    }
    class onClicklistener implements OnClickListener {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_0: {
                    click_num(0);
                    vi = v;
                    break;
                }
                case R.id.btn_1: {
                    click_num(1);
                    vi = v;
                    break;
                }
                case R.id.btn_2: {
                    click_num(2);
                    vi = v;
                    break;
                }
                case R.id.btn_3: {
                    click_num(3);
                    vi = v;
                    break;
                }
                case R.id.btn_4: {
                    click_num(4);
                    vi = v;
                    break;
                }
                case R.id.btn_5: {
                    click_num(5);
                    vi = v;
                    break;
                }
                case R.id.btn_6: {
                    click_num(6);
                    vi = v;
                    break;
                }
                case R.id.btn_7: {
                    click_num(7);
                    vi = v;
                    break;
                }
                case R.id.btn_8: {
                    click_num(8);
                    vi = v;
                    break;
                }
                case R.id.btn_9: {
                    click_num(9);
                    vi = v;
                    break;
                }
                case R.id.btn_add: {
                    click_mark(0,v);
                    break;
                }
                case R.id.btn_sub: {
                    click_mark(1,v);
                    break;
                }
                case R.id.btn_mlt: {
                    click_mark(2,v);
                    break;
                }
                case R.id.btn_div: {
                    click_mark(3,v);
                    break;
                }
                case R.id.btn_spot: {
                    str = str + ".";
                    et_show.setText(str);
                    break;
                }
                case R.id.btn_equal: {
                    if (str != "" && vi != but_mark[0] && vi != but_mark[1]
                            && vi != but_mark[2] && vi != but_mark[3]) {
                        num2 = Double.parseDouble(str);
                        calculator();
                        str = "" + result;
                        et_show.setText(str);
                        flag = 1;
                        vi = v;
                    }
                    break;
                }
                case R.id.btn_sqrt: {
                    if (str != "") {
                        num2 = Double.parseDouble(str);
                        result = Math.sqrt(num2);
                        str = "" + result;
                        et_show.setText(str);
                    }
                    break;
                }
                case R.id.btn_square: {
                    if (str != "") {
                        num2 = Double.parseDouble(str);
                        result = num2 * num2;
                        str = "" + result;
                        et_show.setText(str);
                    }
                    break;
                }
                case R.id.btn_sin: {
                    if (str != "") {
                        num2 = Double.parseDouble(str);
                        result = Math.sin(num2);
                        str = "" + result;
                        et_show.setText(str);
                    }
                    break;
                }
                case R.id.btn_cos: {
                    if (str != "") {
                        num2 = Double.parseDouble(str);
                        result = Math.cos(num2);
                        str = "" + result;
                        et_show.setText(str);
                    }
                    break;
                }
                case R.id.btn_tan: {
                    if (str != "") {
                        num2 = Double.parseDouble(str);
                        result = Math.tan(num2);
                        str = "" + result;
                        et_show.setText(str);
                    }
                    break;
                }
                case R.id.btn_cot: {
                    if (str != "") {
                        num2 = Double.parseDouble(str);
                        result = (1 / Math.tan(num2));
                        str = "" + result;
                        et_show.setText(str);
                    }
                    break;
                }
                case R.id.clears: {
                    str = "";
                    et_show.setText(str);
                    vi = v;
                    break;
                }
                case R.id.clearAll: {
                    num1 = 0.0;
                    num2 = 0;
                    result = 0.0;
                    str = "";
                    et_show.setText(str);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.add(0, 1, 1, "Exit");
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 1) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
