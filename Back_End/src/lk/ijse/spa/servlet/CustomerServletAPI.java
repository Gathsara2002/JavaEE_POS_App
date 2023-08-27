/**
 * @author : Gathsara
 * created : 8/26/2023 -- 10:22 PM
 **/

package lk.ijse.spa.servlet;

import javax.json.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

@WebServlet(urlPatterns = "/customer")
public class CustomerServletAPI extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Content-Type", "application/json");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/webPos", "root", "1234");
            PreparedStatement pstm = connection.prepareStatement("select * from Customer");
            ResultSet resultSet = pstm.executeQuery();

            resp.addHeader("Access-Control-Allow-Origin", "*");

            JsonArrayBuilder allCustomers = Json.createArrayBuilder();

            while (resultSet.next()) {
                String id = resultSet.getString(1);
                String name = resultSet.getString(2);
                String address = resultSet.getString(3);
                int tp = resultSet.getInt(4);

                JsonObjectBuilder customerObj = Json.createObjectBuilder();
                customerObj.add("id", id);
                customerObj.add("name", name);
                customerObj.add("address", address);
                customerObj.add("tp", tp);

                allCustomers.add(customerObj.build());
            }
            resp.getWriter().print(allCustomers.build());

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("cusId");
        String name = req.getParameter("cusName");
        String address = req.getParameter("cusAddress");
        String tp = req.getParameter("cusTp");
        resp.addHeader("Access-Control-Allow-Origin", "*");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/webPos", "root", "1234");
            PreparedStatement pstm = connection.prepareStatement("insert into Customer values(?,?,?,?)");
            pstm.setObject(1, id.trim());
            pstm.setObject(2, name.trim());
            pstm.setObject(3, address.trim());
            pstm.setObject(4, tp.trim());
            boolean isAdded = pstm.executeUpdate() > 0;

            if (isAdded) {
                System.out.println("customer added successfully");
                JsonObjectBuilder builder = Json.createObjectBuilder();
                builder.add("state", "ok");
                builder.add("message", "Successfully added !");
                builder.add("data", "");
                resp.getWriter().print(builder.build());
            }

        } catch (ClassNotFoundException | SQLException e) {
            JsonObjectBuilder builder = Json.createObjectBuilder();
            builder.add("state", "Error");
            builder.add("message", e.getLocalizedMessage());
            builder.add("data", "");
            resp.setStatus(500);
            resp.getWriter().print(builder.build());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("cusId");
        resp.addHeader("Access-Control-Allow-Origin", "*");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/webPos", "root", "1234");
            PreparedStatement pstm = connection.prepareStatement("delete from Customer where id=?");
            pstm.setObject(1, id);

            boolean isDeleted = pstm.executeUpdate() > 0;

            if (isDeleted) {
                System.out.println("customer deleted successfully");
                JsonObjectBuilder builder = Json.createObjectBuilder();
                builder.add("state", "ok");
                builder.add("message", "Successfully deleted !");
                builder.add("data", "");
                resp.getWriter().print(builder.build());
            }

        } catch (ClassNotFoundException | SQLException e) {
            JsonObjectBuilder builder = Json.createObjectBuilder();
            builder.add("state", "Error");
            builder.add("message", e.getLocalizedMessage());
            builder.add("data", "");
            resp.setStatus(500);
            resp.getWriter().print(builder.build());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        JsonReader reader = Json.createReader(req.getReader());
        JsonObject customer = reader.readObject();

        String id = customer.getString("id");
        String name = customer.getString("name");
        String address = customer.getString("address");
        String tp = customer.getString("contact");
        resp.addHeader("Access-Control-Allow-Origin", "*");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/webPos", "root", "1234");
            PreparedStatement pstm = connection.prepareStatement("update  Customer set name=?,address=?,telephone=? where id=?");
            pstm.setObject(1, name);
            pstm.setObject(2, address);
            pstm.setObject(3, tp);
            pstm.setObject(4, id);

            boolean isUpdated = pstm.executeUpdate() > 0;

            if (isUpdated) {
                System.out.println("customer updated");
                JsonObjectBuilder builder = Json.createObjectBuilder();
                builder.add("state", "ok");
                builder.add("message", "Successfully updated !");
                builder.add("data", "");
                resp.getWriter().print(builder.build());
            }

        } catch (ClassNotFoundException | SQLException e) {
            JsonObjectBuilder builder = Json.createObjectBuilder();
            builder.add("state", "Error");
            builder.add("message", e.getLocalizedMessage());
            builder.add("data", "");
            resp.setStatus(500);
            resp.getWriter().print(builder.build());
        }
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.addHeader("Access-Control-Allow-Methods", "DELETE");
        resp.addHeader("Access-Control-Allow-Methods", "PUT");
        resp.addHeader("Access-Control-Allow-Headers", "Content-Type");
    }
}
