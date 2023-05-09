package Providers;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ExampleProgramArgumentProvider implements ArgumentsProvider {
    final String example_folder = "examples_to_compile";
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        String path = classLoader.getResource(example_folder).getPath();
        var fileListStream = Stream.of(new File(path).listFiles())
                .filter(file -> !file.isDirectory())
                .map(File::getAbsolutePath)
                .map(Arguments::of);
       return fileListStream;
    }
}
