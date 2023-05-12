import Providers.ExampleProgramArgumentProvider;
import org.example.tokens.Scanner;
import org.example.tokens.Token;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class LexerComponentTest {
    @DisplayName("Token reverse back to source code")
    @ParameterizedTest
    @ArgumentsSource(ExampleProgramArgumentProvider.class)
    void power(String filePath) throws IOException {
        var path = Path.of(filePath);
        var originalCode = Files.readString(path);

        var scanner = new Scanner(originalCode);
        var tokens = new ArrayList<Token>();
        for (Scanner it = scanner; it.hasNext(); )
            tokens.add(it.next());

        System.out.printf("File: %s\n", path.getFileName());
        System.out.println("Tokens:");
        tokens.stream().forEach(System.out::print);

        var reversedCode = tokens.stream()
                .map(Token::reverse)
                .collect(Collectors.joining(" "));

        int origPtr = 0, revPtr = 0;

        while (origPtr != originalCode.length() && revPtr != originalCode.length()) {
            var orCh = originalCode.charAt(origPtr);
            var revCh = reversedCode.charAt(revPtr);
            switch (orCh) {
                case ' ':
                case '\n':
                case '\t':
                    origPtr++;
                    continue;
            }

            switch(revCh) {
                case ' ':
                case '\n':
                case '\t':
                    revPtr++;
                    continue;
            }

            if (orCh != revCh) {
                Assertions.fail(String.format("Non-converging characters: %c (in original at %d) vs %s (in reversed at %d)", orCh, origPtr, revCh, revPtr));
            }
            origPtr++;
            revPtr++;
        }
    }
}
