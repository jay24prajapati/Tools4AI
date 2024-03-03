package com.t4a.examples.shell;

import com.t4a.action.shell.ShellAction;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class ShellTest {
    public static void main(String[] args) throws IOException, InterruptedException {

        URL resourceUrl = ShellTest.class.getClassLoader().getResource("test_script.cmd");
        if (resourceUrl != null) {
            // Convert URL to file path
            String filePath = resourceUrl.getFile();

            // Get the absolute path of the file
            File file = new File(filePath);
            String absolutePath = file.getAbsolutePath();
            ShellAction action = new ShellAction("This is action to run shelll command", absolutePath,"testMyScript");
            action.executeShell(args);
            // Output the absolute path
            System.out.println("Absolute path of the file: " + absolutePath);
        } else {
            System.out.println("File not found.");
        }

    }
}
