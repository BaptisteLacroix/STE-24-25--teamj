package fr.unice.polytech.equipe.j;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ClassScanner {

    public static List<Class<?>> findClassesInPackage(String root) throws ClassNotFoundException {
        // Get the current class loader (which is module-specific)
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        // Convert the root package name to a path
        String path = root.replace('.', '/');

        // Get the location of a class from the current module
        String classFileLocation = classLoader.getResource(path + "/").getPath();

        // The location will be something like "file:/path/to/project/target/classes/fr/unice/polytech/equipe/j"
        // We need to extract the target directory for the current module.

        // Extract the path of the classes directory (i.e., target/classes/)
        String targetDir = classFileLocation.substring(0, classFileLocation.indexOf("target/classes/")) + "target/classes/";

        System.out.println("Looking for classes in: " + targetDir);

        File directory = new File(targetDir + path);

        List<Class<?>> classes = new ArrayList<>();

        if (directory.exists()) {
            // Use the addFilesRec method to recursively search for classes
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
