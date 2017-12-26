import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.util.HashMap;
import java.util.List;

public class Instruction {

    private static HashMap<String, Integer> labelAddrMap;

    public static final String OPR_ADD          = "ADD";             public static final String BIN_ADD = "0000000001";
    public static final String OPR_AND          = "AND";             public static final String BIN_AND = "0000000010";
    public static final String OPR_SUB          = "SUB";             public static final String BIN_SUB = "0000000011";
    public static final String OPR_OR           = "OR";               public static final String BIN_OR = "0000000100";
    public static final String OPR_XOR          = "XOR";             public static final String BIN_XOR = "0000000101";
    public static final String OPR_MOV          = "MOV";             public static final String BIN_MOV = "0000000110";
    public static final String OPR_ADC          = "ADC";             public static final String BIN_ADC = "0000000111";
    public static final String OPR_NOT          = "NOT";             public static final String BIN_NOT = "0000001000";
    public static final String OPR_SAR          = "SAR";             public static final String BIN_SAR = "0000001001";
    public static final String OPR_SLR          = "SLR";             public static final String BIN_SLR = "0000001010";
    public static final String OPR_SAL          = "SAL";             public static final String BIN_SAL = "0000001011";
    public static final String OPR_SLL          = "SLL";             public static final String BIN_SLL = "0000001100";
    public static final String OPR_ROL          = "ROL";             public static final String BIN_ROL = "0000001101";
    public static final String OPR_ROR          = "ROR";             public static final String BIN_ROR = "0000001110";
    public static final String OPR_INC          = "INC";             public static final String BIN_INC = "0000001111";
    public static final String OPR_DEC          = "DEC";             public static final String BIN_DEC = "0000010000";
    public static final String OPR_NOP          = "NOP";             public static final String BIN_NOP = "0000000000";
    public static final String OPR_ShowR        = "ShowR";         public static final String BIN_ShowR = "0000010010";
    public static final String OPR_ShowRR       = "ShowRR";       public static final String BIN_ShowRR = "0000010011";
    public static final String OPR_LoadDipR     = "LoadDipR";   public static final String BIN_LoadDipR = "0000010100";
    public static final String OPR_LoadDipRR    = "LoadDipRR"; public static final String BIN_LoadDipRR = "0000010101";
    public static final String OPR_CMP          = "CMP";             public static final String BIN_CMP = "0000010110";

    public static final String OPR_JE           = "JE";               public static final String BIN_JE = "10000";
    public static final String OPR_JB           = "JB";               public static final String BIN_JB = "10001";
    public static final String OPR_JA           = "JA";               public static final String BIN_JA = "10010";
    public static final String OPR_JL           = "JL";               public static final String BIN_JL = "10011";
    public static final String OPR_JG           = "JG";               public static final String BIN_JG = "10100";
    public static final String OPR_JMP          = "JMP";             public static final String BIN_JMP = "10101";
    public static final String OPR_LI           = "LI";               public static final String BIN_LI = "10110";
    public static final String OPR_LM           = "LM";               public static final String BIN_LM = "10111";
    public static final String OPR_ShowDM       = "ShowDM";       public static final String BIN_ShowDM = "11000";
    public static final String OPR_ShowIM       = "ShowIM";       public static final String BIN_ShowIM = "11001";

    public static final String OPR_UNKNOWN      = "";           private static final String BIN_INVALID = String.format("%0" + 16 + "d", 0).replace('0','1');

    private static HashMap<String, String> operation2Binary;

    // Operator types
//    public static final int OPR10 = 0;
//    public static final int OPR5 = 1;
//    private static HashMap<String, Integer> operatorTypes;

    // Operand types
    public static final int DC3_DC3 = 0;
    public static final int REG_DC3 = 1;
    public static final int REG_REG = 2;
    public static final int REG_IMM3 = 3;
    public static final int DC3_IMM8 = 4;
    public static final int REG_IMM8 = 5;
    public static final int INV = Integer.MAX_VALUE;
    private static HashMap<String, Integer> operandsTypes;

    public static final String REG_RGX = "[rR][0-7]";
    public static final String IMM3_RGX = "[0-7]";
    public static final String IMM8_DEC_RGX = "[-+]?[0-9]+";
    public static final String IMM8_HEX_RGX = "[-+]?0x[0-9aAbBcCdDeEfF]+";
    public static final String LBL_RGX = "[A-Za-z][_0-9A-Za-z]*|[_]+[0-9A-Za-z][0-9A-Za-z]*";

    static {
        labelAddrMap = new HashMap<String, Integer>();

//        operatorTypes = new HashMap<String, Integer>(32);

        operandsTypes = new HashMap<String, Integer>(32);
        operation2Binary = new HashMap<String, String>(32);
        operandsTypes.put(OPR_ADD, REG_REG);                    operation2Binary.put(OPR_ADD, BIN_ADD);
        operandsTypes.put(OPR_AND, REG_REG);                    operation2Binary.put(OPR_AND, BIN_AND);
        operandsTypes.put(OPR_SUB, REG_REG);                    operation2Binary.put(OPR_SUB, BIN_SUB);
        operandsTypes.put(OPR_OR, REG_REG);                     operation2Binary.put(OPR_OR, BIN_OR);
        operandsTypes.put(OPR_XOR, REG_REG);                    operation2Binary.put(OPR_XOR, BIN_XOR);
        operandsTypes.put(OPR_MOV, REG_REG);                    operation2Binary.put(OPR_MOV, BIN_MOV);
        operandsTypes.put(OPR_ADC, REG_DC3);                    operation2Binary.put(OPR_ADC, BIN_ADC);
        operandsTypes.put(OPR_NOT, REG_DC3);                    operation2Binary.put(OPR_NOT, BIN_NOT);
        operandsTypes.put(OPR_SAR, REG_IMM3);                   operation2Binary.put(OPR_SAR, BIN_SAR);
        operandsTypes.put(OPR_SLR, REG_IMM3);                   operation2Binary.put(OPR_SLR, BIN_SLR);
        operandsTypes.put(OPR_SAL, REG_IMM3);                   operation2Binary.put(OPR_SAL, BIN_SAL);
        operandsTypes.put(OPR_SLL, REG_IMM3);                   operation2Binary.put(OPR_SLL, BIN_SLL);
        operandsTypes.put(OPR_ROL, REG_IMM3);                   operation2Binary.put(OPR_ROL, BIN_ROL);
        operandsTypes.put(OPR_ROR, REG_IMM3);                   operation2Binary.put(OPR_ROR, BIN_ROR);
        operandsTypes.put(OPR_INC, REG_DC3);                    operation2Binary.put(OPR_INC, BIN_INC);
        operandsTypes.put(OPR_DEC, REG_DC3);                    operation2Binary.put(OPR_DEC, BIN_DEC);
        operandsTypes.put(OPR_NOP, DC3_DC3);                    operation2Binary.put(OPR_NOP, BIN_NOP);
        operandsTypes.put(OPR_ShowR, REG_DC3);              operation2Binary.put(OPR_ShowR, BIN_ShowR);
        operandsTypes.put(OPR_ShowRR, REG_REG);           operation2Binary.put(OPR_ShowRR, BIN_ShowRR);
        operandsTypes.put(OPR_LoadDipR, REG_DC3);     operation2Binary.put(OPR_LoadDipR, BIN_LoadDipR);
        operandsTypes.put(OPR_LoadDipRR, REG_REG);  operation2Binary.put(OPR_LoadDipRR, BIN_LoadDipRR);
        operandsTypes.put(OPR_CMP, REG_REG);                    operation2Binary.put(OPR_CMP, BIN_CMP);
        operandsTypes.put(OPR_JE, DC3_IMM8);                      operation2Binary.put(OPR_JE, BIN_JE);
        operandsTypes.put(OPR_JB, DC3_IMM8);                      operation2Binary.put(OPR_JB, BIN_JB);
        operandsTypes.put(OPR_JA, DC3_IMM8);                      operation2Binary.put(OPR_JA, BIN_JA);
        operandsTypes.put(OPR_JL, DC3_IMM8);                      operation2Binary.put(OPR_JL, BIN_JL);
        operandsTypes.put(OPR_JG, DC3_IMM8);                      operation2Binary.put(OPR_JG, BIN_JG);
        operandsTypes.put(OPR_JMP, DC3_IMM8);                   operation2Binary.put(OPR_JMP, BIN_JMP);
        operandsTypes.put(OPR_LI, REG_IMM8);                      operation2Binary.put(OPR_LI, BIN_LI);
        operandsTypes.put(OPR_LM, REG_IMM8);                      operation2Binary.put(OPR_LM, BIN_LM);
        operandsTypes.put(OPR_ShowDM, DC3_IMM8);          operation2Binary.put(OPR_ShowDM, BIN_ShowDM);
        operandsTypes.put(OPR_ShowIM, DC3_IMM8);          operation2Binary.put(OPR_ShowIM, BIN_ShowIM);
    }

    public static void defineLabel(String label, int addr) throws IllegalArgumentException {
        if (labelAddrMap.containsKey(label) && labelAddrMap.get(label) != addr) {
            throw new IllegalArgumentException("Label " + label + " is defined previously.");
        } else {
            labelAddrMap.put(label, addr);
        }
    }

    public static void generateBinaries(List<Instruction> instructions) {
        for (Instruction instr : instructions) {
            instr.validateUsedLabelsAndGenerateBinaries();
        }
    }

    // Instruction fields
    private int lineNumber;
    private String fullLine;
    private String strLine;
    private String operation;

    private String[] operands;

    private String binaryIntruction;

    public Instruction(int lineNumber, @NotNull String fullLine, @NotNull String strLine, @NotNull String operation, @Nullable String[] operands) {
        this.lineNumber = lineNumber;
        this.fullLine = fullLine;
        this.strLine = strLine;
        this.operation = operation;
        this.operands = (operands != null && operands.length > 0 ? operands : null);
        this.validate();
    }

    public String getFullLine() {
        return fullLine;
    }

    public String getBinaryIntruction() {
        if (binaryIntruction == null) throw new AssertionError("Accessed NULL binaryInstruction");
        else if (binaryIntruction.length() != 16) throw new AssertionError("Generated instruction's length is not 16");
        else return binaryIntruction;
    }

    private void validate() {
        int opt = (operandsTypes.get(operation) != null ? operandsTypes.get(operation) : INV);
        switch (opt) {
            case DC3_DC3:
                if (operands != null) {
                    ErrorHandler.putLineError(lineNumber, strLine, operation + " takes no operands.");
                }
                break;
            case REG_DC3:
                if (operands == null || operands.length != 1 || !operands[0].matches(REG_RGX)) {
                    ErrorHandler.putLineError(lineNumber, strLine, operation + " takes one register operand.");
                }
                break;
            case REG_REG:
                if (operands == null || operands.length != 2 || !operands[0].matches(REG_RGX) || !operands[1].matches(REG_RGX)) {
                    ErrorHandler.putLineError(lineNumber, strLine, operation + " takes two register operands.");
                }
                break;
            case REG_IMM3:
                if (operands == null || operands.length != 2 || !operands[0].matches(REG_RGX) || !operands[1].matches(IMM3_RGX)) {
                    ErrorHandler.putLineError(lineNumber, strLine, operation + " takes one register operand and one immediate valid shift amount.");
                }
                break;
            case DC3_IMM8:
                if (operands == null || operands.length != 1 || !(operands[0].matches(IMM8_DEC_RGX) || operands[0].matches(IMM8_HEX_RGX) || operands[0].matches(LBL_RGX))) {
                    ErrorHandler.putLineError(lineNumber, strLine, operation + " takes one immediate 8-bit address, or a valid label.");
                }
                break;
            case REG_IMM8:
                if (operands == null || operands.length != 2 || !operands[0].matches(REG_RGX) || !(operands[1].matches(IMM8_DEC_RGX) || operands[1].matches(IMM8_HEX_RGX))) {
                    ErrorHandler.putLineError(lineNumber, strLine, operation + " takes one register operand and one immediate 8-bit value/address.");
                }
                break;
            default:
                ErrorHandler.putLineError(lineNumber, strLine, "Unknown operation: " + operation);
        }
    }

    private static String integerToPaddedBinaryString(int value, int padding) {
        return String.format("%" + padding + "s", Integer.toBinaryString(value)).replace(' ', '0');
    }

    private static final String DEFAULT_DC3 = "000";

    private void validateUsedLabelsAndGenerateBinaries() {
        // Instruction patterns validated before
        int opt = (operandsTypes.get(operation) != null ? operandsTypes.get(operation) : INV);
        switch (opt) {
            case DC3_DC3:
                binaryIntruction = operation2Binary.get(operation) + DEFAULT_DC3 + DEFAULT_DC3;
                break;
            case REG_DC3:
                binaryIntruction = operation2Binary.get(operation) + integerToPaddedBinaryString(Integer.parseInt(operands[0].substring(1)), 3) + DEFAULT_DC3;
                break;
            case REG_REG:
                binaryIntruction = operation2Binary.get(operation) + integerToPaddedBinaryString(Integer.parseInt(operands[0].substring(1)), 3) + integerToPaddedBinaryString(Integer.parseInt(operands[1].substring(1)), 3);
                break;
            case REG_IMM3:
                binaryIntruction = operation2Binary.get(operation) + integerToPaddedBinaryString(Integer.parseInt(operands[0].substring(1)), 3) + integerToPaddedBinaryString(Integer.parseInt(operands[1]), 3);
                break;
            case DC3_IMM8:
                if (operands[0].matches(IMM8_DEC_RGX)) {
                    binaryIntruction = operation2Binary.get(operation) + DEFAULT_DC3 + integerToPaddedBinaryString(Integer.parseInt(operands[0]), 8);
                } else if (operands[0].matches(IMM8_HEX_RGX)) {
                    binaryIntruction = operation2Binary.get(operation) + DEFAULT_DC3 + integerToPaddedBinaryString(Integer.parseInt(operands[0].substring(2), 16), 8);
                } else {
                    if (labelAddrMap.containsKey(operands[0])) {
                        binaryIntruction = operation2Binary.get(operation) + DEFAULT_DC3 + integerToPaddedBinaryString(labelAddrMap.get(operands[0]), 8);
                    } else {
                        ErrorHandler.putLineError(lineNumber, strLine, "Label " + operands[0] + " is not defined.");
                        binaryIntruction = BIN_INVALID;
                    }
                }
                break;
            case REG_IMM8:
                if (operands[1].matches(IMM8_DEC_RGX)) {
                    binaryIntruction = operation2Binary.get(operation) + integerToPaddedBinaryString(Integer.parseInt(operands[0].substring(1)), 3) + integerToPaddedBinaryString(Integer.parseInt(operands[1]), 8);
                } else {
                    binaryIntruction = operation2Binary.get(operation) + integerToPaddedBinaryString(Integer.parseInt(operands[0].substring(1)), 3) + integerToPaddedBinaryString(Integer.parseInt(operands[1].substring(2), 16), 8);
                }
                break;
            default:
                ErrorHandler.putLineError(lineNumber, strLine, "Unknown operation: " + operation);
        }
        if (binaryIntruction.length() != 16) throw new AssertionError("Generated instruction's length is not 16");
    }
}
