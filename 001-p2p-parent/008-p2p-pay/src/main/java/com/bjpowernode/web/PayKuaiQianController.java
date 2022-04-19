package com.bjpowernode.web;

import com.bjpowernode.service.KuaiQianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Controller
public class PayKuaiQianController {
    @Autowired
    private KuaiQianService kuaiQianService;
    @RequestMapping("/kq/notify")
    public void receiveKQNotify(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("========receiveKQNotify=========");
        try {
            kuaiQianService.handlerKQNotify(request);
        }catch (Exception e){

        }finally {
            PrintWriter out = response.getWriter();
            out.println("<result>1</result><redirecturl>http://mrl.free.idcfengye.com/showKQ.html</redirecturl>");
            out.flush();
            out.close();
        }
    }
}
