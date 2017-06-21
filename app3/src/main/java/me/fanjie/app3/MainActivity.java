package me.fanjie.app3;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.List;

import me.fanjie.app3.entity.Edge;
import me.fanjie.app3.entity.SideWall;
import me.fanjie.app3.entity.sign.Basin;
import me.fanjie.app3.mapping.MapHelper;
import me.fanjie.app3.mapping.initShape.BarShape;
import me.fanjie.app3.mapping.initShape.LShape;
import me.fanjie.app3.mapping.initShape.UShape;
import me.fanjie.app3.mapping.mapper.MapperCallback;

import static java.lang.Integer.parseInt;
import static me.fanjie.app3.ToastUtils.showToast;

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
        steps.add(MapHelper.MappingStep.ADD_POINT_SIGN);
        steps.add(MapHelper.MappingStep.ADD_LINE_SIGN);
        steps.add(MapHelper.MappingStep.DONE);
        titles.add("形状设计，选择顶点或边线开缺");
        titles.add("角度调整，选择顶点输入角度选择贴合角度的边");
        titles.add("尺寸调整，点击边线设置尺寸，选择两个顶点生成自由标注");
        titles.add("标注调整，拖动标注");
        titles.add("下垂挡水，选中边线添加");
        titles.add("添加灶中、盆中并确定位置");
        titles.add("添加断开线和落差线");
        titles.add("确认图纸");
        operations.add(findViewById(R.id.ll_form));
        operations.add(findViewById(R.id.ll_angel));
        operations.add(findViewById(R.id.ll_size));
        operations.add(findViewById(R.id.ll_label));
        operations.add(findViewById(R.id.ll_add_side_wall));
        operations.add(findViewById(R.id.ll_add_point_sign));
        operations.add(findViewById(R.id.ll_add_line_sign));
        operations.add(findViewById(R.id.ll_done));
        nextStep(null);
    }

    public void preStep(final View view) {
        if (stepPosition > 1) {
            new AlertDialog.Builder(this)
                    .setMessage("回退上一步将会丢失当前步的工作，确定吗？")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            stepPosition--;
                            setTitle(titles.get(--stepPosition));
                            operations.get(stepPosition).setVisibility(View.VISIBLE);
                            operations.get(stepPosition + 1).setVisibility(View.INVISIBLE);
                            mapHelper.preStep();
                            stepPosition++;
                        }
                    })
                    .show();

        }

    }

    public void nextStep(View view) {
        if (stepPosition < titles.size()) {
            if (!mapHelper.nextStep(steps.get(stepPosition))) {
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
        int angel = parseInt(angelStr);
        Edge.Direction direction = rgDir.getCheckedRadioButtonId() == R.id.rb_hor ? Edge.Direction.HOR : Edge.Direction.VER;
        if (mapHelper.getAngelApi().setAngel(angel, direction)) {
            etInputAngel.setText("");
        }
    }

    public void addHorLabel(View view) {
        mapHelper.getLabelApi().addHorVertexLabel();
    }

    public void addVerLabel(View view) {
        mapHelper.getLabelApi().addVerVertexLabel();
    }

    public void setLabelSize(View view) {
        String s = etInputLabelSize.getText().toString();
        if (!TextUtils.isEmpty(s) && mapHelper.getSizeApi().setSize(parseInt(s))) {
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
        mapHelper.getSideWallApi().addSideWallSlip();
    }

    public void addKitchen(View view) {
        final View root = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_add_kit_chen_layout, null);
        final LinearLayout llInput = (LinearLayout) root.findViewById(R.id.ll_input);
        final Switch stAddKitChen = (Switch) root.findViewById(R.id.st_add_kit_chen);
        stAddKitChen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                llInput.setVisibility(isChecked ? View.GONE : View.VISIBLE);
            }
        });
        AlertDialog.Builder inputDialog = new AlertDialog.Builder(MainActivity.this);
        inputDialog.setView(root);
        inputDialog.setTitle("添加灶中");
        inputDialog.setNegativeButton("取消", null);
        inputDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText etInputRAngel = (EditText) root.findViewById(R.id.et_input_r_angel);
                EditText etInputWidth = (EditText) root.findViewById(R.id.et_input_width);
                EditText etInputHeight = (EditText) root.findViewById(R.id.et_input_height);
                String rAngel = etInputRAngel.getText().toString();
                String width = etInputWidth.getText().toString();
                String height = etInputHeight.getText().toString();
                if (!JTextUtils.isEmpty(rAngel, width, height)) {
                    mapHelper.getPointSignApi().addKitchen(parseInt(rAngel), parseInt(width), parseInt(height));
                }

            }
        });
        inputDialog.show();

    }

    public void addBasin(View view) {
        final View root = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_add_basin_layout, null);
        final LinearLayout llInput = (LinearLayout) root.findViewById(R.id.ll_input);
        final Switch stAddBasin = (Switch) root.findViewById(R.id.st_add_basin);
        stAddBasin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                llInput.setVisibility(isChecked ? View.GONE : View.VISIBLE);
            }
        });
        AlertDialog.Builder inputDialog = new AlertDialog.Builder(MainActivity.this);
        inputDialog.setView(root);
        inputDialog.setTitle("添加盆中");
        inputDialog.setNegativeButton("取消", null);
        inputDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText etInputRAngel = (EditText) root.findViewById(R.id.et_input_r_angel);
                EditText etInputWidth = (EditText) root.findViewById(R.id.et_input_width);
                EditText etInputHeight = (EditText) root.findViewById(R.id.et_input_height);
                RadioGroup radioGroup = (RadioGroup) root.findViewById(R.id.rg_basin_type);
                Basin.Type type = radioGroup.getCheckedRadioButtonId() == R.id.rb_up ? Basin.Type.UP : Basin.Type.DOWN;
                String rAngel = etInputRAngel.getText().toString();
                String width = etInputWidth.getText().toString();
                String height = etInputHeight.getText().toString();
                if (!JTextUtils.isEmpty(rAngel, width, height)) {
                    mapHelper.getPointSignApi().addBasin(type, parseInt(rAngel), parseInt(width), parseInt(height));
                }

            }
        });
        inputDialog.show();
    }

    public void addSignVerLabel(View view) {
        mapHelper.getPointSignApi().addVerSignLabel();
    }

    public void addSignHorLabel(View view) {
        mapHelper.getPointSignApi().addHorSignLabel();
    }

    public void addHole(View view) {
        mapHelper.getPointSignApi().addHole();
    }

    public void addBreakLine(View view) {
        mapHelper.getLineSignApi().addBreakLine();
    }

    public void addDivideLine(View view) {
        mapHelper.getLineSignApi().addDivideLine();
    }

    public void mapperDone(View view) {
        // TODO: 2017/2/10 提交
    }

    private void simpleInputDialog(String title, final DialogInputCallback callback) {
        final View root = LayoutInflater.from(this).inflate(R.layout.dialog_input_layout, null);
        AlertDialog.Builder inputDialog = new AlertDialog.Builder(this);
        inputDialog.setView(root);
        inputDialog.setNegativeButton("取消", null);
        inputDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText etDialogInput = (EditText) root.findViewById(R.id.et_input);
                String s = etDialogInput.getText().toString();
                if (!TextUtils.isEmpty(s)) {
                    callback.input(parseInt(s));
                }
            }
        });

        inputDialog.setTitle(title);
        inputDialog.show();

    }


    private MapperCallback callback = new MapperCallback() {
        @Override
        public void onEdgeClick() {
            simpleInputDialog("请输入尺寸，然后选择一个顶点来适应尺寸", new DialogInputCallback() {
                @Override
                public void input(int input) {
                    mapHelper.getSizeApi().setSize(input);
                }
            });

        }

        @Override
        public void onSignLabelClick() {
            simpleInputDialog("输入距离", new DialogInputCallback() {
                @Override
                public void input(int input) {
                    mapHelper.getPointSignApi().setDistance(input);
                }
            });
        }

        SideWall.Type checkType;

        @Override
        public void onSideWallSlipClick(final SideWall.Type currentType) {
            final View root = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_set_side_wall_layout, null);
            final RadioGroup radioGroup = (RadioGroup) root.findViewById(R.id.rg_side_wall_type);
            final EditText etInput = (EditText) root.findViewById(R.id.et_input);

            radioGroup.check(currentType == SideWall.Type.DOWN ? R.id.rb_down : R.id.rb_up);
            final Switch stSetSize = (Switch) root.findViewById(R.id.st_set_size);
            stSetSize.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    etInput.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                }
            });
            AlertDialog.Builder inputDialog = new AlertDialog.Builder(MainActivity.this);
            inputDialog.setView(root);
            inputDialog.setTitle("分段设置");
            inputDialog.setNegativeButton("取消", null);
            inputDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    checkType = radioGroup.getCheckedRadioButtonId() == R.id.rb_down ? SideWall.Type.DOWN : SideWall.Type.UP;
                    String s = etInput.getText().toString();
                    if (etInput.isShown() && !TextUtils.isEmpty(s)) {
                        mapHelper.getSideWallApi().setSlipSideWall(parseInt(s), checkType);
                    } else {
                        mapHelper.getSideWallApi().setSlipSideWall(checkType);
                    }
                }
            });
            inputDialog.show();

        }
    };

    public void undo(View view) {
        mapHelper.getMapper().undo();
    }

    public void redo(View view) {
        mapHelper.getMapper().redo();
    }


    interface DialogInputCallback {
        void input(int input);
    }
}
