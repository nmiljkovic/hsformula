package me.nemanjamiljkovic.hsformula.cli;

import me.nemanjamiljkovic.hsformula.DebugPrintVisitor;
import me.nemanjamiljkovic.hsformula.HSFormula;
import me.nemanjamiljkovic.hsformula.ParseResult;

import java.util.Scanner;

public class DebugMain {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter a formula to parse: ");
        while (scanner.hasNextLine()) {
            ParseResult result = HSFormula.parse(scanner.nextLine());
            if (result.hasParseErrors()) {
                result.getParseErrors().forEach(System.err::println);
                continue;
            }
            DebugPrintVisitor visitor = new DebugPrintVisitor();
            String formatted = visitor.visit(result.getAst());
            System.out.println("result: " + formatted);
            System.out.println("\nEnter a formula to parse: ");
        }
    }
}
