package hr.tvz.ntovernic.connect4.client;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DocumentationGenerator {

    public static void generateDocumentation() throws IOException, ClassNotFoundException {
        final List<Path> paths = Files.walk(Path.of("."))
                .filter(path -> path.getFileName().toString().endsWith(".java") && !path.getFileName().toString().contains("module-info"))
                .toList();

        final StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n");
        html.append("<html>\n");
        html.append("  <head>\n");
        html.append("  <title>Documentation</title>\n");
        html.append("</head>\n");
        html.append("<body>\n");

        for (final Path path : paths) {
            final Class<?> clazz = Class.forName(getClassName(path));
            html.append("  <h1>File name: " + path.getFileName().toString() + "</h1>\n");
            html.append("  <h2>Class name: " + clazz.getName() + "</h2>\n");

            html.append("  <h3>Fields</h3>\n");
            Arrays.stream(clazz.getDeclaredFields()).forEach(field -> {
                html.append("    <p>");
                html.append(getModifierName(field.getModifiers()) + " " + field.getType().getName() + " " + field.getName());
                html.append("</p>\n");
            });
            html.append("<br>\n");

            html.append("  <h3>Constructors</h3>\n");
            Arrays.stream(clazz.getDeclaredConstructors()).forEach(constructor -> {
                html.append("    <h4>");
                html.append(getModifierName(constructor.getModifiers()) + " " + constructor.getName());
                html.append("</h4>\n");

                html.append("    <h5>");
                html.append("Parameters");
                html.append("</h5>\n");

                Arrays.stream(constructor.getParameters()).forEach(parameter -> {
                    html.append("      <p>");
                    html.append(parameter.getType().getName() + " " + parameter.getName());
                    html.append("</p>\n");
                });

                html.append("<br>\n");
            });

            html.append("  <h3>Methods</h3>\n");
            Arrays.stream(clazz.getDeclaredMethods()).forEach(method -> {
                html.append("    <h4>");
                html.append(getModifierName(method.getModifiers()) + " " + method.getName());
                html.append("</h4>\n");

                html.append("    <h5>");
                html.append("Parameters");
                html.append("</h5>\n");

                Arrays.stream(method.getParameters()).forEach(parameter -> {
                    html.append("      <p>");
                    html.append(parameter.getType().getName() + " " + parameter.getName());
                    html.append("</p>\n");
                });

                html.append("<br>\n");
            });

            html.append("  <hr>\n");
        }
        html.append("</body>\n");
        html.append("</html>\n");

        BufferedWriter writer = new BufferedWriter(new FileWriter("documentation.html"));
        writer.write(html.toString());
        writer.close();
    }

    private static String getClassName(final Path path) {
        final String classPath = Arrays.stream(path.toString()
                        .replace("\\", "\\\\")
                        .split("\\\\"))
                .filter(DocumentationGenerator::isValid)
                .collect(Collectors.joining("."));

        return classPath.substring(0, classPath.length() - 5);
    }

    private static Boolean isValid(final String part) {
        return !part.equals("") && !part.equals(".") && !part.equals("src") && !part.equals("main") && !part.equals("java");
    }

    private static String getModifierName(final Integer modifiers) {
        if (Modifier.isPublic(modifiers)) {
            return "public";
        }

        if (Modifier.isProtected(modifiers)) {
            return "protected";
        }

        if (Modifier.isPrivate(modifiers)) {
            return "private";
        }

        return "";
    }
}
