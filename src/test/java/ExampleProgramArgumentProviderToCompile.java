import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ExampleProgramArgumentProviderToCompile implements ArgumentsProvider {
    final String EXAMPLE_FOLDER = "examples";
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        String path = classLoader.getResource("examples_to_compile").getPath();
        var fileListStream = Stream.of(new File(path).listFiles())
                .filter(file -> !file.isDirectory())
                .map(File::getAbsolutePath)
                .map(Arguments::of);
        return fileListStream;
    }
}
