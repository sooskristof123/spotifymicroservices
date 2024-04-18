package com.kristof.exp.ConfigService.Service;

import com.kristof.exp.ConfigService.Model.Property;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Service
public class FileService {
    public void writePropertyToFile(Property property) throws IOException {
        Path path = Paths.get("C:/dev/ConfigRepository/"+property.getApplication().getApplicationName());
        Files.createDirectories(path);
        //Files.createFile(path);
        if (Files.exists(Path.of(path.toString()+"/"+property.getApplication().getApplicationName() + "-" + property.getEnvironment().getEnvironmentName() + ".properties"))) {
            Files.writeString(Path.of(path.toString()+"/"+property.getApplication().getApplicationName() + "-" + property.getEnvironment().getEnvironmentName() + ".properties"),
                    "\n"+property.getKey()+"="+property.getValue(),
                    StandardOpenOption.APPEND);
        } else {
            Files.writeString(Path.of(path.toString()+"/"+property.getApplication().getApplicationName() + "-" + property.getEnvironment().getEnvironmentName() + ".properties"),
                    property.getKey()+"="+property.getValue());
        }
    }
}
