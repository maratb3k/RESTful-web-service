package ex.mainpack;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.sun.org.apache.xpath.internal.operations.Or;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.time.LocalDate;
import java.util.Iterator;
import org.json.JSONObject;
import static java.util.stream.Collectors.joining;

public class MainServlet extends HttpServlet {
    private static final Gson GSON = new GsonBuilder().create();
    //private static final long serialVersionUID = 1L;
    private OrganizationService organizationService;

    public MainServlet() {
        this.organizationService = new OrganizationService();
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // String jsonResponse = this.studentService.findAllStudents();
        String jsonResponse = this.organizationService.findAllOrganizations();
        this.outputResponse(resp, jsonResponse, 200);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getPathInfo();
        Integer org_id = Integer.parseInt(uri.substring(1));
        String jsonResponse = this.organizationService.deleteOrganization(org_id);
        this.outputResponse(resp, jsonResponse, 200);
    }

    public static String getBody(HttpServletRequest request)  {
        String body = null;
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;

        try {
            InputStream inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            } else {
                stringBuilder.append("");
            }
        } catch (IOException ex) {
            return "";
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {

                }
            }
        }
        body = stringBuilder.toString();
        return body;
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JSONObject jObj = new JSONObject(getBody(req));
        Organization organization = new Gson().fromJson(String.valueOf(jObj), Organization.class);
        String jsonResponse = this.organizationService.putOrganization(organization);
        outputResponse(resp, jsonResponse, 201);
    }

    private void outputResponse(HttpServletResponse response, String payload, int status) {
        response.setHeader("Content-Type", "application/json");
        try {
            response.setStatus(status);
            if (payload != null) {
                OutputStream outputStream = response.getOutputStream();
                outputStream.write(payload.getBytes());
                outputStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
