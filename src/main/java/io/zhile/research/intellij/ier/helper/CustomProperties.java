package io.zhile.research.intellij.ier.helper;

import com.intellij.openapi.application.PathManager;
import com.intellij.util.SystemProperties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class CustomProperties {
    public static void fix() throws Exception {
        String key = "idea.ignore.disabled.plugins";
        System.clearProperty(key);

        List<Path> paths = new ArrayList<>();
        paths.add(Paths.get(SystemProperties.getUserHome(), PathManager.PROPERTIES_FILE_NAME));

        String customOptionsDir = PathManager.getCustomOptionsDirectory();
        if (null != customOptionsDir) {
            paths.add(Paths.get(customOptionsDir, PathManager.PROPERTIES_FILE_NAME));
        }

        for (Path path : paths) {
            File file = path.toFile();
            if (!file.exists()) {
                continue;
            }

            Properties props = new Properties();
            try (FileInputStream fis = new FileInputStream(file)) {
                props.load(fis);
            }

            props.remove(key);

            if (props.isEmpty()) {
                file.delete();
                continue;
            }

            try (FileOutputStream fos = new FileOutputStream(file)) {
                props.store(fos, null);
            }
        }
    }
}