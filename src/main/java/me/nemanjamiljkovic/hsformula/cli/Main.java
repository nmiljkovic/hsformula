package me.nemanjamiljkovic.hsformula.cli;

import org.fusesource.jansi.AnsiConsole;
import picocli.CommandLine;

public class Main {
    public static void main(String[] args) {
        AnsiConsole.systemInstall();
        CommandLine.call(new CLI(), args);
    }
}
