package persistence;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class FileManager {

    /**
     * Atributos
     */
    private FileWriter fileWriter;
    
    /**
     * Metodo constructor
     */
    public FileManager() {
    }

    /**
     * Metodo para leer los usuarios del archivo de persistencia y los agrega a una lista
     * @return lista con los usuarios y sus atributos
     * @throws IOException arroja una excepcion en caso de no encontrar el archivo
     */
    public List<String> readFileUsers() throws IOException {
        List<String> listLines;
        listLines = Files.readAllLines(Paths.get("src/persistence/users.txt"));
        return listLines;
    }

    /**
     * Metodo para abrir el archivo e persistencia
     * @param path ruta donde se encuentra el archivo
     * @return el archivo
     */
    public File open1(String path) {
        File file = new File(path);
        try {
            fileWriter = new FileWriter(file);
        } catch (IOException ex) {
        }
        return file;
    }

     /**
     * Metodo para abrir el archivo e persistencia
     * @param path ruta donde se encuentra el archivo
     * @return el archivo
     */
    public File open2(String path) {
        File file = new File(path);
        try {
            fileWriter = new FileWriter(file, true);
        } catch (IOException ex) {
        }
        return file;
    }
    
    /**
     * Metodo para cerrar el archivo de persistencia
     * @throws java.io.IOException arroja una excepcion si no encuentra el archivo
     */
    public void close() throws IOException {
            fileWriter.close();
    }

    /**
     * Metodo para escribir en el archivo de persistencia
     * @param cad texto a escribir en el archivo
     * @throws IOException arrja una excepcion en caso de no encontrtar el archivos
     */
    public void write(String cad) throws IOException {
            fileWriter.write(cad);
    }

}
