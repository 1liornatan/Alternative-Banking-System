package abs.servlets;

import abs.utils.ServletUtils;
import abs.utils.SessionUtils;
import bank.logic.manager.BankManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Scanner;

@WebServlet(name = "File Upload Servlet", urlPatterns = "/bank/upload-file")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class FileUploadServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");

        String usernameFromSession = SessionUtils.getUsername(request);

        BankManager bankManager = ServletUtils.getBankManager(getServletContext());

        ServletOutputStream outputStream = response.getOutputStream();
        try {
            if (usernameFromSession == null) { //user is not logged in yet
                outputStream.print("Not logged in yet.");
            } else {

                Collection<Part> parts = request.getParts();

                StringBuilder fileContent = new StringBuilder();

                for (Part part : parts) {

                    //to write the content of the file to a string
                    fileContent.append(readFromInputStream(part.getInputStream()));
                }
                Path tempFile = Files.createTempFile(null, null);
                Files.write(tempFile, fileContent.toString().getBytes(StandardCharsets.UTF_8));

                bankManager.addLoansFromFile(usernameFromSession, tempFile.toString());

                outputStream.print("Successfully Uploaded File.");
            }
        } catch (Exception e) {
            outputStream.print(e.getMessage());
        }
    }

    private String readFromInputStream(InputStream inputStream) {
        return new Scanner(inputStream).useDelimiter("\\Z").next();
    }

    private void printFileContent(String content, PrintWriter out) {
        out.println("File content:");
        out.println(content);
    }
}
