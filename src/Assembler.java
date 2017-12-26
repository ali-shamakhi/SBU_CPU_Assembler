import java.io.*;
import java.util.*;

public class Assembler {

    static FileReader in;
    static FileWriter out;
    static FileWriter outList;

    static ArrayList<Instruction> instructionList;
    static int nextInstrAddr = 0;

    public static void main(String[] args) {
	    if (args.length < 1) {
	        System.err.println("Please specify asm file.");
	        return;
        }
        String inFilePath = args[0];
        try {
            in = new FileReader(inFilePath);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error opening " + inFilePath);
            return;
        }
        String outFilePath;
        if (args.length < 2)
            outFilePath = inFilePath + CONFIG.BIN_FILE_EXTENSION;
        else
            outFilePath = args[1];
        try {
            out = new FileWriter(outFilePath, false);
            outList = new FileWriter(outFilePath + ".list", false);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error creating/opening " + inFilePath);
            return;
        }

        // initialization
        instructionList = new ArrayList<>(CONFIG.DEFAULT_BIN_FILE_SIZE_WORDS);

        LineNumberReader r = new LineNumberReader(in);
        String strLine;
        StringBuilder fullLineSB = new StringBuilder();
        try {
            System.out.println("Assembling " + inFilePath + " into " + outFilePath + " ...");
            while ((strLine = r.readLine()) != null) {
                fullLineSB.append(strLine).append("\n");
                if (strLine.trim().startsWith(CONFIG.ASM_COMMENT_STARTER)) continue;
                String asmLine = (strLine.contains(CONFIG.ASM_COMMENT_STARTER) ? strLine.substring(0, strLine.indexOf(CONFIG.ASM_COMMENT_STARTER)).trim() : strLine.trim());
                // define labels
                while (asmLine.contains(CONFIG.ASM_LABEL_DEFINER)) {
                    int indexLD = asmLine.indexOf(CONFIG.ASM_LABEL_DEFINER);
                    String label = asmLine.substring(0, indexLD);
                    try {
                        Instruction.defineLabel(label, nextInstrAddr);
                    } catch (IllegalArgumentException iae) {
                        ErrorHandler.putLineError(r.getLineNumber(), strLine, iae.getMessage());
                    }
                    asmLine = asmLine.substring(indexLD + 1).trim();
                }
                // check if no instruction in the line
                if (asmLine.equals("")) continue;
                // take the new instruction
                String[] instrParts =
                        asmLine
                        .replaceAll(CONFIG.ASM_OPERAND_SPLITTER, " ")
                        .split(" +", 2);
                String operation = instrParts[0];
                String[] operands = (instrParts.length > 1 ? instrParts[1].split(" +") : null);
                instructionList.add(nextInstrAddr++, new Instruction(r.getLineNumber(), fullLineSB.toString(), strLine, operation, operands));
                fullLineSB.delete(0, fullLineSB.length());
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error reading " + inFilePath);
            return;
        }

        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error closing " + inFilePath);
        }

        if (ErrorHandler.getNumErrors() > 0) {
            ErrorHandler.printErrors();
            System.err.println("Failed. " + ErrorHandler.getNumErrors() + " error(s) found.");
        } else {
            Instruction INSTR_NOP = new Instruction(-1, "", "", Instruction.OPR_NOP, null);
            while (instructionList.size() < CONFIG.DEFAULT_BIN_FILE_SIZE_WORDS) {
                instructionList.add(INSTR_NOP);
            }
            Instruction.generateBinaries(instructionList);  // completes bin generations
            if (ErrorHandler.getNumErrors() > 0) {
                ErrorHandler.printErrors();
                System.err.println("Failed. " + ErrorHandler.getNumErrors() + " error(s) found.");
            } else {
                try {
                    writeToLISTFile(outList, instructionList);
                    writeToBINFile(out, instructionList);
                    System.out.println("Succeed.");
                } catch (IOException e) {
                    e.printStackTrace();
                    System.err.println("Error writing to " + outFilePath);
                }
            }
        }

        try {
            out.close();
            outList.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error closing " + inFilePath);
        }

    }

    private static void writeToBINFile(FileWriter out, List<Instruction> instructions) throws IOException {
        if (instructions == null || instructions.size() == 0) return;
        out.write(CONFIG.BEGIN_BIN_TEXT);
        int instrAddr = 0;
        while (instrAddr < instructions.size() - 1) {
            out.write(instructions.get(instrAddr++).getBinaryIntruction() + CONFIG.MID_INSTR_TEXT);
        }
        out.write(instructions.get(instructions.size() - 1).getBinaryIntruction() + CONFIG.END_BIN_TEXT);
    }

    private static void writeToLISTFile(FileWriter outList, List<Instruction> instructions) throws IOException {
        if (instructions == null || instructions.size() == 0) return;
        int instrAddr = 0;
        for (Instruction instr : instructions) {
            if (!instr.getFullLine().equals("")) {
                outList.write("================\n" + " | " + instr.getFullLine().replace("\n", "\n" + " | ")  + "\n" + instr.getBinaryIntruction() + "\n================\n");
            }
        }
    }
}
