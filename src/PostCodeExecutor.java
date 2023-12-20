import library.*;
import utils.NumberMathUtils;

import java.util.*;

public class PostCodeExecutor {
    private final List<Symbol> symbolList;
    private final List<CodeInstruction> instructionList;

    private final Map<String,Number> symbolValues;

    public PostCodeExecutor(PostCodeDTO data) {
        this.symbolList = data.getSymbolList();
        this.instructionList = data.getInstructionList();
        symbolValues = new HashMap<>();
    }

    public void initSymbols() throws InputMismatchException {
        Scanner scanner = new Scanner(System.in);
        List<Symbol> needInit = symbolList.stream()
                .filter(symbol -> symbol.getSymbolReuse() == SymbolReuse.FORBIDDEN).toList();
        needInit.forEach(symbol -> {
            System.out.println("Введите " + symbol);
            Number result = symbol.getSymbolType() == Type.FLOAT
                    ? scanner.nextDouble() : scanner.nextInt();
            symbolValues.put(symbol.getName(), result);
        });
    }

    private Symbol executeSymbol(Token token) {
        return symbolList.get(token.getAttribute().intValue());
    }

    private Number executeResult(Token token) {
        return token.getLexeme().getLexemeType() == LexemeType.CONST
                ? token.getAttribute()
                : symbolValues.get(executeSymbol(token).getName());
    }

    public Number getResult() {
        Number result = null;
        for (CodeInstruction instruction: instructionList) {
            Symbol resultSymbol = executeSymbol(instruction.getResult());
            Operation operation = instruction.getOperation();
            Number first = executeResult(instruction.getFirstArg());
            Number second = operation == Operation.I2F
                    ? null : executeResult(instruction.getSecondArg());
            result = switch (operation) {
                case MUL -> NumberMathUtils.multiply(first,second);
                case DIV -> NumberMathUtils.divide(first,second);
                case ADD -> NumberMathUtils.add(first,second);
                case SUB -> NumberMathUtils.subtract(first,second);
                case I2F -> first.doubleValue();
            };
            symbolValues.put(resultSymbol.getName(), result);
        }
        return result;
    }
}
