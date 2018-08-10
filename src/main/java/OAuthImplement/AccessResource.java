package OAuthImplement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class AccessResource extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        PrintWriter out = response.getWriter();
        String title = "Access Resource";

        String docType =
                "<!doctype html public \"-//w3c//dtd html 4.0 " + "transitional//en\">\n";

        out.println(docType +
                "<html>\n" +
                "<head>\n" +
                    "<title>" + title + "</title>\n" +
                "</head>\n"
        );

        out.println(
            " <form>\n" +
                    "<input class=\"cb\" type=\"checkbox\" name=\"basic\" id=\"basic\" value=\"Basic\" onchange=\"cbChange(this)\"> Use Basic Authentication<br>\n" +
                    "<input class=\"cb\" type=\"checkbox\" name=\"bearer\" id=\"bearer\" value=\"Bearer\" onchange=\"cbChange(this)\"> Use Access Token<br>\n" +
                    "<script>" +
                        "function cbChange(obj) {\n" +
                            "var cbs = document.getElementsByClassName(\"cb\");\n" +
                            "for (var i = 0; i < cbs.length; i++) {\n" +
                                "cbs[i].checked = false;\n" +
                            "}\n" +
                            "obj.checked = true;\n" +
                            "var form1 = document.getElementById(\"basicForm\");\n" +
                            "var form2 = document.getElementById(\"bearerForm\");\n" +
                            "if(obj.id == \"basic\"){\n" +
                                "form1.style.display = \"block\"\n" +
                                "form2.style.display = \"none\"\n" +
                            "}\n" +
                            "else{\n" +
                                "form1.style.display = \"none\"\n" +
                                "form2.style.display = \"block\"\n" +
                            "}\n" +
                        "}\n" +
                    "</script>\n" +
//                    "<p id=\"text1\" style=\"display:none\">Check1</p>" +
//                    "<p id=\"text2\" style=\"display:none\">Check2</p>" +
                "</form>\n" +
                    "<form id=\"basicForm\" style=\"display:none\" method=\"post\">" +
                        "<table>" +
                            "<tr>" +
                            "<td><label>Username:   </label></td>" +
                            "<td>" +
                                "<input type=\"text\" id=\"username\" name=\"username\" style=\"width:450px\"/>" +
                            "</td>" +
                            "</tr>" +
                            "<tr>" +
                                "<td><label>Password:   </label></td>" +
                                "<td><input type=\"password\" id=\"password\" name=\"password\" style=\"width:450px\"/></td>" +
                            "</tr>" +
                            "<tr>" +
                                 "<td><input type=\"submit\" value=\"Access using Basic Authentication\" /></td>" +
                            "</tr>" +
                        "</table>" +
                    "</form>\n" +
                    "<form id=\"bearerForm\" style=\"display:none\">" +
                        "<input type=\"submit\" value=\"Access using Token\"/>" +
                    "</form>\n"

        );
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{

    }

}
