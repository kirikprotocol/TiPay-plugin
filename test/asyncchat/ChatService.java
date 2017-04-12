package asyncchat;

import javax.servlet.AsyncContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebListener;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@WebListener
public class ChatService implements ServletContextListener {
 
    @Override
    public void contextDestroyed(final ServletContextEvent sce) {
 
    }
 
    @Override
    public void contextInitialized(final ServletContextEvent sce) {
 
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                final Queue<AsyncContext> chatUsers = new ConcurrentLinkedQueue<AsyncContext>();
                sce.getServletContext().setAttribute("chatUsers", chatUsers);
 
                Queue<Message> messages = new ConcurrentLinkedQueue<Message>();
 
                sce.getServletContext().setAttribute("messages", messages);
 
                Executor messageExecutor = Executors.newCachedThreadPool();
                final Executor chatExecutor = Executors.newCachedThreadPool();
 
                while (true) {
                    if (!messages.isEmpty()) {
                        final Message message = messages.poll();
                        messageExecutor.execute(new Runnable() {
                            @Override
                            public void run() {
                                while(!chatUsers.isEmpty()) {
                                    final AsyncContext aCtx = chatUsers.poll();
                                    chatExecutor.execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                ServletResponse response = aCtx.getResponse();
                                                response.setContentType("text/xml");
                                                response.getWriter().write(messageAsXml(message));
                                                aCtx.complete();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        private String messageAsXml(final Message message) {
                                            StringBuffer sb = new StringBuffer();
                                            sb.append("<message>")
                                            .append("<username>")
                                            .append(message.username)
                                            .append("</username>")
                                            .append("<text>")
                                            .append(message.message)
                                            .append("</text>")
                                            .append("</message>");
                                            return sb.toString();
                                        }
                                    });
                                }
                            }
                        });
                    } else {
                        synchronized (messages) {
                            try {
                                System.out.println("Outer loop before wait " + messages.hashCode());
                                messages.wait();
                                System.out.println("Outer loop after wait " + messages.hashCode());
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        });
 
        t.start();
    }
}

