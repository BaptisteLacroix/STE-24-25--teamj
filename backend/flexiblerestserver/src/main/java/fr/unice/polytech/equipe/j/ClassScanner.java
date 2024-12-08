package fr.unice.polytech.equipe.j;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ClassScanner {

    public static List<Class<?>> findClassesInPackage(String root, String classpath) throws ClassNotFoundException {
        // Get the current class loader (which is module-specific)
        String classFileLocation;
        if (classpath != null) {
            String path = root.replace('.', '/');
            classFileLocation = classpath + "/" + path;
        } else {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

            // Convert the root package name to a path
            String path = root.replace('.', '/');

            // Get the location of a class from the current module
            classFileLocation = classLoader.getResource(path + "/").getPath();
        }

        // If the classFileLocation contains test-classes, replace it with classes
        if (classFileLocation.contains("test-classes")) {
            classFileLocation = classFileLocation.replace("test-classes", "classes");
        }
        File directory = new File(classFileLocation);

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
