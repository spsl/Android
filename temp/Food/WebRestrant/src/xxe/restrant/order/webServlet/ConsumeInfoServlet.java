package xxe.restrant.order.webServlet;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//import javax.xml.registry.infomodel.Concept;

import xxe.restrant.order.domain.ConsumeInfo;
import xxe.restrant.order.domain.OrderInfo;
import xxe.restrant.order.service.ConsumeInfoService;
import xxe.restrant.order.service.ConsumeInfoServiceImpl;
import xxe.restrant.order.service.OrderInfoService;
import xxe.restrant.order.service.OrderInfoServiceImpl;



public class ConsumeInfoServlet extends HttpServlet {

	private ConsumeInfoService consumeInfoService=new ConsumeInfoServiceImpl(); 


	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		//response.setCharacterEncoding("text/html;UTF-8");

		response.setContentType("text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");

		/////��������
		String action = "";
		action=request.getParameter("action");

		if(action==null){
			action="";
		}

		System.out.println("======================"+action);

		////������������
		if(action.equals("add")){

			///������������������������
			String consumeNum="";
			consumeNum =request.getParameter("consumeNum");

			int count =Integer.valueOf(consumeNum).intValue();

			System.out.println("======================"+consumeNum);
			///����������
			if(count>0){

				int i=0;

				while(i<count){
					///������������������  ������������������
					System.out.println("====count"+i);

					String userId = request.getParameter("userId"+i);
					System.out.println("=======userId  "+userId);

					String foodId = request.getParameter("foodId"+i);
					System.out.println("=======foodId  "+foodId);

					String foodName = request.getParameter("foodName"+i);
					System.out.println("=======foodName  "+foodName);

					String foodPrice = request.getParameter("foodPrice"+i);
					System.out.println("=======foodPrice  "+foodPrice);

					String foodNum = request.getParameter("foodNum"+i);
					System.out.println("=======foodNum  "+foodNum);

					String foodTotalPrice = request.getParameter("foodTotalPrice"+i);
					System.out.println("=======foodTotalPrice  "+foodTotalPrice);

					String foodMark = request.getParameter("foodMark"+i);
					System.out.println("=======foodMark  "+foodMark);

					ConsumeInfo consumeInfo = new ConsumeInfo();
					consumeInfo.setUserId(Integer.valueOf(userId));
					consumeInfo.setFoodId(Integer.valueOf(foodId));
					consumeInfo.setFoodName(foodName);
					consumeInfo.setFoodPrice(Integer.valueOf(foodPrice));
					consumeInfo.setFoodNum(Integer.valueOf(foodNum));
					consumeInfo.setFoodTotalPrice(Integer.valueOf(foodTotalPrice));
					consumeInfo.setFoodMark(Integer.valueOf(foodMark));


					///����������������
					consumeInfoService.addConsumeInfo(consumeInfo);
					i++;
				}

				// PrintWriter pw = response.getWriter();
				//  pw.write("<orderok>");
				System.out.println("==============shop OK");

			}








		}else {
			/*	   PrintWriter pw =response.getWriter();

		   System.out.println("=====================out");

		   pw.println("������");
		   pw.write("������\n");
		   pw.write("\n\n\n");
		   pw.flush();
		   pw.close();*/
		}

	}

}
