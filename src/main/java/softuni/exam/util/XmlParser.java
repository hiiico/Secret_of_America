
package softuni.exam.util;


import jakarta.xml.bind.JAXBException;

public interface XmlParser {

    <T> T fromFile(String filePath, Class<T> tClass) throws JAXBException;

    <T> void toFile(String filePath, T object) throws JAXBException;
}