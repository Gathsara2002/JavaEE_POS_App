/**
 * @author : Gathsara
 * created : 8/27/2023 -- 7:47 PM
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

@WebServlet(urlPatterns = "/order")
public class PlaceOrderServletAPI extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.addHeader("Content-Type", "application/json");

        /*for order*/
        JsonReader reader = Json.createReader(req.getReader());
        JsonObject order = reader.readObject();
        String orderId = order.getString("oid");
        Date date = Date.valueOf(order.getString("date"));
        int count = order.getInt("count");
        double total = Double.parseDouble(order.getString("total"));

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/webPos", "root", "1234");

            /*transaction*/
            /*save order*/
            connection.setAutoCommit(false);
            PreparedStatement pstm = connection.prepareStatement("insert into Orders values(?,?,?,?)");
            pstm.setObject(1, orderId.trim());
            pstm.setObject(2, date);
            pstm.setObject(3, count);
            pstm.setObject(4, total);
            boolean isAdded = pstm.executeUpdate() > 0;

            System.out.println("order saved");

            if (!isAdded) {
                connection.rollback();
                connection.setAutoCommit(true);
                System.out.println("order not saved");

            } else {
                /*for order details*/
               // JsonObject detailArray = reader.readObject();
                JsonArray orderArray = order.getJsonArray("orderArray");

                for (JsonValue value : orderArray) {
                    String cusId = value.asJsonObject().getString("cusId");
                    String oid = value.asJsonObject().getString("orderId");
                    String code = value.asJsonObject().getString("code");
                    String itemName = value.asJsonObject().getString("itemName");
                    int qty = Integer.parseInt(value.asJsonObject().getString("qty"));
                    double price = Double.parseDouble(value.asJsonObject().getString("itemPrice"));

                    System.out.println(cusId);
                    System.out.println(oid);
                    System.out.println(code);
                    System.out.println(itemName);
                    System.out.println(qty);
                    System.out.println(price);

                    /*save order detail*/
                    PreparedStatement pstm1 = connection.prepareStatement("insert into OrderDetails values(?,?,?,?,?,?)");
                    pstm1.setObject(1, oid);
                    pstm1.setObject(2, cusId);
                    pstm1.setObject(3, code);
                    pstm1.setObject(4, itemName);
                    pstm1.setObject(5, qty);
                    pstm1.setObject(6, price);
                    boolean isOrderDetailSaved = pstm1.executeUpdate() > 0;

                    if (!isOrderDetailSaved) {
                        connection.rollback();
                        connection.setAutoCommit(true);
                    }
                }

                connection.commit();
                connection.setAutoCommit(true);

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
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.addHeader("Access-Control-Allow-Methods", "DELETE");
        resp.addHeader("Access-Control-Allow-Methods", "PUT");
        resp.addHeader("Access-Control-Allow-Headers", "Content-Type");
    }
}
