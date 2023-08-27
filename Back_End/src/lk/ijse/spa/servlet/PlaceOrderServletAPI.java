/**
 * @author : Gathsara
 * created : 8/27/2023 -- 7:47 PM
 **/

package lk.ijse.spa.servlet;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

@WebServlet(urlPatterns = "/order")
public class PlaceOrderServletAPI extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String orderId = req.getParameter("orderId");
        Date date = Date.valueOf(req.getParameter("orderDate"));
        int count = Integer.parseInt(req.getParameter("itemCount"));
        double total = Double.parseDouble(req.getParameter("total"));
        resp.addHeader("Access-Control-Allow-Origin", "*");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/webPos", "root", "1234");
            PreparedStatement pstm = connection.prepareStatement("insert into Orders values(?,?,?,?)");
            pstm.setObject(1, orderId.trim());
            pstm.setObject(2, date);
            pstm.setObject(3, count);
            pstm.setObject(4, total);
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
}
