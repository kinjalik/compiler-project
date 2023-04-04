import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.io.File;
import java.util.stream.Stream;

public class ExampleProgramIncorrectProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        String path = classLoader.getResource("incorrect_examples_prog").getPath();
        var fileListStream = Stream.of(new File(path).listFiles())
                .filter(file -> !file.isDirectory())
                .map(File::getAbsolutePath)
                .map(Arguments::of);
        return fileListStream;
    }
}
