import java.io.*;
import java.util.*;

class minDFA {
    private String[][] table;

    public minDFA(DFAMinimizer minimizer, boolean in) {
        table = new String[minimizer.getNumStates() - 1][];
        for (int i = 0; i < minimizer.getNumStates() - 1; i++) {
            table[i] = new String[i + 1];
            for (int k = 0; k < i + 1; k++) {
                table[i][k] = new String("E");
            }
        }
    }

    // Takes a DFA from a text file and minimizes it to an optimal DFA, then outputs that DFA to a text file
    public DFAMinimizer miniz(DFAMinimizer minimizer) {
        for (int i = 0; i < minimizer.getFinalSize(); i++) {
            int row = minimizer.getIndexOf(minimizer.getFinal(i)) - 1;
            for (int col = 0; col < row + 1; col++) {
                if (!minimizer.checkFinal(minimizer.getState(col))) {
                    table[row][col] = new String("X");
                }
            }
            int col = minimizer.getIndexOf(minimizer.getFinal(i));
            for (row = col; row < table.length; row++) {
                if (!minimizer.checkFinal(minimizer.getState(row + 1))) {

                    table[row][col] = new String("X");
                }
            }
        }
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[i].length; j++) {
                if (!table[i][j].equals("X")) {
                    int flag = 0;
                    for (int k = 0; k < minimizer.getAlphabetSize(); k++) {
                        int transitionOne = minimizer
                                .getIndexOf(minimizer.getTransition(minimizer.getState(i + 1), minimizer.getSymbol(k)));
                        int transitionTwo = minimizer
                                .getIndexOf(minimizer.getTransition(minimizer.getState(j), minimizer.getSymbol(k)));
                        if (((transitionOne < transitionTwo) && (table[transitionTwo - 1][transitionOne].equals("X")))
                                || ((transitionTwo < transitionOne) && (table[transitionOne - 1][transitionTwo].equals("X")))) {
                            String[] token = table[i][j].split(" ");
                            table[i][j] = new String("X");
                            for (int p = 1; p < token.length; p++) {
                                String[] coords = token[p].split(",");
                                table[Integer.parseInt(coords[0])][Integer.parseInt(coords[1])] = new String("X");
                            }
                        }
                        else if (transitionOne < transitionTwo) {
                            String[] token = table[i][j].split(" ");
                            table[transitionTwo - 1][transitionOne] = table[transitionTwo - 1][transitionOne] + " " + String.valueOf(i) + ","
                                    + String.valueOf(j);
                            for (int p = 1; p < token.length; p++) {
                                table[transitionTwo - 1][transitionOne] = table[transitionTwo - 1][transitionOne] + " " + token[p];
                            }
                        } else if (transitionOne > transitionTwo) {
                            String[] token = table[i][j].split(" ");
                            table[transitionOne - 1][transitionTwo] = table[transitionOne - 1][transitionTwo] + " " + String.valueOf(i) + ","
                                    + String.valueOf(j);
                            for (int p = 1; p < token.length; p++) {
                                table[transitionOne - 1][transitionTwo] = table[transitionOne - 1][transitionTwo] + " " + token[p];
                            }
                        }
                    }
                }
            }
        }
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[i].length; j++) {
                if (!table[i][j].equals("X"))
                    table[i][j] = new String("O");
            }
        }
        int[][] tTable = minimizer.getTransitionTable();
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[i].length; j++) {
                if (table[i][j].equals("O")) {
                    for (int k = 0; k < tTable[i + 1].length; k++) {
                        tTable[i + 1][k] = minimizer.getNumStates() + j;
                    }
                }
            }
        }
        DFAMinimizer tempMin = new DFAMinimizer();
        tempMin.initAlphabet(minimizer.getAlphabet());
        String[] st = new String[minimizer.getNumStates()];
        st = minimizer.getStates();
        int rmvState = 0;
        for (int i = st.length - 1; i >= 0; i--) {
            if ((tTable[i][0] - minimizer.getNumStates()) >= 0) {
                st[tTable[i][0] - minimizer.getNumStates()] = st[tTable[i][0] - minimizer.getNumStates()] + "," + st[i];
                st[i] = "-";
                rmvState++;
            }
        }
        String[] mst = new String[minimizer.getNumStates() - rmvState];
        for (int i = 0, k = 0; i < st.length; i++) {
            if (!st[i].equals("-")) {
                mst[k] = st[i];
                k++;
            }
        }
        tempMin.initState(mst);
        int mqo = 0;
        for (int i = 0; i < tempMin.getNumStates(); i++) {
            int flag = 0;
            String[] stt = tempMin.getState(i).split(",");
            for (int j = 0; j < stt.length; j++) {
                if (minimizer.getInitial().equals(stt[j])) {
                    mqo = i;
                    flag = 1;
                    break;
                }
            }
            if (flag == 1)
                break;
        }
        tempMin.setInitial(tempMin.getState(mqo));
        ArrayList<String> finalS = new ArrayList<String>();
        for (int i = 0; i < minimizer.getFinalSize(); i++) {
            for (int j = 0; j < tempMin.getNumStates(); j++) {
                int flag = 0;
                String[] stt = tempMin.getState(j).split(",");
                for (int k = 0; k < stt.length; k++) {
                    if (minimizer.getFinal(i).equals(stt[k])) {
                        finalS.add(tempMin.getState(j));
                        flag = 1;
                        break;
                    }
                }
                if (flag == 1)
                    break;
            }
        }
        String[] fState = finalS.toArray(new String[finalS.size()]);
        tempMin.setFinal(fState);
        tempMin.createTransTable();
        for (int i = 0, m = 0; i < tTable.length; i++) {
            String[] stt = new String[tTable[0].length + 1];
            if (tTable[i][0] - minimizer.getNumStates() < 0) {
                stt[0] = tempMin.getState(m);
                m++;
                for (int j = 0; j < tTable[i].length; j++) {
                    String st2 = minimizer.getState(tTable[i][j]);
                    for (int k = 0; k < tempMin.getNumStates(); k++) {
                        int flag = 0;
                        String[] stt2 = tempMin.getState(k).split(",");
                        for (int l = 0; l < stt2.length; l++) {
                            if (stt2[l].equals(st2)) {
                                stt[j + 1] = tempMin.getState(k);
                                flag = 1;
                                break;
                            }
                        }
                        if (flag == 1)
                            break;
                    }
                }
                tempMin.setRowTT(stt);
            }
        }
        System.out.println();
        return tempMin;
    }
}

class DFAMinimizer {
    private String[] state;
    private String[] alphabet;
    private int[][] transitionTable;
    private int qo;
    private ArrayList<Integer> finalState;

    public void initState(String[] states) {
        state = new String[states.length];
        for (int i = 0; i < states.length; i++) {
            state[i] = new String(states[i]);
        }
    }

    public void initAlphabet(String[] alpha) {
        alphabet = new String[alpha.length];
        for (int i = 0; i < alpha.length; i++) {
            alphabet[i] = new String(alpha[i]);
        }
    }

    public int getIndexOf(String st) {
        for (int i = 0; i < state.length; i++) {
            if (st.equals(state[i]))
                return i;
        }
        return -1;
    }

    public void setInitial(String st) {
        qo = this.getIndexOf(st);
    }

    public String getInitial() {
        return state[qo];
    }

    public void setFinal(String[] st) {
        finalState = new ArrayList<Integer>();
        for (int i = 0; i < st.length; i++) {
            finalState.add(getIndexOf(st[i]));
        }
    }

    public int getNumStates() {
        return state.length;
    }

    public int getAlphabetSize() {
        return alphabet.length;
    }

    public int getFinalSize() {
        return finalState.size();
    }

    public String getFinal(int index) {
        return state[finalState.get(index)];
    }

    public boolean checkFinal(String st) {
        for (int i = 0; i < finalState.size(); i++) {
            if (this.getIndexOf(st) == finalState.get(i))
                return true;
        }
        return false;
    }

    public String getState(int index) {
        return state[index];
    }

    public String getSymbol(int index) {
        return alphabet[index];
    }

    public String[] getAlphabet() {
        String[] alpha = new String[alphabet.length];
        System.arraycopy(alphabet, 0, alpha, 0, alphabet.length);
        return alpha;
    }

    public String[] getStates() {
        String[] st = new String[state.length];
        System.arraycopy(state, 0, st, 0, state.length);
        return st;
    }

    public void createTransTable() {
        transitionTable = new int[state.length][alphabet.length];
    }

    public void setRowTT(String st[]) {
        int index = this.getIndexOf(st[0]);
        for (int i = 1; i < st.length; i++) {
            transitionTable[index][i - 1] = this.getIndexOf(st[i]);
        }
    }

    public int getSymbolIndex(String alpha) {
        for (int i = 0; i < alphabet.length; i++) {
            if (alpha.equals(alphabet[i]))
                return i;
        }
        return -1;
    }

    public int[][] getTransitionTable() {
        int[][] ttCopy;
        ttCopy = new int[transitionTable.length][transitionTable[0].length];
        for (int i = 0; i < transitionTable.length; i++) {
            System.arraycopy(transitionTable[i], 0, ttCopy[i], 0, transitionTable[i].length);
        }
        return ttCopy;
    }

    public String getTransition(String from, String symbol) {
        int fromIndex = this.getIndexOf(from);
        int symbolIndex = this.getSymbolIndex(symbol);
        return state[transitionTable[fromIndex][symbolIndex]];
    }
    public void display(String inputFileName, List<String> inputStrings) {
        int largestLen = 0;
        for (int i = 0; i < state.length; i++) {
            if (state[i].length() > largestLen)
                largestLen = state[i].length();
        }
        largestLen = largestLen * -1;
        System.out.printf("%s   ", "Sigma");
        for (int i = 0; i < alphabet.length; i++) {
            System.out.printf("%s   ", alphabet[i]);
        }
        System.out.println();
        System.out.println("-----------------------------");
        for (int i = 0; i < transitionTable.length; i++) {
            System.out.printf("%s:  ", state[i].split(",")[0]);
            for (int j = 0; j < transitionTable[i].length; j++) {
                System.out.printf("%s   ", state[transitionTable[i][j]].split(",")[0]);
            }
            System.out.println();
        }
        System.out.println("---------------");
        System.out.println("0 : Initial State");
        Set<String> finalStates = new HashSet<String>();
        for (Integer val : finalState) {
            finalStates.add(val.toString());
        }
        System.out.print(String.join(",", finalStates));
        System.out.print(" : Accepting State(s)");
        System.out.println("\n");

        validateInputStrings(inputFileName, inputStrings);
    }

    public void validateInputStrings(String inputFileName, List<String> inputStrings) {
        int yesCount = 0;
        int noCount = 0;
        System.out.println("Parsing results of strings in " + inputFileName);
        for (String curString : inputStrings) {
            int currState = 0;
            for (Character alphabet : curString.toCharArray()) {
                int alphaIndex =  getSymbolIndex(alphabet.toString());
                if(alphaIndex == -1) {
                    currState = -1;
                    break;
                }
                else
                    currState = transitionTable[currState][alphaIndex];
            }
            if(finalState.contains(currState)) {
                yesCount++;
                System.out.print("Yes ");
            }
            else {
                noCount++;
                System.out.print("No ");
            }
        }
        System.out.println("\n");
        System.out.println("Yes: "+ yesCount + " No: " + noCount);
    }
}
public class NFA2DFA {
    private List<String> inputStrings = new ArrayList<String>();
    private List<List<Set<Integer>>> NFA = new ArrayList<List<Set<Integer>>>();
    private Set<Integer> acceptingStates = new HashSet<Integer>();
    private Set<Integer> dfaFinalStates = new HashSet<Integer>();
    private List<Set<Integer>> DFAstates = new ArrayList<Set<Integer>>();
    private List<List<Integer>> dfaTransitions = new ArrayList<List<Integer>>();
    private String lambdaAlphabet = "~";
    private String nfaFileName = "";
    private String inputFileName = "";
    private int initialStart;
    private List<String> sigma;

    public NFA2DFA(String nfaFileName) {
        this.nfaFileName = nfaFileName;
        readFiles();
    }

    public void NFAtoDFA() {
        convertNFA();
        printDFA();
    }

    //TODO: Change how the input file is read to fit the new file format
    private void readFiles() {
        try {
            FileReader fr = new FileReader(nfaFileName);
            BufferedReader bufferedReader = new BufferedReader(fr);
            // Grab the first integer which is the number of states after skipping |Q|:
            StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());
            stringTokenizer.nextToken();
            int totalStates = Integer.parseInt(stringTokenizer.nextToken());
            //System.out.println("Q: " + totalStates);
            // Skip the Sigma: in the .nfa file to grab the sigma
            StringTokenizer sigmaTokenizer = new StringTokenizer(bufferedReader.readLine(), " ");
            // Skip sigma
            sigmaTokenizer.nextToken();
            // Grab the letters after the Sigma: in the .nfa file
            sigma = new ArrayList<String>();
            while (sigmaTokenizer.hasMoreTokens()) {
                sigma.add(sigmaTokenizer.nextToken());
            }
            //System.out.println("Sigma: " + sigma);
            // Skip the dashed lines
            bufferedReader.readLine();
            for (int i = 0; i < totalStates; i++) {
                String curLine = bufferedReader.readLine();
                StringTokenizer st = new StringTokenizer(curLine, ": ");
                st.nextToken();
                List<Set<Integer>> stateInfo = new ArrayList<Set<Integer>>();
                while(st.hasMoreTokens()) {
                    String state = st.nextToken();
                    StringTokenizer multistateSt = new StringTokenizer(state, "{,}");
                    Set<Integer> innerState = new HashSet<Integer>();
                    while(multistateSt.hasMoreTokens()){
                        innerState.add(Integer.parseInt(multistateSt.nextToken()));
                    }
                    stateInfo.add(innerState);
                }
                NFA.add(stateInfo);
            }
            //System.out.println("NFA: " + NFA);
            // Skip the part of the line saying 'Initial State:'
            bufferedReader.readLine();
            StringTokenizer st = new StringTokenizer(bufferedReader.readLine(), " ");
            st.nextToken();
            st.nextToken();
            initialStart = Integer.parseInt(st.nextToken());
            //System.out.println("Initial State: " + initialStart);
            // Skip the part of the line saying 'Accepting State(s):'
            StringTokenizer finalStateSt = new StringTokenizer(bufferedReader.readLine(), " ,");
            finalStateSt.nextToken();
            finalStateSt.nextToken();
            while(finalStateSt.hasMoreTokens()){
                acceptingStates.add(Integer.parseInt(finalStateSt.nextToken()));
            }
            //System.out.println("Accepting State(s): " + acceptingStates);

            bufferedReader.readLine();
            bufferedReader.readLine();
            //bufferedReader.readLine();
            String newLine = bufferedReader.readLine();
            while(newLine != null) {
                inputStrings.add(newLine);
                newLine = bufferedReader.readLine();
            }

            System.out.println("Input Strings: " + inputStrings);
        }
        catch(IOException e) {
            System.out.println("Unable to open the file " + e.getMessage());
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    private void MinimizingDFA(String inputFileName, List<String> inputStrings) {
        DFAMinimizer dfaMinimizer = new DFAMinimizer();
        String[] tokens;
        String DFAStates = "";
        for (int i = 0; i < dfaTransitions.size(); i++) {
            DFAStates = DFAStates + String.valueOf(i) + "  ";
        }
        DFAStates = DFAStates.replaceAll("\\s+", " ");
        tokens = DFAStates.replaceFirst("^ ", "").split(" ");
        dfaMinimizer.initState(tokens);
        String DFASigma = "";
        for (String alphabet : sigma) {
            DFASigma = DFASigma + " " + alphabet;
        }
        DFASigma = DFASigma.replaceAll("\\s+", " ");
        tokens = DFASigma.replaceFirst("^ ", "").split(" ");
        dfaMinimizer.initAlphabet(tokens);
        String DFAInitialState = "0";
        DFAInitialState = DFAInitialState.replaceAll("\\s+", " ");
        tokens = DFAInitialState.replaceFirst("^ ", "").split(" ");
        dfaMinimizer.setInitial(tokens[0]);
        String DFA_finalstate = "";
        for (int i : dfaFinalStates) {
            DFA_finalstate = DFA_finalstate + " " + i;
        }
        DFA_finalstate = DFA_finalstate.replaceAll("\\s+", " ");
        tokens = DFA_finalstate.replaceFirst("^ ", "").split(" ");
        dfaMinimizer.setFinal(tokens);
        String DFA_Rows_tt = "";
        dfaMinimizer.createTransTable();
        for (int i = 0; i < dfaTransitions.size(); i++) {
            DFA_Rows_tt = DFA_Rows_tt + String.valueOf(i);
            for (int j = 0; j < dfaTransitions.get(i).size(); j++) {
                int temp3 = dfaTransitions.get(i).get(j);
                DFA_Rows_tt = DFA_Rows_tt + " " + String.valueOf(temp3);
            }
            DFA_Rows_tt = DFA_Rows_tt.replaceAll("\\s+", " ");
            tokens = DFA_Rows_tt.replaceFirst("^ ", "").split(" ");
            dfaMinimizer.setRowTT(tokens);
            DFA_Rows_tt = " ";
        }
        System.out.println();
        System.out.println("The Minimized DFA is");
        minDFA minimizer = new minDFA(dfaMinimizer, false);
        System.out.println();
        DFAMinimizer min = minimizer.miniz(dfaMinimizer);
        min.display(inputFileName, inputStrings);
    }
    //TODO: Output the converted DFA to a .dfa file
    private void convertNFA() {
        Set<Integer> tempStates = new HashSet<Integer>();
        transitionState(lambdaAlphabet, initialStart, tempStates);
        DFAstates.add(tempStates);
        for (Integer integer : tempStates) {
            if(acceptingStates.contains(integer)) {
                dfaFinalStates.add(0);
            }
        }
        for (int idx=0; idx < DFAstates.size(); idx++) {
            List<Integer> transition = new ArrayList<Integer>();
            for (String alphabet : sigma) {
                Set<Integer> tempStatesHolder = new HashSet<Integer>();
                for (Integer state : DFAstates.get(idx)) {
                    transitionState(alphabet, state, tempStatesHolder);
                }

                boolean hasState = false;
                int currIndex = 0;
                for (Set<Integer> updatedStates  : DFAstates) {
                    if(updatedStates.equals(tempStatesHolder)) {
                        hasState = true;
                        break;
                    }
                    currIndex++;
                }
                if(!hasState) {
                    DFAstates.add(tempStatesHolder);
                }
                for(int state : tempStatesHolder) {
                    if(acceptingStates.contains(state)) {
                        dfaFinalStates.add(currIndex);
                    }
                }

                transition.add(currIndex);
            }

            dfaTransitions.add(transition);
        }
    }
    private void transitionState(String alphabet, int state, Set<Integer> tempStatesHolder) {
        List<Set<Integer>> currState = NFA.get(state);
        boolean isFinal = (Objects.equals(alphabet, lambdaAlphabet));
        int lambdaIndex = isFinal ? currState.size()-1 : sigma.indexOf(alphabet);
        Set<Integer> currStateInfo = currState.get(lambdaIndex);
        for (int val : currStateInfo) {
            if(val == state && isFinal)// Reference to itself and final
                tempStatesHolder.add(val);
            else
                transitionState(lambdaAlphabet, val, tempStatesHolder);
        }
    }
    //TODO: Make the DFA get output to a .dfa file
    private void printDFA() {
        System.out.println(nfaFileName + " to DFA:");
        System.out.print("Sigma: ");
        for (String alphabetString : sigma) {
            System.out.print(alphabetString + " ");
        }
        System.out.println("");
        for (String a : sigma) {
            System.out.print("----");
        }
        System.out.println("");
        for (int i=0; i<dfaTransitions.size();i++) {
            System.out.print(i + ": ");
            for (Integer integer : dfaTransitions.get(i)) {
                System.out.print(integer + " ");
            }
            System.out.println("");
        }
        System.out.println("---------------");
        System.out.println("0 : Initial State");
        List<String> finalStates = new ArrayList<String>();
        for (Integer val : dfaFinalStates) {
            finalStates.add(val.toString());
        }
        System.out.print(String.join(",", finalStates));
        System.out.print(": Accepting State(s)\n");
        validateInputStrings();
    }
    private void validateInputStrings() {
        int yesCount = 0;
        int noCount = 0;
        System.out.println("Parsing results of strings attached in " + nfaFileName + ":");
        for (String curString : inputStrings) {
            int currState = 0;
            for (Character alphabet : curString.toCharArray()) {
                int alphaIndex = sigma.indexOf(alphabet.toString());
                if(alphaIndex == -1) {
                    currState = -1;
                    break;
                }
                else
                    currState = dfaTransitions.get(currState).get(alphaIndex);
            }
            //System.out.print("Curstate: " + currState);
            if(dfaFinalStates.contains(currState)) {
                yesCount++;
                System.out.print("Yes ");
            }
            else {
                noCount++;
                System.out.print("No ");
            }
        }
        System.out.println("\n");
        System.out.println("Yes: "+ yesCount + " No: " + noCount);
    }
    public static void main(String[] args) {
        if(args.length < 1) {
            System.out.println("Incorrect input syntax.\nUsage: java NFA2DFA <input file>");
            System.exit(0);
        }
        NFA2DFA program = new NFA2DFA(args[0]);
        program.NFAtoDFA();
        //program.MinimizingDFA(program.inputFileName, program.inputStrings);
    }
}