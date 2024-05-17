/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.vaadin.example.shiro.util;

/**
 *
 * @author Bela Oxmyx
 */
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileSvc {

    private final ResourceLoader resourceLoader;

    public FileSvc(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public List<String> readFile(String filePath) {
        List<String> lines = new ArrayList<>();
        Resource resource = resourceLoader.getResource("classpath:" + filePath);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }
}
