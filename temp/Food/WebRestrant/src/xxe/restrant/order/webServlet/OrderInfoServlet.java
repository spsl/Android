package xxe.restrant.order.webServlet;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import xxe.restrant.order.domain.OrderInfo;
import xxe.restrant.order.service.OrderInfoService;
import xxe.restrant.order.service.OrderInfoServiceImpl;



public class OrderInfoServlet extends HttpServlet {

   private OrderInfoService orderInfoService=new OrderInfoServiceImpl(); 
	
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);
		
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
        
		///response.setCharacterEncoding("UTF-8");
		
		response.setContentType("text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		
		/////��ȡ����
		String action = "";
		 action=request.getParameter("action");
		
		 if(action==null){
			 action="";
		 }
		
		System.out.println("======================"+action);
		////�ύ��������
	   if(action.equals("add")){
		 
		 
		   ///��ȡ�������ڵ�����
			String orderName = request.getParameter("orderName");
			String orderAddr = request.getParameter("orderAddr");
			String orderPhone = request.getParameter("orderPhone");
			String orderMessage = request.getParameter("orderMessage");
			
			///�����ֶ�
			///
			String userName = request.getParameter("userName");
			//String userName ="tt";  //��¼�û���
			
			
			OrderInfo orderInfo = new OrderInfo();
			orderInfo.setOrderName(orderName);
			orderInfo.setUserName(userName);
			orderInfo.setOrderAddr(orderAddr);
			orderInfo.setOrderPhoneNo(orderPhone);
			orderInfo.setOrderMessage(orderMessage);
			
			///��Ӷ�����¼
		   orderInfoService.addOrderInfo(orderInfo);
		    
	   }
	
	}

}
