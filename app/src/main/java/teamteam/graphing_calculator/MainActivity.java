package teamteam.graphing_calculator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.EditText;

//test

public class MainActivity extends AppCompatActivity {

    String str="";
    double result=0, oprand1=0, oprand2=0;
    int sign=0, flag=0;
    View vi;
    EditText et_show;
    Button but_0,but_1,but_2,but_3,but_4,but_5,but_6,but_7;
    Button but_8,but_9,but_add,but_sub,but_mlt,but_div,but_spot;
    Button but_sin,but_cos,but_tan,but_cot,but_equal,but_sqrt,but_square;
    Button clears,clearAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_show=(EditText)findViewById(R.id.et_show);
        et_show.setKeyListener(null);
        but_0=(Button)findViewById(R.id.btn_0);
        but_1=(Button)findViewById(R.id.btn_1);
        but_2=(Button)findViewById(R.id.btn_2);
        but_3=(Button)findViewById(R.id.btn_3);
        but_4=(Button)findViewById(R.id.btn_4);
        but_5=(Button)findViewById(R.id.btn_5);
        but_6=(Button)findViewById(R.id.btn_6);
        but_7=(Button)findViewById(R.id.btn_7);
        but_8=(Button)findViewById(R.id.btn_8);
        but_9=(Button)findViewById(R.id.btn_9);

        but_add=(Button)findViewById(R.id.btn_add);
        but_sub=(Button)findViewById(R.id.btn_sub);
        but_mlt=(Button)findViewById(R.id.btn_mlt);
        but_div=(Button)findViewById(R.id.btn_div);
        but_spot=(Button)findViewById(R.id.btn_spot);
        but_equal=(Button)findViewById(R.id.btn_equal);
        but_sin=(Button)findViewById(R.id.btn_sin);
        but_cos=(Button)findViewById(R.id.btn_cos);
        but_tan=(Button)findViewById(R.id.btn_tan);
        but_cot=(Button)findViewById(R.id.btn_cot);
        but_square=(Button)findViewById(R.id.btn_square);
        but_sqrt=(Button)findViewById(R.id.btn_sqrt);
        clears=(Button)findViewById(R.id.clears);
        clearAll=(Button)findViewById(R.id.clearAll);

        but_0.setOnClickListener(new onclicklistener());
        but_1.setOnClickListener(new onclicklistener());
        but_2.setOnClickListener(new onclicklistener());
        but_3.setOnClickListener(new onclicklistener());
        but_4.setOnClickListener(new onclicklistener());
        but_5.setOnClickListener(new onclicklistener());
        but_6.setOnClickListener(new onclicklistener());
        but_7.setOnClickListener(new onclicklistener());
        but_8.setOnClickListener(new onclicklistener());
        but_9.setOnClickListener(new onclicklistener());
        but_add.setOnClickListener(new onclicklistener());
        but_sub.setOnClickListener(new onclicklistener());
        but_mlt.setOnClickListener(new onclicklistener());
        but_div.setOnClickListener(new onclicklistener());
        but_spot.setOnClickListener(new onclicklistener());
        but_sin.setOnClickListener(new onclicklistener());
        but_cos.setOnClickListener(new onclicklistener());
        but_tan.setOnClickListener(new onclicklistener());
        but_cot.setOnClickListener(new onclicklistener());
        but_equal.setOnClickListener(new onclicklistener());
        but_sqrt.setOnClickListener(new onclicklistener());
        but_square.setOnClickListener(new onclicklistener());
        clears.setOnClickListener(new onclicklistener());
        clearAll.setOnClickListener(new onclicklistener());
    }

    public double calculator(){
        switch(sign){
            case 0:
                result=oprand2;
                break;
            case 1:
                result=oprand1+oprand2;
                break;
            case 2:
                result=oprand1-oprand2;
                break;
            case 3:
                result=oprand1*oprand2;
                break;
            case 4:
                result=oprand1/oprand2;
                break;
        }
        oprand1=result;
        sign=0;
        return result;
    }
    class onclicklistener implements OnClickListener{
        public void onClick(View v){
            switch(v.getId()){
                case R.id.btn_0:
                {
                    if(flag==1){
                        str="";
                        str+=0;
                        et_show.setText(str);
                        flag=0;
                    }else{
                        char ch1[];
                        ch1=str.toCharArray();
                        if(!(ch1.length==1 && ch1[0]=='0')){
                            str+=0;
                            et_show.setText(str);
                        }
                    }
                    vi=v;
                    break;
                }
                case R.id.btn_1:{
                    if(flag==1){
                        str="";
                        str+=1;
                        et_show.setText(str);
                        flag=0;
                    }else{
                        str+=1;
                        et_show.setText(str);
                    }
                    vi=v;
                    break;
                }
                case R.id.btn_2:{
                    if(flag==1){
                        str="";
                        str+=2;
                        et_show.setText(str);
                        flag=0;
                    }else{
                        str+=2;
                        et_show.setText(str);
                    }
                    vi=v;
                    break;
                }
                case R.id.btn_3:{
                    if(flag==1){
                        str="";
                        str+=3;
                        et_show.setText(str);
                        flag=0;
                    }else{
                        str+=3;
                        et_show.setText(str);
                    }
                    vi=v;
                    break;
                }
                case R.id.btn_4:{
                    if(flag==1){
                        str="";
                        str+=4;
                        et_show.setText(str);
                        flag=0;
                    }else{
                        str+=4;
                        et_show.setText(str);
                    }
                    vi=v;
                    break;
                }
                case R.id.btn_5:{
                    if(flag==1){
                        str="";
                        str+=5;
                        et_show.setText(str);
                        flag=0;
                    }else{
                        str+=5;
                        et_show.setText(str);
                    }
                    vi=v;
                    break;
                }
                case R.id.btn_6:{
                    if(flag==1){
                        str="";
                        str+=6;
                        et_show.setText(str);
                        flag=0;
                    }else{
                        str+=6;
                        et_show.setText(str);
                    }
                    vi=v;
                    break;
                }
                case R.id.btn_7:{
                    if(flag==1){
                        str="";
                        str+=7;
                        et_show.setText(str);
                        flag=0;
                    }else{
                        str+=7;
                        et_show.setText(str);
                    }
                    vi=v;
                    break;
                }
                case R.id.btn_8:{
                    if(flag==1){
                        str="";
                        str+=8;
                        et_show.setText(str);
                        flag=0;
                    }else{
                        str+=8;
                        et_show.setText(str);
                    }
                    vi=v;
                    break;
                }
                case R.id.btn_9:{
                    if(flag==1){
                        str="";
                        str+=9;
                        et_show.setText(str);
                        flag=0;
                    }else{
                        str+=9;
                        et_show.setText(str);
                    }
                    vi=v;
                    break;
                }
                case R.id.btn_add:{
                    if(str!=""){
                        if(vi==but_add){
                            sign=1;
                        }else{
                            oprand2=Double.parseDouble(str);  //oprand2=3;
                            calculator();
                            str=""+result;
                            et_show.setText(str);
                            sign=1;
                            flag=1;
                            vi=v;
                        }

                    }
                    break;
                }
                case R.id.btn_sub:{
                    if(str!=""){
                        if(vi==but_sub){
                            sign=2;
                        }else{
                            oprand2=Double.parseDouble(str);
                            calculator();
                            str=""+result;
                            et_show.setText(str);
                            sign=2;
                            flag=1;
                            vi=v;
                        }
                    }
                    break;
                }
                case R.id.btn_mlt:{
                    if(str!=""){
                        if(vi==but_mlt){
                            sign=3;
                        }else{
                            oprand2=Double.parseDouble(str);
                            calculator();
                            str=""+result;
                            et_show.setText(str);
                            flag=1;
                            sign=3;
                            vi=v;
                        }
                    }
                    break;
                }
                case R.id.btn_div:{
                    if(str!=""){
                        if(vi==but_div){
                            sign=4;
                        }else{
                            oprand2=Double.parseDouble(str);
                            calculator();
                            str=""+result;
                            et_show.setText(str);
                            flag=1;
                            sign=4;
                            vi=v;
                        }
                    }
                    break;
                }
                case R.id.btn_spot:{
                    str=str+".";
                    et_show.setText(str);
                    break;
                }
                case R.id.btn_equal:{
                    if(str!=""&&vi!=but_add&&vi!=but_sub&&vi!=but_mlt&&vi!=but_div){
                        oprand2=Double.parseDouble(str);
                        calculator();
                        str=""+result;
                        et_show.setText(str);
                        flag=1;
                        vi=v;
                    }
                    break;
                }
                case R.id.btn_sqrt:{
                    if(str!=""){
                        oprand2=Double.parseDouble(str);
                        result=Math.sqrt(oprand2);
                        str=""+result;
                        et_show.setText(str);
                    }
                    break;
                }
                case R.id.btn_square:{
                    if(str!=""){
                        oprand2=Double.parseDouble(str);
                        result=oprand2*oprand2;
                        str=""+result;
                        et_show.setText(str);
                    }
                    break;
                }
                case R.id.btn_sin:{
                    if(str!=""){
                        oprand2=Double.parseDouble(str);
                        result=Math.sin(oprand2);
                        str=""+result;
                        et_show.setText(str);
                    }
                    break;
                }
                case R.id.btn_cos:{
                    if(str!=""){
                        oprand2=Double.parseDouble(str);
                        result=Math.cos(oprand2);
                        str=""+result;
                        et_show.setText(str);
                    }
                    break;
                }
                case R.id.btn_tan:{
                    if(str!=""){
                        oprand2=Double.parseDouble(str);
                        result=Math.tan(oprand2);
                        str=""+result;
                        et_show.setText(str);
                    }
                    break;
                }
                case R.id.btn_cot:{
                    if(str!=""){
                        oprand2=Double.parseDouble(str);
                        result=(1/Math.tan(oprand2));
                        str=""+result;
                        et_show.setText(str);
                    }
                    break;
                }
                case R.id.clears:{
                    str="";
                    et_show.setText(str);
                    vi=v;
                    break;
                }
                case R.id.clearAll:{
                    oprand1=0.0;oprand2=0;result=0.0;
                    str="";
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
