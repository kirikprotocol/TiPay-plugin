package asyncchat;

import javax.servlet.AsyncContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Queue;

@WebServlet(name = "chatServlet", urlPatterns = { "/chat" }, asyncSupported = true)
public class ChatServlet extends HttpServlet {
 
    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        String userAgent = req.getHeader("User-Agent");
        System.out.println("doGet enter " + userAgent);
        System.out.println("doGet before startAsync " + userAgent);
        AsyncContext aCtx = req.startAsync(req, resp);
        System.out.println("doGet after afterAsync " + userAgent);
        aCtx.setTimeout(1000*60*5L); //5 min timeout
        ServletContext servletContext = req.getServletContext();
        ((Queue<AsyncContext>)servletContext.getAttribute("chatUsers")).add(aCtx);
        System.out.println("doGet exit " + userAgent);
    }
 
    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        String userAgent = req.getHeader("User-Agent");
        System.out.println("doPost called " + userAgent);
        System.out.println("doPost before startAsync " + userAgent);
        AsyncContext aCtx = req.startAsync(req, resp);
        System.out.println("doPost after startAsync " + userAgent);
        ServletContext servletContext = req.getServletContext();
        String message = req.getParameter("message");
        String username = req.getParameter("username");
        Queue<Message> messages = (Queue<Message>) servletContext.getAttribute("messages");
        synchronized (messages) {
            messages.add(new Message(message, username));
            System.out.println("doPost before notify " + messages.hashCode());
            messages.notifyAll();
            System.out.println("doPost after notify " + messages.hashCode());
        }
        System.out.println("doPost before complete " + userAgent);
        aCtx.complete();
        System.out.println("doPost after complete " + userAgent);
        System.out.println("doPost exit " + userAgent);
    }
 
}
