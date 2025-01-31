package uz.kholisbek.calculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.Group;

import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.materialswitch.MaterialSwitch;

import org.mariuszgromada.math.mxparser.Expression;

public class MainActivity extends AppCompatActivity {
    private MaterialSwitch btnSwitchLightOrDark; /* btn for changing light or dark theme */
    private View sectionScreen, sectionNum; /* back view of sectionScreen and sectionNum */
    private TextView resultNumber;/*  resultNumber = the section where result is displayed */
    private EditText operationsProcess;/* operationsProcessSection = the section where your math account books are displayed */
    private Group resultSection, calculateSection; /* to control visibility result section that has equals icon and resultNumber and calculate section*/
    private Group groupOfMathOperations, groupOfNumButtons;
    private View operationSection, mathOperationsSection; /* to control background math operation buttons */
    private ImageView equalsIcon;
    private ImageView backspace;

    private AppCompatButton btnOperationAC, btnOperationPlusMinus, btnBracket, btnOperationDivision, btnOperationMultiplication, btnOperationSubtraction, btnOperationAddition, btnOperationEquals; /* buttons for math operations */
    private AppCompatButton btnNumberOne, btnNumberTwo, btnNumberThree, btnNumberFour, btnNumberFive, btnNumberSix, btnNumberSeven, btnNumberEight, btnNumberNine, btnPoint, btnNumberZero, btnDoubleZero; /* buttons to write numbers */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        setMyTheme(btnSwitchLightOrDark.isChecked());

        btnSwitchLightOrDark.setOnClickListener(view -> {
            setMyTheme(((MaterialSwitch) view).isChecked());
        });

        operationsProcess.setShowSoftInputOnFocus(false);

        btnOperationAC.setOnClickListener(this::clearBtn);
        btnOperationPlusMinus.setOnClickListener(this::plusMinusBtn);
        btnBracket.setOnClickListener(this::btnBracket);
        btnNumberOne.setOnClickListener(this::oneBtn);
        btnNumberTwo.setOnClickListener(this::twoBtn);
        btnNumberThree.setOnClickListener(this::threeBtn);
        btnNumberFour.setOnClickListener(this::fourBtn);
        btnNumberFive.setOnClickListener(this::fiveBtn);
        btnNumberSix.setOnClickListener(this::sixBtn);
        btnNumberSeven.setOnClickListener(this::sevenBtn);
        btnNumberEight.setOnClickListener(this::eightBtn);
        btnNumberNine.setOnClickListener(this::nineBtn);
        btnPoint.setOnClickListener(this::pointBtn);
        btnNumberZero.setOnClickListener(this::zeroBtn);
        btnDoubleZero.setOnClickListener(this::doubleZeroBtn);
        btnOperationDivision.setOnClickListener(this::divisionBtn);
        btnOperationMultiplication.setOnClickListener(this::multiplicationBtn);
        btnOperationSubtraction.setOnClickListener(this::subtractionBtn);
        btnOperationAddition.setOnClickListener(this::additionBtn);
        btnOperationEquals.setOnClickListener(this::equalsBtn);
        backspace.setOnClickListener(this::backspaceBtn);


    }

    private void updateText(String strToAdd) {

        if (calculateSection.getVisibility() == View.GONE && (strToAdd.equals("÷") || strToAdd.equals("×") || strToAdd.equals("-") || strToAdd.equals("+")) && !resultNumber.getText().toString().equals("NaN")) {
            calculateSection.setVisibility(View.VISIBLE);
            operationsProcess.setText(resultNumber.getText().toString());
            operationsProcess.requestFocus();
            resultSection.setVisibility(View.GONE);
            operationsProcess.setSelection(resultNumber.getText().toString().length());
        } else if (calculateSection.getVisibility() == View.GONE) {
            calculateSection.setVisibility(View.VISIBLE);
            operationsProcess.setText("");
            operationsProcess.requestFocus();
            resultSection.setVisibility(View.GONE);
        }

        String oldStr = operationsProcess.getText().toString();
        int cursorPos = operationsProcess.getSelectionStart();/* cursor ning turgan joyini chap tarafdan hisoblab olib beradi */
        String leftStr = oldStr.substring(0, cursorPos);
        String rightStr = oldStr.substring(cursorPos);

        if (operationsProcess.getText().toString().equals("")) {
            operationsProcess.setText(strToAdd);
        } else {
            operationsProcess.setText(String.format("%s%s%s", leftStr, strToAdd, rightStr));
        }
        operationsProcess.setSelection(cursorPos + 1);
    }

    private void backspaceBtn(View view) {
        int cursorPos = operationsProcess.getSelectionStart();
        int textLen = operationsProcess.getText().toString().length();

        if (cursorPos != 0 && textLen != 0) {
            SpannableStringBuilder selection = (SpannableStringBuilder) operationsProcess.getText();
            selection.replace(cursorPos - 1, cursorPos, "");
            operationsProcess.setText(selection);
            operationsProcess.setSelection(cursorPos - 1);
        }
    }

    private void equalsBtn(View view) {
        String userExp = operationsProcess.getText().toString();

        userExp = userExp.replaceAll("÷", "/");
        userExp = userExp.replaceAll("×", "*");

        Expression exp = new Expression(userExp);
        String result = String.valueOf(exp.calculate());


        calculateSection.setVisibility(View.GONE);
        resultSection.setVisibility(View.VISIBLE);
        resultNumber.setText(result);

    }

    private void plusMinusBtn(View view) {
        updateText("(-");
        operationsProcess.setSelection(operationsProcess.getSelectionStart() + 1);
    }

    private void btnBracket(View view) {

        int cursorPos = operationsProcess.getSelectionStart();
        int openPar = 0;
        int closedPar = 0;
        int textLen = operationsProcess.getText().length();

        for (int i = 0; i < cursorPos; i++) {
            if (operationsProcess.getText().toString().charAt(i) == '(') {
                openPar++;
            } else if (operationsProcess.getText().toString().charAt(i) == ')') {
                closedPar++;
            }
        }

        if (closedPar == openPar || operationsProcess.getText().toString().charAt(textLen - 1) == '(') {
            updateText("(");
        } else if (closedPar < openPar) {
            updateText(")");
        }
    }

    private void clearBtn(View view) {
        calculateSection.setVisibility(View.VISIBLE);
        resultSection.setVisibility(View.GONE);
        operationsProcess.setText("");
        operationsProcess.requestFocus();
    }


    private void divisionBtn(View view) {
        String text = operationsProcess.getText().toString();
        int cursorPos = operationsProcess.getSelectionStart();

        if (!(text.equals("") || text.charAt(cursorPos - 1) == '×' || text.charAt(cursorPos - 1) == '-' || text.charAt(cursorPos - 1) == '÷' || text.charAt(cursorPos - 1) == '+' || text.charAt(cursorPos - 1) == '(')) {
            updateText("÷");
        }
    }

    private void multiplicationBtn(View view) {
        String text = operationsProcess.getText().toString();
        int cursorPos = operationsProcess.getSelectionStart();

        if (!(text.equals("") || text.charAt(cursorPos - 1) == '×' || text.charAt(cursorPos - 1) == '-' || text.charAt(cursorPos - 1) == '÷' || text.charAt(cursorPos - 1) == '+' || text.charAt(cursorPos - 1) == '(')) {
            updateText("×");
        }
    }

    private void subtractionBtn(View view) {
        String text = operationsProcess.getText().toString();
        int cursorPos = operationsProcess.getSelectionStart();

        if (!(text.equals("") || text.charAt(cursorPos - 1) == '×' || text.charAt(cursorPos - 1) == '-' || text.charAt(cursorPos - 1) == '÷' || text.charAt(cursorPos - 1) == '+' || text.charAt(cursorPos - 1) == '(')) {
            updateText("-");
        }
    }

    private void additionBtn(View view) {
        String text = operationsProcess.getText().toString();
        int cursorPos = operationsProcess.getSelectionStart();

        if (!(text.equals("") || text.charAt(cursorPos - 1) == '×' || text.charAt(cursorPos - 1) == '-' || text.charAt(cursorPos - 1) == '÷' || text.charAt(cursorPos - 1) == '+' || text.charAt(cursorPos - 1) == '(')) {
            updateText("+");
        }
    }

    private void oneBtn(View view) {
        updateText("1");
    }

    private void twoBtn(View view) {
        updateText("2");
    }

    private void threeBtn(View view) {
        updateText("3");
    }

    private void fourBtn(View view) {
        updateText("4");
    }

    private void fiveBtn(View view) {
        updateText("5");
    }

    private void sixBtn(View view) {
        updateText("6");
    }

    private void sevenBtn(View view) {
        updateText("7");
    }

    private void eightBtn(View view) {
        updateText("8");
    }

    private void nineBtn(View view) {
        updateText("9");
    }

    private void pointBtn(View view) {
        String text = operationsProcess.getText().toString();
        int cursorPos = operationsProcess.getSelectionStart();

        if (text.equals("")) {
            updateText("0.");
            operationsProcess.setSelection(operationsProcess.getSelectionStart() + 1);
        } else if (text.charAt(cursorPos - 1) != '.') {
            updateText(".");
        }
    }

    private void zeroBtn(View view) {
        if (!operationsProcess.getText().toString().equals("0"))
            updateText("0");
    }

    private void doubleZeroBtn(View view) {
        if (operationsProcess.getText().toString().equals("")) {
            updateText("0");
        } else if (!operationsProcess.getText().toString().equals("0")) {
            updateText("00");
            operationsProcess.setSelection(operationsProcess.getSelectionStart() + 1);
        }
    }


    private void setMyTheme(Boolean valueOfSwitch) {
        if (valueOfSwitch) { /* dark theme */
            sectionScreen.setBackgroundResource(R.drawable.back_screen_section_dark);
            sectionNum.setBackgroundResource(R.drawable.back_num_section_dark);
            operationsProcess.setTextColor(getResources().getColor(R.color.textColorOfOperationsProcess_dark));
            operationsProcess.setHintTextColor(getResources().getColor(R.color.textColorOfOperationsProcess_dark));
            resultNumber.setTextColor(getResources().getColor(R.color.textColorOfResultNumber_dark));
            equalsIcon.setImageResource(R.drawable.ic_equals_for_dark_theme);
            operationSection.setBackgroundResource(R.drawable.back_operations_section_dark);
            mathOperationsSection.setBackgroundResource(R.drawable.back_operations_section_dark);
        } else { /* light theme */
            sectionScreen.setBackgroundResource(R.drawable.back_screen_section_light);
            sectionNum.setBackgroundResource(R.drawable.back_num_section_light);
            operationsProcess.setTextColor(getResources().getColor(R.color.textColorOfOperationsProcess_light));
            operationsProcess.setHintTextColor(getResources().getColor(R.color.textColorOfOperationsProcess_light));
            resultNumber.setTextColor(getResources().getColor(R.color.textColorOfResultNumber_light));
            equalsIcon.setImageResource(R.drawable.ic_equals_for_light_theme);
            operationSection.setBackgroundResource(R.drawable.back_operations_section_light);
            mathOperationsSection.setBackgroundResource(R.drawable.back_operations_section_light);
        }
        changeTextColorOfButtons(valueOfSwitch);
        changeBackgroundOfNumberButtons(valueOfSwitch);
        changeBackgroundOfOperationButtons(valueOfSwitch);
    }

    private void changeBackgroundOfOperationButtons(Boolean valueOfSwitch) {
        if (valueOfSwitch) {/* dark theme */
            btnOperationAC.setBackgroundResource(R.drawable.back_unselected_operation_dark);
            btnOperationPlusMinus.setBackgroundResource(R.drawable.back_unselected_operation_dark);
            btnBracket.setBackgroundResource(R.drawable.back_unselected_operation_dark);
            btnOperationDivision.setBackgroundResource(R.drawable.back_unselected_operation_dark);
            btnOperationMultiplication.setBackgroundResource(R.drawable.back_unselected_operation_dark);
            btnOperationSubtraction.setBackgroundResource(R.drawable.back_unselected_operation_dark);
            btnOperationAddition.setBackgroundResource(R.drawable.back_unselected_operation_dark);
            btnOperationEquals.setBackgroundResource(R.drawable.back_unselected_operation_dark);
        } else {/* light theme */
            btnOperationAC.setBackgroundResource(R.drawable.back_unselected_operation_light);
            btnOperationPlusMinus.setBackgroundResource(R.drawable.back_unselected_operation_light);
            btnBracket.setBackgroundResource(R.drawable.back_unselected_operation_light);
            btnOperationDivision.setBackgroundResource(R.drawable.back_unselected_operation_light);
            btnOperationMultiplication.setBackgroundResource(R.drawable.back_unselected_operation_light);
            btnOperationSubtraction.setBackgroundResource(R.drawable.back_unselected_operation_light);
            btnOperationAddition.setBackgroundResource(R.drawable.back_unselected_operation_light);
            btnOperationEquals.setBackgroundResource(R.drawable.back_unselected_operation_light);
        }
    }

    private void changeBackgroundOfNumberButtons(Boolean valueOfSwitch) {
        if (valueOfSwitch) {/*dark theme*/
            btnNumberOne.setBackgroundResource(R.drawable.back_num_btn_dark);
            btnNumberTwo.setBackgroundResource(R.drawable.back_num_btn_dark);
            btnNumberThree.setBackgroundResource(R.drawable.back_num_btn_dark);
            btnNumberFour.setBackgroundResource(R.drawable.back_num_btn_dark);
            btnNumberFive.setBackgroundResource(R.drawable.back_num_btn_dark);
            btnNumberSix.setBackgroundResource(R.drawable.back_num_btn_dark);
            btnNumberSeven.setBackgroundResource(R.drawable.back_num_btn_dark);
            btnNumberEight.setBackgroundResource(R.drawable.back_num_btn_dark);
            btnNumberNine.setBackgroundResource(R.drawable.back_num_btn_dark);
            btnPoint.setBackgroundResource(R.drawable.back_num_btn_dark);
            btnNumberZero.setBackgroundResource(R.drawable.back_num_btn_dark);
            btnDoubleZero.setBackgroundResource(R.drawable.back_num_btn_dark);
        } else {/* light theme */
            btnNumberOne.setBackgroundResource(R.drawable.back_num_btn_light);
            btnNumberTwo.setBackgroundResource(R.drawable.back_num_btn_light);
            btnNumberThree.setBackgroundResource(R.drawable.back_num_btn_light);
            btnNumberFour.setBackgroundResource(R.drawable.back_num_btn_light);
            btnNumberFive.setBackgroundResource(R.drawable.back_num_btn_light);
            btnNumberSix.setBackgroundResource(R.drawable.back_num_btn_light);
            btnNumberSeven.setBackgroundResource(R.drawable.back_num_btn_light);
            btnNumberEight.setBackgroundResource(R.drawable.back_num_btn_light);
            btnNumberNine.setBackgroundResource(R.drawable.back_num_btn_light);
            btnPoint.setBackgroundResource(R.drawable.back_num_btn_light);
            btnNumberZero.setBackgroundResource(R.drawable.back_num_btn_light);
            btnDoubleZero.setBackgroundResource(R.drawable.back_num_btn_light);

        }
    }

    private void changeTextColorOfButtons(Boolean valueOfSwitch) {
        if (valueOfSwitch) {/* dark theme */
//            arithmetic operations
            btnOperationDivision.setTextColor(getResources().getColor(R.color.textColorOfButtons_dark));
            btnOperationMultiplication.setTextColor(getResources().getColor(R.color.textColorOfButtons_dark));
            btnOperationSubtraction.setTextColor(getResources().getColor(R.color.textColorOfButtons_dark));
            btnOperationAddition.setTextColor(getResources().getColor(R.color.textColorOfButtons_dark));
            btnOperationEquals.setTextColor(getResources().getColor(R.color.textColorOfButtons_dark));
//            operations
            btnOperationAC.setTextColor(getResources().getColor(R.color.textColorOfButtons_dark));
            btnOperationPlusMinus.setTextColor(getResources().getColor(R.color.textColorOfButtons_dark));
            btnBracket.setTextColor(getResources().getColor(R.color.textColorOfButtons_dark));
//            number buttons
            btnNumberOne.setTextColor(getResources().getColor(R.color.textColorOfButtons_dark));
            btnNumberTwo.setTextColor(getResources().getColor(R.color.textColorOfButtons_dark));
            btnNumberThree.setTextColor(getResources().getColor(R.color.textColorOfButtons_dark));
            btnNumberFour.setTextColor(getResources().getColor(R.color.textColorOfButtons_dark));
            btnNumberFive.setTextColor(getResources().getColor(R.color.textColorOfButtons_dark));
            btnNumberSix.setTextColor(getResources().getColor(R.color.textColorOfButtons_dark));
            btnNumberSeven.setTextColor(getResources().getColor(R.color.textColorOfButtons_dark));
            btnNumberEight.setTextColor(getResources().getColor(R.color.textColorOfButtons_dark));
            btnNumberNine.setTextColor(getResources().getColor(R.color.textColorOfButtons_dark));
            btnPoint.setTextColor(getResources().getColor(R.color.textColorOfButtons_dark));
            btnNumberZero.setTextColor(getResources().getColor(R.color.textColorOfButtons_dark));
            btnDoubleZero.setTextColor(getResources().getColor(R.color.textColorOfButtons_dark));

        } else {/* light theme */
//            arithmetic operations
            btnOperationDivision.setTextColor(getResources().getColor(R.color.textColorOfButtons_light));
            btnOperationMultiplication.setTextColor(getResources().getColor(R.color.textColorOfButtons_light));
            btnOperationSubtraction.setTextColor(getResources().getColor(R.color.textColorOfButtons_light));
            btnOperationAddition.setTextColor(getResources().getColor(R.color.textColorOfButtons_light));
            btnOperationEquals.setTextColor(getResources().getColor(R.color.textColorOfButtons_light));
//            operations
            btnOperationAC.setTextColor(getResources().getColor(R.color.textColorOfButtons_light));
            btnOperationPlusMinus.setTextColor(getResources().getColor(R.color.textColorOfButtons_light));
            btnBracket.setTextColor(getResources().getColor(R.color.textColorOfButtons_light));
//            number buttons
            btnNumberOne.setTextColor(getResources().getColor(R.color.textColorOfButtons_light));
            btnNumberTwo.setTextColor(getResources().getColor(R.color.textColorOfButtons_light));
            btnNumberThree.setTextColor(getResources().getColor(R.color.textColorOfButtons_light));
            btnNumberFour.setTextColor(getResources().getColor(R.color.textColorOfButtons_light));
            btnNumberFive.setTextColor(getResources().getColor(R.color.textColorOfButtons_light));
            btnNumberSix.setTextColor(getResources().getColor(R.color.textColorOfButtons_light));
            btnNumberSeven.setTextColor(getResources().getColor(R.color.textColorOfButtons_light));
            btnNumberEight.setTextColor(getResources().getColor(R.color.textColorOfButtons_light));
            btnNumberNine.setTextColor(getResources().getColor(R.color.textColorOfButtons_light));
            btnPoint.setTextColor(getResources().getColor(R.color.textColorOfButtons_light));
            btnNumberZero.setTextColor(getResources().getColor(R.color.textColorOfButtons_light));
            btnDoubleZero.setTextColor(getResources().getColor(R.color.textColorOfButtons_light));
        }
    }

    private void findViews() {
        btnSwitchLightOrDark = findViewById(R.id.btnSwitch);
        sectionScreen = findViewById(R.id.sectionScreen);
        sectionNum = findViewById(R.id.sectionNum);
        operationsProcess = findViewById(R.id.operationsProcess);
        resultNumber = findViewById(R.id.resultNumber);
        resultSection = findViewById(R.id.resultSection);
        operationSection = findViewById(R.id.operationSection);
        mathOperationsSection = findViewById(R.id.mathOperationsSection);
        btnOperationAC = findViewById(R.id.operationAC);
        btnOperationPlusMinus = findViewById(R.id.operationPlusMinus);
        btnBracket = findViewById(R.id.bracket);
        btnOperationDivision = findViewById(R.id.operationDivision);
        btnOperationMultiplication = findViewById(R.id.operationMultiplication);
        btnOperationSubtraction = findViewById(R.id.operationSubtraction);
        btnOperationAddition = findViewById(R.id.operationAddition);
        btnOperationEquals = findViewById(R.id.operationEquals);
        btnNumberOne = findViewById(R.id.btnNumberOne);
        btnNumberTwo = findViewById(R.id.btnNumberTwo);
        btnNumberThree = findViewById(R.id.btnNumberThree);
        btnNumberFour = findViewById(R.id.btnNumberFour);
        btnNumberFive = findViewById(R.id.btnNumberFive);
        btnNumberSix = findViewById(R.id.btnNumberSix);
        btnNumberSeven = findViewById(R.id.btnNumberSeven);
        btnNumberEight = findViewById(R.id.btnNumberEight);
        btnNumberNine = findViewById(R.id.btnNumberNine);
        btnPoint = findViewById(R.id.btnPoint);
        btnNumberZero = findViewById(R.id.btnNumberZero);
        btnDoubleZero = findViewById(R.id.btnDoubleZero);
        equalsIcon = findViewById(R.id.equalsIcon);
        groupOfMathOperations = findViewById(R.id.groupOfMathOperationButtons);
        groupOfNumButtons = findViewById(R.id.groupOfNumberButtons);
        backspace = findViewById(R.id.backspace);
        calculateSection = findViewById(R.id.calculateSection);
    }
}