package fr.unice.polytech.equipe.j.flexiblerestserver;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ClassScanner {

    public static List<Class<?>> findClassesInPackage(String root) throws ClassNotFoundException {
        String path = root.replace('.', '/');
        File directory = new File("target/classes/" + path);

        List<Class<?>> classes = new ArrayList<>();
        if (directory.exists()) {
            addFilesRec(root, directory, classes);
        }
        return classes;
    }

    private static void addFilesRec(String packageName, File directory, List<Class<?>> classes) throws ClassNotFoundException {
        assert directory.isDirectory();
        File[] files = directory.listFiles();
        assert files != null;

        for (File file : files) {
            if (file.isDirectory()) {
                String subPackageName = packageName + "." + file.getName();
                addFilesRec(subPackageName, file, classes);
            } else if (file.getName().endsWith(".class")) {
                String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                classes.add(Class.forName(className));
            }
        }
    }
}
