package ec;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.BuyDataBeans;
import beans.ItemDataBeans;
import dao.BuyDAO;
import dao.BuyDetailDAO;

/**
 * 購入履歴画面
 * @author d-yamaguchi
 *
 */
@WebServlet("/UserBuyHistoryDetail")
public class UserBuyHistoryDetail extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// セッション開始
		HttpSession session = request.getSession();
		try {

			// ログイン時に取得したユーザーIDをセッションから取得
			int userId = (int) session.getAttribute("userId");

			//リクエストパラメータの文字コードを指定
			request.setCharacterEncoding("UTF-8");


			String buyId = request.getParameter("buy_id");

			ArrayList<BuyDataBeans> userbdb = BuyDetailDAO.getBuyDataBeansByUserId(userId);
			request.setAttribute("userbdb", userbdb);


			BuyDataBeans bdb = BuyDAO.getBuyDataBeansByBuyId(userId);
			request.setAttribute("bdb", bdb);

			ArrayList<ItemDataBeans> userbddb = BuyDetailDAO.getBuyDetailDataBeansByUserId(buyId);

			request.setAttribute("userbddb", userbddb);


			request.getRequestDispatcher(EcHelper.USER_BUY_HISTORY_DETAIL_PAGE).forward(request, response);


		}catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("errorMessage", e.toString());
			response.sendRedirect("Error");
		}

	}
}
