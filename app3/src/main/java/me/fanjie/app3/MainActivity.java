package me.fanjie.app3;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

import me.fanjie.app3.entity.Edge;
import me.fanjie.app3.entity.SideWall;
import me.fanjie.app3.entity.Vertex;
import me.fanjie.app3.mapping.Interface.MapperCallback;
import me.fanjie.app3.mapping.MapHelper;
import me.fanjie.app3.mapping.initShape.BarShape;
import me.fanjie.app3.mapping.initShape.LShape;
import me.fanjie.app3.mapping.initShape.UShape;

import static me.fanjie.app3.ToastUtils.showToast;
import static me.fanjie.app3.entity.Label.Type.HOR;
import static me.fanjie.app3.entity.Label.Type.VER;

public class MainActivity extends AppCompatActivity {

    private Panel panel;
    private RadioGroup rgDir;
    private EditText etInputAngel;
    private EditText etInputLabelSize;

    private MapHelper mapHelper;

    private List<MapHelper.MappingStep> steps;
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
        etInputLabelSize = (EditText) findViewById(R.id.et_input_label_size);
        mapHelper = new MapHelper(panel, callback);
        steps = new ArrayList<>();
        titles = new ArrayList<>();
//        操作
        operations = new ArrayList<>();
        steps.add(MapHelper.MappingStep.FORM);
        steps.add(MapHelper.MappingStep.ANGEL);
        steps.add(MapHelper.MappingStep.LABEL_SIZE);
        steps.add(MapHelper.MappingStep.LABEL_DRAG);
        steps.add(MapHelper.MappingStep.SIDE_WALL);
        steps.add(MapHelper.MappingStep.ADD_SIGN);
        titles.add("形状设计，选择顶点或边线开缺");
        titles.add("角度调整，选择顶点输入角度选择贴合角度的边");
        titles.add("尺寸调整，点击边线设置尺寸，选择两个顶点生成自由标注");
        titles.add("标注调整，拖动标注");
        titles.add("下垂挡水，选中边线添加");
        titles.add("添加标记，选择边线设置挡水和下垂");
        operations.add(findViewById(R.id.ll_form));
        operations.add(findViewById(R.id.ll_angel));
        operations.add(findViewById(R.id.ll_size));
        operations.add(findViewById(R.id.ll_label));
        operations.add(findViewById(R.id.ll_add_side_wall));
        operations.add(findViewById(R.id.ll_add_sign));
        nextStep(null);


        rgDir.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioCheckId = checkedId;
            }
        });
    }

    public void nextStep(View view) {
        if (stepPosition < titles.size()) {
            if (!mapHelper.setStep(steps.get(stepPosition))) {
                showToast("请先完成当前步骤");
                return;
            }
            setTitle(titles.get(stepPosition));
            operations.get(stepPosition).setVisibility(View.VISIBLE);
            if (stepPosition > 0) {
                operations.get(stepPosition - 1).setVisibility(View.GONE);
            }
            stepPosition++;
        }
    }

    public void addBarShape(View view) {
        mapHelper.getFormApi().addShape(new BarShape(panel));
    }

    public void addLShape(View view) {
        mapHelper.getFormApi().addShape(new LShape(panel));
    }

    public void addUShape(View view) {
        mapHelper.getFormApi().addShape(new UShape(panel));
    }

    public void addGap(View view) {
        mapHelper.getFormApi().addGap();
    }

    public void setAngel(View view) {
        String angelStr = etInputAngel.getText().toString();
        if (TextUtils.isEmpty(angelStr)) {
            return;
        }
        int angel = Integer.parseInt(angelStr);
        Edge.Direction direction = radioCheckId == R.id.rb_hor ? Edge.Direction.HOR : Edge.Direction.VER;
        if (mapHelper.getAngelApi().setAngel(angel, direction)) {
            etInputAngel.setText("");
        }
    }

    public void addHorLabel(View view) {
        mapHelper.getLabelApi().addVertexLabel(HOR);
    }

    public void addVerLabel(View view) {
        mapHelper.getLabelApi().addVertexLabel(VER);
    }

    public void setLabelSize(View view) {
        String s = etInputLabelSize.getText().toString();
        if (!TextUtils.isEmpty(s) && mapHelper.getSizeApi().setSize(Integer.parseInt(s))) {
            etInputLabelSize.setText("");
        }
    }

    public void addDownWall(View view) {
        mapHelper.getSideWallApi().addSideWall(SideWall.Type.DOWN);
    }

    public void addUpWall(View view) {
        mapHelper.getSideWallApi().addSideWall(SideWall.Type.UP);
    }


    public void slipUpDown(View view) {

    }

    public void addKitchen(View view) {
        mapHelper.getSignApi().addKitchen();
    }

    public void addBasin(View view) {
        mapHelper.getSignApi().addBasin();
    }

    private MapperCallback callback = new MapperCallback() {
        @Override
        public void onEdgeClick(Edge edge) {

            showInputDialog("请输入尺寸，然后选择一个顶点来适应尺寸", new DialogInputCallback() {
                @Override
                public void input(int i) {
                    mapHelper.getSizeApi().setSize(input);
                }
            });

        }

        @Override
        public void onEdgeVertexClick(Edge edge, Vertex vertex) {

        }
    };

    private AlertDialog.Builder inputDialog;
    private int input;

    private void showInputDialog(String title, final DialogInputCallback callback) {
        final EditText editText = new EditText(this);
        editText.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED);
        inputDialog = new AlertDialog.Builder(this);
        inputDialog.setView(editText);
        inputDialog.setTitle(title);
        inputDialog.setNegativeButton("取消", null);
        inputDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String s = editText.getText().toString();
                if (!TextUtils.isEmpty(s)) {
                    input = Integer.parseInt(s);
                    callback.input(0);
                }
            }
        });

        inputDialog.show();

    }


    interface DialogInputCallback {
        void input(int input);
    }
}
