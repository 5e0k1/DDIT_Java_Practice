package kr.or.ddit.api.restr;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import kr.or.ddit.vo.RestrVo;

public class RestrServer {
    public static void main(String[] args) throws IOException {
        int port = 38080;
        String responseMessage = "{'data1' : 123, 'data2' : 'abc'}"; // 기본 응답 메시지
        
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", new RootHandler("{\"name\":\"김석원\"}"));
        server.setExecutor(null);
        server.start();
        
        System.out.println("Server started on port " + port);
    }

    static class RootHandler implements HttpHandler {
        private final String message;

        public RootHandler(String message) {
            this.message = message;
        }
        
        
        RestrDao restrDao = RestrDao.getInstance(); 
        
        @Override
        public void handle(HttpExchange exchange) throws IOException {
        	
        	String queryString = exchange.getRequestURI().getQuery();
        	String key = queryString.split("=")[0];
        	String val = queryString.split("=")[1];
        	
        	int pageNo = Integer.valueOf(val);
        	
        	int startNo = 1 + (pageNo-1)*10;
        	int endNo = pageNo*10;
        	
        	List<Object> param = new ArrayList<>();
        	param.add(startNo);
        	param.add(endNo);
        	
        	List<RestrVo> list = restrDao.getList(param);
        	String test = "test";
        	
        	exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(200, message.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(test.getBytes());
            os.close();
        }
    }
}
