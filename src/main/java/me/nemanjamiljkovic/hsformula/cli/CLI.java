package me.nemanjamiljkovic.hsformula.cli;

import com.jakewharton.fliptables.FlipTable;
import me.nemanjamiljkovic.hsformula.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import picocli.CommandLine.Parameters;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

import static picocli.CommandLine.Command;

@Command(
    description = "Executes formula on a provided spreadsheet.",
    name = "hsformula"
)
public class CLI implements Callable<Void> {

    @Parameters(index = "0")
    private Path csvPath;

    @Override
    public Void call() throws Exception {
        BufferedReader reader = Files.newBufferedReader(csvPath);
        CSVParser parser = CSVFormat.EXCEL.parse(reader);
        Spreadsheet spreadsheet = Spreadsheet.fromCSV(parser.iterator());

        // Sweep the sheet from left to right, top to bottom,
        // and find formulas to execute.
        for (int i = 0; i < spreadsheet.getRows(); i++) {
            for (int j = 0; j < spreadsheet.getColumns(); j++) {
                CellReference cell = new CellReference(j, i);
                String value = spreadsheet.valueAt(cell);
                if (value.isEmpty() || value.charAt(0) != '=') {
                    continue;
                }
                String formula = value.substring(1);
                executeFormula(spreadsheet, cell, formula);
            }
        }

        // Split cells into headers and the rest of the table.
        String[] headers = Arrays.copyOf(spreadsheet.getRow(0), spreadsheet.getColumns());
        for (int i = 0; i < spreadsheet.getColumns(); i++) {
            if (headers[i].isEmpty()) {
                // FlipTable doesn't support empty string, but 1 spaced strings
                // work fine D:
                headers[i] = " ";
            }
        }
        String[][] cells = Arrays.copyOfRange(spreadsheet.getCells(), 1, spreadsheet.getRows());

        System.out.println(
            FlipTable.of(headers, cells)
        );
        return null;
    }

    private void executeFormula(Spreadsheet spreadsheet, CellReference cell, String formula) {
        ParseResult parse = HSFormula.parse(formula);
        // Formula must parse correctly.
        if (parse.hasParseErrors()) {
            System.err.println(String.format("Failed parsing formula: %s", formula));
            parse.getParseErrors().forEach(System.err::println);
            System.exit(1);
        }
        List<ErrorReporter.Error> analysisErrors = HSFormula.analyze(parse.getAst());
        // Formula must pass analysis.
        if (analysisErrors.size() > 0) {
            analysisErrors.forEach(err -> {
                String message = String.format("%s at %d:%d",
                    err.getMessage(),
                    err.getLine(),
                    err.getColumn()
                );
                System.err.println(message);
            });
            System.exit(1);
        }
        // Execute formula and store result based on type.
        Object result = HSFormula.execute(parse.getAst(), spreadsheet);
        if (result instanceof Double) {
            spreadsheet.store(cell, (Double) result);
        } else if (result instanceof String) {
            spreadsheet.store(cell, (String) result);
        } else {
            String message = String.format("Cannot store type %s at %s",
                cell.getClass().getSimpleName(),
                cell.toString()
            );
            System.err.println(message);
            System.exit(1);
        }
    }
}
