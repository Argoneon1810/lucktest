package ark.noah.lucktest;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.slider.RangeSlider;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import ark.noah.lucktest.RandomModule.ClassRandomModule;
import ark.noah.lucktest.RandomModule.MathRandomModule;
import ark.noah.lucktest.RandomModule.RandomModule;
import ark.noah.lucktest.databinding.FragmentFirstBinding;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;

    Drawable ic_collapse, ic_expand;

    RandomModule randomModule;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);

        ic_collapse = requireContext().getDrawable(R.drawable.ic_up_arrow);
        ic_expand = requireContext().getDrawable(R.drawable.ic_down_arrow);

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeRecyclerView();
        initializeCollapsableLayout();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void initializeRecyclerView() {
        binding.rec.setLayoutManager(new GridLayoutManager(requireContext(), 10));
        MyAdapter adapter = new MyAdapter(new ArrayList<>());
        adapter.setOnItemClickListener((v, pos) -> {
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_layout, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setView(dialogView);
            RecyclerItem item = adapter.get(pos);
            ((TextView) dialogView.findViewById(R.id.txt_indiv)).setText(getText(item.isIndiv ? R.string.txt_yes: R.string.txt_no));
            ((TextView) dialogView.findViewById(R.id.txt_thisval)).setText(String.valueOf(item.givenValue));
            ((TextView) dialogView.findViewById(R.id.txt_range)).setText(
                    String.format(
                            Locale.KOREA,
                            "%s%f~%f%s",
                            item.headInclusive ? "[" : "(",
                            item.valueRangedFrom,
                            item.valueRangedTo,
                            item.tailInclusive ? "]" : ")"
                    )
            );
            builder.create().show();
            ((TextView) dialogView.findViewById(R.id.txt_mode)).setText(item.randomMode.toString());
            ((TextView) dialogView.findViewById(R.id.txt_result)).setText(item.result() ? getText(R.string.txt_pass) : getText(R.string.txt_fail));
        });
        binding.rec.setAdapter(adapter);
    }

    private void initializeCollapsableLayout() {
        binding.btnColExp.setOnClickListener(this::collapseAndExpandDetailsView);

        initializeRadioButtonGroup();
        initializeTestRunButtons();
        initializeSliders();
        initializeEditTexts();
    }

    private void initializeRadioButtonGroup() {
        randomModule = new ClassRandomModule();
        binding.rbtnClassRandom.setOnClickListener(this::onRadioButtonClicked);
        binding.rbtnMathRandom.setOnClickListener(this::onRadioButtonClicked);
    }

    private void initializeTestRunButtons() {
        binding.btnOnce.setOnClickListener(v -> {
            tryout(randomModule.getValue() * 100, true);
        });

        binding.btnMass.setOnClickListener(v -> {
            float val = 0;
            int num = (int)binding.slider.getValue();
            for(int i = 0; i < num; ++i) {
                tryout(randomModule.getValue() * 100);
            }
        });
    }

    private void initializeSliders() {
        binding.rangeslider.setLabelFormatter(String::valueOf);
        binding.rangeslider.addOnSliderTouchListener(new RangeSlider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull RangeSlider slider) { }
            @Override
            public void onStopTrackingTouch(@NonNull RangeSlider slider) {
                List<Float> values = slider.getValues();
                Editable editableMin = Objects.requireNonNull(binding.etxtMin.getEditText()).getText();
                Editable editableMax = Objects.requireNonNull(binding.etxtMax.getEditText()).getText();
                editableMin.replace(0, editableMin.length(), String.valueOf(values.get(0)));
                editableMax.replace(0, editableMax.length(), String.valueOf(values.get(1)));
            }
        });
        binding.btnApplyValueToSlider.setOnClickListener(this::applyGivenValuesToSlider);
    }

    private void initializeEditTexts() {
        TextWatcher tw = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() == 0) return;
                if(editable.length() == 1 && editable.charAt(0)=='-') {
                    editable.replace(0, editable.length(), "");
                    return;
                }

                float val = Float.parseFloat(editable.toString());
                boolean changed = false;
                if(val > 100) {
                    while(val > 100) {
                        val /= 10;
                    }
                    changed = true;
                }
                if(val < 0) {
                    val *= -1;
                    changed = true;
                }
                if(changed) editable.replace(0, editable.length(), String.valueOf(val));
            }
        };
        Objects.requireNonNull(binding.etxtMin.getEditText()).addTextChangedListener(tw);
        Objects.requireNonNull(binding.etxtMax.getEditText()).addTextChangedListener(tw);
    }

    private void onRadioButtonClicked(View v) {
        if (!((RadioButton) v).isChecked()) return;

        try {
            randomModule.release();
        } catch (Throwable e) {
            e.printStackTrace();
        }

        switch(v.getId()) {
            case R.id.rbtn_class_random:
                randomModule = new ClassRandomModule();
                break;
            case R.id.rbtn_math_random:
                randomModule = new MathRandomModule();
                break;
        }
    }

    private void applyGivenValuesToSlider(View v) {
        String minString = Objects.requireNonNull(binding.etxtMin.getEditText()).getText().toString();
        String maxString = Objects.requireNonNull(binding.etxtMax.getEditText()).getText().toString();
        float min = Float.parseFloat(minString.length() == 0 ? "0" : minString);
        float max = Float.parseFloat(maxString.length() == 0 ? "0" : maxString);
        binding.rangeslider.setValues(min, max);
    }

    float dp2px(float dp) {
        return dp * ((float) requireContext().getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    float px2dp(float px) {
        return px / ((float) requireContext().getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    boolean isInRange(float from, float to, float testcase, boolean leftInclusive, boolean rightInclusive) {
        return (leftInclusive ? testcase >= from : testcase > from) && (rightInclusive ? testcase <= to : testcase < to);
    }

    private void collapseAndExpandDetailsView(View v) {
        ViewGroup.LayoutParams colExpParams = binding.layoutCollapsable.getLayoutParams();
        ViewGroup.LayoutParams spacerParams = binding.spacer.getLayoutParams();
        if (colExpParams.height < 0) {
            ((MaterialButton) binding.btnColExp).setIcon(ic_expand);
            colExpParams.height = 0;
            spacerParams.height = (int) dp2px(24);
        } else {
            ((MaterialButton) binding.btnColExp).setIcon(ic_collapse);
            colExpParams.height = -2;
            spacerParams.height = 0;
        }
        binding.layoutCollapsable.setLayoutParams(colExpParams);
        binding.spacer.setLayoutParams(spacerParams);
    }

    void tryout(float val) {
        tryout(val, false);
    }
    void tryout(float val, boolean isIndiv) {
        List<Float> values = binding.rangeslider.getValues();
        float from = values.get(0);
        float to = values.get(1);

        MyAdapter adapter = (MyAdapter) binding.rec.getAdapter();
        assert adapter != null;

        if(isInRange(from, to, val, true, true))
            adapter.add(new RecyclerItem(RecyclerItem.pass, isIndiv, val, from, to, true, true, randomModule.getRandomMode()));
        else
            adapter.add(new RecyclerItem(RecyclerItem.fail, isIndiv, val, from, to, true, true, randomModule.getRandomMode()));
    }
}