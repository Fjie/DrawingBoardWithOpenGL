package me.fanjie.app3;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

import me.fanjie.app3.test.mapping.Edge;
import me.fanjie.app3.test.mapping.Interface.IMapperAngelApi;
import me.fanjie.app3.test.mapping.Interface.IMapperFormApi;
import me.fanjie.app3.test.mapping.Interface.IMapperLabelApi;
import me.fanjie.app3.test.mapping.Mapper;
import me.fanjie.app3.test.mapping.shape.BarShape;
import me.fanjie.app3.test.mapping.shape.LShape;
import me.fanjie.app3.test.mapping.shape.UShape;

import static me.fanjie.app3.ToastUtils.showToast;
import static me.fanjie.app3.test.mapping.Label.Type.HOR;
import static me.fanjie.app3.test.mapping.Label.Type.VER;

public class MainActivity extends AppCompatActivity {

    private Panel panel;
    private RadioGroup rgDir;
    private EditText etInputAngel;

    private Mapper mapper;
    private IMapperFormApi formApi;
    private IMapperAngelApi angelApi;
    private IMapperLabelApi labelApi;

    private List<Mapper.MappingStep> steps;
    private List<String> titles;
    private List<View> operations;
    private int stepPosition;
    private int radioCheckId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        panel = (Panel) findViewById(R.id.panel);
        rgDir = (RadioGroup) findViewById(R.id.rg_direction);
        etInputAngel = (EditText) findViewById(R.id.et_input_angel);
        mapper = new Mapper(panel);
        steps = new ArrayList<>();
        titles = new ArrayList<>();
//        操作
        operations = new ArrayList<>();
        steps.add(Mapper.MappingStep.FORM);
        steps.add(Mapper.MappingStep.ANGEL);
        steps.add(Mapper.MappingStep.SIZE_LABEL);
        steps.add(Mapper.MappingStep.LABEL_DRAG);
        titles.add("形状设计");
        titles.add("角度调整");
        titles.add("尺寸调整");
        titles.add("标注调整");
        operations.add(findViewById(R.id.ll_form));
        operations.add(findViewById(R.id.ll_angel));
        operations.add(findViewById(R.id.ll_size));
        operations.add(findViewById(R.id.ll_label));
        nextStep(null);
        formApi = mapper;
        angelApi = mapper;
        labelApi = mapper;

        rgDir.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioCheckId = checkedId;
            }
        });
    }

    public void nextStep(View view) {
        if(stepPosition < titles.size()){
            if(!mapper.setStep(steps.get(stepPosition))){
                showToast("请先完成当前步骤");
                return;
            }
            setTitle(titles.get(stepPosition));
            operations.get(stepPosition).setVisibility(View.VISIBLE);
            if(stepPosition>0){
                operations.get(stepPosition-1).setVisibility(View.GONE);
            }
            stepPosition ++;
        }
    }


    public void addBarShape(View view) {
        mapper.addShape(new BarShape(panel));
    }

    public void addLShape(View view) {
        mapper.addShape(new LShape(panel));

    }

    public void addUShape(View view) {
        mapper.addShape(new UShape(panel));
    }


    public void addGap(View view) {
        formApi.addGap();
    }



    public void setAngel(View view) {
        String angelStr = etInputAngel.getText().toString();
        if(TextUtils.isEmpty(angelStr)){
            return;
        }
        etInputAngel.setText("");
        etInputAngel.clearFocus();
        int angel = Integer.parseInt(angelStr);
        Edge.Direction direction = radioCheckId == R.id.rb_hor ? Edge.Direction.HOR : Edge.Direction.VER;
        angelApi.setAngel(angel, direction);
    }

    public void addEdgeLabel(View view) {
        labelApi.addEdgeLabel();
    }

    public void addHorLabel(View view) {
        labelApi.addVertexLabel(HOR);
    }

    public void addVerLabel(View view) {
        labelApi.addVertexLabel(VER);
    }
}
